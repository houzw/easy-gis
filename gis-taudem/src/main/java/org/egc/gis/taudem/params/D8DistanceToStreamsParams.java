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
 * Wrapper of parameters of D8DistanceToStreams
 * @author houzhiwei
 * @date 2018-12-03T19:26:14+08:00
 */
@Getter
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class D8DistanceToStreamsParams implements Params {

    /**
     * <pre>
     * Input_D8_Flow_Direction_Grid
     * Input_Stream_Raster_Grid
     *  </pre>
     * @see Builder#Builder( String,  String)
     */
    public D8DistanceToStreamsParams(){}
    /**
     * This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_D8_Flow_Direction_Grid;
    /**
     * A grid indicating streams.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Stream_Raster_Grid;
    /**
    * default is 50d.
    * This value acts as threshold on the Stream Raster Grid to determine the location of streams.
    */
    @XmlElement(defaultValue = "50")
    private Double Threshold = 50d;

    /**
    * A grid giving the horizontal distance along the flow path as defined by the D8 Flow Directions Grid to the streams in the Stream Raster Grid.
    */
    private String Output_Distance_to_Streams_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_Distance_to_Streams_Grid() {
        if (StringUtils.isBlank(Output_Distance_to_Streams_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_Distance_to_Streams_Grid", "Raster Dataset", null));
        }
        return this.Output_Distance_to_Streams_Grid;
    }

    private D8DistanceToStreamsParams(Builder builder){
        this.Input_D8_Flow_Direction_Grid = builder.Input_D8_Flow_Direction_Grid;
        this.Input_Stream_Raster_Grid = builder.Input_Stream_Raster_Grid;
        this.Threshold = builder.Threshold;
        this.Output_Distance_to_Streams_Grid = builder.Output_Distance_to_Streams_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_D8_Flow_Direction_Grid;
        private String Input_Stream_Raster_Grid;
        private Double Threshold;
        private String Output_Distance_to_Streams_Grid;

        /**
        * @param Input_D8_Flow_Direction_Grid This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
        * @param Input_Stream_Raster_Grid A grid indicating streams.
        */
        public Builder(@NotBlank String Input_D8_Flow_Direction_Grid, @NotBlank String Input_Stream_Raster_Grid){
            this.Input_D8_Flow_Direction_Grid = Input_D8_Flow_Direction_Grid;
            this.Input_Stream_Raster_Grid = Input_Stream_Raster_Grid;
        }

        public Builder Input_D8_Flow_Direction_Grid(String val){
            this.Input_D8_Flow_Direction_Grid = val;
            return this;
        }
        public Builder Input_Stream_Raster_Grid(String val){
            this.Input_Stream_Raster_Grid = val;
            return this;
        }
        public Builder Threshold(Double val){
            this.Threshold = val;
            return this;
        }
        public Builder Output_Distance_to_Streams_Grid(String val){
            this.Output_Distance_to_Streams_Grid = val;
            return this;
        }

        public D8DistanceToStreamsParams build() {
            return new D8DistanceToStreamsParams(this);
        }
    }
}