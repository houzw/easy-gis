package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of GridNetwork
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class GridNetworkParams implements Params {


    /**
     * <pre>
     * Input_D8_Flow_Direction_Grid
     *  </pre>
     * @see #GridNetworkParams( String)
     */
    public GridNetworkParams(){}
    /**
     * A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     */
    @NotNull
    private String Input_D8_Flow_Direction_Grid;
    /**
    * A point feature  defining the outlets of interest.
    */
    private String Input_Outlets;
    /**
    * A grid that is used to determine the domain do be analyzed.
    */
    private String Input_Mask_Grid;
    /**
    * default is 100d.
    * This input parameter is used in the calculation mask grid value >= mask threshold to determine if the grid cell is in the domain to be analyzed.
    */
    private Double Input_Mask_Threshold_Value = 100d;

    /**
    * A grid giving the Strahler order number for each cell.
    */
    private String Output_Strahler_Network_Order_Grid;
    /**
    * A grid that gives the length of the longest upslope D8 flow path terminating at each grid cell.
    */
    private String Output_Longest_Upslope_Length_Grid;
    /**
    * The total upslope path length is the length of the entire D8 flow grid network upslope of each grid cell.
    */
    private String Output_Total_Upslope_Length_Grid;
    private String outputDir = "./";


    /**
    * @param Input_D8_Flow_Direction_Grid A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
    */
    public GridNetworkParams(@NotBlank String Input_D8_Flow_Direction_Grid){
        this.Input_D8_Flow_Direction_Grid = Input_D8_Flow_Direction_Grid;
    }

    public String getOutput_Strahler_Network_Order_Grid() {
        if (StringUtils.isBlank(Output_Strahler_Network_Order_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_Strahler_Network_Order_Grid", "Raster Dataset", null));
        }
        return this.Output_Strahler_Network_Order_Grid;
    }
    public String getOutput_Longest_Upslope_Length_Grid() {
        if (StringUtils.isBlank(Output_Longest_Upslope_Length_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_Longest_Upslope_Length_Grid", "Raster Dataset", null));
        }
        return this.Output_Longest_Upslope_Length_Grid;
    }
    public String getOutput_Total_Upslope_Length_Grid() {
        if (StringUtils.isBlank(Output_Total_Upslope_Length_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_Total_Upslope_Length_Grid", "Raster Dataset", null));
        }
        return this.Output_Total_Upslope_Length_Grid;
    }
}