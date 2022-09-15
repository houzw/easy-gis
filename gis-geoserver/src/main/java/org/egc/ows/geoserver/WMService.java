package org.egc.ows.geoserver;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.GSResourceEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

/**
 * https://docs.geoserver.org/master/en/user/rest/index.html#rest
 * https://geoserver.geo-solutions.it/edu/en/rest/gs_manager.html
 */
public class WMService {
    String RESTURL = "http://localhost:8083/geoserver";
    String RESTUSER = "admin";
    String RESTPW = "Geos";

    public void read() throws MalformedURLException {
        // instantiate GeoServerRESTReader for reading data from a running GeoServer instance:
        GeoServerRESTReader reader = new GeoServerRESTReader(RESTURL, RESTUSER, RESTPW);
    }

    public void publish() throws FileNotFoundException {
        //GeoServerRESTPublisher to modify the catalog (publish or remove styles, featuretypes or coverages):
        GeoServerRESTPublisher publisher = new GeoServerRESTPublisher(RESTURL, RESTUSER, RESTPW);
        boolean created = publisher.createWorkspace("myWorkspace");
        
        File sldFile = new File("");
        // Will take the name from SLD contents
        boolean published = publisher.publishStyle(sldFile);
        //or gave a name
        boolean published2 = publisher.publishStyle(sldFile, "myStyle");
        File zipFile = new File("");
        //the layerName should be the same as the shapefile in the .zip file.
        boolean published3 = publisher.publishShp("myWorkspace", "myStore", "cities", zipFile, "EPSG:4326", "default_point");

        //to remove this layer entirely from the configuration,
        // you will have to remove the layer and the datastore:
        boolean ftRemoved = publisher.unpublishFeatureType("myWorkspace", "myStore", "cities");
        // remove  datastore
        boolean dsRemoved = publisher.removeDatastore("myWorkspace", "myStore", true);
    }

    public void db() {
        GeoServerRESTPublisher publisher = new GeoServerRESTPublisher(RESTURL, RESTUSER, RESTPW);

        GSFeatureTypeEncoder fte = new GSFeatureTypeEncoder();
        fte.setProjectionPolicy(GSResourceEncoder.ProjectionPolicy.REPROJECT_TO_DECLARED);
        //_pg_kids_ is a PostGIS datastore already configured in GeoServer
        //_easia_gaul_0_aggr_ is a table that has not yet been published in GeoServer
        fte.addKeyword("KEYWORD");
        fte.setTitle("easia_gaul_0_aggr");
        fte.setName("easia_gaul_0_aggr");
        fte.setSRS("EPSG:4326");

        final GSLayerEncoder layerEncoder = new GSLayerEncoder();
        //
        layerEncoder.setDefaultStyle("default_polygon");
        publisher.configureLayer("myWorkspace", "layerName", layerEncoder);
        boolean ok = publisher.publishDBLayer("myWorkspace", "pg_kids", fte, layerEncoder);
    }
}
