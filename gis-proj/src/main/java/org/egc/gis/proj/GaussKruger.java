package org.egc.gis.proj;

import lombok.extern.slf4j.Slf4j;

/**
 * 高斯克吕格
 * https://www.bbsmax.com/A/A2dmRLoxze/
 *
 * @author houzhiwei
 * @date 2020/5/13 21:29
 */
@Slf4j
public class GaussKruger {

    /**
     * <pre>
     * Gets central longitude.
     * long < 0 means west
     *
     * @param zone6 the zone in 6 degree
     * @return the central longitude
     */
    public static int getCentralLongitude6(int zone6) {
        if (zone6 < 30) {
            //east
            return 6 * zone6 - 3;
        } else {
            //west
            return 6 * zone6 - 3 - 360;
        }
    }

    public static int getCentralLongitude3(int zone3) {
        return zone3 * 3;
    }

    public static int getZone3(double longitude) {
        return (int) (longitude + 1.5) / 3;
    }

    public static double centralLongitude3(double longitude) {
        return getCentralLongitude3(getZone3(longitude));
    }

    public static int getZone6(double longitude) {
        return (int) (longitude + 6) / 6;
    }


}
