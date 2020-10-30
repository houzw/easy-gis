package org.egc.ows.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.egc.ows.commons.Consts;
import org.egc.ows.commons.HttpUtils;
import org.egc.ows.enums.ServiceTypeEnum;

import javax.xml.bind.JAXB;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author houzhiwei
 * @date 2020/8/25 19:40
 */
@Slf4j
public class WFSClientOld implements OWSClientOld {

    @Override
    public URIBuilder serviceBuilder() throws URISyntaxException {
        return buildService(this.serviceRoot, ServiceTypeEnum.WFS, this.version.getVersion());
    }

    public enum Version {
        Version_100("1.0.0"),
        Version_110("1.1.0"),
        Version_200("2.0.0"),
        Version_30("3.0");

        public String getVersion() {
            return version;
        }

        private String version;

        Version(String version) {
            this.version = version;
        }
    }


    public String getServiceRoot() {
        return serviceRoot;
    }

    public Version getVersion() {
        return version;
    }

    private String serviceRoot;
    private WFSClientOld.Version version;

    public WFSClientOld(String serviceRoot, WFSClientOld.Version version) {
        this.serviceRoot = serviceRoot;
        this.version = version;
    }

    public net.opengis.wfs.WFSCapabilitiesType getCapabilities() {
        net.opengis.wfs.WFSCapabilitiesType wct = null;
        try {
            URI uri = buildRequest(serviceBuilder(), Consts.GET_CAPABILITIES).build();
            String xml = HttpUtils.doGetString(uri);
            if (this.version.equals(Version.Version_100)) {
                wct = JAXB.unmarshal(xml, net.opengis.wfs.WFSCapabilitiesType.class);
            }
        } catch (URISyntaxException e) {
            log.error("Request url error", e);
        }
        return wct;
    }

    public net.opengis.wfs20.WFSCapabilitiesType getCapabilities20() {
        net.opengis.wfs20.WFSCapabilitiesType wct20 = null;
        try {
            URI uri = buildRequest(serviceBuilder(), Consts.GET_CAPABILITIES).build();
            String xml = HttpUtils.doGetString(uri);
            if (this.version.equals(Version.Version_200)) {
                wct20 = JAXB.unmarshal(xml, net.opengis.wfs20.WFSCapabilitiesType.class);
            }
        } catch (URISyntaxException e) {
            log.error("Request url error", e);
        }
        return wct20;
    }

    public void describeFeatureType(String typename, String format) {
        try {
            URI uri = buildParameters(serviceBuilder(),
                    "DescribeFeatureType",
                    new BasicNameValuePair("TYPENAME", typename),
                    new BasicNameValuePair(Consts.OUTPUT_FORMAT, format));

        } catch (URISyntaxException e) {
            log.error(e.getMessage());
        }
    }

    public String getFeature(String typename, int featureId, String outputFormat) {
        return getFeature(typename, featureId, outputFormat, 0, null, null);
    }

    /**
     * Gets getFeature request url.
     *
     * @param typename     the type list. e.g.,
     * @param featureId    the feature id
     * @param outputFormat the output format
     * @param maxFeatures  the max features, "0" means not set
     * @param filter       the filter
     * @param propertyList the property list, "*" means all.                     e.g., (1) parameter=item1,item2,item3                     (2)
     * @return the feature
     */
    public String getFeature(String typename, int featureId, String outputFormat, int maxFeatures, String filter, String propertyList) {
        String url = null;
        try {
            URIBuilder uriBuilder = buildRequest(serviceBuilder(), "GetFeature");
            if (StringUtils.isNotBlank(typename)) {
                uriBuilder.addParameter("TYPENAME", typename);
            }
            if (maxFeatures > 0) {
                uriBuilder.addParameter("MAXFEATURES", String.valueOf(maxFeatures));
            }
            if (featureId > 0) {
                uriBuilder.addParameter("FEATUREID", String.valueOf(featureId));
            }
            if (StringUtils.isNotBlank(propertyList)) {
                uriBuilder.addParameter(Consts.PROPERTY_NAME, propertyList);
            }
            if (StringUtils.isNotBlank(filter)) {
                uriBuilder.addParameter(Consts.FILTER, filter);
            }
            if (StringUtils.isNotBlank(outputFormat)) {
                uriBuilder.addParameter(Consts.OUTPUT_FORMAT, outputFormat);
            }
            url = uriBuilder.build().toString();
        } catch (URISyntaxException e) {
            log.error(e.getMessage());
        }
        return url;
    }
}
