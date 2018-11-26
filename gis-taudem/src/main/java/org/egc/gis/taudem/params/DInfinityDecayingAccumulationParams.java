package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of DInfinityDecayingAccumulation
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class DInfinityDecayingAccumulationParams implements Params {


    /**
     * <pre>
     * Input_DInfinity_Flow_Direction_Grid
     * Input_Decay_Multiplier_Grid
     *  </pre>
     * @see #DInfinityDecayingAccumulationParams( String,  String)
     */
    public DInfinityDecayingAccumulationParams(){}
    /**
     * A grid giving flow direction by the D-infinity method.
     */
    @NotNull
    private String Input_DInfinity_Flow_Direction_Grid;
    /**
     * A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
     */
    @NotNull
    private String Input_Decay_Multiplier_Grid;
    /**
    * A grid giving weights (loadings) to be used in the accumulation.
    */
    private String Input_Weight_Grid;
    /**
    * This optional input is a point feature  defining outlets of interest.
    */
    private String Input_Outlets;
    /**
    * default is true.
    * This checkbox determines whether the tool should check for edge contamination.
    */
    private Boolean Check_for_Edge_Contamination = true;

    /**
    * The D-Infinity Decaying Accumulation tool creates a grid of the accumulated mass at each location in the domain where mass moves with the D-infinity flow field, but is subject to first order decay in moving from cell to cell.
    */
    private String Output_Decayed_Specific_Catchment_Area_Grid;
    private String outputDir = "./";


    /**
    * @param Input_DInfinity_Flow_Direction_Grid A grid giving flow direction by the D-infinity method.
    * @param Input_Decay_Multiplier_Grid A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
    */
    public DInfinityDecayingAccumulationParams(@NotBlank String Input_DInfinity_Flow_Direction_Grid, @NotBlank String Input_Decay_Multiplier_Grid){
        this.Input_DInfinity_Flow_Direction_Grid = Input_DInfinity_Flow_Direction_Grid;
        this.Input_Decay_Multiplier_Grid = Input_Decay_Multiplier_Grid;
    }

    public String getOutput_Decayed_Specific_Catchment_Area_Grid() {
        if (StringUtils.isBlank(Output_Decayed_Specific_Catchment_Area_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_DInfinity_Flow_Direction_Grid, "Output_Decayed_Specific_Catchment_Area_Grid", "Raster Dataset", null));
        }
        return this.Output_Decayed_Specific_Catchment_Area_Grid;
    }
}