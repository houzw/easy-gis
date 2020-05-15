package org.egc.gis.gdal.raster;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.gdal.IOFactory;
import org.egc.gis.gdal.dto.GDALDriversEnum;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.osr.SpatialReference;

/**
 * Description:
 * <pre>
 * Raster Utilities
 * </pre>
 *
 * @author houzhiwei
 * @date 2018 /11/13 9:08
 * @date 2020-5-2 15:39:52
 */
@Slf4j
public class RasterUtils {

    /**
     * To geo tiff boolean.
     *
     * @param srcFile the src file
     * @param dstFile the dst file
     * @param dstEpsg the dst epsg
     * @return the boolean
     */
    public static boolean toGeoTiff(String srcFile, String dstFile, Integer dstEpsg) {
        Dataset src = IOFactory.createRasterIO().read(srcFile);
        Driver driver = gdal.GetDriverByName(GDALDriversEnum.GTiff.name());
        Dataset outDs = driver.CreateCopy(dstFile, src);

        SpatialReference srs = new SpatialReference();
        if (dstEpsg != null) {
            srs.ImportFromEPSG(dstEpsg);
            outDs.SetProjection(srs.ExportToWkt());
        }
        driver.delete();
        src.delete();
        outDs.delete();
        log.debug("--------------转换成功--------------");
        return true;
    }

    /**
     * Convert raster.
     *
     * @param inputRaster  the input raster
     * @param outputRaster the output raster
     * @param format       the format
     * @return the string
     */
    public static String convertRaster(String inputRaster, String outputRaster, GDALDriversEnum format) {
        Dataset ds = IOFactory.createRasterIO().read(inputRaster);
        Driver driver = gdal.GetDriverByName(format.getName());
        Dataset dstDs = driver.CreateCopy(outputRaster, ds, 0);
        driver.delete();
        ds.delete();
        dstDs.delete();
        return outputRaster;
    }
}
