package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of MoveOutletsToStreams
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class MoveOutletsToStreamsParams implements Params {


    /**
     * <pre>
     * Input_D8_Flow_Direction_Grid
     * Input_Stream_Raster_Grid
     * Input_Outlets
     *  </pre>
     * @see #MoveOutletsToStreamsParams( String,  String,  String)
     */
    public MoveOutletsToStreamsParams(){}
    /**
     * This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
     */
    @NotNull
    private String Input_D8_Flow_Direction_Grid;
    /**
     * This output is an indicator grid (1,0) that indicates the location of streams, with a value of 1 for each of the stream cells and 0 for the remainder of the cells.
     */
    @NotNull
    private String Input_Stream_Raster_Grid;
    /**
     * A point feature defining points of interest or outlets that should ideally be located on a stream, but may not be exactly on the stream due to the fact that the feature point locations may not have been accurately registered with respect to the stream raster grid.
     */
    @NotNull
    private String Input_Outlets;
    /**
    * default is 50d.
    * This input paramater is the maximum number of grid cells that the points in the input outlet feature  will be moved before they are saved to the output outlet feature.
    */
    private Double Input_Maximum_Distance = 50d;

    /**
    * A point  OGR file defining points of interest or outlets.
    */
    private String Output_Outlets_file;
    private String outputDir = "./";


    /**
    * @param Input_D8_Flow_Direction_Grid This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
    * @param Input_Stream_Raster_Grid This output is an indicator grid (1,0) that indicates the location of streams, with a value of 1 for each of the stream cells and 0 for the remainder of the cells.
    * @param Input_Outlets A point feature defining points of interest or outlets that should ideally be located on a stream, but may not be exactly on the stream due to the fact that the feature point locations may not have been accurately registered with respect to the stream raster grid.
    */
    public MoveOutletsToStreamsParams(@NotBlank String Input_D8_Flow_Direction_Grid, @NotBlank String Input_Stream_Raster_Grid, @NotBlank String Input_Outlets){
        this.Input_D8_Flow_Direction_Grid = Input_D8_Flow_Direction_Grid;
        this.Input_Stream_Raster_Grid = Input_Stream_Raster_Grid;
        this.Input_Outlets = Input_Outlets;
    }

    public String getOutput_Outlets_file() {
        if (StringUtils.isBlank(Output_Outlets_file)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_Outlets_file", "File", null));
        }
        return this.Output_Outlets_file;
    }
}