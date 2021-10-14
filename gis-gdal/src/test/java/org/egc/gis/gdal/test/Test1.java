package org.egc.gis.gdal.test;

import org.egc.gis.gdal.IOFactory;
import org.egc.gis.gdal.crs.CoordinateTransformer;
import org.egc.gis.gdal.dto.Area;
import org.egc.gis.gdal.dto.RasterMetadata;
import org.egc.gis.gdal.dto.ResamplingMethods;
import org.egc.gis.gdal.dto.VectorMetadata;
import org.egc.gis.gdal.raster.RasterInfo;
import org.egc.gis.gdal.raster.RasterUtils;
import org.egc.gis.gdal.vector.VectorFormat;
import org.egc.gis.gdal.vector.VectorUtils;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.ogr.DataSource;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class Test1 {
    String shp = "D:\\data\\WebSites\\egcDataFiles\\1\\50\\outlet_beijing1954.shp";

    /**
     * 测试GDAL是否安装配置正常，并输出版本
     */
    @Test
    public void installed() {
        System.out.println(gdal.VersionInfo());
    }

    String dem = "J:/demos/xcdem.tif";

    @Test
    public void testResample() {
        RasterUtils.resample(dem, "J:/demos/xcdem_200.tif", 200, ResamplingMethods.NEAR);
    }

    @Test
    public void test() {
        Map<String, Object> authority = RasterInfo.getAuthority(dem);
        System.out.println(authority.get("auth"));
        System.out.println(authority.get("srs_id"));
        System.out.println(authority.get("prj_name"));
    }

    @Test
    public void testMeta() {
        String s = "F:/workspace/SEIMS/data/youwuzhen/data_prepare/spatial/ywzdem30m.tif";
        RasterMetadata metadata = RasterInfo.getMetadata(s);
        System.out.println(metadata.getEpsg());
        System.out.println(metadata.getCrs());
        System.out.println(metadata.getMinY());
        System.out.println(metadata.getExtent());
        double[] extent = CoordinateTransformer.transformExtent(2415, 4326, 39444018.9, 2840045.8, 39447828.9, 2842985.8);
        System.out.println(extent[0]+" "+extent[1]+" "+extent[2]+" "+extent[3]);
        //youwuzheng:116.44297141848418 25.666323798766907 116.4808037526092 25.693001173347675
    }
    @Test
    public void testMeta2() {
        String s = "F:\\data\\global_seims\\spatial\\meichuangjiang_aw3d30_2415.tif";
        Dataset dataset = IOFactory.createRasterIO().read(s);

        RasterMetadata metadata = RasterInfo.getMetadata(dataset,false,false,true,true);
        System.out.println(metadata.getNodataCount());
    }

    @Test
    public void testVectorMeta() {
        VectorMetadata metadata = VectorUtils.getShapefileMetadata(shp);
        System.out.println(metadata.getSrid());
        System.out.println(metadata.getCrs());
        System.out.println(metadata.getMinY());
        System.out.println(metadata.getExtent());
    }

    @Test
    public void testVector() {
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
    public void write() {
        DataSource ds = IOFactory.createVectorIO().read(shp);
        IOFactory.createVectorIO().write(ds, "H:\\gisdemo\\out\\outlet_new.shp");
    }

    @Test
    public void area() {
        String shp = "F:\\data\\黑河\\流域边界\\Heihe_Basin_Boundary_2010.shp";
        Area area = VectorUtils.calculateArea(shp);
        System.out.println(area);
    }

    @Test
    public void rasterArea() {
        Dataset r = IOFactory.createRasterIO().read("F:/data/meiChuangJiang 梅川江/meichuanjiang/data_prepare/spatial/dem.img");
        Area area = RasterInfo.getArea(r);
        System.out.println(area);
    }

}
