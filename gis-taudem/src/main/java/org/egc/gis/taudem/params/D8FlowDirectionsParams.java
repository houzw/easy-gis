package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of D8FlowDirections
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class D8FlowDirectionsParams implements Params {


    /**
     * <pre>
     * Input_Pit_Filled_Elevation_Grid
     *  </pre>
     * @see #D8FlowDirectionsParams( String)
     */
    public D8FlowDirectionsParams(){}
    /**
     * A grid of elevation values.
     */
    @NotNull
    private String Input_Pit_Filled_Elevation_Grid;
    /**
    * A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
    */
    private String Output_D8_Flow_Direction_Grid;
    /**
    * A grid giving slope in the D8 flow direction.
    */
    private String Output_D8_Slope_Grid;
    private String outputDir = "./";


    /**
    * @param Input_Pit_Filled_Elevation_Grid A grid of elevation values.
    */
    public D8FlowDirectionsParams(@NotBlank String Input_Pit_Filled_Elevation_Grid){
        this.Input_Pit_Filled_Elevation_Grid = Input_Pit_Filled_Elevation_Grid;
    }

    public String getOutput_D8_Flow_Direction_Grid() {
        if (StringUtils.isBlank(Output_D8_Flow_Direction_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_Filled_Elevation_Grid, "Output_D8_Flow_Direction_Grid", "Raster Dataset", null));
        }
        return this.Output_D8_Flow_Direction_Grid;
    }
    public String getOutput_D8_Slope_Grid() {
        if (StringUtils.isBlank(Output_D8_Slope_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_Filled_Elevation_Grid, "Output_D8_Slope_Grid", "Raster Dataset", null));
        }
        return this.Output_D8_Slope_Grid;
    }
}