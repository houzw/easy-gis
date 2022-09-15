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
 * Wrapper of parameters of dInfinityDecayingAccumulation
 *
 * @author houzhiwei
 * @date 2020-06-28T12:02:31+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityDecayingAccumulationParams implements Params {
    /**
     * <pre>     * dinfinityFlowDirection     * decayMultiplier     * weight     * outlets
     *  </pre>
     *
     * @see Builder#Builder(String, String)
     */
    public DInfinityDecayingAccumulationParams() {
    }

    /**
     * A grid giving flow direction by the D-infinity method.
     */
    @NotNull
    private String dinfinityFlowDirection;

    /**
     * A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
     */
    @NotNull
    private String decayMultiplier;

    /**
     * A grid giving weights (loadings) to be used in the accumulation.
     */
    private String weight;

    /**
     * This optional input is a point feature  defining outlets of interest.
     */
    private String outlets;


    /**
     * default is true.
     * This checkbox determines whether the tool should check for edge contamination.
     */
    @XmlElement(defaultValue = "true")
    private Boolean checkForEdgeContamination = true;


    /**
     * The D-Infinity Decaying Accumulation tool creates a grid of the accumulated mass at each location in the domain where mass moves with the D-infinity flow field, but is subject to first order decay in moving from cell to cell.
     */
    @NotNull
    private String decayedSpecificCatchmentArea;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getDecayedSpecificCatchmentArea() {
        // if no output filename provided
        if (StringUtils.isBlank(decayedSpecificCatchmentArea)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(dinfinityFlowDirection, "decayedSpecificCatchmentArea", "Raster Dataset", "tif"));
        }
        return this.decayedSpecificCatchmentArea;
    }

    private DInfinityDecayingAccumulationParams(Builder builder) {
        this.dinfinityFlowDirection = builder.dinfinityFlowDirection;
        this.decayMultiplier = builder.decayMultiplier;
        this.weight = builder.weight;
        this.outlets = builder.outlets;
        this.checkForEdgeContamination = builder.checkForEdgeContamination;
        this.decayedSpecificCatchmentArea = builder.decayedSpecificCatchmentArea;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String dinfinityFlowDirection;
        private String decayMultiplier;
        private String weight;
        private String outlets;
        private Boolean checkForEdgeContamination;
        private String decayedSpecificCatchmentArea;

        /**
         * @param dinfinityFlowDirection A grid giving flow direction by the D-infinity method.
         * @param decayMultiplier        A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
         */
        public Builder(@NotBlank String dinfinityFlowDirection, @NotBlank String decayMultiplier) {
            this.dinfinityFlowDirection = dinfinityFlowDirection;
            this.decayMultiplier = decayMultiplier;
        }

        /**
         * @param val A grid giving flow direction by the D-infinity method.
         */
        public Builder dinfinityFlowDirection(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.dinfinityFlowDirection = val;
            }
            return this;
        }

        /**
         * @param val A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
         */
        public Builder decayMultiplier(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.decayMultiplier = val;
            }
            return this;
        }

        /**
         * @param val A grid giving weights (loadings) to be used in the accumulation.
         */
        public Builder weight(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.weight = val;
            }
            return this;
        }

        /**
         * @param val This optional input is a point feature  defining outlets of interest.
         */
        public Builder outlets(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.outlets = val;
            }
            return this;
        }

        /**
         * default value is  true
         *
         * @param val This checkbox determines whether the tool should check for edge contamination.
         */
        public Builder checkForEdgeContamination(Boolean val) {

            if (val != null) {
                this.checkForEdgeContamination = val;
            }
            return this;
        }

        /**
         * @param val The D-Infinity Decaying Accumulation tool creates a grid of the accumulated mass at each location in the domain where mass moves with the D-infinity flow field, but is subject to first order decay in moving from cell to cell.
         */
        public Builder decayedSpecificCatchmentArea(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.decayedSpecificCatchmentArea = val;
            }
            return this;
        }

        public DInfinityDecayingAccumulationParams build() {
            return new DInfinityDecayingAccumulationParams(this);
        }
    }
}