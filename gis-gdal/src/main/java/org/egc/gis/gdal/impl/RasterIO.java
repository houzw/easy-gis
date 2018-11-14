package org.egc.gis.gdal.impl;

import org.egc.gis.gdal.IO;
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
public class RasterIO implements IO {

    @Override
    public Dataset read(String file) {
        gdal.AllRegister();
        // 默认 gdalconst.GA_ReadOnly
        return gdal.Open(file);
    }

    @Override
    public Dataset read4Update(String raster) {
        gdal.AllRegister();
        // 默认 gdalconst.GA_ReadOnly
        return gdal.Open(raster, gdalconst.GA_Update);
    }


}
