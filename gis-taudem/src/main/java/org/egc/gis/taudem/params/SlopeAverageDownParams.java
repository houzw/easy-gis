package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of SlopeAverageDown
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class SlopeAverageDownParams implements Params {


    /**
     * <pre>
     * Input_D8_Flow_Direction_Grid
     * Input_Pit_Filled_Elevation_Grid
     *  </pre>
     * @see #SlopeAverageDownParams( String,  String)
     */
    public SlopeAverageDownParams(){}
    /**
     * This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
     */
    @NotNull
    private String Input_D8_Flow_Direction_Grid;
    /**
     * A grid of elevation values.
     */
    @NotNull
    private String Input_Pit_Filled_Elevation_Grid;
    /**
    * default is 50d.
    * Input parameter of downslope distance over which to calculate the slope (in horizontal map units).
    */
    private Double Distance = 50d;

    /**
    * A grid of the ratio of  specific catchment area (contributing area) to slope.
    */
    private String Output_Slope_Average_Down_Grid;
    private String outputDir = "./";


    /**
    * @param Input_D8_Flow_Direction_Grid This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
    * @param Input_Pit_Filled_Elevation_Grid A grid of elevation values.
    */
    public SlopeAverageDownParams(@NotBlank String Input_D8_Flow_Direction_Grid, @NotBlank String Input_Pit_Filled_Elevation_Grid){
        this.Input_D8_Flow_Direction_Grid = Input_D8_Flow_Direction_Grid;
        this.Input_Pit_Filled_Elevation_Grid = Input_Pit_Filled_Elevation_Grid;
    }

    public String getOutput_Slope_Average_Down_Grid() {
        if (StringUtils.isBlank(Output_Slope_Average_Down_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_Slope_Average_Down_Grid", "Raster Dataset", null));
        }
        return this.Output_Slope_Average_Down_Grid;
    }
}