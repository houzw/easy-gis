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
 * Wrapper of parameters of SlopeAverageDown
 * @author houzhiwei
 * @date 2018-12-03T19:26:14+08:00
 */
@Getter
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class SlopeAverageDownParams implements Params {

    /**
     * <pre>
     * Input_D8_Flow_Direction_Grid
     * Input_Pit_Filled_Elevation_Grid
     *  </pre>
     * @see Builder#Builder( String,  String)
     */
    public SlopeAverageDownParams(){}
    /**
     * This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_D8_Flow_Direction_Grid;
    /**
     * A grid of elevation values.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Pit_Filled_Elevation_Grid;
    /**
    * default is 50d.
    * Input parameter of downslope distance over which to calculate the slope (in horizontal map units).
    */
    @XmlElement(defaultValue = "50")
    private Double Distance = 50d;

    /**
    * A grid of the ratio of  specific catchment area (contributing area) to slope.
    */
    private String Output_Slope_Average_Down_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_Slope_Average_Down_Grid() {
        if (StringUtils.isBlank(Output_Slope_Average_Down_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_Slope_Average_Down_Grid", "Raster Dataset", null));
        }
        return this.Output_Slope_Average_Down_Grid;
    }

    private SlopeAverageDownParams(Builder builder){
        this.Input_D8_Flow_Direction_Grid = builder.Input_D8_Flow_Direction_Grid;
        this.Input_Pit_Filled_Elevation_Grid = builder.Input_Pit_Filled_Elevation_Grid;
        this.Distance = builder.Distance;
        this.Output_Slope_Average_Down_Grid = builder.Output_Slope_Average_Down_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_D8_Flow_Direction_Grid;
        private String Input_Pit_Filled_Elevation_Grid;
        private Double Distance;
        private String Output_Slope_Average_Down_Grid;

        /**
        * @param Input_D8_Flow_Direction_Grid This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
        * @param Input_Pit_Filled_Elevation_Grid A grid of elevation values.
        */
        public Builder(@NotBlank String Input_D8_Flow_Direction_Grid, @NotBlank String Input_Pit_Filled_Elevation_Grid){
            this.Input_D8_Flow_Direction_Grid = Input_D8_Flow_Direction_Grid;
            this.Input_Pit_Filled_Elevation_Grid = Input_Pit_Filled_Elevation_Grid;
        }

        public Builder Input_D8_Flow_Direction_Grid(String val){
            this.Input_D8_Flow_Direction_Grid = val;
            return this;
        }
        public Builder Input_Pit_Filled_Elevation_Grid(String val){
            this.Input_Pit_Filled_Elevation_Grid = val;
            return this;
        }
        public Builder Distance(Double val){
            this.Distance = val;
            return this;
        }
        public Builder Output_Slope_Average_Down_Grid(String val){
            this.Output_Slope_Average_Down_Grid = val;
            return this;
        }

        public SlopeAverageDownParams build() {
            return new SlopeAverageDownParams(this);
        }
    }
}