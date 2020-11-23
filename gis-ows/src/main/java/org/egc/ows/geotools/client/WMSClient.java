package org.egc.ows.geotools.client;

import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.MultithreadedHttpClient;
import org.geotools.ows.wms.WMSCapabilities;
import org.geotools.ows.wms.WebMapServer;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * @author houzhiwei
 * @date 2020/11/18 16:31
 */
public class WMSClient {
    private String serviceEndpoint;
    private WebMapServer wms;

    private WMSCapabilities capabilities;

    public URL getServerUrl() {
        return serverUrl;
    }

    //"http://127.0.0.1:8080/geoserver/ows?SERVICE=WMS&";
    private URL serverUrl;

    public WMSClient(URL serverUrl) throws IOException, ServiceException {
        this.serverUrl = serverUrl;
        wms = new WebMapServer(serverUrl, new MultithreadedHttpClient());
    }

    public WMSCapabilities getCapabilities() {
        this.capabilities = wms.getCapabilities();
        return this.capabilities;
    }

    public List<Layer> getLayerList() {
        return this.capabilities.getLayerList();
    }
}
