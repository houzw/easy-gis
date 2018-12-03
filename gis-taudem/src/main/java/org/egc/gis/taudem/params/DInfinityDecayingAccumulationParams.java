package org.egc.gis.taudem.params;

import lombok.Getter;
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
 * Wrapper of parameters of DInfinityDecayingAccumulation
 * @author houzhiwei
 * @date 2018-12-03T19:26:15+08:00
 */
@Getter
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityDecayingAccumulationParams implements Params {

    /**
     * <pre>
     * Input_DInfinity_Flow_Direction_Grid
     * Input_Decay_Multiplier_Grid
     *  </pre>
     * @see Builder#Builder( String,  String)
     */
    public DInfinityDecayingAccumulationParams(){}
    /**
     * A grid giving flow direction by the D-infinity method.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_DInfinity_Flow_Direction_Grid;
    /**
     * A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Decay_Multiplier_Grid;
    /**
    * A grid giving weights (loadings) to be used in the accumulation.
    */
    private String Input_Weight_Grid;
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
    * The D-Infinity Decaying Accumulation tool creates a grid of the accumulated mass at each location in the domain where mass moves with the D-infinity flow field, but is subject to first order decay in moving from cell to cell.
    */
    private String Output_Decayed_Specific_Catchment_Area_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_Decayed_Specific_Catchment_Area_Grid() {
        if (StringUtils.isBlank(Output_Decayed_Specific_Catchment_Area_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_DInfinity_Flow_Direction_Grid, "Output_Decayed_Specific_Catchment_Area_Grid", "Raster Dataset", null));
        }
        return this.Output_Decayed_Specific_Catchment_Area_Grid;
    }

    private DInfinityDecayingAccumulationParams(Builder builder){
        this.Input_DInfinity_Flow_Direction_Grid = builder.Input_DInfinity_Flow_Direction_Grid;
        this.Input_Decay_Multiplier_Grid = builder.Input_Decay_Multiplier_Grid;
        this.Input_Weight_Grid = builder.Input_Weight_Grid;
        this.Input_Outlets = builder.Input_Outlets;
        this.Check_for_Edge_Contamination = builder.Check_for_Edge_Contamination;
        this.Output_Decayed_Specific_Catchment_Area_Grid = builder.Output_Decayed_Specific_Catchment_Area_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_DInfinity_Flow_Direction_Grid;
        private String Input_Decay_Multiplier_Grid;
        private String Input_Weight_Grid;
        private String Input_Outlets;
        private Boolean Check_for_Edge_Contamination;
        private String Output_Decayed_Specific_Catchment_Area_Grid;

        /**
        * @param Input_DInfinity_Flow_Direction_Grid A grid giving flow direction by the D-infinity method.
        * @param Input_Decay_Multiplier_Grid A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
        */
        public Builder(@NotBlank String Input_DInfinity_Flow_Direction_Grid, @NotBlank String Input_Decay_Multiplier_Grid){
            this.Input_DInfinity_Flow_Direction_Grid = Input_DInfinity_Flow_Direction_Grid;
            this.Input_Decay_Multiplier_Grid = Input_Decay_Multiplier_Grid;
        }

        public Builder Input_DInfinity_Flow_Direction_Grid(String val){
            this.Input_DInfinity_Flow_Direction_Grid = val;
            return this;
        }
        public Builder Input_Decay_Multiplier_Grid(String val){
            this.Input_Decay_Multiplier_Grid = val;
            return this;
        }
        public Builder Input_Weight_Grid(String val){
            this.Input_Weight_Grid = val;
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
        public Builder Output_Decayed_Specific_Catchment_Area_Grid(String val){
            this.Output_Decayed_Specific_Catchment_Area_Grid = val;
            return this;
        }

        public DInfinityDecayingAccumulationParams build() {
            return new DInfinityDecayingAccumulationParams(this);
        }
    }
}