package org.egc.gis.geotools;

import lombok.extern.slf4j.Slf4j;
import org.gdal.ogr.DataSource;
import org.geotools.data.DataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;

import java.io.File;

/**
 * Description:
 * <pre>
 * vector data input output
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/11/13 10:18
 */
@Slf4j
public class VectorIO {

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


    public boolean write(DataSource data, String dstFile) {
        return false;
    }
}
