package org.egc.gis.gdal.test;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.gdal.crs.UtmUtilities;
import org.gdal.osr.SpatialReference;
import org.junit.Test;

/**
 * @author houzhiwei
 * @date 2020/5/13 21:10
 */
@Slf4j
public class UtmTest {
    @Test
    public void utm() {
        //49
        System.out.println(UtmUtilities.utmZone(113));
        //https://epsg.io/?q=UTM+zone+49N
        System.out.println(UtmUtilities.getUtmEpsg(113, 20));
    }

    @Test
    public void utm1() {
        //https://epsg.io/?q=UTM+zone+49N
        SpatialReference wgs84Utm = UtmUtilities.getWgs84Utm(113, 20);
        //null
        System.out.println(wgs84Utm.GetAuthorityCode(null));
        System.out.println(wgs84Utm.GetAttrValue("AUTHORITY", 1));
        System.out.println(wgs84Utm.ExportToPrettyWkt());

        SpatialReference srs = new SpatialReference();
        srs.ImportFromProj4("+proj=utm +zone=50 +datum=WGS84 +units=m +no_defs");
        System.out.println(srs.GetAttrValue("AUTHORITY", 1));
//        System.out.println(srs.ExportToPrettyWkt());

    }

    @Test
    public void utm2() {
//        677962.1897772038
//        4096742.0593422
//        0.0
        double[] doubles = UtmUtilities.transformWgs84ToUtm(113, 37);
        System.out.println(doubles[0]);
        System.out.println(doubles[1]);
        System.out.println(doubles[2]);
    }
}
