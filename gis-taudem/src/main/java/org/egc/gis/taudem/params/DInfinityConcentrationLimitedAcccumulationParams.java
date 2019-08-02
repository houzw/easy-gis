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
 * Wrapper of parameters of DInfinityConcentrationLimitedAcccumulation
 * @author houzhiwei
 * @date 2018-12-17T22:40:56+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityConcentrationLimitedAcccumulationParams implements Params {

    /**
     * <pre>
     * Input_DInfinity_Flow_Direction_Grid
     * Input_Effective_Runoff_Weight_Grid
     * Input_Disturbance_Indicator_Grid
     * Input_Decay_Multiplier_Grid
     *  </pre>
     * @see Builder#Builder( String,  String,  String,  String)
     */
    public DInfinityConcentrationLimitedAcccumulationParams(){}
    /**
     * A grid giving flow direction by the D-infinity method.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_DInfinity_Flow_Direction_Grid;
    /**
     * A grid giving the input quantity (notionally effective runoff or excess precipitation) to be used in the D-infinity weighted contributing area evaluation of Overland Flow Specific Discharge.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Effective_Runoff_Weight_Grid;
    /**
     * A grid that indicates the source zone of the area of substance supply and must be 1 inside the zone and 0 or "no data" over the rest of the domain.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Disturbance_Indicator_Grid;
    /**
     * A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Decay_Multiplier_Grid;
    /**
    * This optional input is a point feature  defining outlets of interest.
    */
    private String Input_Outlets;
    /**
    * default is 1d.
    * The concentration or solubility threshold.
    */
    @XmlElement(defaultValue = "1")
    private Double Concentration_Threshold = 1d;

    /**
    * default is true.
    * This checkbox determines whether the tool should check for edge contamination.
    */
    @XmlElement(defaultValue = "true")
    private Boolean Check_for_Edge_Contamination = true;

    /**
    * The grid giving the specific discharge of the flow carrying the constituent being loaded at the concentration threshold specified.
    */
    private String Output_Overland_Flow_Specific_Discharge_Grid;
    /**
    * A grid giving the resulting concentration of the compound of interest in the flow.
    */
    private String Output_Concentration_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_Overland_Flow_Specific_Discharge_Grid() {
        if (StringUtils.isBlank(Output_Overland_Flow_Specific_Discharge_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_DInfinity_Flow_Direction_Grid, "Output_Overland_Flow_Specific_Discharge_Grid", "Raster Dataset", null));
        }
        return this.Output_Overland_Flow_Specific_Discharge_Grid;
    }
    public String getOutput_Concentration_Grid() {
        if (StringUtils.isBlank(Output_Concentration_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_DInfinity_Flow_Direction_Grid, "Output_Concentration_Grid", "Raster Dataset", null));
        }
        return this.Output_Concentration_Grid;
    }

    private DInfinityConcentrationLimitedAcccumulationParams(Builder builder){
        this.Input_DInfinity_Flow_Direction_Grid = builder.Input_DInfinity_Flow_Direction_Grid;
        this.Input_Effective_Runoff_Weight_Grid = builder.Input_Effective_Runoff_Weight_Grid;
        this.Input_Disturbance_Indicator_Grid = builder.Input_Disturbance_Indicator_Grid;
        this.Input_Decay_Multiplier_Grid = builder.Input_Decay_Multiplier_Grid;
        this.Input_Outlets = builder.Input_Outlets;
        this.Concentration_Threshold = builder.Concentration_Threshold;
        this.Check_for_Edge_Contamination = builder.Check_for_Edge_Contamination;
        this.Output_Overland_Flow_Specific_Discharge_Grid = builder.Output_Overland_Flow_Specific_Discharge_Grid;
        this.Output_Concentration_Grid = builder.Output_Concentration_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_DInfinity_Flow_Direction_Grid;
        private String Input_Effective_Runoff_Weight_Grid;
        private String Input_Disturbance_Indicator_Grid;
        private String Input_Decay_Multiplier_Grid;
        private String Input_Outlets;
        private Double Concentration_Threshold;
        private Boolean Check_for_Edge_Contamination;
        private String Output_Overland_Flow_Specific_Discharge_Grid;
        private String Output_Concentration_Grid;

        /**
        * @param Input_DInfinity_Flow_Direction_Grid A grid giving flow direction by the D-infinity method.
        * @param Input_Effective_Runoff_Weight_Grid A grid giving the input quantity (notionally effective runoff or excess precipitation) to be used in the D-infinity weighted contributing area evaluation of Overland Flow Specific Discharge.
        * @param Input_Disturbance_Indicator_Grid A grid that indicates the source zone of the area of substance supply and must be 1 inside the zone and 0 or "no data" over the rest of the domain.
        * @param Input_Decay_Multiplier_Grid A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
        */
        public Builder(@NotBlank String Input_DInfinity_Flow_Direction_Grid, @NotBlank String Input_Effective_Runoff_Weight_Grid, @NotBlank String Input_Disturbance_Indicator_Grid, @NotBlank String Input_Decay_Multiplier_Grid){
            this.Input_DInfinity_Flow_Direction_Grid = Input_DInfinity_Flow_Direction_Grid;
            this.Input_Effective_Runoff_Weight_Grid = Input_Effective_Runoff_Weight_Grid;
            this.Input_Disturbance_Indicator_Grid = Input_Disturbance_Indicator_Grid;
            this.Input_Decay_Multiplier_Grid = Input_Decay_Multiplier_Grid;
        }

        public Builder Input_DInfinity_Flow_Direction_Grid(String val){
            this.Input_DInfinity_Flow_Direction_Grid = val;
            return this;
        }
        public Builder Input_Effective_Runoff_Weight_Grid(String val){
            this.Input_Effective_Runoff_Weight_Grid = val;
            return this;
        }
        public Builder Input_Disturbance_Indicator_Grid(String val){
            this.Input_Disturbance_Indicator_Grid = val;
            return this;
        }
        public Builder Input_Decay_Multiplier_Grid(String val){
            this.Input_Decay_Multiplier_Grid = val;
            return this;
        }
        public Builder Input_Outlets(String val){
            this.Input_Outlets = val;
            return this;
        }
        public Builder Concentration_Threshold(Double val){
            this.Concentration_Threshold = val;
            return this;
        }
        public Builder Check_for_Edge_Contamination(Boolean val){
            this.Check_for_Edge_Contamination = val;
            return this;
        }
        public Builder Output_Overland_Flow_Specific_Discharge_Grid(String val){
            this.Output_Overland_Flow_Specific_Discharge_Grid = val;
            return this;
        }
        public Builder Output_Concentration_Grid(String val){
            this.Output_Concentration_Grid = val;
            return this;
        }

        public DInfinityConcentrationLimitedAcccumulationParams build() {
            return new DInfinityConcentrationLimitedAcccumulationParams(this);
        }
    }
}