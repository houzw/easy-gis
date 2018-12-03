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
 * Wrapper of parameters of LengthAreaStreamSource
 * @author houzhiwei
 * @date 2018-12-03T19:26:14+08:00
 */
@Getter
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class LengthAreaStreamSourceParams implements Params {

    /**
     * <pre>
     * Input_Length_Grid
     * Input_Contributing_Area_Grid
     *  </pre>
     * @see Builder#Builder( String,  String)
     */
    public LengthAreaStreamSourceParams(){}
    /**
     * A grid of the maximum upslope length for each cell.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Length_Grid;
    /**
     * A grid of contributing area values for each cell that were calculated using the D8 algorithm.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Contributing_Area_Grid;
    /**
    * default is 0.03d.
    * The multiplier threshold (M) parameter which is used in the formula: A > (M)(L^y), to identify the beginning of streams.
    */
    @XmlElement(defaultValue = "0.03")
    private Double Threshold_M = 0.03d;

    /**
    * default is 1.3d.
    * The exponent (y) parameter which is used in the formula: A > (M)(L^y), to identify the beginning of streams.
    */
    @XmlElement(defaultValue = "1.3")
    private Double Exponent_y = 1.3d;

    /**
    * An indicator grid (1,0) that evaluates A >= (M)(L^y), based on the maximum upslope path length, the D8 contributing area grid inputs, and parameters M and y.
    */
    private String Output_Stream_Source_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_Stream_Source_Grid() {
        if (StringUtils.isBlank(Output_Stream_Source_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Length_Grid, "Output_Stream_Source_Grid", "Raster Dataset", null));
        }
        return this.Output_Stream_Source_Grid;
    }

    private LengthAreaStreamSourceParams(Builder builder){
        this.Input_Length_Grid = builder.Input_Length_Grid;
        this.Input_Contributing_Area_Grid = builder.Input_Contributing_Area_Grid;
        this.Threshold_M = builder.Threshold_M;
        this.Exponent_y = builder.Exponent_y;
        this.Output_Stream_Source_Grid = builder.Output_Stream_Source_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_Length_Grid;
        private String Input_Contributing_Area_Grid;
        private Double Threshold_M;
        private Double Exponent_y;
        private String Output_Stream_Source_Grid;

        /**
        * @param Input_Length_Grid A grid of the maximum upslope length for each cell.
        * @param Input_Contributing_Area_Grid A grid of contributing area values for each cell that were calculated using the D8 algorithm.
        */
        public Builder(@NotBlank String Input_Length_Grid, @NotBlank String Input_Contributing_Area_Grid){
            this.Input_Length_Grid = Input_Length_Grid;
            this.Input_Contributing_Area_Grid = Input_Contributing_Area_Grid;
        }

        public Builder Input_Length_Grid(String val){
            this.Input_Length_Grid = val;
            return this;
        }
        public Builder Input_Contributing_Area_Grid(String val){
            this.Input_Contributing_Area_Grid = val;
            return this;
        }
        public Builder Threshold_M(Double val){
            this.Threshold_M = val;
            return this;
        }
        public Builder Exponent_y(Double val){
            this.Exponent_y = val;
            return this;
        }
        public Builder Output_Stream_Source_Grid(String val){
            this.Output_Stream_Source_Grid = val;
            return this;
        }

        public LengthAreaStreamSourceParams build() {
            return new LengthAreaStreamSourceParams(this);
        }
    }
}