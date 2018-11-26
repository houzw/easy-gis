package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of DInfinityDistanceUp
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class DInfinityDistanceUpParams implements Params {


    /**
     * <pre>
     * Input_DInfinity_Flow_Direction_Grid
     * Input_Pit_Filled_Elevation_Grid
     * Input_Slope_Grid
     *  </pre>
     * @see #DInfinityDistanceUpParams( String,  String,  String)
     */
    public DInfinityDistanceUpParams(){}
    /**
     * A grid giving flow directions by the D-Infinity method.
     */
    @NotNull
    private String Input_DInfinity_Flow_Direction_Grid;
    /**
     * This input is a grid of elevation values.
     */
    @NotNull
    private String Input_Pit_Filled_Elevation_Grid;
    /**
     * This input is a grid of slope values.
     */
    @NotNull
    private String Input_Slope_Grid;
    /**
    * default is 0.5d.
    * The proportion threshold parameter where only grid cells that contribute flow with a proportion greater than this user specified threshold (t) is considered to be upslope of any given grid cell.
    */
    private Double Input_Proportion_Threshold = 0.5d;

    /**
    * default is "ave".
    * Statistical method used to calculate the distance down to the stream.
    */
    private String Statistical_Method = "ave";

    /**
    * default is "h".
    * Distance method used to calculate the distance down to the stream.
    */
    private String Distance_Method = "h";

    /**
    * default is true.
    * A flag that determines whether the tool should check for edge contamination.
    */
    private Boolean Check_for_Edge_Contamination = true;

    /**
    * 
    */
    private String Output_DInfinity_Distance_Up_Grid;
    private String outputDir = "./";


    /**
    * @param Input_DInfinity_Flow_Direction_Grid A grid giving flow directions by the D-Infinity method.
    * @param Input_Pit_Filled_Elevation_Grid This input is a grid of elevation values.
    * @param Input_Slope_Grid This input is a grid of slope values.
    */
    public DInfinityDistanceUpParams(@NotBlank String Input_DInfinity_Flow_Direction_Grid, @NotBlank String Input_Pit_Filled_Elevation_Grid, @NotBlank String Input_Slope_Grid){
        this.Input_DInfinity_Flow_Direction_Grid = Input_DInfinity_Flow_Direction_Grid;
        this.Input_Pit_Filled_Elevation_Grid = Input_Pit_Filled_Elevation_Grid;
        this.Input_Slope_Grid = Input_Slope_Grid;
    }

    public String getOutput_DInfinity_Distance_Up_Grid() {
        if (StringUtils.isBlank(Output_DInfinity_Distance_Up_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_DInfinity_Flow_Direction_Grid, "Output_DInfinity_Distance_Up_Grid", "Raster Dataset", null));
        }
        return this.Output_DInfinity_Distance_Up_Grid;
    }
}