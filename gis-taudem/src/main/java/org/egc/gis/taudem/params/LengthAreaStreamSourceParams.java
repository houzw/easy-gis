package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of LengthAreaStreamSource
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class LengthAreaStreamSourceParams implements Params {


    /**
     * <pre>
     * Input_Length_Grid
     * Input_Contributing_Area_Grid
     *  </pre>
     * @see #LengthAreaStreamSourceParams( String,  String)
     */
    public LengthAreaStreamSourceParams(){}
    /**
     * A grid of the maximum upslope length for each cell.
     */
    @NotNull
    private String Input_Length_Grid;
    /**
     * A grid of contributing area values for each cell that were calculated using the D8 algorithm.
     */
    @NotNull
    private String Input_Contributing_Area_Grid;
    /**
    * default is 0.03d.
    * The multiplier threshold (M) parameter which is used in the formula: A > (M)(L^y), to identify the beginning of streams.
    */
    private Double Threshold_M = 0.03d;

    /**
    * default is 1.3d.
    * The exponent (y) parameter which is used in the formula: A > (M)(L^y), to identify the beginning of streams.
    */
    private Double Exponent_y = 1.3d;

    /**
    * An indicator grid (1,0) that evaluates A >= (M)(L^y), based on the maximum upslope path length, the D8 contributing area grid inputs, and parameters M and y.
    */
    private String Output_Stream_Source_Grid;
    private String outputDir = "./";


    /**
    * @param Input_Length_Grid A grid of the maximum upslope length for each cell.
    * @param Input_Contributing_Area_Grid A grid of contributing area values for each cell that were calculated using the D8 algorithm.
    */
    public LengthAreaStreamSourceParams(@NotBlank String Input_Length_Grid, @NotBlank String Input_Contributing_Area_Grid){
        this.Input_Length_Grid = Input_Length_Grid;
        this.Input_Contributing_Area_Grid = Input_Contributing_Area_Grid;
    }

    public String getOutput_Stream_Source_Grid() {
        if (StringUtils.isBlank(Output_Stream_Source_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Length_Grid, "Output_Stream_Source_Grid", "Raster Dataset", null));
        }
        return this.Output_Stream_Source_Grid;
    }
}