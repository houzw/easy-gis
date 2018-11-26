package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of TopographicWetnessIndex
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class TopographicWetnessIndexParams implements Params {


    /**
     * <pre>
     * Input_Specific_Catchment_Area_Grid
     * Input_Slope_Grid
     *  </pre>
     * @see #TopographicWetnessIndexParams( String,  String)
     */
    public TopographicWetnessIndexParams(){}
    /**
     * A grid of specific catchment area which is the contributing area per unit contour length.
     */
    @NotNull
    private String Input_Specific_Catchment_Area_Grid;
    /**
     * A grid of slope.
     */
    @NotNull
    private String Input_Slope_Grid;
    /**
    * A grid of the natural log of the ratio of specific catchment area (contributing area) to slope, ln(a/S).
    */
    private String Output_Wetness_Index_Grid;
    private String outputDir = "./";


    /**
    * @param Input_Specific_Catchment_Area_Grid A grid of specific catchment area which is the contributing area per unit contour length.
    * @param Input_Slope_Grid A grid of slope.
    */
    public TopographicWetnessIndexParams(@NotBlank String Input_Specific_Catchment_Area_Grid, @NotBlank String Input_Slope_Grid){
        this.Input_Specific_Catchment_Area_Grid = Input_Specific_Catchment_Area_Grid;
        this.Input_Slope_Grid = Input_Slope_Grid;
    }

    public String getOutput_Wetness_Index_Grid() {
        if (StringUtils.isBlank(Output_Wetness_Index_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Specific_Catchment_Area_Grid, "Output_Wetness_Index_Grid", "Raster Dataset", null));
        }
        return this.Output_Wetness_Index_Grid;
    }
}