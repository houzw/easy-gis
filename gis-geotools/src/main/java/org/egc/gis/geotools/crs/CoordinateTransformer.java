package org.egc.gis.geotools.crs;

import lombok.extern.slf4j.Slf4j;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.operation.DefaultMathTransformFactory;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

import java.awt.geom.Rectangle2D;

/**
 * Description:
 * <pre>
 * https://www.programcreek.com/java-api-examples/?class=org.geotools.geometry.jts.JTS&method=transform
 * </pre>
 *
 * @author houzhiwei
 * @date 2018 /10/7 15:08
 */
@Slf4j
public class CoordinateTransformer {

    private CoordinateTransformer() {
    }

    private static final double WGS84_AXIS_MINOR = 6356752.314;
    private static final double WGS84_AXIS_MAJOR = 6378137.000;

    // latitude, longitude
    private static final Coordinate ORIGIN = new Coordinate(0, 0);

    public static MathTransform customCrs() {
        MathTransformFactory factory = new DefaultMathTransformFactory();
        try {
            ParameterValueGroup p = factory.getDefaultParameters("Transverse_Mercator");
            // wkt 的 PROJECTION 部分的 parameter
            p.parameter("semi_major").setValue(WGS84_AXIS_MAJOR);
            p.parameter("semi_minor").setValue(WGS84_AXIS_MINOR);
            p.parameter("central_meridian").setValue(ORIGIN.y);
            p.parameter("latitude_of_origin").setValue(ORIGIN.x);
            //false_easting/false_northing/standard_parallel_1/standard_parallel_2
            MathTransform tr = factory.createParameterizedTransform(p).inverse();
            return tr;
        } catch (NoninvertibleTransformException | FactoryException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * http://johnewart.net/posts/2013/geotools_custom_crs/
     *
     * @param wkt
     * @return
     */
    public static CoordinateReferenceSystem customCrsFromWkt(String wkt) {
        CRSFactory factory = ReferencingFactoryFinder.getCRSFactory(null);
        try {
            CoordinateReferenceSystem customCRS = factory.createFromWKT(wkt);
            return customCRS;
        } catch (FactoryException e) {
            log.error("Create custom CRS error", e);
            return null;
        }
    }

    /**
     * Transform with epsg coordinate.
     *
     * @param fromEPSG 源 epsg, 如 4326
     * @param toEPSG   目标 epsg
     * @param x        x 坐标或<b>纬度/latitude</b>
     * @param y        y 坐标或<b>经度/longitude</b>
     * @return the coordinate
     * @throws TransformException the transform exception
     * @throws FactoryException   the factory exception
     */
    public static Coordinate transformEpsgCoordinate(int fromEPSG, int toEPSG, double x, double y) throws
            TransformException, FactoryException {
        return transformCoordinate("EPSG:" + fromEPSG, "EPSG:" + toEPSG, x, y);
    }

    /**
     * Transform coordinate.
     *
     * @param fromAuthority the source AuthorityCode, e.g. "EPSG:4326", "AUTO:42001"
     * @param toAuthority   the target AuthorityCode
     * @param x             x 坐标或<b>纬度/lat</b>
     * @param y             y 坐标或<b>经度/lon</b>
     * @return the coordinate
     * @throws TransformException the transform exception
     * @throws FactoryException   the factory exception
     */
    public static Coordinate transformCoordinate(String fromAuthority, String toAuthority, double x, double y) throws
            TransformException, FactoryException {
        CoordinateReferenceSystem sourceCRS = CRS.decode(fromAuthority);
        CoordinateReferenceSystem targetCRS = CRS.decode(toAuthority);
        // allow for some error due to different datums
        boolean lenient = true;
        MathTransform mathTransform = CRS.findMathTransform(sourceCRS, targetCRS, lenient);
        // x 竖轴，纬度
        // y 横轴，经度
        Coordinate source = new Coordinate(x, y);
        Coordinate target = new Coordinate();
        JTS.transform(source, target, mathTransform);
        return target;
    }

    /**
     * Transform envelope referenced envelope.
     *
     * @param fromAuthority the from authority
     * @param toAuthority   the to authority
     * @param bounds        the bounds
     * @return the referenced envelope
     * @throws FactoryException the factory exception
     */
    public static ReferencedEnvelope transformEnvelope(String fromAuthority, String toAuthority,
                                                       Rectangle2D bounds) throws FactoryException {
        CoordinateReferenceSystem fromCRS = CRS.decode(fromAuthority);
        CoordinateReferenceSystem toCRS = CRS.decode(toAuthority);
        try {
            MathTransform transform = CRS.findMathTransform(fromCRS, toCRS);
            Envelope sourceEnvelope = new Envelope(bounds.getMinX(), bounds.getMaxX(),
                    bounds.getMinY(), bounds.getMaxY());
            Envelope envelope = JTS.transform(sourceEnvelope, transform);
            return new ReferencedEnvelope(envelope.getMinX(), envelope.getMaxX(),
                    envelope.getMinY(), envelope.getMaxY(),
                    toCRS);
        } catch (FactoryException | TransformException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * From beijing 54 to wgs 84 coordinate.
     * https://blog.csdn.net/breaker892902/article/details/9069609
     *
     * @param lon 经度
     * @param lat 纬度
     * @return the coordinate <ul><li>{@link Coordinate#y} 经度</li><li>{@link Coordinate#x} 纬度</li></ul>
     * @throws TransformException the transform exception
     * @throws FactoryException   the factory exception
     */
    public static Coordinate fromBeijing54ToWgs84(double lon, double lat) throws TransformException, FactoryException {
        return transformEpsgCoordinate(4214, 4326, lat, lon);
    }


}
