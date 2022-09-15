package org.egc.gis.db.postgis;

import org.geotools.data.*;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 参考 https://github.com/yieryi/gts4vect
 */
public class PGVectorIO {
    private static final Logger log = LoggerFactory.getLogger(PGVectorIO.class);

    //https://stackoverflow.com/questions/52611282/save-modified-shape-contents-to-postgis-using-geotools-fails-with-nullpointerexc
    public boolean load2Postgis(String shapefile, DataStore pgDataStore, String tableName, String geomField) throws IOException, FactoryException {
        ShapefileDataStore shapefileDataStore = new ShapefileDataStore(new File(shapefile).toURI().toURL());

        // feature type
        String typeName = shapefileDataStore.getTypeNames()[0];
        FeatureSource<SimpleFeatureType, SimpleFeature> featSource = shapefileDataStore.getFeatureSource(typeName);
        FeatureCollection<SimpleFeatureType, SimpleFeature> featSrcCollection = featSource.getFeatures();

        SimpleFeatureType ft = shapefileDataStore.getSchema(typeName);
        // create new schema
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(ft.getName());
        builder.setSuperType((SimpleFeatureType) ft.getSuper());
        builder.addAll(ft.getAttributeDescriptors());
        // add new attribute(s)
        builder.add("shapeID", String.class);
        // build new schema
        SimpleFeatureType nSchema = builder.buildFeatureType();

        SimpleFeatureSource featureSource = pgDataStore.getFeatureSource(tableName);
        if (featureSource instanceof SimpleFeatureStore) {
            SimpleFeatureStore store = (SimpleFeatureStore) featureSource;
            store.addFeatures(DataUtilities.collection(featSrcCollection));
        } else {
            System.err.println("Unable to wtire to database");
        }

        return false;
    }

    public boolean table2shp(DataStore pgDataStore, String tableName, String shpPath, String geomField) {
        boolean result = false;
        try {
            SimpleFeatureSource featureSource = pgDataStore.getFeatureSource(tableName);

            // 初始化 ShapefileDataStore
            File file = new File(shpPath);
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
            ShapefileDataStore shpDataStore = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);

            //postgis获取的Featuretype获取坐标系代码
            SimpleFeatureType pgfeaturetype = ((SimpleFeatureSource) featureSource).getSchema();
            String srid = pgfeaturetype.getGeometryDescriptor().getUserData().get("nativeSRID").toString();
            SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
            typeBuilder.init(pgfeaturetype);
            if (!srid.equals("0")) {
                CoordinateReferenceSystem crs = CRS.decode("EPSG:" + srid, true);
                typeBuilder.setCRS(crs);
            }
            pgfeaturetype = typeBuilder.buildFeatureType();
            //设置成utf-8编码
            shpDataStore.setCharset(StandardCharsets.UTF_8);
            shpDataStore.createSchema(pgfeaturetype);
            write2shp(featureSource.getFeatures(), shpDataStore, geomField);
            result = true;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public boolean write2shp(FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection, ShapefileDataStore dataStore, String geomField) {
        boolean b = false;
        try {
            FeatureIterator<SimpleFeature> iterator = featureCollection.features();
            FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter = dataStore.getFeatureWriter(dataStore.getTypeNames()[0], Transaction.AUTO_COMMIT);
            while (iterator.hasNext()) {
                Feature feature = iterator.next();
                SimpleFeature simpleFeature = featureWriter.next();
                Collection<Property> properties = feature.getProperties();
                for (Property property : properties) {
                    if (property.getName().toString().equalsIgnoreCase(geomField)) {
                        simpleFeature.setAttribute("the_geom", property.getValue());
                    } else {
                        simpleFeature.setAttribute(property.getName().toString(), property.getValue());
                    }
                }
                featureWriter.write();
            }
            iterator.close();
            featureWriter.close();
            dataStore.dispose();
            b = true;
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return b;
    }

    public boolean shp2postgis(DataStore pgDataStore, String shpPath, String pgTableName, Charset shpCharset) {
        boolean result = false;
        try {
            ShapefileDataStore shapefileDataStore = new ShapefileDataStore(new File(shpPath).toURI().toURL());
            shapefileDataStore.setCharset(shpCharset);
            FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection = shapefileDataStore.getFeatureSource().getFeatures();
            write2pg(pgDataStore, featureCollection, pgTableName);
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public boolean write2pg(DataStore pgDataStore,
                            FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection,
                            String tableName) {
        boolean b = false;
        try {
            SimpleFeatureType simpleFeatureType = (SimpleFeatureType) featureCollection.getSchema();
            SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
            typeBuilder.init(simpleFeatureType);
            typeBuilder.setName(tableName);
            SimpleFeatureType type = typeBuilder.buildFeatureType();
            pgDataStore.createSchema(type);
            FeatureIterator<SimpleFeature> iterator = featureCollection.features();
            FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter = pgDataStore.getFeatureWriterAppend(tableName, Transaction.AUTO_COMMIT);
            while (iterator.hasNext()) {
                Feature feature = iterator.next();
                SimpleFeature simpleFeature = featureWriter.next();
                Collection<Property> properties = feature.getProperties();
                for (Property property : properties) {
                    simpleFeature.setAttribute(property.getName().toString(), property.getValue());
                }
                featureWriter.write();
            }
            iterator.close();
            featureWriter.close();
            b = true;
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return b;
    }

    public boolean write2pgWgs84(DataStore pgDataStore,
                                 FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection,
                                 String tableName) {
        boolean b = false;
        try {
            SimpleFeatureType simpleFeatureType = (SimpleFeatureType) featureCollection.getSchema();
            SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
            typeBuilder.init(simpleFeatureType);
            typeBuilder.setName(tableName);
            SimpleFeatureType type = typeBuilder.buildFeatureType();
            pgDataStore.createSchema(type);
            FeatureIterator<SimpleFeature> iterator = featureCollection.features();
            FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter = pgDataStore.getFeatureWriterAppend(tableName, Transaction.AUTO_COMMIT);

            CoordinateReferenceSystem sourceCrs = null;
            if (iterator.hasNext()) {
                SimpleFeature next = iterator.next();
                sourceCrs = next.getDefaultGeometryProperty().getDescriptor().getCoordinateReferenceSystem();
            }

            //默认转为wgs84
            CoordinateReferenceSystem worldCRS = DefaultGeographicCRS.WGS84;
            boolean lenient = true; // allow for some error due to different datums
            //定义坐标转换
            MathTransform transform = CRS.findMathTransform(sourceCrs, worldCRS, lenient);

            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                SimpleFeature next = featureWriter.next();
                Collection<Property> properties = feature.getProperties();
                for (Property property : properties) {
                    next.setAttribute(property.getName().toString(), property.getValue());
                }

                //坐标系转换
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                Geometry geometry2 = JTS.transform(geometry, transform);
                feature.setDefaultGeometry(geometry2);

                featureWriter.write();
            }

            iterator.close();
            featureWriter.close();
            b = true;
        } catch (IOException | FactoryException | TransformException e) {
            log.error(e.getLocalizedMessage());
        }
        return b;
    }

    public boolean geojson2pg(DataStore dataStore, String geojsonPath, String tableName) {
        boolean result = false;
        try {
            FeatureJSON featureJSON = new FeatureJSON();
            SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) featureJSON.readFeatureCollection(new FileInputStream(geojsonPath));
            write2pg(dataStore, featureCollection, tableName);
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

}
