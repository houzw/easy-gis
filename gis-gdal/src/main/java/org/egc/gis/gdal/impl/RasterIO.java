package org.egc.gis.gdal.impl;

import org.egc.gis.gdal.dto.Consts;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/11/13 10:15
 */
public class RasterIO  {

    public Dataset read(String file) {
        gdal.SetConfigOption(Consts.GDAL_FILENAME_IS_UTF8, Consts.YES);
        gdal.AllRegister();
        // 默认 gdalconst.GA_ReadOnly
        return gdal.Open(file);
    }

    public Dataset read4Update(String raster) {
        gdal.SetConfigOption(Consts.GDAL_FILENAME_IS_UTF8, Consts.YES);
        gdal.AllRegister();
        // 默认 gdalconst.GA_ReadOnly
        return gdal.Open(raster, gdalconst.GA_Update);
    }

    public boolean write(Dataset data, String dstFile) {
        return false;
    }
}
