package org.egc.gis.gdal.test;

import org.egc.gis.gdal.IOFactory;
import org.egc.gis.gdal.dto.RasterMetadata;
import org.egc.gis.gdal.raster.RasterInfo;
import org.egc.gis.gdal.vector.VectorFormat;
import org.gdal.gdal.gdal;
import org.gdal.ogr.DataSource;
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

        VectorFormat vectorFormat = new VectorFormat();
//        IOFactory.createVectorIO().formatConvert(shp, json, GDALDriversEnum.GeoJSON);
        vectorFormat.toGeoJson(shp);
    }

  
    @Test
    public void testExport() throws IOException {
        VectorFormat vectorFormat = new VectorFormat();
        System.out.println(vectorFormat.toGeoJson(shp));
        System.out.println(vectorFormat.toWkt(shp));
    }

    @Test
    public void write() throws IOException {
        DataSource ds = IOFactory.createVectorIO().read(shp);
        IOFactory.createVectorIO().write(ds, "H:\\gisdemo\\out\\outlet_new.shp");
    }
}
