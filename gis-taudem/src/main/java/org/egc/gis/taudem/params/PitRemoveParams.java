package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of PitRemove
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class PitRemoveParams implements Params {


    /**
     * <pre>
     * Input_Elevation_Grid
     *  </pre>
     * @see #PitRemoveParams( String)
     */
    public PitRemoveParams(){}
    /**
     * A digital elevation model (DEM) grid to serve as the base input for the terrain analysis and stream delineation.
     */
    @NotNull
    private String Input_Elevation_Grid;
    /**
    * If this option is selected Fill ensures that the grid is hydrologically conditioned with cell to cell connectivity in only 4 directions (N, S, E or W neighbors).
    */
    private Boolean Fill_Considering_only_4_way_neighbors;
    /**
    * 
    */
    private String Input_Depression_Mask_Grid;
    /**
    * A grid of elevation values with pits removed so that flow is routed off of the domain.
    */
    private String Output_Pit_Removed_Elevation_Grid;
    private String outputDir = "./";


    /**
    * @param Input_Elevation_Grid A digital elevation model (DEM) grid to serve as the base input for the terrain analysis and stream delineation.
    */
    public PitRemoveParams(@NotBlank String Input_Elevation_Grid){
        this.Input_Elevation_Grid = Input_Elevation_Grid;
    }

    public String getOutput_Pit_Removed_Elevation_Grid() {
        if (StringUtils.isBlank(Output_Pit_Removed_Elevation_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Elevation_Grid, "Output_Pit_Removed_Elevation_Grid", "Raster Dataset", null));
        }
        return this.Output_Pit_Removed_Elevation_Grid;
    }
}