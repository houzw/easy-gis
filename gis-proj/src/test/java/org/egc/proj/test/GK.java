package org.egc.proj.test;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.proj.GaussKruger;
import org.junit.Test;

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
}
