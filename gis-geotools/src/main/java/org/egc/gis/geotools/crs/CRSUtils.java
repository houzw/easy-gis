package org.egc.gis.geotools.crs;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * @author houzhiwei
 * @date 2020/11/19 20:26
 */
public class CRSUtils {

    //urn:x-ogc:def:crs:EPSG:6.11:4326
   /* public void get() throws FactoryException {
        CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null);
        CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("urn:x-ogc:def:crs:EPSG::4326");
    }*/

    public static final CoordinateReferenceSystem CRS_WGS84 = DefaultGeographicCRS.WGS84;

    public static Geometry transformGeometry(Geometry srcGeom, CoordinateReferenceSystem sourceCrs, CoordinateReferenceSystem targetCrs) throws
            TransformException, FactoryException {
        // allow for some error due to different datums
        boolean lenient = true;
        MathTransform transform = CRS.findMathTransform(sourceCrs, targetCrs, lenient);
        return JTS.transform(srcGeom, transform);
    }
}
