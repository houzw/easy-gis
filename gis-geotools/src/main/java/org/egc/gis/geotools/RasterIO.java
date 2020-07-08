package org.egc.gis.geotools;

import lombok.extern.slf4j.Slf4j;
import org.egc.commons.util.FileUtil;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.gce.geotiff.GeoTiffWriteParams;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Description:
 * <pre>
 * raster input/output/format
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/11/13 10:15
 * @date 2020-5-2 22:05:13
 */
@Slf4j
public class RasterIO {

    public static GridCoverage2D readZippedGeoTIFF(String zipFile) {
        Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER,
                Boolean.TRUE);
        File[] files = null;
        // zip文件，解压并获取其中的tiff文件
        try {
            String dir = FileUtil.unzip(zipFile, null);
            files = new File(dir).listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".tif") || name.toLowerCase().endsWith(".tiff");
                }
            });
        } catch (IOException e) {
            log.error("unzip file failed");
        }
        // multiple tiff ?
        File zippedFile = files[0];
        GeoTiffReader reader;
        try {
            reader = new GeoTiffReader(zippedFile, hints);
            return (GridCoverage2D) reader.read(null);
        } catch (Exception e) {
            log.error("Exception while trying to read zipped geotiff.", e);
            throw new RuntimeException("Exception while trying to read zipped geotiff.", e);
        }
    }

    /**
     * read geotiff to GridCoverage2D
     *
     * @param file
     * @return
     */
    public static GridCoverage2D readGeoTIFF(String file) {
        Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        GeoTiffReader reader = null;
        GridCoverage2D coverage = null;
        try {
            reader = new GeoTiffReader(file, hints);
            coverage = (GridCoverage2D) reader.read(null);
        } catch (Exception e) {
            log.error("Exception while trying to read geotiff.", e);
            throw new RuntimeException("Read GeoTiff failed", e);
        }
        return coverage;
    }

    public static GridCoverage2D readAsciiGrass(String input) {
        File file = new File(input);
        GridCoverageReader reader = null;
        GridCoverage2D grid = null;
        try {
            reader = new ArcGridReader(file);
            grid = (GridCoverage2D) reader.read(null);
            log.info("CoordinateReferenceSystem2D: {} ", grid.getCoordinateReferenceSystem2D().toString());
            log.info("Envelope: {}", grid.getEnvelope2D().toString());
        } catch (IOException e) {
            log.error("Exception while trying to read Ascii Grass file.", e);
            throw new RuntimeException("Read Ascii Grass file failed", e);
        }
        return grid;
    }

    /**
     * write GridCoverage2D to geotiff
     * https://www.programcreek.com/java-api-examples/?class=org.geotools.gce.geotiff.GeoTiffWriter&method=write
     *
     * @param coverage2D
     * @param dstFile
     * @return
     */
    public static boolean write(GridCoverage2D coverage2D, String dstFile) {
        try {
            GeoTiffWriteParams wp = new GeoTiffWriteParams();
            wp.setCompressionMode(GeoTiffWriteParams.MODE_DEFAULT);
            wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
            wp.setCompressionType("LZW");
            wp.setTilingMode(GeoToolsWriteParams.MODE_DEFAULT);
            GeoTiffFormat format = new GeoTiffFormat();
            ParameterValueGroup paramWrite = format.getWriteParameters();

            paramWrite.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString()).setValue(wp);
            GeoTiffWriter gtw = (GeoTiffWriter) format.getWriter(new File(dstFile));
            gtw.write(coverage2D, (GeneralParameterValue[]) paramWrite.values().toArray(new GeneralParameterValue[1]));
            return true;
        } catch (IOException e) {
            log.error("Exception while trying to write geotiff.", e);
            return false;
        }
    }
}
