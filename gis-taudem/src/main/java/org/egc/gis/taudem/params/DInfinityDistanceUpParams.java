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
 * @date 2020-06-28T12:02:31+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityDistanceUpParams implements Params {
    /**
     * <pre>     * dinfinityFlowDirection     * pitFilledElevation     * slope
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
    @NotNull
    private String dinfinityDistanceUp;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getDinfinityDistanceUp() {
        // if no output filename provided
        if (StringUtils.isBlank(dinfinityDistanceUp)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(dinfinityFlowDirection, "dinfinityDistanceUp", "Raster Dataset", "tif"));
        }
        return this.dinfinityDistanceUp;
    }

    private DInfinityDistanceUpParams(Builder builder) {
        this.dinfinityFlowDirection = builder.dinfinityFlowDirection;
        this.pitFilledElevation = builder.pitFilledElevation;
        this.slope = builder.slope;
        this.proportionThreshold = builder.proportionThreshold;
        this.statisticalMethod = builder.statisticalMethod;
        this.distanceMethod = builder.distanceMethod;
        this.checkForEdgeContamination = builder.checkForEdgeContamination;
        this.dinfinityDistanceUp = builder.dinfinityDistanceUp;
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

        /**
         * @param val A grid giving flow directions by the D-Infinity method.
         */
        public Builder dinfinityFlowDirection(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.dinfinityFlowDirection = val;
            }
            return this;
        }

        /**
         * @param val This input is a grid of elevation values.
         */
        public Builder pitFilledElevation(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.pitFilledElevation = val;
            }
            return this;
        }

        /**
         * @param val This input is a grid of slope values.
         */
        public Builder slope(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.slope = val;
            }
            return this;
        }

        /**
         * default value is 0.5d
         *
         * @param val The proportion threshold parameter where only grid cells that contribute flow with a proportion greater than this user specified threshold (t) is considered to be upslope of any given grid cell.
         */
        public Builder proportionThreshold(Double val) {

            if (val != null) {
                this.proportionThreshold = val;
            }
            return this;
        }

        /**
         * default value is ave
         *
         * @param val Statistical method used to calculate the distance down to the stream.
         */
        public Builder statisticalMethod(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.statisticalMethod = val;
            }
            return this;
        }

        /**
         * default value is h
         *
         * @param val Distance method used to calculate the distance down to the stream.
         */
        public Builder distanceMethod(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.distanceMethod = val;
            }
            return this;
        }

        /**
         * default value is  true
         *
         * @param val A flag that determines whether the tool should check for edge contamination.
         */
        public Builder checkForEdgeContamination(Boolean val) {

            if (val != null) {
                this.checkForEdgeContamination = val;
            }
            return this;
        }

        /**
         * @param val
         */
        public Builder dinfinityDistanceUp(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.dinfinityDistanceUp = val;
            }
            return this;
        }

        public DInfinityDistanceUpParams build() {
            return new DInfinityDistanceUpParams(this);
        }
    }
}