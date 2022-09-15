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
 * Wrapper of parameters of lengthAreaStreamSource
 *
 * @author houzhiwei
 * @date 2020-06-28T12:02:30+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class LengthAreaStreamSourceParams implements Params {
    /**
     * <pre>     * length     * contributingArea
     *  </pre>
     *
     * @see Builder#Builder(String, String)
     */
    public LengthAreaStreamSourceParams() {
    }

    /**
     * A grid of the maximum upslope length for each cell.
     */
    @NotNull
    private String length;

    /**
     * A grid of contributing area values for each cell that were calculated using the D8 algorithm.
     */
    @NotNull
    private String contributingArea;


    /**
     * default is 0.03d.
     * The multiplier threshold (M) parameter which is used in the formula: A > (M)(L^y), to identify the beginning of streams.
     */
    @XmlElement(defaultValue = "0.03")
    private Double thresholdM = 0.03d;


    /**
     * default is 1.3d.
     * The exponent (y) parameter which is used in the formula: A > (M)(L^y), to identify the beginning of streams.
     */
    @XmlElement(defaultValue = "1.3")
    private Double exponentY = 1.3d;


    /**
     * An indicator grid (1,0) that evaluates A >= (M)(L^y), based on the maximum upslope path length, the D8 contributing area grid inputs, and parameters M and y.
     */
    @NotNull
    private String streamSource;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getStreamSource() {
        // if no output filename provided
        if (StringUtils.isBlank(streamSource)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(length, "streamSource", "Raster Dataset", "tif"));
        }
        return this.streamSource;
    }

    private LengthAreaStreamSourceParams(Builder builder) {
        this.length = builder.length;
        this.contributingArea = builder.contributingArea;
        this.thresholdM = builder.thresholdM;
        this.exponentY = builder.exponentY;
        this.streamSource = builder.streamSource;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String length;
        private String contributingArea;
        private Double thresholdM;
        private Double exponentY;
        private String streamSource;

        /**
         * @param length           A grid of the maximum upslope length for each cell.
         * @param contributingArea A grid of contributing area values for each cell that were calculated using the D8 algorithm.
         */
        public Builder(@NotBlank String length, @NotBlank String contributingArea) {
            this.length = length;
            this.contributingArea = contributingArea;
        }

        /**
         * @param val A grid of the maximum upslope length for each cell.
         */
        public Builder length(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.length = val;
            }
            return this;
        }

        /**
         * @param val A grid of contributing area values for each cell that were calculated using the D8 algorithm.
         */
        public Builder contributingArea(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.contributingArea = val;
            }
            return this;
        }

        /**
         * default value is 0.03d
         *
         * @param val The multiplier threshold (M) parameter which is used in the formula: A > (M)(L^y), to identify the beginning of streams.
         */
        public Builder thresholdM(Double val) {

            if (val != null) {
                this.thresholdM = val;
            }
            return this;
        }

        /**
         * default value is 1.3d
         *
         * @param val The exponent (y) parameter which is used in the formula: A > (M)(L^y), to identify the beginning of streams.
         */
        public Builder exponentY(Double val) {

            if (val != null) {
                this.exponentY = val;
            }
            return this;
        }

        /**
         * @param val An indicator grid (1,0) that evaluates A >= (M)(L^y), based on the maximum upslope path length, the D8 contributing area grid inputs, and parameters M and y.
         */
        public Builder streamSource(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.streamSource = val;
            }
            return this;
        }

        public LengthAreaStreamSourceParams build() {
            return new LengthAreaStreamSourceParams(this);
        }
    }
}