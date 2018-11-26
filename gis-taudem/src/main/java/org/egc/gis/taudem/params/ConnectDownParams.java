package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of ConnectDown
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class ConnectDownParams implements Params {


    /**
     * <pre>
     * Input_D8_Flow_Direction_Grid
     * Input_D8_Contributing_Area_Grid
     * Input_Watershed_Grid
     *  </pre>
     * @see #ConnectDownParams( String,  String,  String)
     */
    public ConnectDownParams(){}
    /**
     * This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
     */
    @NotNull
    private String Input_D8_Flow_Direction_Grid;
    /**
     * A grid giving the contributing area value in terms of the number of grid cells (or the summation of weights) for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it using the D8 algorithm.
     */
    @NotNull
    private String Input_D8_Contributing_Area_Grid;
    /**
     * Watershed grid delineated from gage watershed function or streamreachwatershed function.
     */
    @NotNull
    private String Input_Watershed_Grid;
    /**
    * Number of grid cells move to downstream based on flow directions.
    */
    private Long Input_Number_of_Grid_Cells;
    /**
    * This output is point a OGR file where each point is created from watershed grid having the largest contributing area for each zone.
    */
    private String Output_Outlets_file;
    /**
    * This output is a point OGR file where each outlet is moved downflow a specified number of grid cells using flow directions.
    */
    private String Output_MovedOutlets_file;
    private String outputDir = "./";


    /**
    * @param Input_D8_Flow_Direction_Grid This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
    * @param Input_D8_Contributing_Area_Grid A grid giving the contributing area value in terms of the number of grid cells (or the summation of weights) for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it using the D8 algorithm.
    * @param Input_Watershed_Grid Watershed grid delineated from gage watershed function or streamreachwatershed function.
    */
    public ConnectDownParams(@NotBlank String Input_D8_Flow_Direction_Grid, @NotBlank String Input_D8_Contributing_Area_Grid, @NotBlank String Input_Watershed_Grid){
        this.Input_D8_Flow_Direction_Grid = Input_D8_Flow_Direction_Grid;
        this.Input_D8_Contributing_Area_Grid = Input_D8_Contributing_Area_Grid;
        this.Input_Watershed_Grid = Input_Watershed_Grid;
    }

    public String getOutput_Outlets_file() {
        if (StringUtils.isBlank(Output_Outlets_file)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_Outlets_file", "File", null));
        }
        return this.Output_Outlets_file;
    }
    public String getOutput_MovedOutlets_file() {
        if (StringUtils.isBlank(Output_MovedOutlets_file)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_MovedOutlets_file", "File", null));
        }
        return this.Output_MovedOutlets_file;
    }
}