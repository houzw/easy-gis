package org.egc.gis.gdal.vector;

import org.apache.commons.lang3.tuple.Pair;
import org.gdal.ogr.Geometry;
import org.gdal.ogr.ogr;

import java.util.List;

/**
 * https://pcjericks.github.io/py-gdalogr-cookbook/geometry.html
 *
 * @author houzhiwei
 * @date 2020/11/1 14:31
 */
public class GeometryUtils {

    public static Geometry createPoint(double x, double y) {
        Geometry point = new Geometry(ogr.wkbPoint);
        point.AddPoint(x, y);
        return point;
    }

    /**
     * Create line string geometry.
     *
     * @param coordinates the coordinates, each is a `Pair.of(x, y);`
     * @return the linestring
     */
    public static Geometry createLineString(List<Pair<Double, Double>> coordinates) {
        Geometry line = new Geometry(ogr.wkbLineString);
        for (Pair<Double, Double> coord : coordinates) {
            line.AddPoint(coord.getLeft(), coord.getRight());
        }
        return line;
    }

    public static Geometry createPolygon(List<Pair<Double, Double>> coordinates) {
        Geometry ring = new Geometry(ogr.wkbLinearRing);
        for (Pair<Double, Double> coord : coordinates) {
            ring.AddPoint(coord.getLeft(), coord.getRight());
        }
        //必须是一个闭环，即首尾为同一点
        Pair<Double, Double> first = coordinates.get(0);
        Pair<Double, Double> last = coordinates.get(coordinates.size() - 1);
        if (!first.getLeft().equals(last.getLeft()) || !first.getRight().equals(last.getRight())) {
            ring.AddPoint(first.getLeft(), first.getRight());
        }
        Geometry polygon = new Geometry(ogr.wkbPolygon);
        polygon.AddGeometry(ring);
        return polygon;
    }

}
