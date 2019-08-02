package org.egc.gis.geotools.utils;

import lombok.extern.slf4j.Slf4j;
import org.geotools.data.DataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;

import java.io.File;

/**
 * Description:
 * <pre>
 * 读写shapefile
 * https://www.cnblogs.com/cugwx/p/3719195.html
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/12/15 17:12
 */
@Slf4j
public class ShapefileUtils {

    public static SimpleFeatureCollection readShp(String shp) {
        File shpFile = new File(shp);
        DataStore store = null;
        SimpleFeatureCollection features = null;
        try {
            store = new ShapefileDataStore(shpFile.toURI().toURL());
            features = store.getFeatureSource(
                    store.getTypeNames()[0]).getFeatures();
        } catch (java.io.IOException e) {
            log.error("Exception while trying to read shapefile.", e);
            throw new RuntimeException("Exception while trying to read shapefile");
        }
        return features;
    }
}
