package org.egc.gis.db;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.OS;
import org.egc.commons.command.CommonsExec;
import org.egc.commons.exception.BusinessException;
import org.egc.commons.raster.PostGISInfo;
import org.egc.commons.util.FileUtil;
import org.egc.commons.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <pre>
 * Load geosptaial data to postgis
 * </pre>
 *
 * @author houzhiwei
 * @date 2018 /9/26 10:42
 */
@Slf4j
public class File2PostGIS {

    private File2PostGIS() { }

    private static final String PGPASSWORD = "PGPASSWORD";
    private static final String PSQL = "psql";
    private static final String RASTER_2_PGSQL = "raster2pgsql";
    private static final String SHP_2_PGSQL = "shp2pgsql";
    private static final String PGSQL_2_SHP = "pgsql2shp";
    private static final String OSM_2_PGROUTING = "osm2pgrouting";

    /**
     * Import raster file to postgis.
     *
     * @param srid     the srid
     * @param filePath the file path
     * @param pgInfo   the postgis database info
     * @return the boolean
     */
    public static Map<String, Object> raster2PostGIS(Integer srid, String filePath, PostGISInfo pgInfo) {

        String tableName = pgInfo.getRasterTable();
        StringUtil.isNullOrEmptyPrecondition(tableName, "Must set raster table name ");
        String pgBinDir = pgInfo.getBinDirectory();
        String password = pgInfo.getPassword();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new BusinessException("Raster to load does not exists!");
        }

        pgBinDir = FileUtil.normalizeDirectory(pgBinDir);

        CommandLine commandLine = initShellCommand();

        List<String> envs = getEnvironmentVariables(pgInfo);

        commandLine.addArgument(RASTER_2_PGSQL);
        commandLine.addArgument("-s ${srid}", false);
        // Create a GiST index on the raster column
        commandLine.addArgument("-I");
        // Vacuum analyze the raster table.
        commandLine.addArgument("-M");
        //-F Add a column with the name of the file
        commandLine.addArgument("-F");
        // raster file or files (eg. *.tif)
        commandLine.addArgument("${file}");
        // Append raster(s) to an existing table.
        commandLine.addArgument("-a ${schema}.${table}", false);
        //pipe psql
        commandLine.addArgument("|");
        commandLine.addArgument(PSQL);
        commandLine.addArgument("-U ${username}", false);
        commandLine.addArgument("-d ${db}", false);

        Map map = new HashMap();
        map.put("srid", srid);
        map.put("file", file);
        map.put("schema", pgInfo.getSchema());
        map.put("table", pgInfo.getRasterTable());
        map.put("username", pgInfo.getUsername());
        map.put("db", pgInfo.getDatabase());
        commandLine.setSubstitutionMap(map);

        Map out = null;
        String cmd = String.join(" ", commandLine.toStrings());
        try {
            out = CommonsExec.execWithOutput(cmd, envs);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Load raster to postgis error: ", e);
            out = new HashMap();
            out.put("error", e.getMessage());
        }
        return out;
    }

    /**
     * TODO not fully implemented
     * @param srid
     * @param filePath
     * @param pgInfo
     * @return
     */
    public static Map<String, Object> shp2PostGIS(Integer srid, String filePath, PostGISInfo pgInfo){
        String tableName = pgInfo.getRasterTable();
        StringUtil.isNullOrEmptyPrecondition(tableName, "Must set vector table name ");
        String pgBinDir = pgInfo.getBinDirectory();
        String password = pgInfo.getPassword();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new BusinessException("Shapefile to load does not exists!");
        }

        pgBinDir = FileUtil.normalizeDirectory(pgBinDir);

        CommandLine commandLine = initShellCommand();

        List<String> envs = getEnvironmentVariables(pgInfo);

        commandLine.addArgument(SHP_2_PGSQL);
        commandLine.addArgument("-s ${srid}", false);
        // Create a GiST index on the geometry column.
        commandLine.addArgument("-I");
        //-F Add a column with the name of the file
        commandLine.addArgument("-F");
        // raster file or files (eg. *.tif)
        commandLine.addArgument("${file}");
        // Appends data from the Shape file into the database table.
        commandLine.addArgument("-a ${schema}.${table}", false);
        //pipe psql
        commandLine.addArgument("|");
        commandLine.addArgument(PSQL);
        commandLine.addArgument("-U ${username}", false);
        commandLine.addArgument("-d ${db}", false);

        Map map = new HashMap();
        map.put("srid", srid);
        map.put("file", file);
        map.put("schema", pgInfo.getSchema());
        map.put("table", pgInfo.getRasterTable());
        map.put("username", pgInfo.getUsername());
        map.put("db", pgInfo.getDatabase());
        commandLine.setSubstitutionMap(map);

        Map out = null;
        String cmd = String.join(" ", commandLine.toStrings());
        try {
            out = CommonsExec.execWithOutput(cmd, envs);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Load shape file to postgis error: ", e);
            out = new HashMap();
            out.put("error", e.getMessage());
        }
        return out;
    }

    /**
     * 初始化shell命令
     *
     * @return
     */
    private static CommandLine initShellCommand() {
        CommandLine commandLine = null;
        // use shell
        if (OS.isFamilyWindows()) {
            commandLine = new CommandLine("cmd");
            commandLine.addArgument("/C");
        } else if (OS.isFamilyUnix()) {
            commandLine = new CommandLine("/bin/sh ");
        }
        return commandLine;
    }

    /**
     * 设置环境变量
     *
     * @param pgInfo
     * @return
     */
    private static List<String> getEnvironmentVariables(PostGISInfo pgInfo) {
        // 环境变量
        List<String> envs = Lists.newArrayList();
        envs.add(PGPASSWORD + "=" + pgInfo.getPassword());
        if (Strings.isNullOrEmpty(System.getenv("PSQL"))) {
            envs.add("PSQL=" + pgInfo.getBinDirectory());
        }
        return envs;
    }
}
