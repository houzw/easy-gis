package org.egc.gis.geotools.test;

import org.egc.gis.geotools.geometry.GeometryUtils;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author houzhiwei
 * @date 2020/11/18 16:41
 */
public class GeometryTest {
    @Test
    public void testBbox() {
        String s = GeometryUtils.bbox2Wkt(-125.0, 38.4, -121.8, 40.9, 4326);
        System.out.println(s);
    }

    @Test
    public void testCrs() throws FactoryException {
        Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null);
//        CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("OGC", null);
        CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("EPSG:4326");
//        CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("urn:x-ogc:def:crs:EPSG:4326");
//        CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("urn:ogc:def:crs:OGC:1.3:CRS84");

        CRS.AxisOrder axisOrder = CRS.getAxisOrder(crs);
        System.out.println(axisOrder);
//        CRSAuthorityFactory   factory = CRS.getAuthorityFactory(true);
    }
}
