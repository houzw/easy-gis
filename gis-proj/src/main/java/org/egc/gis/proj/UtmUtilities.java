package org.egc.gis.proj;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * Universal Transverse Mercator (UTM) Utilities
 * 通用横轴墨卡托
 * @author houzhiwei
 * @date 2020/5/13 20:50
 */
@Slf4j
public class UtmUtilities {
    static final String WGS84 = "WGS84";

    public static int getCenteralLongitude(int zone) {
        return (6 * zone - 3) - 180;
    }

    /**
     * get Universal Transverse Mercator (UTM) Zone (6)
     *
     * @param longitude the longitude
     * @return the utm zone
     * @link http://www.dmap.co.uk/utmworld.htm
     */
    public static int utmZone(double longitude) {
        return (int) Math.floor((longitude / 6) + 31);
    }

    /**
     * Determines if given latitude is northern (N) for UTM
     *
     * @param latitude the latitude
     * @return the int
     */
    public static int isNorthern(double latitude) {
        return latitude > 0 ? 1 : 0;
    }


    /**
     * Gets utm epsg.
     * https://gis.stackexchange.com/questions/365584/convert-utm-zone-into-epsg-code
     *
     * @param lon the lon
     * @param lat the lat
     * @return the utm epsg
     */
    public static int getUtmEpsg(double lon, double lat) {
        int epsgCode = 32600;
        epsgCode += utmZone(lon);
        if (lat < 0) {
            epsgCode += 100;
        }
        return epsgCode;
    }

}
