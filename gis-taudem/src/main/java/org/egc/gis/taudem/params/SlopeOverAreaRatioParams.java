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
 * Wrapper of parameters of SlopeOverAreaRatio
 * @author houzhiwei
 * @date 2018-12-03T19:26:14+08:00
 */
@Getter
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class SlopeOverAreaRatioParams implements Params {

    /**
     * <pre>
     * Input_Slope_Grid
     * Input_Specific_Catchment_Area_Grid
     *  </pre>
     * @see Builder#Builder( String,  String)
     */
    public SlopeOverAreaRatioParams(){}
    /**
     * A grid of slope.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Slope_Grid;
    /**
     * A grid giving the contributing area value for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Specific_Catchment_Area_Grid;
    /**
    * A grid of the ratio of slope to specific catchment area (contributing area).
    */
    private String Output_Slope_Divided_By_Area_Ratio_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_Slope_Divided_By_Area_Ratio_Grid() {
        if (StringUtils.isBlank(Output_Slope_Divided_By_Area_Ratio_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Slope_Grid, "Output_Slope_Divided_By_Area_Ratio_Grid", "Raster Dataset", null));
        }
        return this.Output_Slope_Divided_By_Area_Ratio_Grid;
    }

    private SlopeOverAreaRatioParams(Builder builder){
        this.Input_Slope_Grid = builder.Input_Slope_Grid;
        this.Input_Specific_Catchment_Area_Grid = builder.Input_Specific_Catchment_Area_Grid;
        this.Output_Slope_Divided_By_Area_Ratio_Grid = builder.Output_Slope_Divided_By_Area_Ratio_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_Slope_Grid;
        private String Input_Specific_Catchment_Area_Grid;
        private String Output_Slope_Divided_By_Area_Ratio_Grid;

        /**
        * @param Input_Slope_Grid A grid of slope.
        * @param Input_Specific_Catchment_Area_Grid A grid giving the contributing area value for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it.
        */
        public Builder(@NotBlank String Input_Slope_Grid, @NotBlank String Input_Specific_Catchment_Area_Grid){
            this.Input_Slope_Grid = Input_Slope_Grid;
            this.Input_Specific_Catchment_Area_Grid = Input_Specific_Catchment_Area_Grid;
        }

        public Builder Input_Slope_Grid(String val){
            this.Input_Slope_Grid = val;
            return this;
        }
        public Builder Input_Specific_Catchment_Area_Grid(String val){
            this.Input_Specific_Catchment_Area_Grid = val;
            return this;
        }
        public Builder Output_Slope_Divided_By_Area_Ratio_Grid(String val){
            this.Output_Slope_Divided_By_Area_Ratio_Grid = val;
            return this;
        }

        public SlopeOverAreaRatioParams build() {
            return new SlopeOverAreaRatioParams(this);
        }
    }
}