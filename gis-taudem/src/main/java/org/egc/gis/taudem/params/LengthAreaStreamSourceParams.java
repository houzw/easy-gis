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
 * @date 2020-05-21T14:24:36+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class LengthAreaStreamSourceParams implements Params {
    private static final long serialVersionUID = 2570066969224877268L;

    /**
     * <pre>
     * length
     * contributingArea
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
    private String streamSource;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    // if no output filename provided
    public String getStreamSource() {
        if (StringUtils.isBlank(streamSource)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(length, "streamSource", "Raster Dataset", "tif"));
        }
        return streamSource;
    }

    private LengthAreaStreamSourceParams(Builder builder) {
        length = builder.length;
        contributingArea = builder.contributingArea;
        thresholdM = builder.thresholdM;
        exponentY = builder.exponentY;
        streamSource = builder.streamSource;
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

        public Builder length(String val) {
            length = val;
            return this;
        }

        public Builder contributingArea(String val) {
            contributingArea = val;
            return this;
        }

        public Builder thresholdM(Double val) {
            thresholdM = val;
            return this;
        }

        public Builder exponentY(Double val) {
            exponentY = val;
            return this;
        }

        public Builder streamSource(String val) {
            streamSource = val;
            return this;
        }

        public LengthAreaStreamSourceParams build() {
            return new LengthAreaStreamSourceParams(this);
        }
    }
}