package org.egc.gis.raster.test;

import org.gdal.gdal.gdal;
import org.junit.Test;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/10/20 14:55
 */
public class GdalTest {
    @Test
    public void test(){
        System.out.println(gdal.VersionInfo());
    }
}
