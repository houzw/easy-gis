package org.egc.gis.geotools.utils;


import lombok.extern.slf4j.Slf4j;
import org.geotools.data.DataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.JTS;
import org.geotools.measure.Measure;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import si.uom.SI;

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
