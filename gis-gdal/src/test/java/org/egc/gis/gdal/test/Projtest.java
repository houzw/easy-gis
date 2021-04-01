package org.egc.gis.gdal.test;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.gdal.crs.CoordinateTransformer;
import org.egc.gis.gdal.crs.ProjectionUtils;
import org.egc.gis.gdal.crs.ChinaProjections;
import org.egc.gis.gdal.dto.RasterMetadata;
import org.egc.gis.gdal.raster.RasterInfo;
import org.egc.gis.gdal.vector.ReprojectVector;
import org.gdal.osr.SpatialReference;
import org.junit.Test;

import java.net.URL;

/**
 * @author houzhiwei
 * @date 2020/7/9 16:18
 */
@Slf4j
public class Projtest {


    public String getPath() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource("shp/10_bound.shp");
        return resource.getPath().substring(1);
    }

    String shp = "H:\\GIS data\\hydrology_data\\TaoXi_model data\\outlet\\outlet.shp";
    String shp2 = "H:\\GIS data\\hydrology_data\\meiChuangJiang 梅川江\\fenkeng_30m\\data\\basin.shp";

    @Test
    public void proj4() {
        String shp = getPath();
        System.out.println(shp);
        System.out.println(ProjectionUtils.getProj4(shp));
        System.out.println(ProjectionUtils.getProjectionWkt(shp));
    }
    // 1053912.4522102726623416,2785149.6758514959365129 : 1154982.4522102726623416,2913309.6758514959365129

    @Test
    public void extent() {
        SpatialReference wgs84 = new SpatialReference();
        wgs84.ImportFromEPSG(4326);
        RasterMetadata metadata = RasterInfo.getMetadata("F:/data/meiChuangJiang 梅川江/fenkeng_30m/data/dem.img");
        System.out.println(metadata.getEpsg());
        System.out.println(metadata.getSrid());
        double[] doubles = CoordinateTransformer.transformExtent(ChinaProjections.krasovsky1940Albers(),wgs84,
                1053912.4522102726623416, 2785149.6758514959365129, 1154982.4522102726623416, 2913309.6758514959365129);
        System.out.println(doubles[0]);
        System.out.println(doubles[1]);
        System.out.println(doubles[2]);
        System.out.println(doubles[3]);
    }

    @Test
    public void testCoordinate() {
        double[] doubles = ProjectionUtils.transformWgs84ToUtm(113, 36);
        System.out.println(doubles[0] + " " + doubles[1]);
        //680266.6581228462 3985798.2082587383
        SpatialReference srs = new SpatialReference();
        srs.ImportFromEPSG(32649);
        double[] transform = CoordinateTransformer.transform(32649, 4326, 680266.6581228462, 3985798.2082587383);
        System.out.println(transform[0]);
        System.out.println(transform[1]);
        CoordinateTransformer.transformToLatLong(srs, 680266.6581228462, 3985798.2082587383);
    }

    @Test
    public void reproject() {
       String shp =  "F:/data/meiChuangJiang 梅川江/fenkeng_30m/swat_project/Watershed/Shapes/outlets1.shp";
        ReprojectVector.reproject(shp, "F:\\data\\global_seims\\spatial\\meichuangjiang_outlets_2415.shp", 2415);
    }

    @Test
    public void reproject2() {
//        ReprojectVector.reprojectUseOgr(shp, "H:\\gisdemo\\out\\project\\outlet_4326.shp", 4326);
        ReprojectVector.reprojectUseOgr(shp2, "H:\\gisdemo\\out\\project\\basin_4326.shp", 4326);
    }

}
