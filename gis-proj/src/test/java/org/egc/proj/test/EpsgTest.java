package org.egc.proj.test;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.proj.Epsg;
import org.egc.gis.proj.dto.EpsgInfo;
import org.junit.Test;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;

/**
 * @author houzhiwei
 * @date 2020/5/17 15:56
 */
@Slf4j
public class EpsgTest {
    @Test
    public void epsg() {
        EpsgInfo response = Epsg.fetchEpsgInfo(4326);
        System.out.println(response.getProj4());
    }

    @Test
    public void testEpsg() {
        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem fromParameters = factory.createFromParameters(null, "+proj=utm +zone=36 +south");
        System.out.println(fromParameters.getProjection().getEPSGCode());
    }

    @Test
    public void testEpsg2() {
        CRSFactory factory = new CRSFactory();
        String s = "+proj=utm +zone=50 +datum=WGS84 +units=m +no_defs";
        CoordinateReferenceSystem fromParameters = factory.createFromParameters(null, s);
        //0
        System.out.println(fromParameters.getProjection().getEPSGCode());
    }
}
