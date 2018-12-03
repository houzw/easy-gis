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
 * Wrapper of parameters of TopographicWetnessIndex
 * @author houzhiwei
 * @date 2018-12-03T19:26:14+08:00
 */
@Getter
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class TopographicWetnessIndexParams implements Params {

    /**
     * <pre>
     * Input_Specific_Catchment_Area_Grid
     * Input_Slope_Grid
     *  </pre>
     * @see Builder#Builder( String,  String)
     */
    public TopographicWetnessIndexParams(){}
    /**
     * A grid of specific catchment area which is the contributing area per unit contour length.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Specific_Catchment_Area_Grid;
    /**
     * A grid of slope.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Slope_Grid;
    /**
    * A grid of the natural log of the ratio of specific catchment area (contributing area) to slope, ln(a/S).
    */
    private String Output_Wetness_Index_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_Wetness_Index_Grid() {
        if (StringUtils.isBlank(Output_Wetness_Index_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Specific_Catchment_Area_Grid, "Output_Wetness_Index_Grid", "Raster Dataset", null));
        }
        return this.Output_Wetness_Index_Grid;
    }

    private TopographicWetnessIndexParams(Builder builder){
        this.Input_Specific_Catchment_Area_Grid = builder.Input_Specific_Catchment_Area_Grid;
        this.Input_Slope_Grid = builder.Input_Slope_Grid;
        this.Output_Wetness_Index_Grid = builder.Output_Wetness_Index_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_Specific_Catchment_Area_Grid;
        private String Input_Slope_Grid;
        private String Output_Wetness_Index_Grid;

        /**
        * @param Input_Specific_Catchment_Area_Grid A grid of specific catchment area which is the contributing area per unit contour length.
        * @param Input_Slope_Grid A grid of slope.
        */
        public Builder(@NotBlank String Input_Specific_Catchment_Area_Grid, @NotBlank String Input_Slope_Grid){
            this.Input_Specific_Catchment_Area_Grid = Input_Specific_Catchment_Area_Grid;
            this.Input_Slope_Grid = Input_Slope_Grid;
        }

        public Builder Input_Specific_Catchment_Area_Grid(String val){
            this.Input_Specific_Catchment_Area_Grid = val;
            return this;
        }
        public Builder Input_Slope_Grid(String val){
            this.Input_Slope_Grid = val;
            return this;
        }
        public Builder Output_Wetness_Index_Grid(String val){
            this.Output_Wetness_Index_Grid = val;
            return this;
        }

        public TopographicWetnessIndexParams build() {
            return new TopographicWetnessIndexParams(this);
        }
    }
}