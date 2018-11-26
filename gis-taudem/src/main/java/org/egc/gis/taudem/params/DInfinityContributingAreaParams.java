package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of DInfinityContributingArea
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class DInfinityContributingAreaParams implements Params {


    /**
     * <pre>
     * Input_DInfinity_Flow_Direction_Grid
     *  </pre>
     * @see #DInfinityContributingAreaParams( String)
     */
    public DInfinityContributingAreaParams(){}
    /**
     * A grid of flow directions based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997, "A New Method for the Determination of Flow Directions and Contributing Areas in Grid Digital Elevation Models," Water Resources Research, 33(2): 309-319).
     */
    @NotNull
    private String Input_DInfinity_Flow_Direction_Grid;
    /**
    * A point feature defining the outlets of interest.
    */
    private String Input_Outlets;
    /**
    * A grid giving contribution to flow for each cell.
    */
    private String Input_Weight_Grid;
    /**
    * default is true.
    * A flag that indicates whether the tool should check for edge contamination.
    */
    private Boolean Check_for_Edge_Contamination = true;

    /**
    * A grid of specific catchment area which is the contributing area per unit contour length using the multiple flow direction D-infinity approach.
    */
    private String Output_DInfinity_Specific_Catchment_Area_Grid;
    private String outputDir = "./";


    /**
    * @param Input_DInfinity_Flow_Direction_Grid A grid of flow directions based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997, "A New Method for the Determination of Flow Directions and Contributing Areas in Grid Digital Elevation Models," Water Resources Research, 33(2): 309-319).
    */
    public DInfinityContributingAreaParams(@NotBlank String Input_DInfinity_Flow_Direction_Grid){
        this.Input_DInfinity_Flow_Direction_Grid = Input_DInfinity_Flow_Direction_Grid;
    }

    public String getOutput_DInfinity_Specific_Catchment_Area_Grid() {
        if (StringUtils.isBlank(Output_DInfinity_Specific_Catchment_Area_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_DInfinity_Flow_Direction_Grid, "Output_DInfinity_Specific_Catchment_Area_Grid", "Raster Dataset", null));
        }
        return this.Output_DInfinity_Specific_Catchment_Area_Grid;
    }
}