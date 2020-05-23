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
 * Wrapper of parameters of dInfinityConcentrationLimitedAcccumulation
 *
 * @author houzhiwei
 * @date 2020-05-21T14:24:36+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityConcentrationLimitedAcccumulationParams implements Params {
    private static final long serialVersionUID = -2123491500314228973L;

    /**
     * <pre>
     * dinfinityFlowDirection
     * effectiveRunoffWeight
     * disturbanceIndicator
     * decayMultiplier
     * outlets
     *  </pre>
     *
     * @see Builder#Builder(String, String, String, String)
     */
    public DInfinityConcentrationLimitedAcccumulationParams() {
    }

    /**
     * A grid giving flow direction by the D-infinity method.
     */
    @NotNull
    private String dinfinityFlowDirection;

    /**
     * A grid giving the input quantity (notionally effective runoff or excess precipitation) to be used in the D-infinity weighted contributing area evaluation of Overland Flow Specific Discharge.
     */
    @NotNull
    private String effectiveRunoffWeight;

    /**
     * A grid that indicates the source zone of the area of substance supply and must be 1 inside the zone and 0 or "no data" over the rest of the domain.
     */
    @NotNull
    private String disturbanceIndicator;

    /**
     * A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
     */
    @NotNull
    private String decayMultiplier;

    /**
     * This optional input is a point feature  defining outlets of interest.
     */
    private String outlets;


    /**
     * default is 1d.
     * The concentration or solubility threshold.
     */
    @XmlElement(defaultValue = "1")
    private Double concentrationThreshold = 1d;


    /**
     * default is true.
     * This checkbox determines whether the tool should check for edge contamination.
     */
    @XmlElement(defaultValue = "true")
    private Boolean checkForEdgeContamination = true;


    /**
     * The grid giving the specific discharge of the flow carrying the constituent being loaded at the concentration threshold specified.
     */
    private String overlandFlowSpecificDischarge;

    /**
     * A grid giving the resulting concentration of the compound of interest in the flow.
     */
    private String concentration;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    // if no output filename provided
    public String getOverlandFlowSpecificDischarge() {
        if (StringUtils.isBlank(overlandFlowSpecificDischarge)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(dinfinityFlowDirection, "overlandFlowSpecificDischarge", "Raster Dataset", "tif"));
        }
        return overlandFlowSpecificDischarge;
    }

    // if no output filename provided
    public String getConcentration() {
        if (StringUtils.isBlank(concentration)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(dinfinityFlowDirection, "concentration", "Raster Dataset", "tif"));
        }
        return concentration;
    }

    private DInfinityConcentrationLimitedAcccumulationParams(Builder builder) {
        dinfinityFlowDirection = builder.dinfinityFlowDirection;
        effectiveRunoffWeight = builder.effectiveRunoffWeight;
        disturbanceIndicator = builder.disturbanceIndicator;
        decayMultiplier = builder.decayMultiplier;
        outlets = builder.outlets;
        concentrationThreshold = builder.concentrationThreshold;
        checkForEdgeContamination = builder.checkForEdgeContamination;
        overlandFlowSpecificDischarge = builder.overlandFlowSpecificDischarge;
        concentration = builder.concentration;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String dinfinityFlowDirection;
        private String effectiveRunoffWeight;
        private String disturbanceIndicator;
        private String decayMultiplier;
        private String outlets;
        private Double concentrationThreshold;
        private Boolean checkForEdgeContamination;
        private String overlandFlowSpecificDischarge;
        private String concentration;

        /**
         * @param dinfinityFlowDirection A grid giving flow direction by the D-infinity method.
         * @param effectiveRunoffWeight  A grid giving the input quantity (notionally effective runoff or excess precipitation) to be used in the D-infinity weighted contributing area evaluation of Overland Flow Specific Discharge.
         * @param disturbanceIndicator   A grid that indicates the source zone of the area of substance supply and must be 1 inside the zone and 0 or "no data" over the rest of the domain.
         * @param decayMultiplier        A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
         */
        public Builder(@NotBlank String dinfinityFlowDirection, @NotBlank String effectiveRunoffWeight, @NotBlank String disturbanceIndicator, @NotBlank String decayMultiplier) {
            this.dinfinityFlowDirection = dinfinityFlowDirection;
            this.effectiveRunoffWeight = effectiveRunoffWeight;
            this.disturbanceIndicator = disturbanceIndicator;
            this.decayMultiplier = decayMultiplier;
        }

        public Builder dinfinityFlowDirection(String val) {
            dinfinityFlowDirection = val;
            return this;
        }

        public Builder effectiveRunoffWeight(String val) {
            effectiveRunoffWeight = val;
            return this;
        }

        public Builder disturbanceIndicator(String val) {
            disturbanceIndicator = val;
            return this;
        }

        public Builder decayMultiplier(String val) {
            decayMultiplier = val;
            return this;
        }

        public Builder outlets(String val) {
            outlets = val;
            return this;
        }

        public Builder concentrationThreshold(Double val) {
            concentrationThreshold = val;
            return this;
        }

        public Builder checkForEdgeContamination(Boolean val) {
            checkForEdgeContamination = val;
            return this;
        }

        public Builder overlandFlowSpecificDischarge(String val) {
            overlandFlowSpecificDischarge = val;
            return this;
        }

        public Builder concentration(String val) {
            concentration = val;
            return this;
        }

        public DInfinityConcentrationLimitedAcccumulationParams build() {
            return new DInfinityConcentrationLimitedAcccumulationParams(this);
        }
    }
}