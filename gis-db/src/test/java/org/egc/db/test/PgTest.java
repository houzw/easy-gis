package org.egc.db.test;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.db.postgis.PGDataStore;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author houzhiwei
 * @date 2020/7/8 15:15
 */
@Slf4j
public class PgTest {
    String path = "";

    //https://stackoverflow.com/questions/52554587/add-new-column-attribute-to-the-shapefile-and-save-it-to-database-using-geotools
    public void loadShp() throws IOException, FactoryException {
        PGDataStore dataStore = new PGDataStore("pgis", "giser", "pg_gis");
        DataStore pgStore = dataStore.getDataStore();
        //or use
        // FileDataStore ds = FileDataStoreFinder.getDataStore(new File("/home/ian/Data/states/states.shp"));
        ShapefileDataStore shp = new ShapefileDataStore(new File(path).toURI().toURL());
        shp.setCharset(StandardCharsets.UTF_8);
        //数据的模式
        SimpleFeatureType shpSchema = shp.getSchema();
        System.out.println(shpSchema);
        System.out.println(shp.getTypeNames()[0]);
        // 定义数据库的模式
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(shpSchema.getName());
        builder.setSuperType((SimpleFeatureType) shpSchema.getSuper());
        //加入数据的所有属性字段
        builder.addAll(shpSchema.getAttributeDescriptors());
        builder.setCRS(shpSchema.getCoordinateReferenceSystem());
        // add new attribute(s) 定义额外的字段
        builder.add("shapeID", String.class);
        // 生成模式
        SimpleFeatureType pgSchema = builder.buildFeatureType();

        // management of the projection system
        CoordinateReferenceSystem crs = shpSchema.getCoordinateReferenceSystem();

        // test of the CRS based on the .prj file
        Integer crsCode = CRS.lookupEpsgCode(crs, true);

        Set<ReferenceIdentifier> refIds = shpSchema.getCoordinateReferenceSystem().getIdentifiers();
        if (((refIds == null) || (refIds.isEmpty())) && (crsCode == null)) {
            CoordinateReferenceSystem crsEpsg = CRS.decode("EPSG:4326");
            pgSchema = SimpleFeatureTypeBuilder.retype(pgSchema, crsEpsg);
        }

        // 遍历所有要素，为其添加新的属性值，即上述的"shapeID"
        // loop through features adding new attribute
        List<SimpleFeature> features = new ArrayList<>();
       /* try (SimpleFeatureIterator itr = shp.getFeatureSource().getFeatures().features()) {
            while (itr.hasNext()) {
                SimpleFeature f = itr.next();
                //复制属性
                SimpleFeature f2 = DataUtilities.reType(pgSchema, f);
                //添加属性值
                f2.setAttribute("shapeID", "");
                features.add(f2);
            }
        }*/
        //为了获得 index
        int i = 0;
//        SimpleFeatureIterator itr = shp.getFeatureSource().getFeatures().features()
        for (SimpleFeatureIterator itr = shp.getFeatureSource().getFeatures().features(); itr.hasNext(); i++) {
            SimpleFeature f = itr.next();
            //复制属性
            SimpleFeature f2 = DataUtilities.reType(pgSchema, f);
            //添加属性值
            f2.setAttribute("shapeID", i);
            features.add(f2);
        }

        SimpleFeatureSource source = pgStore.getFeatureSource("tablename");
        if (source instanceof SimpleFeatureStore) {
            SimpleFeatureStore store = (SimpleFeatureStore) source;
            store.addFeatures(DataUtilities.collection(features));
        } else {
            System.err.println("Unable to write to database");
        }
    }

    public void loadShpGeom() throws IOException, FactoryException {
        PGDataStore dataStore = new PGDataStore("pgis", "giser", "pg_gis");
        DataStore pgStore = dataStore.getDataStore();
        //or use
        // FileDataStore ds = FileDataStoreFinder.getDataStore(new File("/home/ian/Data/states/states.shp"));
        ShapefileDataStore shp = new ShapefileDataStore(new File(path).toURI().toURL());
        shp.setCharset(StandardCharsets.UTF_8);

        //List<SimpleFeature> features = new ArrayList<>();
       /*try (SimpleFeatureIterator itr = shp.getFeatureSource().getFeatures().features()) {
            while (itr.hasNext()) {
                SimpleFeature f = itr.next();
                //复制属性
                SimpleFeature f2 = DataUtilities.reType(pgSchema, f);
                //添加属性值
                f2.setAttribute("shapeID", "");
                features.add(f2);
            }
        }*/

        Transaction transaction = new DefaultTransaction("create");

        SimpleFeatureSource source = pgStore.getFeatureSource("tablename");
        if (source instanceof SimpleFeatureStore) {
            SimpleFeatureStore store = (SimpleFeatureStore) source;
            store.setTransaction(transaction);
            try {
                store.addFeatures(DataUtilities.collection(shp.getFeatureSource().getFeatures()));
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
                transaction.rollback();
            } finally {
                transaction.close();
            }
        } else {
            System.err.println("Unable to write to database");
        }
    }

}
