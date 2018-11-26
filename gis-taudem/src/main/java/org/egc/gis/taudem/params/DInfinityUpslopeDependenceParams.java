package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of DInfinityUpslopeDependence
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class DInfinityUpslopeDependenceParams implements Params {


    /**
     * <pre>
     * Input_DInfinity_Flow_Direction_Grid
     * Input_Destination_Grid
     *  </pre>
     * @see #DInfinityUpslopeDependenceParams( String,  String)
     */
    public DInfinityUpslopeDependenceParams(){}
    /**
     * A grid giving flow direction by the D-Infinity method where the flow direction angle is determined as the direction of the steepest downward slope on the eight triangular facets formed in a 3 x 3 grid cell window centered on the grid cell of interest.
     */
    @NotNull
    private String Input_DInfinity_Flow_Direction_Grid;
    /**
     * A grid that encodes the destination zone that may receive flow from upslope.
     */
    @NotNull
    private String Input_Destination_Grid;
    /**
    * A grid quantifing the amount each source point in the domain contributes to the zone defined by the destination grid.
    */
    private String Output_Upslope_Dependence_Grid;
    private String outputDir = "./";


    /**
    * @param Input_DInfinity_Flow_Direction_Grid A grid giving flow direction by the D-Infinity method where the flow direction angle is determined as the direction of the steepest downward slope on the eight triangular facets formed in a 3 x 3 grid cell window centered on the grid cell of interest.
    * @param Input_Destination_Grid A grid that encodes the destination zone that may receive flow from upslope.
    */
    public DInfinityUpslopeDependenceParams(@NotBlank String Input_DInfinity_Flow_Direction_Grid, @NotBlank String Input_Destination_Grid){
        this.Input_DInfinity_Flow_Direction_Grid = Input_DInfinity_Flow_Direction_Grid;
        this.Input_Destination_Grid = Input_Destination_Grid;
    }

    public String getOutput_Upslope_Dependence_Grid() {
        if (StringUtils.isBlank(Output_Upslope_Dependence_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_DInfinity_Flow_Direction_Grid, "Output_Upslope_Dependence_Grid", "Raster Dataset", null));
        }
        return this.Output_Upslope_Dependence_Grid;
    }
}