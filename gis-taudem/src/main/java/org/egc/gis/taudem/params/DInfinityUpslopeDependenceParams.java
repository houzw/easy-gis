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
 * Wrapper of parameters of DInfinityUpslopeDependence
 * @author houzhiwei
 * @date 2018-12-17T22:40:56+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityUpslopeDependenceParams implements Params {

    /**
     * <pre>
     * Input_DInfinity_Flow_Direction_Grid
     * Input_Destination_Grid
     *  </pre>
     * @see Builder#Builder( String,  String)
     */
    public DInfinityUpslopeDependenceParams(){}
    /**
     * A grid giving flow direction by the D-Infinity method where the flow direction angle is determined as the direction of the steepest downward slope on the eight triangular facets formed in a 3 x 3 grid cell window centered on the grid cell of interest.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_DInfinity_Flow_Direction_Grid;
    /**
     * A grid that encodes the destination zone that may receive flow from upslope.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Destination_Grid;
    /**
    * A grid quantifing the amount each source point in the domain contributes to the zone defined by the destination grid.
    */
    private String Output_Upslope_Dependence_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_Upslope_Dependence_Grid() {
        if (StringUtils.isBlank(Output_Upslope_Dependence_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_DInfinity_Flow_Direction_Grid, "Output_Upslope_Dependence_Grid", "Raster Dataset", null));
        }
        return this.Output_Upslope_Dependence_Grid;
    }

    private DInfinityUpslopeDependenceParams(Builder builder){
        this.Input_DInfinity_Flow_Direction_Grid = builder.Input_DInfinity_Flow_Direction_Grid;
        this.Input_Destination_Grid = builder.Input_Destination_Grid;
        this.Output_Upslope_Dependence_Grid = builder.Output_Upslope_Dependence_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_DInfinity_Flow_Direction_Grid;
        private String Input_Destination_Grid;
        private String Output_Upslope_Dependence_Grid;

        /**
        * @param Input_DInfinity_Flow_Direction_Grid A grid giving flow direction by the D-Infinity method where the flow direction angle is determined as the direction of the steepest downward slope on the eight triangular facets formed in a 3 x 3 grid cell window centered on the grid cell of interest.
        * @param Input_Destination_Grid A grid that encodes the destination zone that may receive flow from upslope.
        */
        public Builder(@NotBlank String Input_DInfinity_Flow_Direction_Grid, @NotBlank String Input_Destination_Grid){
            this.Input_DInfinity_Flow_Direction_Grid = Input_DInfinity_Flow_Direction_Grid;
            this.Input_Destination_Grid = Input_Destination_Grid;
        }

        public Builder Input_DInfinity_Flow_Direction_Grid(String val){
            this.Input_DInfinity_Flow_Direction_Grid = val;
            return this;
        }
        public Builder Input_Destination_Grid(String val){
            this.Input_Destination_Grid = val;
            return this;
        }
        public Builder Output_Upslope_Dependence_Grid(String val){
            this.Output_Upslope_Dependence_Grid = val;
            return this;
        }

        public DInfinityUpslopeDependenceParams build() {
            return new DInfinityUpslopeDependenceParams(this);
        }
    }
}