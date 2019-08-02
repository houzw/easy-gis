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
 * Wrapper of parameters of D8ExtremeUpslopeValue
 * @author houzhiwei
 * @date 2018-12-17T22:40:56+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class D8ExtremeUpslopeValueParams implements Params {

    /**
     * <pre>
     * Input_D8_Flow_Direction_Grid
     * Input_Value_Grid
     *  </pre>
     * @see Builder#Builder( String,  String)
     */
    public D8ExtremeUpslopeValueParams(){}
    /**
     * A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_D8_Flow_Direction_Grid;
    /**
     * This is the grid of values of which the maximum or minimum upslope value is selected.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Value_Grid;
    /**
    * default is true.
    * A flag to indicate whether the maximum or minimum upslope value is to be calculated.
    */
    @XmlElement(defaultValue = "true")
    private Boolean Use_maximum_upslope_value = true;

    /**
    * default is true.
    * A flag that indicates whether the tool should check for edge contamination.
    */
    @XmlElement(defaultValue = "true")
    private Boolean Check_for_edge_contamination = true;

    /**
    * A point feature defining outlets of interest.
    */
    private String Input_Outlets;
    /**
    * A grid of the maximum/minimum upslope values.
    */
    private String Output_Extreme_Value_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_Extreme_Value_Grid() {
        if (StringUtils.isBlank(Output_Extreme_Value_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_Extreme_Value_Grid", "Raster Dataset", null));
        }
        return this.Output_Extreme_Value_Grid;
    }

    private D8ExtremeUpslopeValueParams(Builder builder){
        this.Input_D8_Flow_Direction_Grid = builder.Input_D8_Flow_Direction_Grid;
        this.Input_Value_Grid = builder.Input_Value_Grid;
        this.Use_maximum_upslope_value = builder.Use_maximum_upslope_value;
        this.Check_for_edge_contamination = builder.Check_for_edge_contamination;
        this.Input_Outlets = builder.Input_Outlets;
        this.Output_Extreme_Value_Grid = builder.Output_Extreme_Value_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_D8_Flow_Direction_Grid;
        private String Input_Value_Grid;
        private Boolean Use_maximum_upslope_value;
        private Boolean Check_for_edge_contamination;
        private String Input_Outlets;
        private String Output_Extreme_Value_Grid;

        /**
        * @param Input_D8_Flow_Direction_Grid A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        * @param Input_Value_Grid This is the grid of values of which the maximum or minimum upslope value is selected.
        */
        public Builder(@NotBlank String Input_D8_Flow_Direction_Grid, @NotBlank String Input_Value_Grid){
            this.Input_D8_Flow_Direction_Grid = Input_D8_Flow_Direction_Grid;
            this.Input_Value_Grid = Input_Value_Grid;
        }

        public Builder Input_D8_Flow_Direction_Grid(String val){
            this.Input_D8_Flow_Direction_Grid = val;
            return this;
        }
        public Builder Input_Value_Grid(String val){
            this.Input_Value_Grid = val;
            return this;
        }
        public Builder Use_maximum_upslope_value(Boolean val){
            this.Use_maximum_upslope_value = val;
            return this;
        }
        public Builder Check_for_edge_contamination(Boolean val){
            this.Check_for_edge_contamination = val;
            return this;
        }
        public Builder Input_Outlets(String val){
            this.Input_Outlets = val;
            return this;
        }
        public Builder Output_Extreme_Value_Grid(String val){
            this.Output_Extreme_Value_Grid = val;
            return this;
        }

        public D8ExtremeUpslopeValueParams build() {
            return new D8ExtremeUpslopeValueParams(this);
        }
    }
}