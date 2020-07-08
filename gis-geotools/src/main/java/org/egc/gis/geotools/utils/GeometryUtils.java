package org.egc.gis.geotools.utils;

import lombok.extern.slf4j.Slf4j;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/**
 * http://www.tsusiatsoftware.net/jts/jtsfeatures.html
 * https://blog.csdn.net/yatsov/article/details/80215278
 *
 * @author houzhiwei
 * @date 2020/6/19 17:03
 */
@Slf4j
public class GeometryUtils {

    private static GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

    /**
     * 直接用 {@link Geometry#toText()} 即可
     *
     * @param geom
     * @return
     */
    public static String toWkt(Geometry geom) {
        return geom.toText();
    }

    public static Point point(double x, double y, int srid) {
        //坐标点的存储方式，单精度、双精度等. Java中内置的数学并不是非常精确
        // FLOATING
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), srid);
        Coordinate coordinate = new Coordinate(x, y);
        return geometryFactory.createPoint(coordinate);
    }

    public static Point point(double x, double y) {
        Coordinate coordinate = new Coordinate(x, y);
        return geometryFactory.createPoint(coordinate);
    }

    public static Geometry geometry(String wkt) throws ParseException {
        WKTReader reader = new WKTReader(geometryFactory);
        return reader.read(wkt);
    }

    /**
     * @param pointWkt e.g., "POINT (1 1)"
     * @return
     * @throws ParseException
     */
    public static Point point(String pointWkt) throws ParseException {
        WKTReader reader = new WKTReader(geometryFactory);
        return (Point) reader.read(pointWkt);
    }

    public static Polygon polygon(Coordinate[] coords) {
        LinearRing ring = geometryFactory.createLinearRing(coords);
        return geometryFactory.createPolygon(ring, null);
    }

    public static Coordinate coordinate(double x, double y) {
        return new Coordinate(x, y);
    }

    public static LineString lineString(Coordinate[] coordinates) {
        return geometryFactory.createLineString(coordinates);
    }

    public GeometryCollection createGeoCollect(Geometry[] geometries) {
        return geometryFactory.createGeometryCollection(geometries);
    }

    public static MultiLineString multiLineString(LineString[] lineStrings) {
        return geometryFactory.createMultiLineString(lineStrings);
    }
}
