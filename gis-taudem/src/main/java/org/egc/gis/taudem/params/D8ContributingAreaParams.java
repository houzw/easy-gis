package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of D8ContributingArea
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class D8ContributingAreaParams implements Params {


    /**
     * <pre>
     * Input_D8_Flow_Direction_Grid
     *  </pre>
     * @see #D8ContributingAreaParams( String)
     */
    public D8ContributingAreaParams(){}
    /**
     * A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     */
    @NotNull
    private String Input_D8_Flow_Direction_Grid;
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
    private Boolean Check_for_edge_contamination = true;

    /**
    * A grid of contributing area values calculated as the cells own contribution plus the contribution from upslope neighbors that drain in to it according to the D8 flow model.
    */
    private String Output_D8_Contributing_Area_Grid;
    private String outputDir = "./";


    /**
    * @param Input_D8_Flow_Direction_Grid A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
    */
    public D8ContributingAreaParams(@NotBlank String Input_D8_Flow_Direction_Grid){
        this.Input_D8_Flow_Direction_Grid = Input_D8_Flow_Direction_Grid;
    }

    public String getOutput_D8_Contributing_Area_Grid() {
        if (StringUtils.isBlank(Output_D8_Contributing_Area_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_D8_Contributing_Area_Grid", "Raster Dataset", null));
        }
        return this.Output_D8_Contributing_Area_Grid;
    }
}