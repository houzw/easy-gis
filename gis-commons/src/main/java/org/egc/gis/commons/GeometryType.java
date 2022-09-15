package org.egc.gis.commons;

/**
 * https://gdal.org/programs/ogr2ogr.html#ogr2ogr
 * GDAL geometry types<br/>
 * Add Z, M, or ZM to the type name to specify coordinates with elevation, measure, or elevation and measure.
 *
 * @author houzhiwei
 * @date 2020/7/12 12:13
 */
public class GeometryType {
    public static final String NONE = "NONE";
    public static final String GEOMETRY = "GEOMETRY";
    public static final String POINT = "POINT";
    public static final String LINESTRING = "LINESTRING";
    public static final String POLYGON = "POLYGON";
    public static final String GEOMETRY_COLLECTION = "GEOMETRYCOLLECTION";
    public static final String MULTI_POINT = "MULTIPOINT";
    public static final String MULTI_POLYGON = "MULTIPOLYGON";
    public static final String MULTILINE_STRING = "MULTILINESTRING";
    public static final String CIRCULAR_STRING = "CIRCULARSTRING";
    public static final String COMPOUND_CURVE = "COMPOUNDCURVE";
    public static final String CURVE_POLYGON = "CURVEPOLYGON";
    public static final String MULTI_CURVE = "MULTICURVE";
    public static final String MULTI_SURFACE = "MULTISURFACE";
    public static final String PROMOTE_TO_MULTI = "PROMOTE_TO_MULTI";
    public static final String CONVERT_TO_LINEAR = "CONVERT_TO_LINEAR";
}
