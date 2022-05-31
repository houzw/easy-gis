package org.egc.gis.geotools.geometry;

import lombok.extern.slf4j.Slf4j;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.io.IOException;
import java.io.StringWriter;

/**
 * http://www.tsusiatsoftware.net/jts/jtsfeatures.html
 * https://blog.csdn.net/yatsov/article/details/80215278
 *
 * @author houzhiwei
 * @date 2020/6/19 17:03
 */
@Slf4j
public class GeometryUtils {

    private static final GeometryFactory GEOMETRY_FACTORY = JTSFactoryFinder.getGeometryFactory();

    /**
     * 直接用 {@link Geometry#toText()} 即可
     *
     * @param geom the geom
     * @return string
     */
    public static String toWkt(Geometry geom) {
        return geom.toText();
    }

    public static String toGeoJson(Geometry geom) throws IOException {
        GeometryJSON geometryJson = new GeometryJSON();
        StringWriter writer = new StringWriter();
        geometryJson.write(geom, writer);
        String s = writer.toString();
        writer.close();
        return s;
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
        return GEOMETRY_FACTORY.createPoint(coordinate);
    }

    public static Geometry geometry(String wkt) throws ParseException {
        WKTReader reader = new WKTReader(GEOMETRY_FACTORY);
        return reader.read(wkt);
    }

    public static String bbox2Wkt(double minx, double miny, double maxx, double maxy, int epsg) {
        Coordinate[] coordinates = new Coordinate[]{
                coordinate(minx, miny),
                coordinate(minx, maxy),
                coordinate(maxx, maxy),
                coordinate(maxx, miny),
                coordinate(minx, miny)};
        return polygon(coordinates, epsg).toText();
    }

    public static String crs84Bbox2Gml(double minx, double miny, double maxx, double maxy) {
        //WGS 84 longitude-latitude
        StringBuilder sb = new StringBuilder();
        sb.append("<gml:Envelope srsName=\"urn:ogc:def:crs:OGC:1.3:CRS84\">");
        sb.append("<gml:lowerCorner>");
        sb.append(minx).append(" ").append(miny);
        sb.append("</gml:lowerCorner>");
        sb.append("<gml:upperCorner>");
        sb.append(maxx).append(" ").append(maxy);
        sb.append("</gml:upperCorner>");
        sb.append("</gml:Envelope>");
        return sb.toString();
    }

    /**
     * Point point.
     *
     * @param pointWkt e.g., "POINT (1 1)"
     * @return point
     * @throws ParseException the parse exception
     */
    public static Point point(String pointWkt) throws ParseException {
        WKTReader reader = new WKTReader(GEOMETRY_FACTORY);
        return (Point) reader.read(pointWkt);
    }

    /**
     * Polygon polygon. <br/>
     * Points of LinearRing must form a closed linestring
     *
     * @param coords the coords
     * @return the polygon
     */
    public static Polygon polygon(Coordinate[] coords) {
        LinearRing ring = GEOMETRY_FACTORY.createLinearRing(coords);
        return GEOMETRY_FACTORY.createPolygon(ring, null);
    }

    public static Polygon polygon(Coordinate[] coords, int srid) {
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), srid);
        LinearRing ring = gf.createLinearRing(coords);
        return gf.createPolygon(ring, null);
    }

    public static Coordinate coordinate(double x, double y) {
        return new Coordinate(x, y);
    }

    public static LineString lineString(Coordinate[] coordinates) {
        return GEOMETRY_FACTORY.createLineString(coordinates);
    }

    public GeometryCollection createGeoCollect(Geometry[] geometries) {
        return GEOMETRY_FACTORY.createGeometryCollection(geometries);
    }

    public static MultiLineString multiLineString(LineString[] lineStrings) {
        return GEOMETRY_FACTORY.createMultiLineString(lineStrings);
    }
}
