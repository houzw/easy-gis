package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of D8ExtremeUpslopeValue
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class D8ExtremeUpslopeValueParams implements Params {


    /**
     * <pre>
     * Input_D8_Flow_Direction_Grid
     * Input_Value_Grid
     *  </pre>
     * @see #D8ExtremeUpslopeValueParams( String,  String)
     */
    public D8ExtremeUpslopeValueParams(){}
    /**
     * A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     */
    @NotNull
    private String Input_D8_Flow_Direction_Grid;
    /**
     * This is the grid of values of which the maximum or minimum upslope value is selected.
     */
    @NotNull
    private String Input_Value_Grid;
    /**
    * default is true.
    * A flag to indicate whether the maximum or minimum upslope value is to be calculated.
    */
    private Boolean Use_maximum_upslope_value = true;

    /**
    * default is true.
    * A flag that indicates whether the tool should check for edge contamination.
    */
    private Boolean Check_for_edge_contamination = true;

    /**
    * A point feature defining outlets of interest.
    */
    private String Input_Outlets;
    /**
    * A grid of the maximum/minimum upslope values.
    */
    private String Output_Extreme_Value_Grid;
    private String outputDir = "./";


    /**
    * @param Input_D8_Flow_Direction_Grid A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
    * @param Input_Value_Grid This is the grid of values of which the maximum or minimum upslope value is selected.
    */
    public D8ExtremeUpslopeValueParams(@NotBlank String Input_D8_Flow_Direction_Grid, @NotBlank String Input_Value_Grid){
        this.Input_D8_Flow_Direction_Grid = Input_D8_Flow_Direction_Grid;
        this.Input_Value_Grid = Input_Value_Grid;
    }

    public String getOutput_Extreme_Value_Grid() {
        if (StringUtils.isBlank(Output_Extreme_Value_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_Extreme_Value_Grid", "Raster Dataset", null));
        }
        return this.Output_Extreme_Value_Grid;
    }
}