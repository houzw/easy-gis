package org.egc.gis.proj;

import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;

/**
 * https://desktop.arcgis.com/en/arcmap/latest/map/projections/pdf/geographic_coordinate_systems.pdf
 * https://blog.csdn.net/m0_37667770/article/details/104024952?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522160600972619725222450945%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fblog.%2522%257D&request_id=160600972619725222450945&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~blog~first_rank_v2~rank_blog_default-14-104024952.pc_v2_rank_blog_default&utm_term=%E5%8C%97%E4%BA%AC54+wgs84+java&spm=1018.2118.3001.4450
 *
 * @author houzhiwei
 * @date 2020/11/22 10:04
 */
public class ChinaProjections {
    //bj54
    //xian 80
    //cgcs2000
    // Beijing 1954 / 3-degree Gauss-Kruger CM 117E (code 2436)
//Xian 1980 / 3-degree Gauss-Kruger zone 39 (code 2363).

    public static void wgs84ToBeijing54(double lon, double lat) {

    }

    /**
     * 根据已知的（3度带） EPSG 获取相应的高斯克吕格投影系统
     * Gets Beijing 54 Gauss-Kruger.
     * e.g.,https://epsg.io/2415-15919
     *
     * @param epsg the epsg
     * @return the beijing 54 Gauss-Kruger
     */
    public static CoordinateReferenceSystem getBeijing54GaussKruger(int epsg) {
        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem crs = factory.createFromName("EPSG:" + epsg);
        String name = "Beijing 1954 / 3-degree Gauss-Kruger zone "
                + GaussKruger.getCentralLongitude3((int) crs.getProjection().getProjectionLongitudeDegrees());
        return new CoordinateReferenceSystem(name, crs.getParameters(), crs.getDatum(), crs.getProjection());
    }

    public static CoordinateReferenceSystem getBeijing54GaussKruger(double lon) {
        CRSFactory factory = new CRSFactory();
        String name = "Beijing 1954 / 3-degree Gauss-Kruger CM " + GaussKruger.centralLongitude3(lon) + "E";
        int centralLon = (int) GaussKruger.centralLongitude3(lon);
        // 根据 epsg 数据库
        int epsg = (centralLon - 75) / 3 + 2422;
        CoordinateReferenceSystem crs = factory.createFromName("EPSG:" + epsg);
        return new CoordinateReferenceSystem(name, crs.getParameters(), crs.getDatum(), crs.getProjection());
    }

    public static CoordinateReferenceSystem gcsXian80() {
        CRSFactory factory = new CRSFactory();
        return factory.createFromName("EPSG:4610");
    }

    /**
     * Gcs beijing 54 spatial reference.
     * http://epsg.io/4214
     *
     * @return the spatial reference
     */
    public static CoordinateReferenceSystem gcsBeijing54() {
        CRSFactory factory = new CRSFactory();
        return factory.createFromName("EPSG:4214");
    }

    public static CoordinateReferenceSystem gcsCgcs2000() {
        CRSFactory factory = new CRSFactory();
        //default is epsg
        return factory.createFromName("4490");
    }

}
