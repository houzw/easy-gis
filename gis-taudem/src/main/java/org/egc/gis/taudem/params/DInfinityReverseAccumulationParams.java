package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of DInfinityReverseAccumulation
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class DInfinityReverseAccumulationParams implements Params {


    /**
     * <pre>
     * Input_DInfinity_Flow_Direction_Grid
     * Input_Weight_Grid
     *  </pre>
     * @see #DInfinityReverseAccumulationParams( String,  String)
     */
    public DInfinityReverseAccumulationParams(){}
    /**
     * A grid giving flow direction by the Dinfinity method.
     */
    @NotNull
    private String Input_DInfinity_Flow_Direction_Grid;
    /**
     * A grid giving weights (loadings) to be used in the accumulation.
     */
    @NotNull
    private String Input_Weight_Grid;
    /**
    * The grid giving the result of the "Reverse Accumulation" function.
    */
    private String Output_Reverse_Accumulation_Grid;
    /**
    * The grid giving the maximum of the weight loading grid downslope from each grid cell.
    */
    private String Output_Maximum_Downslope_Grid;
    private String outputDir = "./";


    /**
    * @param Input_DInfinity_Flow_Direction_Grid A grid giving flow direction by the Dinfinity method.
    * @param Input_Weight_Grid A grid giving weights (loadings) to be used in the accumulation.
    */
    public DInfinityReverseAccumulationParams(@NotBlank String Input_DInfinity_Flow_Direction_Grid, @NotBlank String Input_Weight_Grid){
        this.Input_DInfinity_Flow_Direction_Grid = Input_DInfinity_Flow_Direction_Grid;
        this.Input_Weight_Grid = Input_Weight_Grid;
    }

    public String getOutput_Reverse_Accumulation_Grid() {
        if (StringUtils.isBlank(Output_Reverse_Accumulation_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_DInfinity_Flow_Direction_Grid, "Output_Reverse_Accumulation_Grid", "Raster Dataset", null));
        }
        return this.Output_Reverse_Accumulation_Grid;
    }
    public String getOutput_Maximum_Downslope_Grid() {
        if (StringUtils.isBlank(Output_Maximum_Downslope_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_DInfinity_Flow_Direction_Grid, "Output_Maximum_Downslope_Grid", "Raster Dataset", null));
        }
        return this.Output_Maximum_Downslope_Grid;
    }
}