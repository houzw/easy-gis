package org.egc.gis.gdal.raster;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.egc.gis.gdal.dto.Consts;
import org.egc.gis.gdal.dto.GDALDriversEnum;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;

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
        ds.delete();
        return true;
    }
}
