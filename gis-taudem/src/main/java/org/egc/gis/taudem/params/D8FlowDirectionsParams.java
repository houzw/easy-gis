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
 * Wrapper of parameters of D8FlowDirections
 *
 * @author houzhiwei
 * @date 2018-12-03T19:26:15+08:00
 */
@Getter
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class D8FlowDirectionsParams implements Params {

    /**
     * <pre>
     * Input_Pit_Filled_Elevation_Grid
     *  </pre>
     *
     * @see Builder#Builder(String)
     */
    public D8FlowDirectionsParams() {}

    /**
     * A grid of elevation values.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Pit_Filled_Elevation_Grid;
    /**
     * A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     */
    private String Output_D8_Flow_Direction_Grid;
    /**
     * A grid giving slope in the D8 flow direction.
     */
    private String Output_D8_Slope_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_D8_Flow_Direction_Grid() {
        if (StringUtils.isBlank(Output_D8_Flow_Direction_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_Filled_Elevation_Grid,
                                                                                     "Output_D8_Flow_Direction_Grid",
                                                                                     "Raster Dataset", null));
        }
        return this.Output_D8_Flow_Direction_Grid;
    }

    public String getOutput_D8_Slope_Grid() {
        if (StringUtils.isBlank(Output_D8_Slope_Grid)) {
            return FilenameUtils.normalize(
                    outputDir + File.separator + namingOutput(Input_Pit_Filled_Elevation_Grid, "Output_D8_Slope_Grid",
                                                              "Raster Dataset", null));
        }
        return this.Output_D8_Slope_Grid;
    }

    private D8FlowDirectionsParams(Builder builder) {
        this.Input_Pit_Filled_Elevation_Grid = builder.Input_Pit_Filled_Elevation_Grid;
        this.Output_D8_Flow_Direction_Grid = builder.Output_D8_Flow_Direction_Grid;
        this.Output_D8_Slope_Grid = builder.Output_D8_Slope_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_Pit_Filled_Elevation_Grid;
        private String Output_D8_Flow_Direction_Grid;
        private String Output_D8_Slope_Grid;

        /**
         * @param Input_Pit_Filled_Elevation_Grid A grid of elevation values.
         */
        public Builder(@NotBlank String Input_Pit_Filled_Elevation_Grid) {
            this.Input_Pit_Filled_Elevation_Grid = Input_Pit_Filled_Elevation_Grid;
        }

        public Builder Input_Pit_Filled_Elevation_Grid(String val) {
            this.Input_Pit_Filled_Elevation_Grid = val;
            return this;
        }

        public Builder Output_D8_Flow_Direction_Grid(String val) {
            this.Output_D8_Flow_Direction_Grid = val;
            return this;
        }

        public Builder Output_D8_Slope_Grid(String val) {
            this.Output_D8_Slope_Grid = val;
            return this;
        }

        public D8FlowDirectionsParams build() {
            return new D8FlowDirectionsParams(this);
        }
    }
}