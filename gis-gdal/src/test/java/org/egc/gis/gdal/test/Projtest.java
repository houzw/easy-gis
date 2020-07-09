package org.egc.gis.gdal.test;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.gdal.crs.ProjectionUtils;
import org.egc.gis.gdal.vector.ReprojectVector;
import org.junit.Test;

/**
 * @author houzhiwei
 * @date 2020/7/9 16:18
 */
@Slf4j
public class Projtest {
    String shp = "H:\\GIS data\\hydrology_data\\TaoXi_model data\\outlet\\outlet.shp";
    String shp2 = "H:\\GIS data\\hydrology_data\\meiChuangJiang 梅川江\\fenkeng_30m\\data\\basin.shp";


    @Test
    public void proj4() {
        System.out.println(ProjectionUtils.getProj4(shp));
        System.out.println(ProjectionUtils.getProjectionWkt(shp));
    }

    @Test
    public void reproject() {
        ReprojectVector.reproject(shp2, "H:\\gisdemo\\out\\project\\basin_4326.shp", 4326);
    }
}
