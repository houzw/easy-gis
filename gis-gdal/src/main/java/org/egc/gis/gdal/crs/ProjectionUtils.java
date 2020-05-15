package org.egc.gis.gdal.crs;

import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

/**
 * Description:
 * <pre>
 * https://gdal.org/tutorials/osr_api_tut.html
 * https://stackoverflow.com/questions/343865/how-to-convert-from-utm-to-latlng-in-python-or-javascript
 *
 * Starting with GDAL 3.0,  CRS created with the “EPSG:4326” or “WGS84” strings use the latitude first, longitude second axis order.
 * set to longitude, latitude: oSRS.SetAxisMappingStrategy(OAMS_TRADITIONAL_GIS_ORDER);
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/10/15 9:43
 */
public class ProjectionUtils {

    /**
     * get Universal Transverse Mercator (UTM) Zone
     *
     * @param centralLongitude the central longitude
     * @return the utm zone
     * @link http ://www.dmap.co.uk/utmworld.htm
     */
    public static int utmZone(double centralLongitude) {
        // Math.round() 四舍五入
        // Math.ceil() 向上取整
        return (int) Math.ceil((centralLongitude + 180) / 6);
    }

    /**
     * Determines if given latitude is northern for UTM
     *
     * @param centralLatitude the central latitude
     * @return the int
     */
    public static int isNorthern(double centralLatitude) {
        return centralLatitude > 0 ? 1 : 0;
    }

    /**
     * TODO test
     *
     * @param centralLon
     * @param centralLat
     * @return
     */
    public static SpatialReference getWGS84UTM(double centralLon, double centralLat) {
        SpatialReference utm = new SpatialReference();
        //Set geographic coordinate system to handle lat/lon
        utm.SetWellKnownGeogCS("WGS84");
        utm.SetUTM(utmZone(centralLon), isNorthern(centralLat));
        return utm;
    }

    public static double[] transformWGS84ToUTM(double lon, double lat) {
        SpatialReference utm = new SpatialReference();
        //Set geographic coordinate system to handle lat/lon
        utm.SetWellKnownGeogCS("WGS84");
        utm.SetUTM(utmZone(lon), isNorthern(lat));
        ////# Clone ONLY the geographic coordinate system
        SpatialReference wgs84 = utm.CloneGeogCS();
        CoordinateTransformation transformation = CoordinateTransformation.CreateCoordinateTransformation(wgs84, utm);
        //# returns easting, northing, altitude
        return transformation.TransformPoint(lon, lat);
    }

    public static double[] transformUTMToWGS84(double easting, double northing, int zone) {
        SpatialReference utm = new SpatialReference();
        //Set geographic coordinate system to handle lat/lon
        utm.SetWellKnownGeogCS("WGS84");
        utm.SetUTM(zone, northing > 0 ? 1 : 0);
        // Clone ONLY the geographic coordinate system
        SpatialReference wgs84 = utm.CloneGeogCS();
        //wgs84.SetDataAxisToSRSAxisMapping(gdalconst.OAMS_TRADITIONAL_GIS_ORDER);
        CoordinateTransformation transformation = CoordinateTransformation.CreateCoordinateTransformation(utm, wgs84);
        // returns lon,lat, altitude
        return transformation.TransformPoint(easting, northing, 0);
    }
}
