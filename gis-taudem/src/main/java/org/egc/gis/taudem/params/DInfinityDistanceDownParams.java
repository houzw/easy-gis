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
 * Wrapper of parameters of DInfinityDistanceDown
 * @author houzhiwei
 * @date 2018-12-03T19:26:14+08:00
 */
@Getter
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityDistanceDownParams implements Params {

    /**
     * <pre>
     * Input_DInfinity_Flow_Direction_Grid
     * Input_Pit_Filled_Elevation_Grid
     * Input_Stream_Raster_Grid
     *  </pre>
     * @see Builder#Builder( String,  String,  String)
     */
    public DInfinityDistanceDownParams(){}
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
     * A grid indicating streams, by using a grid cell value of 1 on streams and 0 off streams.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Stream_Raster_Grid;
    /**
    * default is "ave".
    * Alternative values are ave, max, min, where ave for Average, max for Maximum, min for Minimum Statistical method used to calculate the distance down to the stream.
    */
    @XmlElement(defaultValue = "ave")
    private String Statistical_Method = "ave";

    /**
    * default is "h".
    * Alternative values are h, v, p, s, where h for Horizontal, v for Vertical, p for Pythagoras, s for Surface Distance method used to calculate the distance down to the stream.
    */
    @XmlElement(defaultValue = "h")
    private String Distance_Method = "h";

    /**
    * default is true.
    * A flag that determines whether the tool should check for edge contamination.
    */
    @XmlElement(defaultValue = "true")
    private Boolean Check_for_edge_contamination = true;

    /**
    * A grid giving weights (loadings) to be used in the distance calculation.
    */
    private String Input_Weight_Path_Grid;
    /**
    * Creates a grid containing the distance to stream calculated using the D-infinity flow model and the statistical and path methods chosen.
    */
    private String Output_DInfinity_Drop_to_Stream_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_DInfinity_Drop_to_Stream_Grid() {
        if (StringUtils.isBlank(Output_DInfinity_Drop_to_Stream_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_DInfinity_Flow_Direction_Grid, "Output_DInfinity_Drop_to_Stream_Grid", "Raster Dataset", null));
        }
        return this.Output_DInfinity_Drop_to_Stream_Grid;
    }

    private DInfinityDistanceDownParams(Builder builder){
        this.Input_DInfinity_Flow_Direction_Grid = builder.Input_DInfinity_Flow_Direction_Grid;
        this.Input_Pit_Filled_Elevation_Grid = builder.Input_Pit_Filled_Elevation_Grid;
        this.Input_Stream_Raster_Grid = builder.Input_Stream_Raster_Grid;
        this.Statistical_Method = builder.Statistical_Method;
        this.Distance_Method = builder.Distance_Method;
        this.Check_for_edge_contamination = builder.Check_for_edge_contamination;
        this.Input_Weight_Path_Grid = builder.Input_Weight_Path_Grid;
        this.Output_DInfinity_Drop_to_Stream_Grid = builder.Output_DInfinity_Drop_to_Stream_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_DInfinity_Flow_Direction_Grid;
        private String Input_Pit_Filled_Elevation_Grid;
        private String Input_Stream_Raster_Grid;
        private String Statistical_Method;
        private String Distance_Method;
        private Boolean Check_for_edge_contamination;
        private String Input_Weight_Path_Grid;
        private String Output_DInfinity_Drop_to_Stream_Grid;

        /**
        * @param Input_DInfinity_Flow_Direction_Grid A grid giving flow directions by the D-Infinity method.
        * @param Input_Pit_Filled_Elevation_Grid This input is a grid of elevation values.
        * @param Input_Stream_Raster_Grid A grid indicating streams, by using a grid cell value of 1 on streams and 0 off streams.
        */
        public Builder(@NotBlank String Input_DInfinity_Flow_Direction_Grid, @NotBlank String Input_Pit_Filled_Elevation_Grid, @NotBlank String Input_Stream_Raster_Grid){
            this.Input_DInfinity_Flow_Direction_Grid = Input_DInfinity_Flow_Direction_Grid;
            this.Input_Pit_Filled_Elevation_Grid = Input_Pit_Filled_Elevation_Grid;
            this.Input_Stream_Raster_Grid = Input_Stream_Raster_Grid;
        }

        public Builder Input_DInfinity_Flow_Direction_Grid(String val){
            this.Input_DInfinity_Flow_Direction_Grid = val;
            return this;
        }
        public Builder Input_Pit_Filled_Elevation_Grid(String val){
            this.Input_Pit_Filled_Elevation_Grid = val;
            return this;
        }
        public Builder Input_Stream_Raster_Grid(String val){
            this.Input_Stream_Raster_Grid = val;
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
        public Builder Check_for_edge_contamination(Boolean val){
            this.Check_for_edge_contamination = val;
            return this;
        }
        public Builder Input_Weight_Path_Grid(String val){
            this.Input_Weight_Path_Grid = val;
            return this;
        }
        public Builder Output_DInfinity_Drop_to_Stream_Grid(String val){
            this.Output_DInfinity_Drop_to_Stream_Grid = val;
            return this;
        }

        public DInfinityDistanceDownParams build() {
            return new DInfinityDistanceDownParams(this);
        }
    }
}