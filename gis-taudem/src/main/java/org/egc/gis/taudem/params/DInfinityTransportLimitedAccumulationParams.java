package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of DInfinityTransportLimitedAccumulation
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class DInfinityTransportLimitedAccumulationParams implements Params {


    /**
     * <pre>
     * Input_DInfinity_Flow_Direction_Grid
     * Input_Supply_Grid
     * Input_Transport_Capacity_Grid
     *  </pre>
     * @see #DInfinityTransportLimitedAccumulationParams( String,  String,  String)
     */
    public DInfinityTransportLimitedAccumulationParams(){}
    /**
     * A grid giving flow direction by the D-infinity method.
     */
    @NotNull
    private String Input_DInfinity_Flow_Direction_Grid;
    /**
     * A grid giving the supply (loading) of material to a transport limited accumulation function.
     */
    @NotNull
    private String Input_Supply_Grid;
    /**
     * A grid giving the transport capacity at each grid cell for the transport limited accumulation function.
     */
    @NotNull
    private String Input_Transport_Capacity_Grid;
    /**
    * A grid giving the concentration of a compound of interest in the supply to the transport limited accumulation function.
    */
    private String Input_Concentration_Grid;
    /**
    * This optional input is a point feature  defining outlets of interest.
    */
    private String Input_Outlets;
    /**
    * default is true.
    * This checkbox determines whether the tool should check for edge contamination.
    */
    private Boolean Check_for_Edge_Contamination = true;

    /**
    * This grid is the weighted accumulation of supply accumulated respecting the limitations in transport capacity and reports the transport rate calculated by accumulating the substance flux subject to the rule that the transport out of any grid cell is the minimum of the total supply (local supply plus transport in) to that grid cell and the transport capacity.
    */
    private String Output_Transport_Limited_Accumulation_Grid;
    /**
    * A grid giving the deposition resulting from the transport limited accumulation.
    */
    private String Output_Deposition_Grid;
    /**
    * If an input concentation in supply grid is given, then this grid is also output and gives the concentration of a compound (contaminant) adhered or bound to the transported substance (e.
    */
    private String Output_Concentration_Grid;
    private String outputDir = "./";


    /**
    * @param Input_DInfinity_Flow_Direction_Grid A grid giving flow direction by the D-infinity method.
    * @param Input_Supply_Grid A grid giving the supply (loading) of material to a transport limited accumulation function.
    * @param Input_Transport_Capacity_Grid A grid giving the transport capacity at each grid cell for the transport limited accumulation function.
    */
    public DInfinityTransportLimitedAccumulationParams(@NotBlank String Input_DInfinity_Flow_Direction_Grid, @NotBlank String Input_Supply_Grid, @NotBlank String Input_Transport_Capacity_Grid){
        this.Input_DInfinity_Flow_Direction_Grid = Input_DInfinity_Flow_Direction_Grid;
        this.Input_Supply_Grid = Input_Supply_Grid;
        this.Input_Transport_Capacity_Grid = Input_Transport_Capacity_Grid;
    }

    public String getOutput_Transport_Limited_Accumulation_Grid() {
        if (StringUtils.isBlank(Output_Transport_Limited_Accumulation_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_DInfinity_Flow_Direction_Grid, "Output_Transport_Limited_Accumulation_Grid", "Raster Dataset", null));
        }
        return this.Output_Transport_Limited_Accumulation_Grid;
    }
    public String getOutput_Deposition_Grid() {
        if (StringUtils.isBlank(Output_Deposition_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_DInfinity_Flow_Direction_Grid, "Output_Deposition_Grid", "Raster Dataset", null));
        }
        return this.Output_Deposition_Grid;
    }
    public String getOutput_Concentration_Grid() {
        if (StringUtils.isBlank(Output_Concentration_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_DInfinity_Flow_Direction_Grid, "Output_Concentration_Grid", "Raster Dataset", null));
        }
        return this.Output_Concentration_Grid;
    }
}