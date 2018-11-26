package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of StreamDefinitionByThreshold
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class StreamDefinitionByThresholdParams implements Params {


    /**
     * <pre>
     * Input_Accumulated_Stream_Source_Grid
     *  </pre>
     * @see #StreamDefinitionByThresholdParams( String)
     */
    public StreamDefinitionByThresholdParams(){}
    /**
     * This grid nominally accumulates some characteristic or combination of characteristics of the watershed.
     */
    @NotNull
    private String Input_Accumulated_Stream_Source_Grid;
    /**
    * This optional input is a grid that is used to mask the domain of interest and output is only provided where this grid is >= 0.
    */
    private String Input_Mask_Grid;
    /**
    * default is 100d.
    * This parameter is compared to the value in the Accumulated Stream Source grid (*ssa) to determine if the cell should be considered a stream cell.
    */
    private Double Threshold = 100d;

    /**
    * This is an indicator grid (1,0) that indicates the location of streams, with a value of 1 for each of the stream cells and 0 for the remainder of the cells.
    */
    private String Output_Stream_Raster_Grid;
    private String outputDir = "./";


    /**
    * @param Input_Accumulated_Stream_Source_Grid This grid nominally accumulates some characteristic or combination of characteristics of the watershed.
    */
    public StreamDefinitionByThresholdParams(@NotBlank String Input_Accumulated_Stream_Source_Grid){
        this.Input_Accumulated_Stream_Source_Grid = Input_Accumulated_Stream_Source_Grid;
    }

    public String getOutput_Stream_Raster_Grid() {
        if (StringUtils.isBlank(Output_Stream_Raster_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Accumulated_Stream_Source_Grid, "Output_Stream_Raster_Grid", "Raster Dataset", null));
        }
        return this.Output_Stream_Raster_Grid;
    }
}