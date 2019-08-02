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
 * Wrapper of parameters of DInfinityFlowDirections
 * @author houzhiwei
 * @date 2018-12-17T22:40:56+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityFlowDirectionsParams implements Params {

    /**
     * <pre>
     * Input_Pit_FIlled_Elevation_Grid
     *  </pre>
     * @see Builder#Builder( String)
     */
    public DInfinityFlowDirectionsParams(){}
    /**
     * A grid of elevation values.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Pit_FIlled_Elevation_Grid;
    /**
    * A grid of flow directions based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997, "A New Method for the Determination of Flow Directions and Contributing Areas in Grid Digital Elevation Models," Water Resources Research, 33(2): 309-319).
    */
    private String Output_DInfinity_Flow_Direction_Grid;
    /**
    * A grid of slope evaluated using the D-infinity method described in Tarboton, D.
    */
    private String Output_DInfinity_Slope_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_DInfinity_Flow_Direction_Grid() {
        if (StringUtils.isBlank(Output_DInfinity_Flow_Direction_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_FIlled_Elevation_Grid, "Output_DInfinity_Flow_Direction_Grid", "Raster Dataset", null));
        }
        return this.Output_DInfinity_Flow_Direction_Grid;
    }
    public String getOutput_DInfinity_Slope_Grid() {
        if (StringUtils.isBlank(Output_DInfinity_Slope_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_FIlled_Elevation_Grid, "Output_DInfinity_Slope_Grid", "Raster Dataset", null));
        }
        return this.Output_DInfinity_Slope_Grid;
    }

    private DInfinityFlowDirectionsParams(Builder builder){
        this.Input_Pit_FIlled_Elevation_Grid = builder.Input_Pit_FIlled_Elevation_Grid;
        this.Output_DInfinity_Flow_Direction_Grid = builder.Output_DInfinity_Flow_Direction_Grid;
        this.Output_DInfinity_Slope_Grid = builder.Output_DInfinity_Slope_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_Pit_FIlled_Elevation_Grid;
        private String Output_DInfinity_Flow_Direction_Grid;
        private String Output_DInfinity_Slope_Grid;

        /**
        * @param Input_Pit_FIlled_Elevation_Grid A grid of elevation values.
        */
        public Builder(@NotBlank String Input_Pit_FIlled_Elevation_Grid){
            this.Input_Pit_FIlled_Elevation_Grid = Input_Pit_FIlled_Elevation_Grid;
        }

        public Builder Input_Pit_FIlled_Elevation_Grid(String val){
            this.Input_Pit_FIlled_Elevation_Grid = val;
            return this;
        }
        public Builder Output_DInfinity_Flow_Direction_Grid(String val){
            this.Output_DInfinity_Flow_Direction_Grid = val;
            return this;
        }
        public Builder Output_DInfinity_Slope_Grid(String val){
            this.Output_DInfinity_Slope_Grid = val;
            return this;
        }

        public DInfinityFlowDirectionsParams build() {
            return new DInfinityFlowDirectionsParams(this);
        }
    }
}