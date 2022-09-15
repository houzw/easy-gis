package org.egc.gis.whitebox.params;

import lombok.Data;
import lombok.Setter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;

/**
 * Wrapper of parameters of extractStreams
 *
 * @author houzhiwei & wangyijie
 * @date 2020-05-24T17:24:32+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ExtractStreamsParams implements Params {
    /**
     * <pre>
     * flowAccumulationDem
     *  </pre>
     *
     * @see Builder#Builder(String)
     */
    public ExtractStreamsParams() {
    }

    /**
     * Input raster D8 flow accumulation file
     */
    @NotNull
    private String flowAccumulationDem;

    /**
     * Output raster file
     */
    private String streamDem;

    /**
     * Threshold in flow accumulation values for channelization
     */
    private Double threshold;

    /**
     * Flag indicating whether a background value of zero should be used
     */
    private Boolean zeroBackground;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getStreamDem() {
        // if no output filename provided
        if (StringUtils.isBlank(streamDem)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(flowAccumulationDem, "streamDem", "tif", "tif"));
        }
        return this.streamDem;
    }

    private ExtractStreamsParams(Builder builder) {
        this.flowAccumulationDem = builder.flowAccumulationDem;
        this.streamDem = builder.streamDem;
        this.threshold = builder.threshold;
        this.zeroBackground = builder.zeroBackground;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String flowAccumulationDem;
        private String streamDem;
        private Double threshold;
        private Boolean zeroBackground;

        /**
         * @param flowAccumulationDem Input raster D8 flow accumulation file
         */
        public Builder(@NotBlank String flowAccumulationDem) {
            this.flowAccumulationDem = flowAccumulationDem;
        }

        public Builder flowAccumulationDem(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.flowAccumulationDem = val;
            }
            return this;
        }

        public Builder streamDem(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.streamDem = val;
            }
            return this;
        }

        public Builder threshold(Double val) {

            if (val != null) {
                this.threshold = val;
            }
            return this;
        }

        public Builder zeroBackground(Boolean val) {

            if (val != null) {
                this.zeroBackground = val;
            }
            return this;
        }

        public ExtractStreamsParams build() {
            return new ExtractStreamsParams(this);
        }
    }
}