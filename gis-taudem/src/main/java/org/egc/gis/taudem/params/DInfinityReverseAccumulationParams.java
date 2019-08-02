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
 * Wrapper of parameters of DInfinityReverseAccumulation
 * @author houzhiwei
 * @date 2018-12-17T22:40:56+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityReverseAccumulationParams implements Params {

    /**
     * <pre>
     * Input_DInfinity_Flow_Direction_Grid
     * Input_Weight_Grid
     *  </pre>
     * @see Builder#Builder( String,  String)
     */
    public DInfinityReverseAccumulationParams(){}
    /**
     * A grid giving flow direction by the Dinfinity method.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_DInfinity_Flow_Direction_Grid;
    /**
     * A grid giving weights (loadings) to be used in the accumulation.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Weight_Grid;
    /**
    * The grid giving the result of the "Reverse Accumulation" function.
    */
    private String Output_Reverse_Accumulation_Grid;
    /**
    * The grid giving the maximum of the weight loading grid downslope from each grid cell.
    */
    private String Output_Maximum_Downslope_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

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

    private DInfinityReverseAccumulationParams(Builder builder){
        this.Input_DInfinity_Flow_Direction_Grid = builder.Input_DInfinity_Flow_Direction_Grid;
        this.Input_Weight_Grid = builder.Input_Weight_Grid;
        this.Output_Reverse_Accumulation_Grid = builder.Output_Reverse_Accumulation_Grid;
        this.Output_Maximum_Downslope_Grid = builder.Output_Maximum_Downslope_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_DInfinity_Flow_Direction_Grid;
        private String Input_Weight_Grid;
        private String Output_Reverse_Accumulation_Grid;
        private String Output_Maximum_Downslope_Grid;

        /**
        * @param Input_DInfinity_Flow_Direction_Grid A grid giving flow direction by the Dinfinity method.
        * @param Input_Weight_Grid A grid giving weights (loadings) to be used in the accumulation.
        */
        public Builder(@NotBlank String Input_DInfinity_Flow_Direction_Grid, @NotBlank String Input_Weight_Grid){
            this.Input_DInfinity_Flow_Direction_Grid = Input_DInfinity_Flow_Direction_Grid;
            this.Input_Weight_Grid = Input_Weight_Grid;
        }

        public Builder Input_DInfinity_Flow_Direction_Grid(String val){
            this.Input_DInfinity_Flow_Direction_Grid = val;
            return this;
        }
        public Builder Input_Weight_Grid(String val){
            this.Input_Weight_Grid = val;
            return this;
        }
        public Builder Output_Reverse_Accumulation_Grid(String val){
            this.Output_Reverse_Accumulation_Grid = val;
            return this;
        }
        public Builder Output_Maximum_Downslope_Grid(String val){
            this.Output_Maximum_Downslope_Grid = val;
            return this;
        }

        public DInfinityReverseAccumulationParams build() {
            return new DInfinityReverseAccumulationParams(this);
        }
    }
}