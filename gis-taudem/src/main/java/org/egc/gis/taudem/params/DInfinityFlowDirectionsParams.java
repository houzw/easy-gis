package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of DInfinityFlowDirections
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class DInfinityFlowDirectionsParams implements Params {


    /**
     * <pre>
     * Input_Pit_FIlled_Elevation_Grid
     *  </pre>
     * @see #DInfinityFlowDirectionsParams( String)
     */
    public DInfinityFlowDirectionsParams(){}
    /**
     * A grid of elevation values.
     */
    @NotNull
    private String Input_Pit_FIlled_Elevation_Grid;
    /**
    * A grid of flow directions based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997, "A New Method for the Determination of Flow Directions and Contributing Areas in Grid Digital Elevation Models," Water Resources Research, 33(2): 309-319).
    */
    private String Output_DInfinity_Flow_Direction_Grid;
    /**
    * A grid of slope evaluated using the D-infinity method described in Tarboton, D.
    */
    private String Output_DInfinity_Slope_Grid;
    private String outputDir = "./";


    /**
    * @param Input_Pit_FIlled_Elevation_Grid A grid of elevation values.
    */
    public DInfinityFlowDirectionsParams(@NotBlank String Input_Pit_FIlled_Elevation_Grid){
        this.Input_Pit_FIlled_Elevation_Grid = Input_Pit_FIlled_Elevation_Grid;
    }

    public String getOutput_DInfinity_Flow_Direction_Grid() {
        if (StringUtils.isBlank(Output_DInfinity_Flow_Direction_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_FIlled_Elevation_Grid, "Output_DInfinity_Flow_Direction_Grid", "Raster Dataset", null));
        }
        return this.Output_DInfinity_Flow_Direction_Grid;
    }
    public String getOutput_DInfinity_Slope_Grid() {
        if (StringUtils.isBlank(Output_DInfinity_Slope_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_FIlled_Elevation_Grid, "Output_DInfinity_Slope_Grid", "Raster Dataset", null));
        }
        return this.Output_DInfinity_Slope_Grid;
    }
}