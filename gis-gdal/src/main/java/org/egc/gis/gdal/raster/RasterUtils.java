package org.egc.gis.gdal.raster;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egc.gis.gdal.IOFactory;
import org.egc.gis.gdal.dto.GDALDriversEnum;
import org.gdal.gdal.*;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.FieldDefn;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;
import org.gdal.osr.SpatialReference;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Vector;

import static org.gdal.gdalconst.gdalconstConstants.GDT_Float32;

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

    /**
     * @param inputRaster  inputRaster
     * @param outputRaster outputRaster
     * @param dx           user selected resolution x
     * @param dy           user selected resolution y
     * @param resample     regridding/interpolation method, set to "near" if blank.
     *                     use {@link org.egc.gis.gdal.dto.ResamplingMethods}
     */
    public static void resample(String inputRaster, String outputRaster, int dx, int dy, String resample) {
        if (StringUtils.isBlank(resample)) {
            resample = "near";
        }
        Dataset ds = IOFactory.createRasterIO().read(inputRaster);
        Band inband = ds.GetRasterBand(1);
        Double[] nodata = new Double[1];
        inband.GetNoDataValue(nodata);
        // ref_data = None
        Vector<String> optionsVector = new Vector<>();
        optionsVector.add("-tr");
        optionsVector.add(String.valueOf(dx));
        optionsVector.add(String.valueOf(dy));
        optionsVector.add("-r");
        optionsVector.add(resample);
        optionsVector.add("-overwrite");
        optionsVector.add("-srcnodata");
        optionsVector.add(String.valueOf(nodata[0]));
        optionsVector.add("-dstnodata");
        optionsVector.add(String.valueOf(nodata[0]));
        WarpOptions options = new WarpOptions(optionsVector);
        gdal.Warp(outputRaster, new Dataset[]{ds}, options);
        ds.delete();
        gdal.GDALDestroyDriverManager();
    }


    /**
     * <pre>
     * Polygonize.
     * 每个不同像素值都会成为独立的多边形
     * https://blog.csdn.net/Prince999999/article/details/105822718
     * @param raster     the raster
     * @param dstShp     the dst shp
     * @param srcBandNum the src band num, set to 1 if <=0 or greater than band count
     */
    public static void polygonize(String raster, String dstShp, int srcBandNum) {
        Dataset ds = IOFactory.createRasterIO().read(raster);
        if (srcBandNum <= 0 || srcBandNum > ds.GetRasterCount()) {
            srcBandNum = 1;
        }
        Band srcBand = ds.GetRasterBand(srcBandNum);
        Band maskBand = srcBand.GetMaskBand();
        org.gdal.ogr.Driver driver = ogr.GetDriverByName("ESRI Shapefile");
        DataSource dst = driver.CreateDataSource(dstShp);
        SpatialReference srs = new SpatialReference(ds.GetProjectionRef());
        Layer dstLayer = dst.CreateLayer("polygon", srs);
        FieldDefn fd = new FieldDefn("DN", ogr.OFTInteger);
        dstLayer.CreateField(fd);
        // 输入栅格图像波段
        // 掩码图像波段，可以为NULL
        // 矢量化后的矢量图层
        // 需要将像元DN值写入矢量属性字段的字段索引
        // 算法选项，目前算法中没有用到，设置为NULL即可
        // 进度条回调函数
        // 进度条参数
        gdal.Polygonize(srcBand, maskBand, dstLayer, 0);
        dst.delete();
        ds.delete();
    }


    public static void polygonizeBoundry(String raster, String dstVector) {

    }

    /**
     * TODO 测试
     * Binarize.
     * 依据选定的阈值对指定的波段进行二值化
     * https://gis.stackexchange.com/questions/69062/gdal-polygonize-how-to-filter-pixels-above-a-given-value-elevation
     * @param src       the src
     * @param dst       the dst (tif)
     * @param threshold the threshold, can be null (use nodata)
     * @param band      the band， 0或1 都取第一个波段
     */
    public static void binarize(String src, String dst, Double threshold, int band) {
        Dataset ds = IOFactory.createRasterIO().read(src);
        if (band <= 0 || band > ds.GetRasterCount()) {
            band = 1;
        }
        Double[] nodataVal = new Double[1];
        Band rasterBand = ds.GetRasterBand(band);
        rasterBand.GetNoDataValue(nodataVal);
        Double nodata = nodataVal[0];
        if (nodata == null) {
            nodata = -9999d;
        }
        if (threshold == null) {
            threshold = nodata;
        }
        int xSize = ds.GetRasterXSize();
        int ySize = ds.GetRasterYSize();
        float[] data = new float[xSize * ySize];
        rasterBand.ReadRaster(0, 0, xSize, ySize, data);
        rasterBand.GetRasterDataType();
        Driver driver = gdal.GetDriverByName("GTiff");

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(xSize * ySize);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();

        //Vector<String> option = new Vector<>();
        //option.add("INTERLEAVE");
       // option.add("PIXEL");
        //Dataset outputDs = driver.Create(dst, xSize, ySize, 1, GDT_Float32, option);
        Dataset outputDs = driver.Create(dst, xSize, ySize, 1, GDT_Float32);
        outputDs.SetGeoTransform(ds.GetGeoTransform());
        outputDs.SetProjection(ds.GetProjectionRef());
        Band outBand = outputDs.GetRasterBand(1);
       /* for (int i = 0; i < ySize; i++) {
            for (int j = 0; j < xSize; j++) {
                ByteBuffer byteBuffer1 = rasterBand.ReadRaster_Direct(j, i, xSize, 1, GDT_Float32);
                floatBuffer.put(j, byteBuffer1.getFloat());
            }
            outBand.WriteRaster_Direct(0, i, xSize, 1, gdalconst.GDT_Float32, byteBuffer);
        }*/

        for (int i = 0; i < data.length; i++) {
            floatBuffer.put(i, data[i] >= threshold ? 1L : 0L);
        }

        outBand.WriteRaster_Direct(0, 0, xSize, ySize,
                xSize, ySize, gdalconstConstants.GDT_UInt16, byteBuffer);

        outputDs.delete();
        ds.delete();
        driver.delete();
    }
}
