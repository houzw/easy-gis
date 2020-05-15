package org.egc.gis.gdal.test;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.gdal.crs.UtmUtilities;
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
        System.out.println(Math.ceil(113 / 6) + 31);
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
