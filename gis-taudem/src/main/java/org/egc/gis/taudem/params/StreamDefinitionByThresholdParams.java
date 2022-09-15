package org.egc.gis.taudem.params;

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
 * Wrapper of parameters of streamDefinitionByThreshold
 *
 * @author houzhiwei
 * @date 2020-06-28T12:02:30+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class StreamDefinitionByThresholdParams implements Params {
    /**
     * <pre>     * accumulatedStreamSource     * mask
     *  </pre>
     *
     * @see Builder#Builder(String)
     */
    public StreamDefinitionByThresholdParams() {
    }

    /**
     * This grid nominally accumulates some characteristic or combination of characteristics of the watershed.
     */
    @NotNull
    private String accumulatedStreamSource;

    /**
     * This optional input is a grid that is used to mask the domain of interest and output is only provided where this grid is >= 0.
     */
    private String mask;


    /**
     * default is 100d.
     * This parameter is compared to the value in the Accumulated Stream Source grid (*ssa) to determine if the cell should be considered a stream cell.
     */
    @XmlElement(defaultValue = "100")
    private Double threshold = 100d;


    /**
     * This is an indicator grid (1,0) that indicates the location of streams, with a value of 1 for each of the stream cells and 0 for the remainder of the cells.
     */
    @NotNull
    private String streamRaster;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getStreamRaster() {
        // if no output filename provided
        if (StringUtils.isBlank(streamRaster)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(accumulatedStreamSource, "streamRaster", "Raster Dataset", "tif"));
        }
        return this.streamRaster;
    }

    private StreamDefinitionByThresholdParams(Builder builder) {
        this.accumulatedStreamSource = builder.accumulatedStreamSource;
        this.mask = builder.mask;
        this.threshold = builder.threshold;
        this.streamRaster = builder.streamRaster;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String accumulatedStreamSource;
        private String mask;
        private Double threshold;
        private String streamRaster;

        /**
         * @param accumulatedStreamSource This grid nominally accumulates some characteristic or combination of characteristics of the watershed.
         */
        public Builder(@NotBlank String accumulatedStreamSource) {
            this.accumulatedStreamSource = accumulatedStreamSource;
        }

        /**
         * @param val This grid nominally accumulates some characteristic or combination of characteristics of the watershed.
         */
        public Builder accumulatedStreamSource(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.accumulatedStreamSource = val;
            }
            return this;
        }

        /**
         * @param val This optional input is a grid that is used to mask the domain of interest and output is only provided where this grid is >= 0.
         */
        public Builder mask(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.mask = val;
            }
            return this;
        }

        /**
         * default value is 100d
         *
         * @param val This parameter is compared to the value in the Accumulated Stream Source grid (*ssa) to determine if the cell should be considered a stream cell.
         */
        public Builder threshold(Double val) {

            if (val != null) {
                this.threshold = val;
            }
            return this;
        }

        /**
         * @param val This is an indicator grid (1,0) that indicates the location of streams, with a value of 1 for each of the stream cells and 0 for the remainder of the cells.
         */
        public Builder streamRaster(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.streamRaster = val;
            }
            return this;
        }

        public StreamDefinitionByThresholdParams build() {
            return new StreamDefinitionByThresholdParams(this);
        }
    }
}