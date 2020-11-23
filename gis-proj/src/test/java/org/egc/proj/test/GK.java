package org.egc.proj.test;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.proj.GaussKruger;
import org.junit.Test;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author houzhiwei
 * @date 2020/5/13 21:51
 */
@Slf4j
public class GK {

    @Test
    public void gk() {
        assertEquals(GaussKruger.getZone6(117), 20);
        assertEquals(GaussKruger.getZone3(117), 39);
        assertEquals(GaussKruger.getZone6(120), 21);
        assertEquals(GaussKruger.getCentralLongitude6(20), 117);
    }

    @Test
    public void testGK() {
        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem fromName = factory.createFromName("EPSG:2415");
        System.out.println(fromName);
        System.out.println(fromName.getParameterString());
        System.out.println(fromName.getName());
        System.out.println(Math.toDegrees(fromName.getProjection().getProjectionLongitude()));
    }

    @Test
    public void testzone() throws IOException {
        String s = "+proj=tmerc +lat_0=0 +lon_0=117 +k=1 +x_0=39500000 +y_0=0 +ellps=krass +units=m +no_defs";
        CRSFactory factory = new CRSFactory();
        String a = factory.readEpsgFromParameters(s);
        System.out.println(a);
    }
}
