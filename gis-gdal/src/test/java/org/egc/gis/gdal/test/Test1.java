package org.egc.gis.gdal.test;

import org.egc.gis.gdal.IOFactory;
import org.egc.gis.gdal.dto.GDALDriversEnum;
import org.egc.gis.gdal.dto.RasterMetadata;
import org.egc.gis.gdal.raster.RasterInfo;
import org.gdal.gdal.gdal;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class Test1 {
    String shp = "H:\\GIS data\\hydrology_data\\TaoXi_model data\\outlet\\outlet.shp";

    /**
     * 测试GDAL是否安装配置正常，并输出版本
     */
    @Test
    public void installed() {
        System.out.println(gdal.VersionInfo());
    }

    String dem = "H:/xcDEM.tif";

    @Test
    public void test() {
        Map<String, Object> authority = RasterInfo.getAuthority(dem);
        System.out.println(authority.get("auth"));
        System.out.println(authority.get("srs_id"));
        System.out.println(authority.get("prj_name"));
    }

    @Test
    public void testMeta() {
        RasterMetadata metadata = RasterInfo.getMetadata(dem);
        System.out.println(metadata.getSrid());
        System.out.println(metadata.getCrs());
        System.out.println(metadata.getMinY());
    }

    @Test
    public void testVector() throws IOException {
        String json = "H:\\GIS data\\hydrology_data\\TaoXi_model data\\outlet\\outlet.json";

        IOFactory.createVectorIO().formatConvert(shp, json, GDALDriversEnum.GeoJSON);
        IOFactory.createVectorIO().toGeoJSON(shp);
    }


    @Test
    public void testExport() throws IOException {
        System.out.println(IOFactory.createVectorIO().toGeoJSON(shp));
        System.out.println(IOFactory.createVectorIO().toWKT(shp));
    }
}
