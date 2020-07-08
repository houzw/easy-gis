package org.egc.gis.geotools.formats;

import lombok.extern.slf4j.Slf4j;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.gml2.GML;
import org.geotools.gml2.GMLConfiguration;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Encoder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.opengis.feature.simple.SimpleFeature;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author houzhiwei
 * @date 2020/5/17 0:30
 */
@Slf4j
public class FormatConversion {
    // https://gis.stackexchange.com/questions/53863/what-is-the-best-way-to-convert-wkt-string-to-gml-string

    public static String wkt2Gml2(String wkt) throws IOException, ParseException {
        WKTReader wktR = new WKTReader();
        Geometry geom = wktR.read(wkt);
        Configuration configuration = new GMLConfiguration();
        Encoder encoder = new Encoder(configuration);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        encoder.encode(geom, GML._Geometry, out);
        return out.toString();
    }

    public static String wktToGml3(String wkt) throws IOException, ParseException {
        WKTReader wktR = new WKTReader();
        Geometry geom = wktR.read(wkt);
        Configuration configuration = new org.geotools.gml3.GMLConfiguration();
        Encoder encoder = new Encoder(configuration);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        encoder.encode(geom, org.geotools.gml3.GML.geometryMember, out);
        return out.toString();
    }

    public static String feature2Wkt(SimpleFeatureCollection collection) {
        WKTWriter wktWriter = new WKTWriter();
        StringBuilder sb = new StringBuilder();
        try (SimpleFeatureIterator itr = collection.features()) {
            while (itr.hasNext()) {
                SimpleFeature feature = itr.next();
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                sb.append(wktWriter.write(geometry));
            }
            itr.close();
        }
        return sb.toString();
    }

}
