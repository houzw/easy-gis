package org.egc.gis.gdal.crs;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.gis.gdal.IOFactory;
import org.gdal.gdal.Dataset;
import org.gdal.ogr.DataSource;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
@Slf4j
public class ProjectionUtils {

    private static final String CRS_PATTERN = "(PROJCS|GEOGCS)[\"[0-9a-zA-Z_(.+/)-]+(?=\")";

    public static int lookupEsriEpsg(String crsLookupName) {
        URL resource = ProjectionUtils.class.getClassLoader().getResource("esri_epsg.wkt");
        try {
            assert resource != null;
            String file = resource.getFile();
            List<String> strings = FileUtils.readLines(new File(file), StandardCharsets.UTF_8);
            for (String line : strings) {
                if (StringUtils.isBlank(line) || line.startsWith("#")) {
                    continue;
                }
                String[] temp = line.split(",", 1);
                String wkt = temp[1];
                String epsg = temp[0];
                Pattern p = Pattern.compile(CRS_PATTERN);
                Matcher matcher = p.matcher(wkt);
                String crsName = matcher.group();
                crsName = crsName.replace("(PROJCS|GEOGCS)\\[\\\"", "");
                if (crsLookupName.equalsIgnoreCase(crsName)) {
                    return Integer.parseInt(epsg);
                }
            }
        } catch (IOException | NullPointerException e) {
            log.error(e.getLocalizedMessage());
        }
        return 0;
    }

    public static String getProj4(String vectorFile) {
        DataSource ds = IOFactory.createVectorIO().read(vectorFile);
        return ds.GetLayer(0).GetSpatialRef().ExportToProj4();
    }

    public static String getRasterProj4(String rasterFile) {
        Dataset ds = IOFactory.createRasterIO().read(rasterFile);
        SpatialReference sr = new SpatialReference();
        sr.ImportFromWkt(ds.GetProjection());
        return sr.ExportToProj4();
    }

    public static String getRasterProjWkt(String rasterFile) {
        Dataset ds = IOFactory.createRasterIO().read(rasterFile);
        return ds.GetProjection();
    }

    public static String getProjectionWkt(String vectorFile) {
        DataSource ds = IOFactory.createVectorIO().read(vectorFile);
        return ds.GetLayer(0).GetSpatialRef().ExportToWkt();
    }

    public static SpatialReference createSpatialReference(int epsg) {
        SpatialReference srs = new SpatialReference();
        if (epsg <= 0) {
            log.error("Invalid EPSG code {}", epsg);
            throw new RuntimeException("Invalid EPSG code " + epsg + ". Must larger than 0");
        }
        srs.ImportFromEPSG(epsg);
        return srs;
    }

    public static SpatialReference createSpatialReference(String wktCrs) {
        SpatialReference srs = new SpatialReference();
        if (StringUtils.isBlank(wktCrs)) {
            log.error("Invalid projection string");
            throw new RuntimeException("Invalid projection string Must larger than 0");
        }
        srs.ImportFromWkt(wktCrs);
        return srs;
    }

    public static SpatialReference createSpatialReferenceFromProj(String proj4Str) {
        SpatialReference srs = new SpatialReference();
        if (StringUtils.isBlank(proj4Str)) {
            log.error("Invalid projection string");
            throw new RuntimeException("Invalid projection string Must larger than 0");
        }
        srs.ImportFromProj4(proj4Str);
        return srs;
    }

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

    public static int utmZone(double minLon, double maxLon) {
        // Math.round() 四舍五入
        // Math.ceil() 向上取整
        return (int) Math.ceil(((maxLon - minLon) / 2 + 180) / 6);
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
     * @param centralLon the central lon
     * @param centralLat the central lat
     * @return wgs 84 utm
     */
    public static SpatialReference getWgs84Utm(double centralLon, double centralLat) {
        SpatialReference utm = new SpatialReference();
        //Set geographic coordinate system to handle lat/lon
        utm.SetWellKnownGeogCS("WGS84");
        utm.SetUTM(utmZone(centralLon), isNorthern(centralLat));
        return utm;
    }

    public static double[] transformWgs84ToUtm(double lon, double lat) {
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

    /**
     * Transform utm to wgs 84 double [ ].
     *
     * @param x    the x
     * @param y    the y
     * @param zone the zone
     * @return the double [ ]
     */
    public static double[] transformUtmToWgs84(double x, double y, int zone) {
        SpatialReference utm = new SpatialReference();
        //Set geographic coordinate system to handle y/x
        utm.SetWellKnownGeogCS("WGS84");
        utm.SetUTM(zone, y > 0 ? 1 : 0);
        // Clone ONLY the geographic coordinate system
        SpatialReference wgs84 = utm.CloneGeogCS();
        //wgs84.SetDataAxisToSRSAxisMapping(gdalconst.OAMS_TRADITIONAL_GIS_ORDER);
        CoordinateTransformation transformation = CoordinateTransformation.CreateCoordinateTransformation(utm, wgs84);
        // returns x,y, altitude
        return transformation.TransformPoint(x, y, 0);
    }
}
