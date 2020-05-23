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
 * @date 2020-05-21T14:24:36+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityDecayingAccumulationParams implements Params {
    private static final long serialVersionUID = 3716566273567797162L;

    /**
     * <pre>
     * dinfinityFlowDirection
     * decayMultiplier
     * weight
     * outlets
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
    private String decayedSpecificCatchmentArea;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    // if no output filename provided
    public String getDecayedSpecificCatchmentArea() {
        if (StringUtils.isBlank(decayedSpecificCatchmentArea)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(dinfinityFlowDirection, "decayedSpecificCatchmentArea", "Raster Dataset", "tif"));
        }
        return decayedSpecificCatchmentArea;
    }

    private DInfinityDecayingAccumulationParams(Builder builder) {
        dinfinityFlowDirection = builder.dinfinityFlowDirection;
        decayMultiplier = builder.decayMultiplier;
        weight = builder.weight;
        outlets = builder.outlets;
        checkForEdgeContamination = builder.checkForEdgeContamination;
        decayedSpecificCatchmentArea = builder.decayedSpecificCatchmentArea;
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

        public Builder dinfinityFlowDirection(String val) {
            dinfinityFlowDirection = val;
            return this;
        }

        public Builder decayMultiplier(String val) {
            decayMultiplier = val;
            return this;
        }

        public Builder weight(String val) {
            weight = val;
            return this;
        }

        public Builder outlets(String val) {
            outlets = val;
            return this;
        }

        public Builder checkForEdgeContamination(Boolean val) {
            checkForEdgeContamination = val;
            return this;
        }

        public Builder decayedSpecificCatchmentArea(String val) {
            decayedSpecificCatchmentArea = val;
            return this;
        }

        public DInfinityDecayingAccumulationParams build() {
            return new DInfinityDecayingAccumulationParams(this);
        }
    }
}