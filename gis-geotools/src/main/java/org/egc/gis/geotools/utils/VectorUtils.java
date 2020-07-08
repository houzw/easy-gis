package org.egc.gis.geotools.utils;


import lombok.extern.slf4j.Slf4j;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.measure.Measure;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.*;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import si.uom.SI;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * <pre>
 * vector data utilities
 * </pre>
 *
 * @author houzhiwei
 * @date 2018 /12/15 17:12
 */
@Slf4j
public class VectorUtils {

    public static Geometry boundaryPolygon(SimpleFeatureCollection inputFeatures) throws ShapefileException {
        SimpleFeature feature = inputFeatures.features().next();
        Class<?> binding = feature.getFeatureType().getGeometryDescriptor().getType().getBinding();
        if (!binding.isAssignableFrom(Polygon.class) && !binding.isAssignableFrom(MultiPolygon.class)) {
            throw new ShapefileException("The geometry type is " + binding.getName() + ", not a polygon/multipolygon.");
        }
        return (Geometry) feature.getDefaultGeometry();
    }

    public static List<Coordinate> getCoordinateList(SimpleFeatureCollection inputFeatures) {
        List<Coordinate> pointList = new ArrayList<Coordinate>();
        try (SimpleFeatureIterator featureIter = inputFeatures.features()) {
            while (featureIter.hasNext()) {
                SimpleFeature feature = featureIter.next();
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                pointList.add(geometry.getCentroid().getCoordinate());
            }
        }
        return pointList;
    }

    /**
     * <pre>
     * To simple features list.
     *
     * @param geoms     the geoms
     * @param geomClazz the geom clazz
     * @return the list
     */
    public static List<SimpleFeature> toSimpleFeatures(List<Geometry> geoms, Class<?> geomClazz) {
        List<SimpleFeature> simpleFeatures = new ArrayList<>();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(SimpleFeatureTypes.createDefaultType(geomClazz, null));
        for (Geometry geometry : geoms) {
            featureBuilder.add(geometry);
            simpleFeatures.add(featureBuilder.buildFeature(null));
        }
        return simpleFeatures;
    }

    /**
     * To simple features list.
     *
     * @param geoms    the geoms
     * @param geomType the geom clazz
     * @param type     the type
     * @return list list
     */
    public static List<SimpleFeature> toSimpleFeatures(List<Geometry> geoms, Class<?> geomType, SimpleFeatureType type) {
        List<SimpleFeature> simpleFeatures = new ArrayList<>();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);
        for (Geometry geometry : geoms) {
            featureBuilder.add(geometry);
            simpleFeatures.add(featureBuilder.buildFeature(null));
        }
        return simpleFeatures;
    }

    /**
     * https://blog.ianturton.com/geotools,/projections/2017/08/01/area-of-a-polygon.html
     *
     * @param feature
     * @return
     */
    public static Measure calcArea(SimpleFeature feature) {
        Polygon p = (Polygon) feature.getDefaultGeometry();
        Point centroid = p.getCentroid();
        try {
            String code = "AUTO:42001," + centroid.getX() + "," + centroid.getY();
            CoordinateReferenceSystem auto = CRS.decode(code);

            MathTransform transform = CRS.findMathTransform(DefaultGeographicCRS.WGS84, auto);

            Polygon projed = (Polygon) JTS.transform(p, transform);
            return new Measure(projed.getArea(), SI.SQUARE_METRE);
        } catch (MismatchedDimensionException | FactoryException | TransformException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new Measure(0.0, SI.SQUARE_METRE);
    }
}
