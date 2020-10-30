package org.egc.ows.request.wfs;

import java.util.List;

import static org.egc.ows.commons.Consts.GML2;

/**
 * @author houzhiwei
 * @date 2020/10/14 20:22
 */
public class GetFeature100 {
    private String typeName;
    private List<String> typeNames;
    private String propertyName;
    private String featureVersion;
    private int maxFeatures;
    private String featureId;
    private String filter;
    private String bbox;//=minx,miny,maxx,maxy;
    private String outputFormat = GML2;

    public static final class Builder {
        private String typeName;
        private List<String> typeNames;
        private String propertyName;
        private String featureVersion;
        private int maxFeatures;
        private String featureId;
        private String filter;
        private String bbox;
        private String outputFormat = GML2;

        public Builder(String typeName) {
            this.typeName = typeName;
        }

        public Builder(List<String> typeNames) {
            this.typeNames = typeNames;
        }


        public Builder typeName(String typeName) {
            this.typeName = typeName;
            return this;
        }

        public Builder typeNames(List<String> typeNames) {
            this.typeNames = typeNames;
            return this;
        }

        public Builder propertyName(String propertyName) {
            this.propertyName = propertyName;
            return this;
        }

        public Builder featureVersion(String featureVersion) {
            this.featureVersion = featureVersion;
            return this;
        }

        public Builder maxFeatures(int maxFeatures) {
            this.maxFeatures = maxFeatures;
            return this;
        }

        public Builder featureId(String featureId) {
            this.featureId = featureId;
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

        public GetFeature100 build() {
            GetFeature100 getFeature100 = new GetFeature100();
            getFeature100.outputFormat = this.outputFormat;
            getFeature100.maxFeatures = this.maxFeatures;
            getFeature100.propertyName = this.propertyName;
            getFeature100.bbox = this.bbox;
            getFeature100.featureVersion = this.featureVersion;
            getFeature100.filter = this.filter;
            getFeature100.typeName = this.typeName;
            getFeature100.typeNames = this.typeNames;
            getFeature100.featureId = this.featureId;
            return getFeature100;
        }
    }
}
