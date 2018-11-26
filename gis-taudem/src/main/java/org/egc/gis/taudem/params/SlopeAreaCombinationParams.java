package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of SlopeAreaCombination
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class SlopeAreaCombinationParams implements Params {


    /**
     * <pre>
     * Input_Slope_Grid
     * Input_Area_Grid
     *  </pre>
     * @see #SlopeAreaCombinationParams( String,  String)
     */
    public SlopeAreaCombinationParams(){}
    /**
     * This input is a grid of slope values.
     */
    @NotNull
    private String Input_Slope_Grid;
    /**
     * A grid giving the specific catchment area for each cell taken as its own contribution (grid cell length or summation of weights) plus the proportional contribution from upslope neighbors that drain in to it.
     */
    @NotNull
    private String Input_Area_Grid;
    /**
    * default is 2d.
    * The slope exponent (m) parameter which will be used in the formula: (S^m)(A^n), that is used to create the slope-area grid.
    */
    private Double Slope_Exponent_m = 2d;

    /**
    * default is 1d.
    * The area exponent (n) parameter which will be used in the formula: (S^m)(A^n), that is used to create the slope-area grid.
    */
    private Double Area_Exponent_n = 1d;

    /**
    * A grid of slope-area values = (S^m)(A^n) calculated from the slope grid, specific catchment area grid, m slope exponent parameter, and n area exponent parameter.
    */
    private String Output_Slope_Area_Grid;
    private String outputDir = "./";


    /**
    * @param Input_Slope_Grid This input is a grid of slope values.
    * @param Input_Area_Grid A grid giving the specific catchment area for each cell taken as its own contribution (grid cell length or summation of weights) plus the proportional contribution from upslope neighbors that drain in to it.
    */
    public SlopeAreaCombinationParams(@NotBlank String Input_Slope_Grid, @NotBlank String Input_Area_Grid){
        this.Input_Slope_Grid = Input_Slope_Grid;
        this.Input_Area_Grid = Input_Area_Grid;
    }

    public String getOutput_Slope_Area_Grid() {
        if (StringUtils.isBlank(Output_Slope_Area_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Slope_Grid, "Output_Slope_Area_Grid", "Raster Dataset", null));
        }
        return this.Output_Slope_Area_Grid;
    }
}