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
 * Wrapper of parameters of SlopeAreaCombination
 * @author houzhiwei
 * @date 2018-12-17T22:40:56+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class SlopeAreaCombinationParams implements Params {

    /**
     * <pre>
     * Input_Slope_Grid
     * Input_Area_Grid
     *  </pre>
     * @see Builder#Builder( String,  String)
     */
    public SlopeAreaCombinationParams(){}
    /**
     * This input is a grid of slope values.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Slope_Grid;
    /**
     * A grid giving the specific catchment area for each cell taken as its own contribution (grid cell length or summation of weights) plus the proportional contribution from upslope neighbors that drain in to it.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Area_Grid;
    /**
    * default is 2d.
    * The slope exponent (m) parameter which will be used in the formula: (S^m)(A^n), that is used to create the slope-area grid.
    */
    @XmlElement(defaultValue = "2")
    private Double Slope_Exponent_m = 2d;

    /**
    * default is 1d.
    * The area exponent (n) parameter which will be used in the formula: (S^m)(A^n), that is used to create the slope-area grid.
    */
    @XmlElement(defaultValue = "1")
    private Double Area_Exponent_n = 1d;

    /**
    * A grid of slope-area values = (S^m)(A^n) calculated from the slope grid, specific catchment area grid, m slope exponent parameter, and n area exponent parameter.
    */
    private String Output_Slope_Area_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_Slope_Area_Grid() {
        if (StringUtils.isBlank(Output_Slope_Area_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Slope_Grid, "Output_Slope_Area_Grid", "Raster Dataset", null));
        }
        return this.Output_Slope_Area_Grid;
    }

    private SlopeAreaCombinationParams(Builder builder){
        this.Input_Slope_Grid = builder.Input_Slope_Grid;
        this.Input_Area_Grid = builder.Input_Area_Grid;
        this.Slope_Exponent_m = builder.Slope_Exponent_m;
        this.Area_Exponent_n = builder.Area_Exponent_n;
        this.Output_Slope_Area_Grid = builder.Output_Slope_Area_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_Slope_Grid;
        private String Input_Area_Grid;
        private Double Slope_Exponent_m;
        private Double Area_Exponent_n;
        private String Output_Slope_Area_Grid;

        /**
        * @param Input_Slope_Grid This input is a grid of slope values.
        * @param Input_Area_Grid A grid giving the specific catchment area for each cell taken as its own contribution (grid cell length or summation of weights) plus the proportional contribution from upslope neighbors that drain in to it.
        */
        public Builder(@NotBlank String Input_Slope_Grid, @NotBlank String Input_Area_Grid){
            this.Input_Slope_Grid = Input_Slope_Grid;
            this.Input_Area_Grid = Input_Area_Grid;
        }

        public Builder Input_Slope_Grid(String val){
            this.Input_Slope_Grid = val;
            return this;
        }
        public Builder Input_Area_Grid(String val){
            this.Input_Area_Grid = val;
            return this;
        }
        public Builder Slope_Exponent_m(Double val){
            this.Slope_Exponent_m = val;
            return this;
        }
        public Builder Area_Exponent_n(Double val){
            this.Area_Exponent_n = val;
            return this;
        }
        public Builder Output_Slope_Area_Grid(String val){
            this.Output_Slope_Area_Grid = val;
            return this;
        }

        public SlopeAreaCombinationParams build() {
            return new SlopeAreaCombinationParams(this);
        }
    }
}