package org.egc.gis.taudem.params;

import lombok.Data;
import lombok.Setter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;

/**
 * Wrapper of parameters of DInfinityTransportLimitedAccumulation
 * @author houzhiwei
 * @date 2018-12-17T22:40:56+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityTransportLimitedAccumulationParams implements Params {

    /**
     * <pre>
     * Input_DInfinity_Flow_Direction_Grid
     * Input_Supply_Grid
     * Input_Transport_Capacity_Grid
     *  </pre>
     * @see Builder#Builder( String,  String,  String)
     */
    public DInfinityTransportLimitedAccumulationParams(){}
    /**
     * A grid giving flow direction by the D-infinity method.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_DInfinity_Flow_Direction_Grid;
    /**
     * A grid giving the supply (loading) of material to a transport limited accumulation function.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Supply_Grid;
    /**
     * A grid giving the transport capacity at each grid cell for the transport limited accumulation function.
     */
    @NotNull
    @XmlElement(required = true)
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
    @XmlElement(defaultValue = "true")
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
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

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

    private DInfinityTransportLimitedAccumulationParams(Builder builder){
        this.Input_DInfinity_Flow_Direction_Grid = builder.Input_DInfinity_Flow_Direction_Grid;
        this.Input_Supply_Grid = builder.Input_Supply_Grid;
        this.Input_Transport_Capacity_Grid = builder.Input_Transport_Capacity_Grid;
        this.Input_Concentration_Grid = builder.Input_Concentration_Grid;
        this.Input_Outlets = builder.Input_Outlets;
        this.Check_for_Edge_Contamination = builder.Check_for_Edge_Contamination;
        this.Output_Transport_Limited_Accumulation_Grid = builder.Output_Transport_Limited_Accumulation_Grid;
        this.Output_Deposition_Grid = builder.Output_Deposition_Grid;
        this.Output_Concentration_Grid = builder.Output_Concentration_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_DInfinity_Flow_Direction_Grid;
        private String Input_Supply_Grid;
        private String Input_Transport_Capacity_Grid;
        private String Input_Concentration_Grid;
        private String Input_Outlets;
        private Boolean Check_for_Edge_Contamination;
        private String Output_Transport_Limited_Accumulation_Grid;
        private String Output_Deposition_Grid;
        private String Output_Concentration_Grid;

        /**
        * @param Input_DInfinity_Flow_Direction_Grid A grid giving flow direction by the D-infinity method.
        * @param Input_Supply_Grid A grid giving the supply (loading) of material to a transport limited accumulation function.
        * @param Input_Transport_Capacity_Grid A grid giving the transport capacity at each grid cell for the transport limited accumulation function.
        */
        public Builder(@NotBlank String Input_DInfinity_Flow_Direction_Grid, @NotBlank String Input_Supply_Grid, @NotBlank String Input_Transport_Capacity_Grid){
            this.Input_DInfinity_Flow_Direction_Grid = Input_DInfinity_Flow_Direction_Grid;
            this.Input_Supply_Grid = Input_Supply_Grid;
            this.Input_Transport_Capacity_Grid = Input_Transport_Capacity_Grid;
        }

        public Builder Input_DInfinity_Flow_Direction_Grid(String val){
            this.Input_DInfinity_Flow_Direction_Grid = val;
            return this;
        }
        public Builder Input_Supply_Grid(String val){
            this.Input_Supply_Grid = val;
            return this;
        }
        public Builder Input_Transport_Capacity_Grid(String val){
            this.Input_Transport_Capacity_Grid = val;
            return this;
        }
        public Builder Input_Concentration_Grid(String val){
            this.Input_Concentration_Grid = val;
            return this;
        }
        public Builder Input_Outlets(String val){
            this.Input_Outlets = val;
            return this;
        }
        public Builder Check_for_Edge_Contamination(Boolean val){
            this.Check_for_Edge_Contamination = val;
            return this;
        }
        public Builder Output_Transport_Limited_Accumulation_Grid(String val){
            this.Output_Transport_Limited_Accumulation_Grid = val;
            return this;
        }
        public Builder Output_Deposition_Grid(String val){
            this.Output_Deposition_Grid = val;
            return this;
        }
        public Builder Output_Concentration_Grid(String val){
            this.Output_Concentration_Grid = val;
            return this;
        }

        public DInfinityTransportLimitedAccumulationParams build() {
            return new DInfinityTransportLimitedAccumulationParams(this);
        }
    }
}