package org.egc.gis.gdal.raster;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.egc.gis.gdal.dto.Consts;
import org.egc.gis.gdal.dto.GDALDriversEnum;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.osr.SpatialReference;

import java.io.File;

import static org.gdal.gdalconst.gdalconstConstants.GDT_Byte;

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

    public Dataset read(String file) {
        gdal.SetConfigOption(Consts.GDAL_FILENAME_IS_UTF8, Consts.YES);
        gdal.AllRegister();
        // 默认 gdalconst.GA_ReadOnly
        Dataset dataset = gdal.Open(file);
        if (dataset == null) {
            throw new RuntimeException(gdal.GetLastErrorMsg());
        } else {
            return dataset;
        }
    }

    public Dataset read4Update(String raster) {
        gdal.SetConfigOption(Consts.GDAL_FILENAME_IS_UTF8, Consts.YES);
        gdal.AllRegister();
        return gdal.Open(raster, gdalconst.GA_Update);
    }

    /**
     * TODO 测试
     * https://www.gislite.com/tutorial/k8024
     * @param data    the data
     * @param dstFile the dst file
     * @return boolean
     */
    public boolean write(Dataset data, String dstFile) {
        String ext = FilenameUtils.getExtension(dstFile);
        GDALDriversEnum driversEnum = GDALDriversEnum.lookupByExtension(ext);
        Driver driver = gdal.GetDriverByName(driversEnum.getName());
        if (driver == null) {
            return false;
        }
        Dataset ds = driver.CreateCopy(dstFile, data);
        ds.FlushCache();
        ds.delete();
        data.delete();
        return true;
    }

    /*double[]data = new double[nXSize];
    for(int i = 0; i < nYSize; i++)
    {
        band.ReadRaster(0, i, nXsize, 1, data);
        //do something with your data
    }*/

    /**
     * Closes the given {@link Dataset}.
     *
     * @param ds {@link Dataset} to close.
     */
    public static void closeDataSet(Dataset ds) {
        if (ds == null) {
            throw new NullPointerException("The provided dataset is null");
        }
        try {
            ds.delete();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }


    /**
     * Write geotiff file. <br/>
     * <b>only one band</b>
     *
     * @param filepath     the output file name
     * @param nRows        the number of rows, ySize
     * @param nCols        the number of columns, xSize
     * @param data         the data to write
     * @param geotransform the geographic transformation
     * @param srs          the srs Spatial Reference
     * @param nodataValue  the nodata value
     * @param gdalDataType the gdal data type, set to {@link org.gdal.gdalconst.gdalconstConstants#GDT_Float32} if is null
     */
    public void writeGeotiffFile(String filepath, int nRows, int nCols, float[] data, double[] geotransform, SpatialReference srs, double nodataValue, Integer gdalDataType) {
        File dstFile = new File(filepath);
        if (!dstFile.getParentFile().exists()) {
            dstFile.getParentFile().mkdirs();
        }
        if (gdalDataType == null) {
            gdalDataType = GDT_Byte;
        }
        //must call AllRegister() otherwise GetDriverByName() is null
        gdal.AllRegister();
        Driver tiff = gdal.GetDriverByName("GTiff");

        //new write out dataset
        Dataset ds = tiff.Create(filepath, nCols, nRows, 1, gdalDataType);
        ds.SetGeoTransform(geotransform);
        ds.SetProjection(srs.ExportToWkt());
        ds.GetRasterBand(1).SetNoDataValue(nodataValue);
        ds.GetRasterBand(1).WriteRaster(0, 0, nCols, nRows, gdalDataType, data);
        ds.FlushCache();
        RasterIO.closeDataSet(ds);
    }

}
