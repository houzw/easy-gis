package org.egc.gis.gdal.crs;

import lombok.extern.slf4j.Slf4j;
import org.gdal.osr.SpatialReference;

/**
 * @author houzhiwei
 * @date 2020/7/15 12:35
 */
@Slf4j
public class ProjectionsInChina {
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
}
