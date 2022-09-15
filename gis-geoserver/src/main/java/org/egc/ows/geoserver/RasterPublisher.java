package org.egc.ows.geoserver;

import it.geosolutions.geoserver.rest.encoder.GSResourceEncoder;

import java.io.File;
import java.io.IOException;

public class RasterPublisher extends GSManager {
   /* public void raster() throws MalformedURLException {
        new GeoServerRESTStructuredGridCoverageReaderManager(new URL(""), "", "");
    }*/

    String storeName = "testRESTStoreGeotiff";
    String layerName = "resttestdem";

    public void publishGeoTiff(File geotiff) throws IOException {

        //File tiff = new ClassPathResource("testdata/resttestdem.tif").getFile();
        if (reader.getWorkspaces().isEmpty()) {
            publisher.createWorkspace(DEFAULT_WS);
        }
        reader.existsLayer(DEFAULT_WS, layerName);
        boolean pc = publisher.publishExternalGeoTIFF(DEFAULT_WS, storeName, geotiff, layerName,"EPSG:4326", GSResourceEncoder.ProjectionPolicy.FORCE_DECLARED,"raster");
        //RESTCoverageStore reloadedCS = reader.getCoverageStore(DEFAULT_WS, storeName);
        boolean pub = publisher.publishGeoTIFF(DEFAULT_WS, storeName, geotiff);
        boolean pub2 = publisher.publishGeoTIFF(DEFAULT_WS, storeName, storeName,
                geotiff, "EPSG:4326", GSResourceEncoder.ProjectionPolicy.FORCE_DECLARED, DEFAULT_WS + ":" + "defaultStyle", null);
    }
}
