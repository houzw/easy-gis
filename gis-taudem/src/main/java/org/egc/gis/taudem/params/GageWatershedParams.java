package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of GageWatershed
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class GageWatershedParams implements Params {


    /**
     * <pre>
     * Input_D8_Flow_Direction_Grid
     * Input_Gages_file
     *  </pre>
     * @see #GageWatershedParams( String,  String)
     */
    public GageWatershedParams(){}
    /**
     * A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     */
    @NotNull
    private String Input_D8_Flow_Direction_Grid;
    /**
     * A point feature defining the gages(outlets) to which watersheds will be delineated.
     */
    @NotNull
    private String Input_Gages_file;
    /**
    * This output grid identifies each gage watershed.
    */
    private String Output_GageWatershed;
    /**
    * 
    */
    private String Output_Downstream_Identefier;
    private String outputDir = "./";


    /**
    * @param Input_D8_Flow_Direction_Grid A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
    * @param Input_Gages_file A point feature defining the gages(outlets) to which watersheds will be delineated.
    */
    public GageWatershedParams(@NotBlank String Input_D8_Flow_Direction_Grid, @NotBlank String Input_Gages_file){
        this.Input_D8_Flow_Direction_Grid = Input_D8_Flow_Direction_Grid;
        this.Input_Gages_file = Input_Gages_file;
    }

    public String getOutput_GageWatershed() {
        if (StringUtils.isBlank(Output_GageWatershed)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_GageWatershed", "Raster Dataset", null));
        }
        return this.Output_GageWatershed;
    }
    public String getOutput_Downstream_Identefier() {
        if (StringUtils.isBlank(Output_Downstream_Identefier)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_Downstream_Identefier", "File", null));
        }
        return this.Output_Downstream_Identefier;
    }
}