package org.egc.ows.request.wfs;

import java.util.List;

import static org.egc.ows.commons.Consts.GML3;

/**
 * http://support.supermap.com.cn/DataWarehouse/WebDocHelp/iServer/API/WFS/WFS200/GetFeature/GetFeature_Request.htm
 * @author houzhiwei
 * @date 2020/10/14 20:22
 */
public class GetFeature200 {

    private String NAMESPACES;
    //提供者特定参数。
    private String VSPs;
    private String STARTINDEX;
    private int count;
    //"results"和"hits"，默认值为"results"。"results"表示返回满足操作请求的完整响应文档，"hits"表示返回一个没有资源实例的空响应文档。
    private String RESULTTYPE;
    private String RESOLVE;
    private String RESOLVEDEPTH;
    private String RESOLVETIMEOUT;
    private String SRSNAME;
    private String ALIASES;
    private List<String> typeNames;

    private String filter;
    private String FILTER_LANGUAGE;
    private String RESOURCEID;
    private String SORTBY;

    private String propertyName;
    private String featureVersion;

    private String bbox;
    private String outputFormat = GML3;//"application/gml+xml; version=3.2";

    public static final class Builder {
        private List<String> typeNames;
        private int count;
        private String filter;
        private String bbox;
        private String outputFormat = GML3;

        public Builder(List<String> typeNames) {
            this.typeNames = typeNames;
        }

        public Builder typeNames(List<String> typeNames) {
            this.typeNames = typeNames;
            return this;
        }



        public Builder maxFeatures(int maxFeatures) {
            this.count = maxFeatures;
            return this;
        }


        public Builder filter(String filter) {
            this.filter = filter;
            return this;
        }

        public Builder bbox(double minx, double miny, double maxx, double maxy) {
            this.bbox = minx + "," + miny + "," + maxx + "," + maxy;
            return this;
        }

        /**
         * GML2 (default) or GML3
         *
         * @param outputFormat the output format
         * @return builder
         */
        public Builder outputFormat(String outputFormat) {
            this.outputFormat = outputFormat;
            return this;
        }

        public GetFeature200 build() {
            GetFeature200 getFeature100 = new GetFeature200();
            getFeature100.outputFormat = this.outputFormat;
            getFeature100.count = this.count;
            getFeature100.bbox = this.bbox;
            getFeature100.filter = this.filter;
            getFeature100.typeNames = this.typeNames;
            return getFeature100;
        }
    }
}
