package org.egc.gis.proj;

import org.apache.commons.lang3.StringUtils;
import org.locationtech.jts.geom.Coordinate;

/**
 * @author houzhiwei
 * @date 2020/11/26 19:02
 */
public class ProjUtilities {

    /**
     * Converting degrees-minutes-seconds values to decimal degree values
     *
     * @param degree the degree
     * @param minute the minute
     * @param second the second
     * @return double  decimal degree
     */
    public static double dms2dd(int degree, int minute, double second) {
        //DD = (Seconds/3600) + (Minutes/60) + Degrees
        //DD = - (Seconds/3600) - (Minutes/60) + Degrees
        if (degree > 0) {
            return (second / 3600.0) + (minute / 60.0) + degree;
        } else {
            return -(second / 3600.0) - (minute / 60.0) + degree;
        }
    }

    /**
     * decimal degree to degrees-minutes-seconds.
     *
     * @param dd the dd
     * @return the double [], d,m,s
     */
    public static double[] dd2dms(double dd) {
        int d = (int) dd;
        double m = Math.abs(dd - d) * 60;
        double sd = (dd - d - m / 60) * 3600;
        return new double[]{d, m, sd};
    }

    /**
     * Parse degrees-minutes-seconds to decimal degree.
     * e.g., "78°55'44.29458\"N" or "119d17'13.26\"E"
     * https://www.ubergizmo.com/how-to/read-gps-coordinates/
     *
     * @param dms the dms
     * @return double
     */
    public static double parseDms2dd(String dms) {
        dms = StringUtils.deleteWhitespace(dms);
        String[] parts = dms.split("[°d'\"]+");
        System.out.println(parts.length);
        if (parts.length < 3) {
            throw new RuntimeException("Format of " + dms + " is invalid!");
        }
        if (parts.length == 4) {
            if ("S".equalsIgnoreCase(parts[3]) || "W".equalsIgnoreCase(parts[3])) {
                return dms2dd(-Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Double.parseDouble(parts[2]));
            } else {
                return dms2dd(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Double.parseDouble(parts[2]));
            }
        } else {
            return dms2dd(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Double.parseDouble(parts[2]));
        }
    }

    /**
     * At the equator for longitude and for latitude anywhere, the following approximations are valid:
     *
     * <pre>
     * 1° = 111 km  (or 60 nautical miles)
     * 0.1° = 11.1 km
     * 0.01° = 1.11 km (2 decimals, km accuracy)
     * 0.001° =111 m
     * 0.0001° = 11.1 m
     * 0.00001° = 1.11 m
     * 0.000001° = 0.11 m (7 decimals, cm accuracy)
     * <pre>
     * 1' = 1.85 km  (or 1 nautical mile)
     * 0.1' = 185 m
     * 0.01' = 18.5 m
     * 0.001' = 1.85 m
     * <pre>
     * 30" = 900 m
     * 15" = 450 m
     * 3" = 90 m
     * 1" = 30 m
     * 1/3" = 10 m
     * 0.1" = 3 m
     * 1/9" = 3 m
     * 1/27" = 1 m
     * https://www.usna.edu/Users/oceano/pguth/md_help/html/approx_equivalents.htm
     * @param resolutionInDegrees the resolution in degree (DDD, NOTDMS)
     * @return the resolution in meters
     */
    public static double degree2meters(double resolutionInDegrees) {
        return resolutionInDegrees * 111000;
    }

    /**
     * Arc second 2 degree.
     * 弧秒到度
     * @param resolutionInArcsecond the resolution in arcsecond
     * @return the double
     */
    public static double arcsecond2degree(double resolutionInArcsecond) {
        return Math.toDegrees(2 * Math.PI / (360 * 60 * 60) * resolutionInArcsecond);
    }

    /**
     * spatial resolution from degrees.<br/>
     * Sphere distance double.
     * https://www.usna.edu/Users/oceano/pguth/md_help/html/approx_equivalents.htm
     * [Haversine formula](http://en.wikipedia.org/wiki/Haversine_formula)<br/>
     * https://community.esri.com/t5/coordinate-reference-systems/distance-on-a-sphere-the-haversine-formula/ba-p/902128
     * https://github.com/jasonwinn/haversine/blob/master/Haversine.java
     *
     * @param coord1 the coord 1
     * @param coord2 the coord 2
     * @return the double
     */
    public static double sphereDistance(Coordinate coord1, Coordinate coord2) {
        //radius of Earth in meters
        int R = 6371000;
        // Coordinates in decimal degrees (e.g. 2.89078, 12.79797)
        double lon1 = coord1.x;
        double lat1 = coord1.y;
        double lon2 = coord2.x;
        double lat2 = coord2.y;
        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);

        double deltaPhi = Math.toRadians(lat2 - lat1);
        double deltaLambda = Math.toRadians(lon2 - lon1);
        //haversin(d/R)=haversin(dLat)+cos(phi1)cos(phi2)haversin(dLon)
        double a = haversin(deltaPhi) + Math.cos(phi1) * Math.cos(phi2) * haversin(deltaLambda);
        return 2 * R * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    public static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}
