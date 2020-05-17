package org.egc.proj.test;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.proj.Epsg;
import org.egc.gis.proj.EpsgInfo;
import org.junit.Test;

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
}
