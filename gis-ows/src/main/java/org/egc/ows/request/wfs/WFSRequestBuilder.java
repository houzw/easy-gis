package org.egc.ows.request.wfs;

import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

import static org.egc.ows.commons.Consts.*;

/**
 * @author houzhiwei
 * @date 2020/10/14 20:02
 */
public class WFSRequestBuilder {
    public enum Operation {
        GetCapabilities,
        DescribeFeatureType,
        GetFeature,
        Transaction,
        // v 1.1.0
        GetGmlObject,
        // v 2.0.0
        GetPropertyValue,
        ListStoredQueries,
        DescribeStoredQueries
    }

    /**
     * 返回 WFS 服务提供的要素类型列表。
     */
    public static final String DESCRIBE_FEATURE_TYPE = "DescribeFeatureType";
    public static final String GET_FEATURE = "GetFeature";
    /**
     * 返回执行事务
     */
    public static final String TRANSACTION = "Transaction";
    public static final String WFS = "WFS";
    public static final String TYPE_NAME = "TYPENAME";
    public static final String MAX_FEATURES = "MAXFEATURES";
    public static final String FEATURE_ID = "FEATUREID";
    public static final String NAMESPACES = "NAMESPACES";
    //提供者特定参数。
    public static final String VSPs = "VSPs";
    public static final String START_INDEX = "STARTINDEX";
    public static final String COUNT = "COUNT";
    //"results"和"hits"，默认值为"results"。"results"表示返回满足操作请求的完整响应文档，"hits"表示返回一个没有资源实例的空响应文档。
    public static final String RESULT_TYPE = "RESULTTYPE";
    public static final String RESOLVE = "RESOLVE";
    public static final String RESOLVE_DEPTH = "RESOLVEDEPTH";
    public static final String RESOLVE_TIMEOUT = "RESOLVETIMEOUT";
    public static final String SRS_NAME = "SRSNAME";
    public static final String ALIASES = "ALIASES";
    public static final String TYPE_NAMES = "TYPENAMES";
    public static final String FILTER = "FILTER";
    public static final String FILTER_LANGUAGE = "FILTER_LANGUAGE";
    public static final String RESOURCE_ID = "RESOURCEID";
    public static final String SORT_BY = "SORTBY";
    /**
     * 返回要素类型指定字段的属性值。
     */
    public static final String GET_PROPERTY_VALUE = "GetPropertyValue";
    /**
     * 返回 WFS 服务支持的存储查询方式列表。
     */
    public static final String LIST_STORED_QUERIES = "ListStoredQueries";
    /**
     * 返回存储查询方式的详细元数据描述。
     */
    public static final String DESCRIBE_STORED_QUERIES = "DescribeStoredQueries";

    private String serviceRoot;

    public enum Version {
        Version_100("1.0.0"),
        Version_110("1.1.0"),
        Version_200("2.0.0");

        public String getVersion() {
            return version;
        }

        private String version;

        Version(String version) {
            this.version = version;
        }
    }

    /**
     * Gets capabilities.
     * e.g., https://demo.mapserver.org/cgi-bin/wfs?SERVICE=WFS&VERSION=1.0.0&REQUEST=GetCapabilities
     *
     * @param version the version
     * @return the capabilities
     * @throws URISyntaxException the uri syntax exception
     */
    public URI getCapabilities(WFSRequestBuilder.Version version) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(this.serviceRoot);
        uriBuilder.addParameter(SERVICE, WFS);
        uriBuilder.addParameter(VERSION, version.getVersion());
        uriBuilder.addParameter(REQUEST, GET_CAPABILITIES);
        return uriBuilder.build();
    }

    public URI getFesture100(String typeName, int maxFeatures) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(this.serviceRoot);
        uriBuilder.addParameter(SERVICE, WFS);
        uriBuilder.addParameter(VERSION, Version.Version_100.getVersion());
        uriBuilder.addParameter(REQUEST, GET_FEATURE);
        uriBuilder.addParameter(TYPE_NAME, typeName);
        if (maxFeatures > 0) {
            uriBuilder.addParameter(MAX_FEATURES, String.valueOf(maxFeatures));
        }
        return uriBuilder.build();
    }
}
