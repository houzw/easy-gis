package org.egc.ows.client;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.egc.ows.commons.Consts;
import org.egc.ows.enums.ServiceTypeEnum;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author houzhiwei
 * @date 2020/8/25 20:40
 */
@Deprecated
public interface OWSClientOld {

    /**
     * Service builder uri builder.
     *
     * @return the uri builder
     * @throws URISyntaxException the uri syntax exception
     */
    URIBuilder serviceBuilder() throws URISyntaxException;

    /**
     * get service uri builder.
     *
     * @param serviceRoot the service root
     * @param service     the service type
     * @param version     the version
     * @return uri builder
     * @throws URISyntaxException the uri syntax exception
     */
    default URIBuilder buildService(String serviceRoot, ServiceTypeEnum service, String version) throws URISyntaxException {
        return new URIBuilder(serviceRoot)
                .setParameter(Consts.SERVICE, service.name())
                .setParameter(Consts.VERSION, version);
    }

    /**
     * Build request uri builder.
     *
     * @param builder the builder
     * @param request the request
     * @return the uri builder
     */
    default URIBuilder buildRequest(URIBuilder builder, String request) {
        return builder.setParameter(Consts.REQUEST, request);
    }

    /**
     * Build parameters uri.
     *
     * @param builder the builder
     * @param request the request
     * @param nvps    the NameValuePairs
     * @return the request uri
     * @throws URISyntaxException the uri syntax exception
     */
    default URI buildParameters(URIBuilder builder, String request, NameValuePair... nvps) throws URISyntaxException {
        return builder.setParameter(Consts.REQUEST, request).setParameters(nvps).build();
    }
}
