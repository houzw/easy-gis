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
 * Wrapper of parameters of DInfinityDistanceUp
 * @author houzhiwei
 * @date 2018-12-17T22:40:56+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityDistanceUpParams implements Params {

    /**
     * <pre>
     * Input_DInfinity_Flow_Direction_Grid
     * Input_Pit_Filled_Elevation_Grid
     * Input_Slope_Grid
     *  </pre>
     * @see Builder#Builder( String,  String,  String)
     */
    public DInfinityDistanceUpParams(){}
    /**
     * A grid giving flow directions by the D-Infinity method.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_DInfinity_Flow_Direction_Grid;
    /**
     * This input is a grid of elevation values.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Pit_Filled_Elevation_Grid;
    /**
     * This input is a grid of slope values.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Slope_Grid;
    /**
    * default is 0.5d.
    * The proportion threshold parameter where only grid cells that contribute flow with a proportion greater than this user specified threshold (t) is considered to be upslope of any given grid cell.
    */
    @XmlElement(defaultValue = "0.5")
    private Double Input_Proportion_Threshold = 0.5d;

    /**
    * default is "ave".
    * Statistical method used to calculate the distance down to the stream.
    */
    @XmlElement(defaultValue = "ave")
    private String Statistical_Method = "ave";

    /**
    * default is "h".
    * Distance method used to calculate the distance down to the stream.
    */
    @XmlElement(defaultValue = "h")
    private String Distance_Method = "h";

    /**
    * default is true.
    * A flag that determines whether the tool should check for edge contamination.
    */
    @XmlElement(defaultValue = "true")
    private Boolean Check_for_Edge_Contamination = true;

    /**
    * 
    */
    private String Output_DInfinity_Distance_Up_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_DInfinity_Distance_Up_Grid() {
        if (StringUtils.isBlank(Output_DInfinity_Distance_Up_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_DInfinity_Flow_Direction_Grid, "Output_DInfinity_Distance_Up_Grid", "Raster Dataset", null));
        }
        return this.Output_DInfinity_Distance_Up_Grid;
    }

    private DInfinityDistanceUpParams(Builder builder){
        this.Input_DInfinity_Flow_Direction_Grid = builder.Input_DInfinity_Flow_Direction_Grid;
        this.Input_Pit_Filled_Elevation_Grid = builder.Input_Pit_Filled_Elevation_Grid;
        this.Input_Slope_Grid = builder.Input_Slope_Grid;
        this.Input_Proportion_Threshold = builder.Input_Proportion_Threshold;
        this.Statistical_Method = builder.Statistical_Method;
        this.Distance_Method = builder.Distance_Method;
        this.Check_for_Edge_Contamination = builder.Check_for_Edge_Contamination;
        this.Output_DInfinity_Distance_Up_Grid = builder.Output_DInfinity_Distance_Up_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_DInfinity_Flow_Direction_Grid;
        private String Input_Pit_Filled_Elevation_Grid;
        private String Input_Slope_Grid;
        private Double Input_Proportion_Threshold;
        private String Statistical_Method;
        private String Distance_Method;
        private Boolean Check_for_Edge_Contamination;
        private String Output_DInfinity_Distance_Up_Grid;

        /**
        * @param Input_DInfinity_Flow_Direction_Grid A grid giving flow directions by the D-Infinity method.
        * @param Input_Pit_Filled_Elevation_Grid This input is a grid of elevation values.
        * @param Input_Slope_Grid This input is a grid of slope values.
        */
        public Builder(@NotBlank String Input_DInfinity_Flow_Direction_Grid, @NotBlank String Input_Pit_Filled_Elevation_Grid, @NotBlank String Input_Slope_Grid){
            this.Input_DInfinity_Flow_Direction_Grid = Input_DInfinity_Flow_Direction_Grid;
            this.Input_Pit_Filled_Elevation_Grid = Input_Pit_Filled_Elevation_Grid;
            this.Input_Slope_Grid = Input_Slope_Grid;
        }

        public Builder Input_DInfinity_Flow_Direction_Grid(String val){
            this.Input_DInfinity_Flow_Direction_Grid = val;
            return this;
        }
        public Builder Input_Pit_Filled_Elevation_Grid(String val){
            this.Input_Pit_Filled_Elevation_Grid = val;
            return this;
        }
        public Builder Input_Slope_Grid(String val){
            this.Input_Slope_Grid = val;
            return this;
        }
        public Builder Input_Proportion_Threshold(Double val){
            this.Input_Proportion_Threshold = val;
            return this;
        }
        public Builder Statistical_Method(String val){
            this.Statistical_Method = val;
            return this;
        }
        public Builder Distance_Method(String val){
            this.Distance_Method = val;
            return this;
        }
        public Builder Check_for_Edge_Contamination(Boolean val){
            this.Check_for_Edge_Contamination = val;
            return this;
        }
        public Builder Output_DInfinity_Distance_Up_Grid(String val){
            this.Output_DInfinity_Distance_Up_Grid = val;
            return this;
        }

        public DInfinityDistanceUpParams build() {
            return new DInfinityDistanceUpParams(this);
        }
    }
}