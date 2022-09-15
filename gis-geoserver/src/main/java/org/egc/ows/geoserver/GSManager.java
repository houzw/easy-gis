package org.egc.ows.geoserver;


import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.exception.BusinessException;
import org.egc.commons.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class GSManager {

    public static final Logger log = LoggerFactory.getLogger(GSManager.class);

    // 应当从配置文件中读取

    public static final String DEFAULT_WS;
    public static final String RESTURL;
    public static final String RESTUSER;
    public static final String RESTPW;

    // geoserver target version

    public static final String GS_VERSION;
    public static URL URL;
    public static GeoServerRESTManager manager;

    public static GeoServerRESTReader reader;

    public static GeoServerRESTPublisher publisher;

    static {
        Properties properties = PropertiesUtil.readPropertiesFromConfig("geoserver");

        String defaultWs = properties.getProperty("default_ws");
        DEFAULT_WS = StringUtils.isBlank(defaultWs) ? "global" : defaultWs;

        RESTURL = properties.getProperty("rest_url");
        RESTUSER = properties.getProperty("rest_user");
        RESTPW = properties.getProperty("rest_pw");

        String version = properties.getProperty("version");
        GS_VERSION = StringUtils.isBlank(version) ? "2.13.1" : version;

        if (StringUtils.isBlank(RESTURL) || StringUtils.isBlank(RESTUSER) || StringUtils.isBlank(RESTPW)) {
            throw new BusinessException("Wrong GeoServer Configurations! Please CHECK!");
        }

        try {
            URL = new URL(RESTURL);
            manager = new GeoServerRESTManager(URL, RESTUSER, RESTPW);
            reader = manager.getReader();
            publisher = manager.getPublisher();
        } catch (MalformedURLException e) {
            throw new BusinessException(e, "Cannot initialize GeoServer!", true);
        }
    }

}
