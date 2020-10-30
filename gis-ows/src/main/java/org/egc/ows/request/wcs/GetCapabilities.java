package org.egc.ows.request.wcs;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/16 20:00
 */
@Deprecated
@Data
public class GetCapabilities {
    private String url;
    private WCSRequestBuilder.Version version;
    private List<WCSRequestBuilder.Version> acceptVersions;
    private String section;
    private String updateSequence;
    private List<String> acceptFormats;

    private GetCapabilities(Builder builder) {
        setUrl(builder.url);
        setVersion(builder.version);
        setAcceptVersions(builder.acceptVersions);
        setSection(builder.section);
        setUpdateSequence(builder.updateSequence);
        setAcceptFormats(builder.acceptFormats);
    }

    public static final class Builder {
        private String url;
        private WCSRequestBuilder.Version version;
        private List<WCSRequestBuilder.Version> acceptVersions;
        private String section;
        private String updateSequence;
        private List<String> acceptFormats;

        public Builder(String url, WCSRequestBuilder.Version version) {
            this.url = url;
            this.version = version;
        }

        public Builder version(WCSRequestBuilder.Version val) {
            version = val;
            return this;
        }

        /**
         * 1.1.0+
         *
         * @param val the val
         * @return builder
         */
        public Builder acceptVersions(List<WCSRequestBuilder.Version> val) {
            acceptVersions = val;
            return this;
        }

        /**
         * / or
         * /WCS_Capabilities/Service or
         * /WCS_Capabilities/Capability or
         * /WCS_Capabilities/ContentMetadata
         *
         * @param val the val
         * @return builder
         */
        public Builder section(String val) {
            section = val;
            return this;
        }

        public Builder updateSequence(String val) {
            updateSequence = val;
            return this;
        }

        /**
         * 1.1.0+
         *
         * @param val the val
         * @return builder
         */
        public Builder acceptFormats(List<String> val) {
            acceptFormats = val;
            return this;
        }

        public GetCapabilities build() {
            return new GetCapabilities(this);
        }
    }
}
