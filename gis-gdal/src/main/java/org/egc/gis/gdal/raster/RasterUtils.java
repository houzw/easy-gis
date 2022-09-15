package org.egc.gis.gdal.raster;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.util.StringUtil;
import org.egc.gis.commons.SpatialArea;
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
import java.util.List;
import java.util.Vector;

import static org.gdal.gdalconst.gdalconstConstants.GDT_Float32;
import static org.gdal.gdalconst.gdalconstConstants.GMF_NODATA;

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
     * Resample.
     *
     * @param inputRaster  the input raster
     * @param outputRaster the output raster
     * @param resolution   the resolution
     * @param resample     regridding/interpolation method, set to "near" if blank.
     *                     use {@link org.egc.gis.gdal.dto.ResamplingMethods}
     */
    public static void resample(String inputRaster, String outputRaster, double resolution, String resample) {
        resample(inputRaster, outputRaster, resolution, resolution, resample);
    }

    /**
     * TODO resample 改成枚举
     *
     * @param inputRaster  inputRaster
     * @param outputRaster outputRaster
     * @param dx           user selected resolution x
     * @param dy           user selected resolution y
     * @param resample     regridding/interpolation method, set to "near" if blank.
     *                     use {@link org.egc.gis.gdal.dto.ResamplingMethods}
     */
    public static void resample(String inputRaster, String outputRaster, double dx, double dy, String resample) {
        if (StringUtils.isBlank(resample)) {
            resample = "near";
        }
        Dataset ds = IOFactory.createRasterIO().read(inputRaster);
        Band inband = ds.GetRasterBand(1);
        SpatialReference srs = new SpatialReference(ds.GetProjectionRef());
        Double[] nodata = new Double[1];
        inband.GetNoDataValue(nodata);
        // ref_data = None
        Vector<String> options = new Vector<>();
        options.add("-t_srs");
        options.add(srs.ExportToProj4());
        options.add("-tr");
        options.add(String.valueOf(dx));
        options.add(String.valueOf(dy));
        options.add("-r");
        options.add(resample);
        options.add("-overwrite");
        options.add("-srcnodata");
        options.add(String.valueOf(nodata[0]));
        options.add("-dstnodata");
        options.add(String.valueOf(nodata[0]));
        WarpOptions warpOptions = new WarpOptions(options);
        gdal.Warp(outputRaster, new Dataset[]{ds}, warpOptions);
        ds.delete();
        gdal.GDALDestroyDriverManager();
        log.info("DONE");
    }


    /**
     * TODO 测试
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

    public static void binarize(String src, String dst) {
        binarize(src, dst, null, 1);
    }

    /**
     * TODO 测试
     * Binarize.
     * 依据选定的阈值对指定的波段进行二值化
     * https://gis.stackexchange.com/questions/69062/gdal-polygonize-how-to-filter-pixels-above-a-given-value-elevation
     *
     * @param src       the src
     * @param dst       the dst (tif)
     * @param threshold the threshold, set to nodata if null
     * @param band      the band， 0 或 1 都取第一个波段
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
        //option.add("PIXEL");
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

        outBand.WriteRaster_Direct(0, 0, xSize, ySize, xSize, ySize, gdalconstConstants.GDT_Float32, byteBuffer);

        RasterIO.closeDataSet(outputDs);
        RasterIO.closeDataSet(ds);
        driver.delete();
    }

    /**
     * generate JPEG format true-color thumbnail
     *
     * @param src       source file
     * @param dst       destination/output file
     * @param redBand   red band
     * @param greenBand greed band
     * @param blueBand  blue band
     * @param scale     in percentage(%)
     */
    public static void truecolorThumbnail(String src, String dst, int redBand, int greenBand, int blueBand, float scale) {
        Dataset ds = IOFactory.createRasterIO().read(src);
        int[] rgbBands = new int[]{redBand, greenBand, blueBand};

        Vector<String> options = new Vector<>();
        options.add("-of");
        options.add("PNG");

        int bandCount = ds.GetRasterCount();

        for (int i = 0; i < 3; i++) {
            if (rgbBands[i] > bandCount) {
                RasterIO.closeDataSet(ds);
                log.error("The selected band number cannot be larger than {}", bandCount);
                return; // no target band exists
            }
            if (rgbBands[i] <= 0) {
                RasterIO.closeDataSet(ds);
                log.error("The selected band number cannot be less than 0");
                return;
            }
        }

        if (bandCount < 3) {
            log.warn("The input image only has {} band(s)", bandCount);
            for (int i = 0; i < bandCount; i++) {
                options.add("-b");
                options.add(String.valueOf(i));
            }
        } else {
            options.add("-b");
            options.add(String.valueOf(redBand));
            options.add("-b");
            options.add(String.valueOf(greenBand));
            options.add("-b");
            options.add(String.valueOf(blueBand));
        }
        options.add("-outsize");
        options.add(String.valueOf(scale) + "%");
        options.add(String.valueOf(scale) + "%");
        TranslateOptions translateOptions = new TranslateOptions(options);
        gdal.Translate(dst, ds, translateOptions);
        RasterIO.closeDataSet(ds);
    }

    /**
     * generate JPEG format true-color thumbnail
     *
     * @param src   source file
     * @param dst   destination/output file
     * @param scale in percentage(%)
     */
    public static void rasterThumbnail(String src, String dst, float scale) {
        Dataset ds = IOFactory.createRasterIO().read(src);
        Vector<String> options = new Vector<>();
        options.add("-of");
        options.add("PNG");
        options.add("-b");
        options.add(String.valueOf(1));
        options.add("-outsize");
        options.add(String.valueOf(scale) + "%");
        options.add(String.valueOf(scale) + "%");
        TranslateOptions translateOptions = new TranslateOptions(options);
        gdal.Translate(dst, ds, translateOptions);
        RasterIO.closeDataSet(ds);
    }

    /**
     * gdaladdo [-r {nearest,average,rms,bilinear,gauss,cubic,cubicspline,lanczos,average_magphase,mode}]
     * [-b band]* [-minsize val]
     * [-ro] [-clean] [-oo NAME=VALUE]* [--help-general] filename [levels]
     * https://gdal.org/programs/gdaladdo.html
     *
     * @param src
     */
    public static void generateOverview(String src, int srcBandNum) {
        Dataset ds = IOFactory.createRasterIO().read4Update(src);
        int bandNum = ds.GetRasterCount();
        Band srcBand = ds.GetRasterBand(srcBandNum);
//        gdal.RegenerateOverview(srcBand,)
        int[] ovrArr = new int[]{1, 2};
        ds.BuildOverviews(ovrArr);
    }

    //TODO test
    //https://github.com/OSGeo/gdal/blob/master/autotest/gcore/tiff_ovr.py
    public static String checkOverviews(String src) {
        Dataset ds = IOFactory.createRasterIO().read(src);
        for (int i = 0; i < 3; i++) { //?
            if (ds.GetRasterBand(i).GetOverviewCount() == 2) {
                return "overviews missing";
            }
            Band ovr_band = ds.GetRasterBand(i).GetOverview(0);
            if (ovr_band.GetXSize() != 10 || ovr_band.GetYSize() != 10) {
                return String.format("overview wrong size: band %d, overview 0, size = %d * %d,", i, ovr_band.GetXSize(), ovr_band.GetYSize());
            }
            if (ovr_band.Checksum() != 1087) {
                return String.format("overview wrong checksum: band %d, overview 0, checksum = %d,", i, ovr_band.Checksum());
            }
            ovr_band = ds.GetRasterBand(i).GetOverview(1);
            if (ovr_band.GetXSize() != 5 || ovr_band.GetYSize() != 5) {
                return String.format("overview wrong size: band %d, overview 1, size = %d * %d,", i, ovr_band.GetXSize(), ovr_band.GetYSize());
            }
            if (ovr_band.Checksum() != 328) {
                return String.format("overview wrong checksum: band %d, overview 1, checksum = %d,", i, ovr_band.Checksum());
            }
        }
        return "false";
    }

    /**
     * TODO 暂未成功
     * Fill raster regions by interpolation from edges.
     *
     * @param src  the src
     * @param band the target band, e.g., 1
     * @see <a href="https://gdal.org/java/org/gdal/gdal/gdal.html#FillNodata-org.gdal.gdal.Band-org.gdal.gdal.Band-double-int-java.util.Vector-org.gdal.gdal.ProgressCallback-">gdal_fillnodata</a>
     */
    public static void fillNodata(String src, int band, String dst) {
        Dataset ds = IOFactory.createRasterIO().read4Update(src);
        if (band <= 0) {
            band = 1;
        }
        Band targetBand = ds.GetRasterBand(band);
        int i = ds.CreateMaskBand(GMF_NODATA);
        gdal.FillNodata(targetBand, targetBand.GetMaskBand(), 100, 0);
        targetBand.FlushCache();
        ds.FlushCache();
        //原始数据未更新
        RasterIO.closeDataSet(ds);
    }

    /**
     * 多波段影像融合为一个文件
     *
     * @param inputs 多波段影像数据列表
     * @param dst    输出影像
     */
    public static void synthesisBands(List<String> inputs, String dst) {
        Dataset ds = IOFactory.createRasterIO().read(inputs.get(0));
        int rasterDataType = ds.GetRasterBand(1).GetRasterDataType();
        if (StringUtils.isEmpty(FilenameUtils.getFullPath(dst))) {
            dst = FilenameUtils.getFullPath(inputs.get(0)) + dst;
        }
        Driver gTiff = gdal.GetDriverByName("GTiff");
        Dataset outDs = gTiff.Create(dst, ds.GetRasterXSize(), ds.GetRasterYSize(), inputs.size(), rasterDataType);
        outDs.SetGeoTransform(ds.GetGeoTransform());
        outDs.SetProjection(ds.GetProjection());
        for (int i = 0; i < inputs.size(); i++) {
            Dataset iDataset = IOFactory.createRasterIO().read(inputs.get(i));
            Band band = iDataset.GetRasterBand(1);
            int bandDType = band.GetRasterDataType();
            int xSize = band.GetXSize();
            int ySize = band.GetYSize();

            int byteToType = gdal.GetDataTypeSize(bandDType) / 8;
            byte[] data = new byte[xSize * ySize * byteToType];

            //or
            // short[] dataBuf = new short[xSize * ySize];
            // band.ReadRaster(0, 0, xSize, ySize, dataBuf);
            band.ReadRaster(0, 0, xSize, ySize, bandDType, data);

            outDs.GetRasterBand(i + 1).WriteRaster(0, 0, band.GetXSize(), band.GetYSize(), bandDType, data);
            RasterIO.closeDataSet(iDataset);
        }
        outDs.FlushCache();
        RasterIO.closeDataSet(ds);
        log.info("The selected inputs have been systhesised to one tif.");
    }

    /**
     * Convert map coordinates to grid pixel coordinates
     *
     * @param gt Affine transformation parameters
     * @param X  Abscissa
     * @param Y  Ordinate
     * @return
     */
    public static int[] coordinates2ColRow(double[] gt, double X, double Y) {
        int[] ints = new int[2];
//        X = gt[0] + Xpixel*gt[1] + Yline*gt[2];
//        Y = gt[3] + Xpixel*gt[4] + Yline*gt[5];
//        Elimination method for solving binary linear equations
//        X-gt[0]=Xpixel*gt[1] + Yline*gt[2]
//        Xpixel = (X-gt[0] - Yline*gt[2])/gt[1]
//        Y - gt[3] = ((X-gt[0] - Yline*gt[2])/gt[1])gt[4] + Yline*gt[5]
//        (Y - gt[3])*gt[1] = ((X-gt[0] - Yline*gt[2]))*gt[4] + Yline*gt[5]*gt[1]
//        (Y - gt[3])*gt[1] =(X-gt[0])*gt[4] - Yline*gt[2]*gt[4] + Yline*gt[5]*gt[1]
//        (Y - gt[3])*gt[1] - (X-gt[0])*gt[4] = Yline(gt[5]*gt[1]-gt[2]*gt[4])

        //Round down. If you round up, the calculation result will be too large, and the data of adjacent pixels will be read later
        double yline = Math.floor(((Y - gt[3]) * gt[1] - (X - gt[0]) * gt[4]) / (gt[5] * gt[1] - gt[2] * gt[4]));
        double xpixel = Math.floor((X - gt[0] - yline * gt[2]) / gt[1]);
        ints[0] = new Double(xpixel).intValue();
        ints[1] = new Double(yline).intValue();
        return ints;
    }

    public static SpatialArea getArea(Dataset dataset) {
        SpatialReference sr = new SpatialReference(dataset.GetProjectionRef());
        Band band = dataset.GetRasterBand(1);
        Double[] nodataVal = new Double[1];
        band.GetNoDataValue(nodataVal);
        Double nodata = nodataVal[0];
        if (nodata == null) {
            nodata = -9999d;
        }
        int xSize = dataset.GetRasterXSize();
        int ySize = dataset.GetRasterYSize();

        float[] dataBuf = new float[xSize * ySize];
        double[] gt = dataset.GetGeoTransform();
        band.ReadRaster(0, 0, xSize, ySize, dataBuf);
        double wePixelResolution = gt[1];
        double nsPixelResolution = Math.abs(gt[5]);
        int count = 0;
        for (float d : dataBuf) {
            if (d != nodata.floatValue()) {
                count++;
            }
        }
        return new SpatialArea(count * wePixelResolution * nsPixelResolution, sr.GetLinearUnitsName());
    }

    public static SpatialArea getArea(String rasterFile) {
        StringUtil.isNullOrEmptyPrecondition(rasterFile, "Raster file must exists");
        //gdal.AllRegister();
        final Dataset dataset = gdal.Open(rasterFile, gdalconstConstants.GA_ReadOnly);
        SpatialArea area = getArea(dataset);
        IOFactory.closeDataSet(dataset);
        return area;
    }
}
