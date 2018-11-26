package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of SlopeOverAreaRatio
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class SlopeOverAreaRatioParams implements Params {


    /**
     * <pre>
     * Input_Slope_Grid
     * Input_Specific_Catchment_Area_Grid
     *  </pre>
     * @see #SlopeOverAreaRatioParams( String,  String)
     */
    public SlopeOverAreaRatioParams(){}
    /**
     * A grid of slope.
     */
    @NotNull
    private String Input_Slope_Grid;
    /**
     * A grid giving the contributing area value for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it.
     */
    @NotNull
    private String Input_Specific_Catchment_Area_Grid;
    /**
    * A grid of the ratio of slope to specific catchment area (contributing area).
    */
    private String Output_Slope_Divided_By_Area_Ratio_Grid;
    private String outputDir = "./";


    /**
    * @param Input_Slope_Grid A grid of slope.
    * @param Input_Specific_Catchment_Area_Grid A grid giving the contributing area value for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it.
    */
    public SlopeOverAreaRatioParams(@NotBlank String Input_Slope_Grid, @NotBlank String Input_Specific_Catchment_Area_Grid){
        this.Input_Slope_Grid = Input_Slope_Grid;
        this.Input_Specific_Catchment_Area_Grid = Input_Specific_Catchment_Area_Grid;
    }

    public String getOutput_Slope_Divided_By_Area_Ratio_Grid() {
        if (StringUtils.isBlank(Output_Slope_Divided_By_Area_Ratio_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Slope_Grid, "Output_Slope_Divided_By_Area_Ratio_Grid", "Raster Dataset", null));
        }
        return this.Output_Slope_Divided_By_Area_Ratio_Grid;
    }
}