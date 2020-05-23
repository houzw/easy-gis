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
 * Wrapper of parameters of dInfinityDistanceUp
 *
 * @author houzhiwei
 * @date 2020-05-21T14:24:36+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityDistanceUpParams implements Params {
    private static final long serialVersionUID = 3613867407453405685L;

    /**
     * <pre>
     * dinfinityFlowDirection
     * pitFilledElevation
     * slope
     *  </pre>
     *
     * @see Builder#Builder(String, String, String)
     */
    public DInfinityDistanceUpParams() {
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
     * This input is a grid of slope values.
     */
    @NotNull
    private String slope;


    /**
     * default is 0.5d.
     * The proportion threshold parameter where only grid cells that contribute flow with a proportion greater than this user specified threshold (t) is considered to be upslope of any given grid cell.
     */
    @XmlElement(defaultValue = "0.5")
    private Double proportionThreshold = 0.5d;


    /**
     * default is "ave".
     * Statistical method used to calculate the distance down to the stream.
     */
    @XmlElement(defaultValue = "ave")
    private String statisticalMethod = "ave";


    /**
     * default is "h".
     * Distance method used to calculate the distance down to the stream.
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
     *
     */
    private String dinfinityDistanceUp;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    // if no output filename provided
    public String getDinfinityDistanceUp() {
        if (StringUtils.isBlank(dinfinityDistanceUp)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(dinfinityFlowDirection, "dinfinityDistanceUp", "Raster Dataset", "tif"));
        }
        return dinfinityDistanceUp;
    }

    private DInfinityDistanceUpParams(Builder builder) {
        dinfinityFlowDirection = builder.dinfinityFlowDirection;
        pitFilledElevation = builder.pitFilledElevation;
        slope = builder.slope;
        proportionThreshold = builder.proportionThreshold;
        statisticalMethod = builder.statisticalMethod;
        distanceMethod = builder.distanceMethod;
        checkForEdgeContamination = builder.checkForEdgeContamination;
        dinfinityDistanceUp = builder.dinfinityDistanceUp;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String dinfinityFlowDirection;
        private String pitFilledElevation;
        private String slope;
        private Double proportionThreshold;
        private String statisticalMethod;
        private String distanceMethod;
        private Boolean checkForEdgeContamination;
        private String dinfinityDistanceUp;

        /**
         * @param dinfinityFlowDirection A grid giving flow directions by the D-Infinity method.
         * @param pitFilledElevation     This input is a grid of elevation values.
         * @param slope                  This input is a grid of slope values.
         */
        public Builder(@NotBlank String dinfinityFlowDirection, @NotBlank String pitFilledElevation, @NotBlank String slope) {
            this.dinfinityFlowDirection = dinfinityFlowDirection;
            this.pitFilledElevation = pitFilledElevation;
            this.slope = slope;
        }

        public Builder dinfinityFlowDirection(String val) {
            dinfinityFlowDirection = val;
            return this;
        }

        public Builder pitFilledElevation(String val) {
            pitFilledElevation = val;
            return this;
        }

        public Builder slope(String val) {
            slope = val;
            return this;
        }

        public Builder proportionThreshold(Double val) {
            proportionThreshold = val;
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

        public Builder dinfinityDistanceUp(String val) {
            dinfinityDistanceUp = val;
            return this;
        }

        public DInfinityDistanceUpParams build() {
            return new DInfinityDistanceUpParams(this);
        }
    }
}