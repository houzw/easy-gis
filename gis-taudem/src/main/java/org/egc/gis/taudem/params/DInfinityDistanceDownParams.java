package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of DInfinityDistanceDown
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class DInfinityDistanceDownParams implements Params {


    /**
     * <pre>
     * Input_DInfinity_Flow_Direction_Grid
     * Input_Pit_Filled_Elevation_Grid
     * Input_Stream_Raster_Grid
     *  </pre>
     * @see #DInfinityDistanceDownParams( String,  String,  String)
     */
    public DInfinityDistanceDownParams(){}
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
     * A grid indicating streams, by using a grid cell value of 1 on streams and 0 off streams.
     */
    @NotNull
    private String Input_Stream_Raster_Grid;
    /**
    * default is "ave".
    * Alternative values are ave, max, min, where ave for Average, max for Maximum, min for Minimum Statistical method used to calculate the distance down to the stream.
    */
    private String Statistical_Method = "ave";

    /**
    * default is "h".
    * Alternative values are h, v, p, s, where h for Horizontal, v for Vertical, p for Pythagoras, s for Surface Distance method used to calculate the distance down to the stream.
    */
    private String Distance_Method = "h";

    /**
    * default is true.
    * A flag that determines whether the tool should check for edge contamination.
    */
    private Boolean Check_for_edge_contamination = true;

    /**
    * A grid giving weights (loadings) to be used in the distance calculation.
    */
    private String Input_Weight_Path_Grid;
    /**
    * Creates a grid containing the distance to stream calculated using the D-infinity flow model and the statistical and path methods chosen.
    */
    private String Output_DInfinity_Drop_to_Stream_Grid;
    private String outputDir = "./";


    /**
    * @param Input_DInfinity_Flow_Direction_Grid A grid giving flow directions by the D-Infinity method.
    * @param Input_Pit_Filled_Elevation_Grid This input is a grid of elevation values.
    * @param Input_Stream_Raster_Grid A grid indicating streams, by using a grid cell value of 1 on streams and 0 off streams.
    */
    public DInfinityDistanceDownParams(@NotBlank String Input_DInfinity_Flow_Direction_Grid, @NotBlank String Input_Pit_Filled_Elevation_Grid, @NotBlank String Input_Stream_Raster_Grid){
        this.Input_DInfinity_Flow_Direction_Grid = Input_DInfinity_Flow_Direction_Grid;
        this.Input_Pit_Filled_Elevation_Grid = Input_Pit_Filled_Elevation_Grid;
        this.Input_Stream_Raster_Grid = Input_Stream_Raster_Grid;
    }

    public String getOutput_DInfinity_Drop_to_Stream_Grid() {
        if (StringUtils.isBlank(Output_DInfinity_Drop_to_Stream_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_DInfinity_Flow_Direction_Grid, "Output_DInfinity_Drop_to_Stream_Grid", "Raster Dataset", null));
        }
        return this.Output_DInfinity_Drop_to_Stream_Grid;
    }
}