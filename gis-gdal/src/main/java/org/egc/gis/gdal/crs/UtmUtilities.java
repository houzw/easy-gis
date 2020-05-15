package org.egc.gis.gdal.crs;

import lombok.extern.slf4j.Slf4j;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

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

    /**
     * get Universal Transverse Mercator (UTM) Zone (6)
     *
     * @param centralLongitude the central longitude
     * @return the utm zone
     * @link http://www.dmap.co.uk/utmworld.htm
     */
    public static int utmZone(double centralLongitude) {
        // Math.round() 四舍五入
        // Math.ceil() 向上取整
        // 或者 (int)(Math.ceil(centralLongitude / 6)+31)
        return (int) Math.ceil((centralLongitude + 180) / 6);
    }

    /**
     * Determines if given latitude is northern (N) for UTM
     *
     * @param centralLatitude the central latitude
     * @return the int
     */
    public static int isNorthern(double centralLatitude) {
        return centralLatitude > 0 ? 1 : 0;
    }

    /**
     * TODO test
     * WGS 84 datum UTM Zone
     *
     * @param centralLon
     * @param centralLat
     * @return
     */
    public static SpatialReference getWgs84Utm(double centralLon, double centralLat) {
        SpatialReference utm = new SpatialReference();
        //Set geographic coordinate system to handle lat/lon
        utm.SetWellKnownGeogCS(WGS84);
        utm.SetUTM(utmZone(centralLon), isNorthern(centralLat));
        return utm;
    }

    /**
     * Transform wgs 84 to utm double [ ].
     *
     * @param lon the lon
     * @param lat the lat
     * @return easting, northing, altitude
     */
    public static double[] transformWgs84ToUtm(double lon, double lat) {
        SpatialReference utm = new SpatialReference();
        //Set geographic coordinate system to handle lat/lon
        utm.SetWellKnownGeogCS(WGS84);
        utm.SetUTM(utmZone(lon), isNorthern(lat));
        ////# Clone ONLY the geographic coordinate system
        SpatialReference wgs84 = utm.CloneGeogCS();
        CoordinateTransformation transformation = CoordinateTransformation.CreateCoordinateTransformation(wgs84, utm);
        return transformation.TransformPoint(lon, lat);
    }

    /**
     * Transform utm to wgs 84.
     *
     * @param easting  the easting
     * @param northing the northing
     * @param zone     the zone
     * @return lon, lat, altitude
     */
    public static double[] transformUtmToWgs84(double easting, double northing, int zone) {
        SpatialReference utm = new SpatialReference();
        //Set geographic coordinate system to handle lat/lon
        utm.SetWellKnownGeogCS(WGS84);
        utm.SetUTM(zone, northing > 0 ? 1 : 0);
        // Clone ONLY the geographic coordinate system
        SpatialReference wgs84 = utm.CloneGeogCS();
        //wgs84.SetDataAxisToSRSAxisMapping(gdalconst.OAMS_TRADITIONAL_GIS_ORDER);
        CoordinateTransformation transformation = CoordinateTransformation.CreateCoordinateTransformation(utm, wgs84);
        return transformation.TransformPoint(easting, northing, 0);
    }

}
