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
     * Utm zone int. <br/>
     * NOTE: not for large area
     * @param minLon the min lon
     * @param maxLon the max lon
     * @return the int
     */
    public static int utmZone(double minLon, double maxLon) {
        double centeral = (maxLon-minLon)/2;
        return (int) Math.floor((centeral / 6) + 31);
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
     * TODO test
     * WGS 84 datum UTM Zone
     *
     * @param lon the central lon
     * @param lat the central lat
     * @return wgs 84 utm
     */
    public static SpatialReference getWgs84Utm(double lon, double lat) {
        SpatialReference utm = new SpatialReference();
        //Set geographic coordinate system to handle lat/lon
        utm.SetWellKnownGeogCS(WGS84);
        utm.SetUTM(utmZone(lon), isNorthern(lat));
        utm.GetAuthorityCode(null);
        return utm;
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
