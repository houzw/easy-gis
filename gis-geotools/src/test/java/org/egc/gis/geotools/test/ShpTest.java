package org.egc.gis.geotools.test;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.geotools.IOFactory;
import org.egc.gis.geotools.formats.FormatConversion;
import org.junit.Test;

/**
 * @author houzhiwei
 * @date 2020/7/8 16:38
 */
@Slf4j
public class ShpTest {
    String shp = "H:\\GIS data\\hydrology_data\\TaoXi_model data\\outlet\\outlet.shp";
    String shp2 = "H:\\GIS data\\hydrology_data\\TaoXi_model data\\TX_basin\\Basin.shp";

    @Test
    public void shp2wkt() {
        String s = FormatConversion.feature2Wkt(IOFactory.createVectorIO().readShp(shp2));
        System.out.println(s);
    }
}
