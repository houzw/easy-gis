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
 * Wrapper of parameters of dInfinityDistanceDown
 *
 * @author houzhiwei
 * @date 2020-05-21T14:24:36+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityDistanceDownParams implements Params {
    private static final long serialVersionUID = -8193415301265100637L;

    /**
     * <pre>
     * dinfinityFlowDirection
     * pitFilledElevation
     * streamRaster
     * weightPath
     *  </pre>
     *
     * @see Builder#Builder(String, String, String)
     */
    public DInfinityDistanceDownParams() {
    }

    /**
     * A grid giving flow directions by the D-Infinity method.
     */
    @NotNull
    private String dinfinityFlowDirection;

    /**
     * This input is a grid of elevation values.
     */
    @NotNull
    private String pitFilledElevation;

    /**
     * A grid indicating streams, by using a grid cell value of 1 on streams and 0 off streams.
     */
    @NotNull
    private String streamRaster;


    /**
     * default is "ave".
     * Alternative values are ave, max, min, where ave for Average, max for Maximum, min for Minimum Statistical method used to calculate the distance down to the stream.
     */
    @XmlElement(defaultValue = "ave")
    private String statisticalMethod = "ave";


    /**
     * default is "h".
     * Alternative values are h, v, p, s, where h for Horizontal, v for Vertical, p for Pythagoras, s for Surface Distance method used to calculate the distance down to the stream.
     */
    @XmlElement(defaultValue = "h")
    private String distanceMethod = "h";


    /**
     * default is true.
     * A flag that determines whether the tool should check for edge contamination.
     */
    @XmlElement(defaultValue = "true")
    private Boolean checkForEdgeContamination = true;


    /**
     * A grid giving weights (loadings) to be used in the distance calculation.
     */
    private String weightPath;

    /**
     * Creates a grid containing the distance to stream calculated using the D-infinity flow model and the statistical and path methods chosen.
     */
    private String dinfinityDropToStream;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    // if no output filename provided
    public String getDinfinityDropToStream() {
        if (StringUtils.isBlank(dinfinityDropToStream)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(dinfinityFlowDirection, "dinfinityDropToStream", "Raster Dataset", "tif"));
        }
        return dinfinityDropToStream;
    }

    private DInfinityDistanceDownParams(Builder builder) {
        dinfinityFlowDirection = builder.dinfinityFlowDirection;
        pitFilledElevation = builder.pitFilledElevation;
        streamRaster = builder.streamRaster;
        statisticalMethod = builder.statisticalMethod;
        distanceMethod = builder.distanceMethod;
        checkForEdgeContamination = builder.checkForEdgeContamination;
        weightPath = builder.weightPath;
        dinfinityDropToStream = builder.dinfinityDropToStream;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String dinfinityFlowDirection;
        private String pitFilledElevation;
        private String streamRaster;
        private String statisticalMethod;
        private String distanceMethod;
        private Boolean checkForEdgeContamination;
        private String weightPath;
        private String dinfinityDropToStream;

        /**
         * @param dinfinityFlowDirection A grid giving flow directions by the D-Infinity method.
         * @param pitFilledElevation     This input is a grid of elevation values.
         * @param streamRaster           A grid indicating streams, by using a grid cell value of 1 on streams and 0 off streams.
         */
        public Builder(@NotBlank String dinfinityFlowDirection, @NotBlank String pitFilledElevation, @NotBlank String streamRaster) {
            this.dinfinityFlowDirection = dinfinityFlowDirection;
            this.pitFilledElevation = pitFilledElevation;
            this.streamRaster = streamRaster;
        }

        public Builder dinfinityFlowDirection(String val) {
            dinfinityFlowDirection = val;
            return this;
        }

        public Builder pitFilledElevation(String val) {
            pitFilledElevation = val;
            return this;
        }

        public Builder streamRaster(String val) {
            streamRaster = val;
            return this;
        }

        public Builder statisticalMethod(String val) {
            statisticalMethod = val;
            return this;
        }

        public Builder distanceMethod(String val) {
            distanceMethod = val;
            return this;
        }

        public Builder checkForEdgeContamination(Boolean val) {
            checkForEdgeContamination = val;
            return this;
        }

        public Builder weightPath(String val) {
            weightPath = val;
            return this;
        }

        public Builder dinfinityDropToStream(String val) {
            dinfinityDropToStream = val;
            return this;
        }

        public DInfinityDistanceDownParams build() {
            return new DInfinityDistanceDownParams(this);
        }
    }
}