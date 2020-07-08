package org.egc.gis.geotools.formats;

import lombok.extern.slf4j.Slf4j;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.geojson.GeoJSONDataStoreFactory;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.util.URLs;
import org.locationtech.jts.geom.GeometryCollection;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author houzhiwei
 * @date 2020/5/17 0:35
 */
@Slf4j
public class GeoJson {

    public static SimpleFeatureCollection readGeoJson(String geojsonFile) throws IOException {
        FeatureJSON json = new FeatureJSON();
        json.setEncodeNullValues(true);
        InputStreamReader streamReader = new InputStreamReader(new FileInputStream(geojsonFile), StandardCharsets.UTF_8);
        return (SimpleFeatureCollection) json.readFeatureCollection(streamReader);
    }

    public static SimpleFeatureCollection readGeoJSONWithStore(String geojsonFile) throws IOException {
        File inFile = new File(geojsonFile);
        Map<String, Object> params = new HashMap<>();
        params.put(GeoJSONDataStoreFactory.URLP.key, URLs.fileToUrl(inFile));
        DataStore jsonStore = DataStoreFinder.getDataStore(params);
        SimpleFeatureSource featureSource = jsonStore.getFeatureSource(jsonStore.getTypeNames()[0]);
        return featureSource.getFeatures();
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
    public static GeometryCollection readGeoJSONString(String json) throws IOException {
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
        FeatureJSON json = new FeatureJSON(new GeometryJSON(15));
        StringWriter writer = new StringWriter();
        json.writeFeatureCollection(collection, writer);
        String s = writer.toString();
        writer.close();
        return s;
    }
}
