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
 * Wrapper of parameters of D8ContributingArea
 * @author houzhiwei
 * @date 2018-12-17T22:40:56+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class D8ContributingAreaParams implements Params {

    /**
     * <pre>
     * Input_D8_Flow_Direction_Grid
     *  </pre>
     * @see Builder#Builder( String)
     */
    public D8ContributingAreaParams(){}
    /**
     * A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     */
    @NotNull
    @XmlElement(required = true)
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
    @XmlElement(defaultValue = "true")
    private Boolean Check_for_edge_contamination = true;

    /**
    * A grid of contributing area values calculated as the cells own contribution plus the contribution from upslope neighbors that drain in to it according to the D8 flow model.
    */
    private String Output_D8_Contributing_Area_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_D8_Contributing_Area_Grid() {
        if (StringUtils.isBlank(Output_D8_Contributing_Area_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_D8_Contributing_Area_Grid", "Raster Dataset", null));
        }
        return this.Output_D8_Contributing_Area_Grid;
    }

    private D8ContributingAreaParams(Builder builder){
        this.Input_D8_Flow_Direction_Grid = builder.Input_D8_Flow_Direction_Grid;
        this.Input_Outlets = builder.Input_Outlets;
        this.Input_Weight_Grid = builder.Input_Weight_Grid;
        this.Check_for_edge_contamination = builder.Check_for_edge_contamination;
        this.Output_D8_Contributing_Area_Grid = builder.Output_D8_Contributing_Area_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_D8_Flow_Direction_Grid;
        private String Input_Outlets;
        private String Input_Weight_Grid;
        private Boolean Check_for_edge_contamination;
        private String Output_D8_Contributing_Area_Grid;

        /**
        * @param Input_D8_Flow_Direction_Grid A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        */
        public Builder(@NotBlank String Input_D8_Flow_Direction_Grid){
            this.Input_D8_Flow_Direction_Grid = Input_D8_Flow_Direction_Grid;
        }

        public Builder Input_D8_Flow_Direction_Grid(String val){
            this.Input_D8_Flow_Direction_Grid = val;
            return this;
        }
        public Builder Input_Outlets(String val){
            this.Input_Outlets = val;
            return this;
        }
        public Builder Input_Weight_Grid(String val){
            this.Input_Weight_Grid = val;
            return this;
        }
        public Builder Check_for_edge_contamination(Boolean val){
            this.Check_for_edge_contamination = val;
            return this;
        }
        public Builder Output_D8_Contributing_Area_Grid(String val){
            this.Output_D8_Contributing_Area_Grid = val;
            return this;
        }

        public D8ContributingAreaParams build() {
            return new D8ContributingAreaParams(this);
        }
    }
}