package org.egc.gis.db.postgis;

import org.postgis.LinearRing;
import org.postgis.Point;
import org.postgis.Polygon;

/**
 * @author houzhiwei
 * @date 2020/11/27 19:27
 */
public class GeometryUtils {

    public static Polygon createBbox(double minLon, double minLat, double maxLon, double maxLat, int srid) {
        Point[] points = new Point[5];
        points[0] = new Point(minLon, minLat);
        points[1] = new Point(minLon, maxLat);
        points[2] = new Point(maxLon, maxLat);
        points[3] = new Point(maxLon, minLat);
        //close the linear ring
        points[4] = points[0];
        LinearRing linearRing = new LinearRing(points);
        Polygon polygon = new Polygon(new LinearRing[]{linearRing});
        polygon.setSrid(srid);
        return polygon;
    }

    public static double[] getBboxCoordinates(Polygon polygon) {
        return new double[]{polygon.getPoint(0).x, polygon.getPoint(0).y,
                polygon.getPoint(2).x, polygon.getPoint(2).y};
    }
}
