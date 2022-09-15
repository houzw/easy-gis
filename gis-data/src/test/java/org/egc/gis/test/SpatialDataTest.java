package org.egc.gis.test;

import org.egc.gis.data.service.CoverageService;
import org.egc.gis.data.topography.DemTypesEnum;
import org.egc.gis.data.topography.OpenTopography;
import org.junit.Test;

import java.io.IOException;

/**
 * @author houzhiwei
 * @date 2020/11/26 18:51
 */
public class SpatialDataTest {
    // 1053912.4522102726623416,2785149.6758514959365129 : 1154982.4522102726623416,2913309.6758514959365129
    // youwuzheng: 116.44297141848418 25.666323798766907 116.4808037526092 25.693001173347675
    String youwuzhen = "F:/data/global_seims/";
    String youwuzhen_soil_dsmw = "F:/data/global_seims/spatial/youwuzhen_dsmw_soil.tif";
    String youwuzhen_soil = "F:/data/global_seims/spatial/youwuzhen_hwsd_soil.tif";
    String youwuzhen_lu = "F:/data/global_seims/spatial/youwuzhen_hwsd_landuse.tif";
    String youwuzhen_dem = "F:/data/global_seims/spatial/youwuzhen_awad30_dem.tif";
    String youwuzhen_dem2 = "F:/data/global_seims/spatial/youwuzhen_srtm30_dem.tif";
    String youwuzhen_dem90 = "F:/data/global_seims/spatial/youwuzhen_srtm90_dem.tif";
    String youwuzhendem = "F:/data/global_seims/spatial/ywzdem30m.tif";

    @Test
    public void testDem() throws IOException {
        OpenTopography ot = new OpenTopography();
//        ot.okDownloadGtiff(DemTypesEnum.AW3D30, 116.44297141848418, 25.666323798766907, 116.4808037526092, 25.693001173347675, youwuzhen_dem);
        ot.okDownloadGtiff(DemTypesEnum.SRTMGL3, 116.44297141848418, 25.666323798766907, 116.4808037526092, 25.693001173347675, youwuzhen_dem90);
    }

    @Test
    public void testGeoserver() {
        CoverageService service = new CoverageService("http://localhost:8088/geoserver/ows");
        //[nurc__Arc_Sample, nurc__Img_Sample, nurc__Pk50095, sf__sfdem, global__taoxi, global__landuse, nurc__mosaic, global__soil]
        System.out.println(service.availableCoverages());
//        service.setFileTempDir(youwuzhen);
        //default name
//        service.getCoverage("global__landuse", 25.666323798766907, 116.44297141848418, 25.693001173347675,
//                116.4808037526092, 4326, 2415, youwuzhen_lu);
        service.getCoverage("egc__china_hwsd", 25.666323798766907, 116.44297141848418, 25.693001173347675,
                116.4808037526092, 4326, 2415, youwuzhen_soil);
        // 太小了下载会报错
        service.getCoverage("global__soil", 25.666323798766907 - 0.2, 116.44297141848418 - 0.2, 25.693001173347675 + 0.2,
                116.4808037526092 + 0.2, 4326, 2415, youwuzhen_soil_dsmw);
        System.out.println(service.availableCoverageInfo());
    }

    @Test
    public void testMcj() throws IOException {
        String meichuanjiang_soil = "F:/data/global_seims/spatial/meichuanjiang_hwsd_soil.tif";
        String meichuanjiang_lu = "F:/data/global_seims/spatial/meichuanjiang_glcc_landuse.tif";
        String meichuanjiang_dem = "F:/data/global_seims/spatial/meichuanjiang_awad30_dem.tif";
        String meichuanjiang_dem2 = "F:/data/global_seims/spatial/meichuanjiang_srtm1_dem.tif";
        OpenTopography ot = new OpenTopography();
        ot.okDownloadGtiff(DemTypesEnum.AW3D30, 115.5808662066601, 26.03576042480186, 116.73808931554, 27.07832416663701, meichuanjiang_dem);

//        CoverageService service = new CoverageService("http://localhost:8088/geoserver/ows");
//        service.getCoverage("egc__china_hwsd", 26.03576042480186, 115.5808662066601, 27.07832416663701,
//                116.73808931554, 4326, 2415, meichuanjiang_soil);
//        service.getCoverage("global__landuse", 26.03576042480186, 115.5808662066601, 27.07832416663701,
//                116.73808931554, 4326, 2415, meichuanjiang_lu);
    }


}
