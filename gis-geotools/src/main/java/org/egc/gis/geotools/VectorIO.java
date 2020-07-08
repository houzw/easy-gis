package org.egc.gis.geotools;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.geotools.utils.SimpleFeatureTypes;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <pre>
 * vector data input output
 * https://docs.geotools.org/stable/userguide/library/data/shape.html
 * https://docs.geotools.org/latest/userguide/tutorial/feature/csv2shp.html
 * https://www.cnblogs.com/cugwx/p/3719195.html
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/11/13 10:18
 */
@Slf4j
public class VectorIO {

    public SimpleFeatureCollection readShp(String shp) {
        File shpFile = new File(shp);
        //DataStore store = null;
        ShapefileDataStore store = null;
        SimpleFeatureCollection features = null;
        try {
            store = new ShapefileDataStore(shpFile.toURI().toURL());
            //store.setCharset(StandardCharsets.UTF_8);
            features = store.getFeatureSource().getFeatures();
//            features = store.getFeatureSource(store.getTypeNames()[0]).getFeatures();
            store.dispose();
            return features;
        } catch (java.io.IOException e) {
            log.error("Exception while trying to read shapefile.", e);
            throw new RuntimeException("Exception while trying to read shapefile");
        }
    }

    /**
     * Write the features to the shapefile
     * <p>
     * The Shapefile format has a couple limitations:
     * - "the_geom" is always first, and used for the geometry attribute name
     * - "the_geom" must be of type Point, MultiPoint, MuiltiLineString, MultiPolygon
     * - Attribute names are limited in length
     * - Not all data types are supported (example Timestamp represented as Date)
     * <p>
     * Each data store has different limitations so check the resulting SimpleFeatureType.
     *
     * @param collection Simple Feature Collection
     * @param shpFile    shapefile
     * @param type       type is used as a template to describe the file contents
     * @return boolean
     * @throws IOException
     */
    public boolean writeShp(SimpleFeatureCollection collection, File shpFile, SimpleFeatureType type) throws IOException {
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        //参数设置
        Map<String, Serializable> params = new HashMap<>();
        params.put(ShapefileDataStoreFactory.URLP.key, shpFile.toURI().toURL());
        params.put(ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key, Boolean.TRUE);
        ShapefileDataStore dataStore = (ShapefileDataStore) dataStoreFactory.createDataStore(params);
        //创建数据文件描述内容
        dataStore.createSchema(type);

        Transaction transaction = new DefaultTransaction("create");
        String typeName = dataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
        //check how closely the shapefile was able to match our template (the SimpleFeatureType TYPE).
        SimpleFeatureType SHAPE_TYPE = featureSource.getSchema();
        log.info("SHAPE: {}", SHAPE_TYPE);

        if (featureSource instanceof SimpleFeatureStore) {
            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
            featureStore.setTransaction(transaction);
            try {
                featureStore.addFeatures(collection);
                transaction.commit();
            } catch (Exception e) {
                log.error(e.getMessage());
                transaction.rollback();
            } finally {
                transaction.close();
            }
            log.info("Create shapefile succeed!");
            return true;
        } else {
            log.error(typeName + " does not support read/write access");
        }
        return false;
    }

    /**
     * <pre>
     * Write geometries to shp.
     * use default feature type (only one field the_geom)
     * @param geoms the geoms
     * @param shpFile the shp file
     * @param geomClazz the geom clazz
     * @return the boolean
     * @throws IOException the io exception
     */
    public boolean writeGeometry2Shp(List<Geometry> geoms, File shpFile, Class<?> geomClazz) throws IOException {
        SimpleFeatureType type = SimpleFeatureTypes.createDefaultType(geomClazz, null);
        return writeGeometry2Shp(geoms, shpFile, geomClazz, type);
    }

    public boolean writeGeometry2Shp(List<Geometry> geoms, File shpFile, Class<?> geomClazz, String typeName) throws IOException {
        SimpleFeatureType type = SimpleFeatureTypes.createDefaultType(geomClazz, null);
        return writeGeometry2Shp(geoms, shpFile, geomClazz, type);
    }

    /**
     * <pre>
     * Write geometry 2 shp boolean.
     *
     * @param geoms the geoms
     * @param shpFile the shp file
     * @param geomClazz the geom clazz
     * @param type the type
     * @return the boolean
     * @throws IOException the io exception
     */
    public boolean writeGeometry2Shp(List<Geometry> geoms, File shpFile, Class<?> geomClazz, SimpleFeatureType type) throws IOException {
        List<SimpleFeature> simpleFeatures = new ArrayList<>();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);
        for (Geometry geometry : geoms) {
            featureBuilder.add(geometry);
            simpleFeatures.add(featureBuilder.buildFeature(null));
        }
        SimpleFeatureCollection collection = DataUtilities.collection(simpleFeatures);
        return writeShp(collection, shpFile, type);
    }
}
