package org.egc.gis.gdal.impl;

import org.egc.gis.gdal.dto.Consts;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.ogr;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/11/13 10:18
 */
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

    public boolean write(DataSource data, String dstFile) {
        return false;
    }
}
