package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of DInfinityConcentrationLimitedAcccumulation
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class DInfinityConcentrationLimitedAcccumulationParams implements Params {


    /**
     * <pre>
     * Input_DInfinity_Flow_Direction_Grid
     * Input_Effective_Runoff_Weight_Grid
     * Input_Disturbance_Indicator_Grid
     * Input_Decay_Multiplier_Grid
     *  </pre>
     * @see #DInfinityConcentrationLimitedAcccumulationParams( String,  String,  String,  String)
     */
    public DInfinityConcentrationLimitedAcccumulationParams(){}
    /**
     * A grid giving flow direction by the D-infinity method.
     */
    @NotNull
    private String Input_DInfinity_Flow_Direction_Grid;
    /**
     * A grid giving the input quantity (notionally effective runoff or excess precipitation) to be used in the D-infinity weighted contributing area evaluation of Overland Flow Specific Discharge.
     */
    @NotNull
    private String Input_Effective_Runoff_Weight_Grid;
    /**
     * A grid that indicates the source zone of the area of substance supply and must be 1 inside the zone and 0 or "no data" over the rest of the domain.
     */
    @NotNull
    private String Input_Disturbance_Indicator_Grid;
    /**
     * A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
     */
    @NotNull
    private String Input_Decay_Multiplier_Grid;
    /**
    * This optional input is a point feature  defining outlets of interest.
    */
    private String Input_Outlets;
    /**
    * default is 1d.
    * The concentration or solubility threshold.
    */
    private Double Concentration_Threshold = 1d;

    /**
    * default is true.
    * This checkbox determines whether the tool should check for edge contamination.
    */
    private Boolean Check_for_Edge_Contamination = true;

    /**
    * The grid giving the specific discharge of the flow carrying the constituent being loaded at the concentration threshold specified.
    */
    private String Output_Overland_Flow_Specific_Discharge_Grid;
    /**
    * A grid giving the resulting concentration of the compound of interest in the flow.
    */
    private String Output_Concentration_Grid;
    private String outputDir = "./";


    /**
    * @param Input_DInfinity_Flow_Direction_Grid A grid giving flow direction by the D-infinity method.
    * @param Input_Effective_Runoff_Weight_Grid A grid giving the input quantity (notionally effective runoff or excess precipitation) to be used in the D-infinity weighted contributing area evaluation of Overland Flow Specific Discharge.
    * @param Input_Disturbance_Indicator_Grid A grid that indicates the source zone of the area of substance supply and must be 1 inside the zone and 0 or "no data" over the rest of the domain.
    * @param Input_Decay_Multiplier_Grid A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
    */
    public DInfinityConcentrationLimitedAcccumulationParams(@NotBlank String Input_DInfinity_Flow_Direction_Grid, @NotBlank String Input_Effective_Runoff_Weight_Grid, @NotBlank String Input_Disturbance_Indicator_Grid, @NotBlank String Input_Decay_Multiplier_Grid){
        this.Input_DInfinity_Flow_Direction_Grid = Input_DInfinity_Flow_Direction_Grid;
        this.Input_Effective_Runoff_Weight_Grid = Input_Effective_Runoff_Weight_Grid;
        this.Input_Disturbance_Indicator_Grid = Input_Disturbance_Indicator_Grid;
        this.Input_Decay_Multiplier_Grid = Input_Decay_Multiplier_Grid;
    }

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
}