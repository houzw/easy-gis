package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of D8DistanceToStreams
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class D8DistanceToStreamsParams implements Params {


    /**
     * <pre>
     * Input_D8_Flow_Direction_Grid
     * Input_Stream_Raster_Grid
     *  </pre>
     * @see #D8DistanceToStreamsParams( String,  String)
     */
    public D8DistanceToStreamsParams(){}
    /**
     * This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
     */
    @NotNull
    private String Input_D8_Flow_Direction_Grid;
    /**
     * A grid indicating streams.
     */
    @NotNull
    private String Input_Stream_Raster_Grid;
    /**
    * default is 50d.
    * This value acts as threshold on the Stream Raster Grid to determine the location of streams.
    */
    private Double Threshold = 50d;

    /**
    * A grid giving the horizontal distance along the flow path as defined by the D8 Flow Directions Grid to the streams in the Stream Raster Grid.
    */
    private String Output_Distance_to_Streams_Grid;
    private String outputDir = "./";


    /**
    * @param Input_D8_Flow_Direction_Grid This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
    * @param Input_Stream_Raster_Grid A grid indicating streams.
    */
    public D8DistanceToStreamsParams(@NotBlank String Input_D8_Flow_Direction_Grid, @NotBlank String Input_Stream_Raster_Grid){
        this.Input_D8_Flow_Direction_Grid = Input_D8_Flow_Direction_Grid;
        this.Input_Stream_Raster_Grid = Input_Stream_Raster_Grid;
    }

    public String getOutput_Distance_to_Streams_Grid() {
        if (StringUtils.isBlank(Output_Distance_to_Streams_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_Distance_to_Streams_Grid", "Raster Dataset", null));
        }
        return this.Output_Distance_to_Streams_Grid;
    }
}