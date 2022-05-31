package org.egc.gis.geotools;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.geotools.utils.SimpleFeatureTypes;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO test
 * https://docs.geotools.org/latest/userguide/tutorial/feature/csv2shp.html
 */
@Slf4j
public class Csv2Shape {

    public String create2Shape(File csvFile, SimpleFeatureType type) throws IOException, SchemaException {
        File shp = newShapeFile(csvFile);
        List<SimpleFeature> features = createFeaturesFromCsv(csvFile);
        SimpleFeatureCollection featureCollection = SimpleFeatureTypes.simpleFeatureCollection(features, type);
        boolean b = IOFactory.createVectorIO().writeShp(featureCollection, shp, type);
        if (b) {
            return shp.getAbsolutePath();
        } else {
            return null;
        }
    }


    /**
     * name and path of the shapefile     *
     *
     * @param csvFile the input csv file used to create a default shapefile name
     * @return name and path for the shapefile as a new File object
     */
    private File newShapeFile(File csvFile) {
        String path = csvFile.getAbsolutePath();
        String newPath = path.substring(0, path.length() - 4) + ".shp";
        File newFile = new File(newPath);
        if (newFile.equals(csvFile)) {
            System.out.println("Error: cannot replace " + csvFile);
        }
        return newFile;
    }

    /**
     * 从csv文件生成featureType
     * 默认 4326
     * @param file
     * @throws SchemaException
     */
    public List<SimpleFeature> createFeaturesFromCsv(File file) throws SchemaException {
        /*
         * use the DataUtilities class to create a FeatureType that will describe the data in the shapefile.
         */
        SimpleFeatureType type =
                DataUtilities.createType("Location",
                        // <- the geometry attribute: Point type
                        "the_geom:Point:srid=4326,"
                                + "name:String,"// <- a String attribute
                                + "number:Integer"); // a number attribute
        List<SimpleFeature> features = new ArrayList<>();

        /*
         * create the geometry attribute of each feature,
         * using a Point object for the location.
         */
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);

        //LAT, LON, CITY, NUMBER
        //46.066667, 11.116667, Trento, 140
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            /* First line of the data file is the header */
            String line = reader.readLine();
            System.out.println("Header: " + line);

            for (line = reader.readLine(); line != null; line = reader.readLine()) {
                // skip blank lines
                if (line.trim().length() > 0) {
                    String[] tokens = line.split("\\,");
                    double latitude = Double.parseDouble(tokens[0]);
                    double longitude = Double.parseDouble(tokens[1]);
                    String name = tokens[2].trim();
                    int number = Integer.parseInt(tokens[3].trim());
                    /* Longitude (= x coord) first ! */
                    Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));

                    featureBuilder.add(point);
                    featureBuilder.add(name);
                    featureBuilder.add(number);
                    SimpleFeature feature = featureBuilder.buildFeature(null);
                    features.add(feature);
                }
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return features;
    }

}
