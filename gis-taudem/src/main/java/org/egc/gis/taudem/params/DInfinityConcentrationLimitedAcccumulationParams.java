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
 * @date 2020-06-28T12:02:31+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityConcentrationLimitedAcccumulationParams implements Params {
    /**
     * <pre>     * dinfinityFlowDirection     * effectiveRunoffWeight     * disturbanceIndicator     * decayMultiplier     * outlets
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
    @NotNull
    private String overlandFlowSpecificDischarge;

    /**
     * A grid giving the resulting concentration of the compound of interest in the flow.
     */
    @NotNull
    private String concentration;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getOverlandFlowSpecificDischarge() {
        // if no output filename provided
        if (StringUtils.isBlank(overlandFlowSpecificDischarge)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(dinfinityFlowDirection, "overlandFlowSpecificDischarge", "Raster Dataset", "tif"));
        }
        return this.overlandFlowSpecificDischarge;
    }

    public String getConcentration() {
        // if no output filename provided
        if (StringUtils.isBlank(concentration)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(dinfinityFlowDirection, "concentration", "Raster Dataset", "tif"));
        }
        return this.concentration;
    }

    private DInfinityConcentrationLimitedAcccumulationParams(Builder builder) {
        this.dinfinityFlowDirection = builder.dinfinityFlowDirection;
        this.effectiveRunoffWeight = builder.effectiveRunoffWeight;
        this.disturbanceIndicator = builder.disturbanceIndicator;
        this.decayMultiplier = builder.decayMultiplier;
        this.outlets = builder.outlets;
        this.concentrationThreshold = builder.concentrationThreshold;
        this.checkForEdgeContamination = builder.checkForEdgeContamination;
        this.overlandFlowSpecificDischarge = builder.overlandFlowSpecificDischarge;
        this.concentration = builder.concentration;
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
         * @param val A grid giving the input quantity (notionally effective runoff or excess precipitation) to be used in the D-infinity weighted contributing area evaluation of Overland Flow Specific Discharge.
         */
        public Builder effectiveRunoffWeight(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.effectiveRunoffWeight = val;
            }
            return this;
        }

        /**
         * @param val A grid that indicates the source zone of the area of substance supply and must be 1 inside the zone and 0 or "no data" over the rest of the domain.
         */
        public Builder disturbanceIndicator(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.disturbanceIndicator = val;
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
         * @param val This optional input is a point feature  defining outlets of interest.
         */
        public Builder outlets(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.outlets = val;
            }
            return this;
        }

        /**
         * default value is 1d
         *
         * @param val The concentration or solubility threshold.
         */
        public Builder concentrationThreshold(Double val) {

            if (val != null) {
                this.concentrationThreshold = val;
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
         * @param val The grid giving the specific discharge of the flow carrying the constituent being loaded at the concentration threshold specified.
         */
        public Builder overlandFlowSpecificDischarge(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.overlandFlowSpecificDischarge = val;
            }
            return this;
        }

        /**
         * @param val A grid giving the resulting concentration of the compound of interest in the flow.
         */
        public Builder concentration(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.concentration = val;
            }
            return this;
        }

        public DInfinityConcentrationLimitedAcccumulationParams build() {
            return new DInfinityConcentrationLimitedAcccumulationParams(this);
        }
    }
}