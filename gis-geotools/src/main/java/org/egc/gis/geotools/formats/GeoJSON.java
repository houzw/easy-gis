package org.egc.gis.geotools.formats;

import lombok.extern.slf4j.Slf4j;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.GeometryCollection;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author houzhiwei
 * @date 2020/5/17 0:35
 */
@Slf4j
public class GeoJSON {

    public static FeatureCollection readGeoJSONFile(String geojsonFile) throws IOException {
        FeatureJSON featureJSON = new FeatureJSON();
        featureJSON.setEncodeNullValues(true);
        InputStreamReader streamReader = new InputStreamReader(new FileInputStream(geojsonFile), "utf-8");
        return featureJSON.readFeatureCollection(streamReader);
    }

    /**
     * 若确定类型，可以直接转换
     * <pre>
     * {@code
     *      Point p = gjson.readPoint(reader);
     * }
     * @param json GeoJSON string
     * @return
     * @throws IOException
     */
    public static GeometryCollection readGeoJSON(String json) throws IOException {
        GeometryJSON gjson = new GeometryJSON();
        Reader reader = new StringReader(json);
        return gjson.readGeometryCollection(reader);
    }

    public boolean shp2geojson(String shpPath, String geojsonPath, Charset shpCharset) {
        boolean result = false;
        try {
            ShapefileDataStore shapefileDataStore = new ShapefileDataStore(new File(shpPath).toURI().toURL());
            shapefileDataStore.setCharset(shpCharset);
            ContentFeatureSource featureSource = shapefileDataStore.getFeatureSource();
            ContentFeatureCollection contentFeatureCollection = featureSource.getFeatures();
            FeatureJSON featureJSON = new FeatureJSON(new GeometryJSON(15));
            featureJSON.writeFeatureCollection(contentFeatureCollection, new File(geojsonPath));
            shapefileDataStore.dispose();
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public static String toGeoJSON(SimpleFeatureCollection collection) throws IOException {
        FeatureJSON fjson = new FeatureJSON(new GeometryJSON(15));
        StringWriter writer = new StringWriter();
        fjson.writeFeatureCollection(collection, writer);
        return writer.toString();
    }
}
