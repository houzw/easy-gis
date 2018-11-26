package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of PeukerDouglas
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class PeukerDouglasParams implements Params {


    /**
     * <pre>
     * Input_Elevation_Grid
     *  </pre>
     * @see #PeukerDouglasParams( String)
     */
    public PeukerDouglasParams(){}
    /**
     * A grid of elevation values.
     */
    @NotNull
    private String Input_Elevation_Grid;
    /**
    * default is 0.4d.
    * The center weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
    */
    private Double Center_Smoothing_Weight = 0.4d;

    /**
    * default is 0.1d.
    * The side weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
    */
    private Double Side_Smoothing_Weight = 0.1d;

    /**
    * default is 0.05d.
    * The diagonal weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
    */
    private Double Diagonal_Smoothing_Weight = 0.05d;

    /**
    * An indicator grid (1,0) of upward curved grid cells according to the Peuker and Douglas algorithm, and if viewed, resembles a channel network.
    */
    private String Output_Stream_Source_Grid;
    private String outputDir = "./";


    /**
    * @param Input_Elevation_Grid A grid of elevation values.
    */
    public PeukerDouglasParams(@NotBlank String Input_Elevation_Grid){
        this.Input_Elevation_Grid = Input_Elevation_Grid;
    }

    public String getOutput_Stream_Source_Grid() {
        if (StringUtils.isBlank(Output_Stream_Source_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Elevation_Grid, "Output_Stream_Source_Grid", "Raster Dataset", null));
        }
        return this.Output_Stream_Source_Grid;
    }
}