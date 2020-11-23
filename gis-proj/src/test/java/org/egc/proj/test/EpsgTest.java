package org.egc.proj.test;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.proj.Epsg;
import org.egc.gis.proj.dto.EpsgInfo;
import org.junit.Test;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;

import java.io.IOException;

/**
 * @author houzhiwei
 * @date 2020/5/17 15:56
 */
@Slf4j
public class EpsgTest {
    @Test
    public void epsg() throws IOException {
        EpsgInfo response = Epsg.fetchEpsgInfo(4326);
        System.out.println(response.getProj4());
    }

    @Test
    public void testEpsg() {
        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem fromParameters = factory.createFromParameters(null, "+proj=utm +zone=36 +south");
        System.out.println(fromParameters.getProjection().getEPSGCode());
    }
}
