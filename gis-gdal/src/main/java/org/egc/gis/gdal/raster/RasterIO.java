package org.egc.gis.gdal.raster;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.egc.gis.gdal.FormatConversion;
import org.egc.gis.gdal.IOFactory;
import org.egc.gis.gdal.dto.Consts;
import org.egc.gis.gdal.dto.GDALDriversEnum;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.TranslateOptions;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;

import java.io.IOException;
import java.util.Vector;

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
public class RasterIO implements FormatConversion {

    /**
     * convert raster format
     *
     * @param srcFile source file
     * @param dstFile destination file
     * @return converted dataset
     * @throws IOException
     */
    @Override
    public String formatConvert(String srcFile, String dstFile) throws IOException {
        Dataset dataset = this.read(srcFile);
        String ext = FilenameUtils.getExtension(dstFile);
        log.info("Convert from {} to {}", srcFile, dstFile);
        GDALDriversEnum driversEnum = GDALDriversEnum.lookupByExtension(ext);
        return this.formatConvert(srcFile, dstFile, driversEnum);
    }

    /**
     * convert raster format
     *
     * @param srcFile the src file
     * @param dstFile the dst file name.
     *                can be null. same as srcFile if null.
     *                extension will be reset according to format parameter
     * @param format  target format
     * @return converted file name
     * @throws IOException the io exception
     */
    @Override
    public String formatConvert(String srcFile, String dstFile, GDALDriversEnum format) throws IOException {
        Dataset dataset = IOFactory.createRasterIO().read(srcFile);
        String ext = format.getExtension();
        String newName = outputName(srcFile, dstFile, ext);
        log.info("Convert from {} to {}", srcFile, newName);
        Driver driver = gdal.GetDriverByName(format.name());
        if (driver == null) {
            log.error("Output Format {} Not Supported", ext);
            throw new IOException("Output Format " + ext + " Not Supported");
        }
        Vector<String> vector = new Vector<String>();
        vector.add("-of");
        vector.add(driver.getShortName());

        //        vector.add("-outsize");
        //        vector.add("128");
        //        vector.add("128");

        TranslateOptions options = new TranslateOptions(vector);
        Dataset translate = gdal.Translate(newName, dataset, options);
        dataset.delete();
        gdal.GDALDestroyDriverManager();
        return translate != null ? newName : null;
    }

    public Dataset read(String file) {
        gdal.SetConfigOption(Consts.GDAL_FILENAME_IS_UTF8, Consts.YES);
        gdal.AllRegister();
        // 默认 gdalconst.GA_ReadOnly
        return gdal.Open(file);
    }

    public Dataset read4Update(String raster) {
        gdal.SetConfigOption(Consts.GDAL_FILENAME_IS_UTF8, Consts.YES);
        gdal.AllRegister();
        return gdal.Open(raster, gdalconst.GA_Update);
    }

    /**
     * TODO 测试
     *
     * @param data
     * @param dstFile
     * @return
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
        return true;
    }
}
