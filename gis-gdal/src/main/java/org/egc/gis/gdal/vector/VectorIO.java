package org.egc.gis.gdal.vector;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.gdal.dto.Consts;
import org.egc.gis.gdal.dto.GDALDriversEnum;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Driver;
import org.gdal.ogr.ogr;

import java.io.File;

/**
 * Description:
 * <pre>
 * vector data input output
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/11/13 10:18
 */
@Slf4j
public class VectorIO {

    public DataSource read(String vector) {
        ogr.RegisterAll();
        gdal.SetConfigOption(Consts.GDAL_FILENAME_IS_UTF8, Consts.YES);
        gdal.SetConfigOption(Consts.SHAPE_ENCODING, Consts.CP936);
        return ogr.Open(vector);
    }

    public DataSource read4Update(String vector) {
        ogr.RegisterAll();
        gdal.SetConfigOption(Consts.GDAL_FILENAME_IS_UTF8, Consts.YES);
        gdal.SetConfigOption(Consts.SHAPE_ENCODING, Consts.CP936);
        return ogr.Open(vector, gdalconst.GA_Update);
    }

    /**
     * write data to shapefile
     *
     * @param ds      the DataSource
     * @param dstFile the dst file
     * @return boolean
     */
    public boolean write(DataSource ds, String dstFile) {
        Driver driver = ogr.GetDriverByName(GDALDriversEnum.ESRI_Shapefile.getName());
        File dst = new File(dstFile);
        if (dst.exists()) {
            boolean delete = dst.delete();
            log.debug("Old file {} deleted: {}", dstFile, delete);
        }
        DataSource dstSrc = driver.CopyDataSource(ds, dstFile);
        int i = dstSrc.SyncToDisk();
        ds.delete();
        driver.delete();
        dstSrc.delete();
        System.out.println(i);
        return i == 0;
    }

    /**
     * Closes the given {@link DataSource}.
     *
     * @param ds {@link DataSource} to close.
     */
    public static void closeDataSource(DataSource ds) {
        if (ds == null) {
            throw new NullPointerException("The provided data source is null");
        }
        try {
            ds.delete();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }
}
