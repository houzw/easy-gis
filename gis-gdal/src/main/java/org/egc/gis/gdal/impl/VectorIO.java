package org.egc.gis.gdal.impl;

import org.egc.gis.gdal.IO;
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
public class VectorIO implements IO {

    @Override
    public DataSource read(String vector) {
        ogr.RegisterAll();
        return ogr.Open(vector);
    }

    @Override
    public DataSource read4Update(String vector) {
        ogr.RegisterAll();
        return ogr.Open(vector, gdalconst.GA_Update);
    }
}
