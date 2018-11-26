package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of DInfinityAvalancheRunout
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class DInfinityAvalancheRunoutParams implements Params {


    /**
     * <pre>
     * Input_Pit_Filled_Elevation_Grid
     * Input_DInfinity_Flow_Direction_Grid
     * Input_Avalanche_Source_Site_Grid
     *  </pre>
     * @see #DInfinityAvalancheRunoutParams( String,  String,  String)
     */
    public DInfinityAvalancheRunoutParams(){}
    /**
     * A grid of elevation values.
     */
    @NotNull
    private String Input_Pit_Filled_Elevation_Grid;
    /**
     * A grid giving flow directions by the D-Infinity method.
     */
    @NotNull
    private String Input_DInfinity_Flow_Direction_Grid;
    /**
     * This is a grid of source areas for snow avalanches that are commonly identified manually using a mix of experience and visual interpretation of maps.
     */
    @NotNull
    private String Input_Avalanche_Source_Site_Grid;
    /**
    * default is 0.2d.
    * This value is a threshold proportion that is used to limit the disperson of flow caused by using the D-infinity multiple flow direction method for determining flow direction.
    */
    private Double Input_Proportion_Threshold = 0.2d;

    /**
    * default is 20d.
    * This value is the threshold angle, called the Alpha Angle, that is used to determine which of the cells downslope from the source cells are in the affected area.
    */
    private Double Input_Alpha_Angle_Threshold = 20d;

    /**
    * This option selects the method used to measure the distance used to calculate the slope angle.
    */
    private String Path_Distance_Method;
    /**
    * This grid Identifies the avalanche's runout zone (affected area) using a runout zone indicator with value 0 to indicate that this grid cell is not in the runout zone and value > 0 to indicate that this grid cell is in the runout zone.
    */
    private String Output_Runout_Zone_Grid;
    /**
    * This is a grid of the flow distance from the source site that has the highest angle to each cell.
    */
    private String Output_Path_Distance_Grid;
    private String outputDir = "./";


    /**
    * @param Input_Pit_Filled_Elevation_Grid A grid of elevation values.
    * @param Input_DInfinity_Flow_Direction_Grid A grid giving flow directions by the D-Infinity method.
    * @param Input_Avalanche_Source_Site_Grid This is a grid of source areas for snow avalanches that are commonly identified manually using a mix of experience and visual interpretation of maps.
    */
    public DInfinityAvalancheRunoutParams(@NotBlank String Input_Pit_Filled_Elevation_Grid, @NotBlank String Input_DInfinity_Flow_Direction_Grid, @NotBlank String Input_Avalanche_Source_Site_Grid){
        this.Input_Pit_Filled_Elevation_Grid = Input_Pit_Filled_Elevation_Grid;
        this.Input_DInfinity_Flow_Direction_Grid = Input_DInfinity_Flow_Direction_Grid;
        this.Input_Avalanche_Source_Site_Grid = Input_Avalanche_Source_Site_Grid;
    }

    public String getOutput_Runout_Zone_Grid() {
        if (StringUtils.isBlank(Output_Runout_Zone_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_Filled_Elevation_Grid, "Output_Runout_Zone_Grid", "Raster Dataset", null));
        }
        return this.Output_Runout_Zone_Grid;
    }
    public String getOutput_Path_Distance_Grid() {
        if (StringUtils.isBlank(Output_Path_Distance_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_Filled_Elevation_Grid, "Output_Path_Distance_Grid", "Raster Dataset", null));
        }
        return this.Output_Path_Distance_Grid;
    }
}