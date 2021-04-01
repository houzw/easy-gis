package org.egc.gis.gdal.crs;

import lombok.extern.slf4j.Slf4j;
import org.gdal.osr.SpatialReference;

/**
 * @author houzhiwei
 * @date 2020/7/15 12:35
 */
@Slf4j
public class ChinaProjections {
    String ITRF2000;

    public static void wgs842ITRF2000() {

    }

    public static SpatialReference gcsXian80() {
        SpatialReference xian80 = new SpatialReference();
        xian80.ImportFromEPSG(4610);
        return xian80;
    }

    /**
     * Gets Beijing 54 Gauss-Kruger.
     * e.g.,https://epsg.io/2415-15919
     *
     * @param zone3 the zone 3
     * @return the beijing 54 Gauss-Kruger
     */
    public static SpatialReference getBeijing54GK(int zone3) {
        String name = "Beijing 1954 / 3-degree Gauss-Kruger zone " + zone3;
        SpatialReference bj54 = gcsBeijing54();
        bj54.SetFromUserInput(name);
//        gdal.SetConfigOption("GDAL_DATA", "gdaldata");
        //clat:centerLat/latitude_of_origin
        //clon:centerLong/central_meridian
        //scale:scale_factor
        //fe:false_easting:带号+“500000”
        //fn:false_northing
        //Transverse_Mercator
        bj54.SetTM(0, getCentralLongitude3(zone3), 1, Integer.parseInt(zone3 + "500000"), 0);
        return bj54;
    }

    public static int getCentralLongitude3(int zone3) {
        return zone3 * 3;
    }

    /**
     * Gcs beijing 54 spatial reference.
     * http://epsg.io/4214
     *
     * @return the spatial reference
     */
    public static SpatialReference gcsBeijing54() {
        SpatialReference bj54 = new SpatialReference();
        bj54.ImportFromEPSG(4214);
        return bj54;
    }

    public static SpatialReference gcsCgcs2000() {
        SpatialReference cgcs = new SpatialReference();
        cgcs.ImportFromEPSG(4490);
        return cgcs;
    }

    public static SpatialReference krasovsky1940Albers() {
        SpatialReference albers = new SpatialReference();
        // 阿尔伯斯投影，正轴等积割圆锥投影
       /* String pszGeogName, 地理坐标系名称
         String pszDatumName, 基准面
         String pszEllipsoidName, 椭球体
         double dfSemiMajor, 半长轴
         double dfInvFlattening, 扁率
         String pszPMName, 本初子午线名称
         double dfPMOffset,
         String pszUnits,单位
         double dfConvertToRadians*/
        albers.SetGeogCS("GCS_Krasovsky_1940", "D_Krasovsky_1940", "Krasovsky_1940",
                6378245, 298.3,
                "Greenwich", 0,
                "degree", 0.0174532925199433);
        albers.SetAuthority("SPHEROID", "EPSG", 7024);
        albers.SetAuthority("DATUM", "EPSG", 6024);
        albers.SetProjCS("Krasovsky_1940_Albers");
        albers.SetProjection("Albers_Conic_Equal_Area");
        //标准纬线
        albers.SetProjParm("standard_parallel_1", 25);
        albers.SetProjParm("standard_parallel_2", 47);
        // latitude of origin
        albers.SetProjParm("latitude_of_center", 0);
        // central meridian
        albers.SetProjParm("longitude_of_center", 105);
        albers.SetProjParm("false_easting", 0);
        albers.SetProjParm("false_northing", 0);
        albers.SetLinearUnits("metre", 1);
        albers.SetAuthority("UNIT", "EPSG", 9001);
        return albers;
    }

}
