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
 * Wrapper of parameters of MoveOutletsToStreams
 * @author houzhiwei
 * @date 2018-12-17T22:40:56+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class MoveOutletsToStreamsParams implements Params {

    /**
     * <pre>
     * Input_D8_Flow_Direction_Grid
     * Input_Stream_Raster_Grid
     * Input_Outlets
     *  </pre>
     * @see Builder#Builder( String,  String,  String)
     */
    public MoveOutletsToStreamsParams(){}
    /**
     * This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_D8_Flow_Direction_Grid;
    /**
     * This output is an indicator grid (1,0) that indicates the location of streams, with a value of 1 for each of the stream cells and 0 for the remainder of the cells.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Stream_Raster_Grid;
    /**
     * A point feature defining points of interest or outlets that should ideally be located on a stream, but may not be exactly on the stream due to the fact that the feature point locations may not have been accurately registered with respect to the stream raster grid.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Outlets;
    /**
    * default is 50d.
    * This input paramater is the maximum number of grid cells that the points in the input outlet feature  will be moved before they are saved to the output outlet feature.
    */
    @XmlElement(defaultValue = "50")
    private Double Input_Maximum_Distance = 50d;

    /**
    * A point  OGR file defining points of interest or outlets.
    */
    private String Output_Outlets_file;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_Outlets_file() {
        if (StringUtils.isBlank(Output_Outlets_file)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_Outlets_file", "File", null));
        }
        return this.Output_Outlets_file;
    }

    private MoveOutletsToStreamsParams(Builder builder){
        this.Input_D8_Flow_Direction_Grid = builder.Input_D8_Flow_Direction_Grid;
        this.Input_Stream_Raster_Grid = builder.Input_Stream_Raster_Grid;
        this.Input_Outlets = builder.Input_Outlets;
        this.Input_Maximum_Distance = builder.Input_Maximum_Distance;
        this.Output_Outlets_file = builder.Output_Outlets_file;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_D8_Flow_Direction_Grid;
        private String Input_Stream_Raster_Grid;
        private String Input_Outlets;
        private Double Input_Maximum_Distance;
        private String Output_Outlets_file;

        /**
        * @param Input_D8_Flow_Direction_Grid This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
        * @param Input_Stream_Raster_Grid This output is an indicator grid (1,0) that indicates the location of streams, with a value of 1 for each of the stream cells and 0 for the remainder of the cells.
        * @param Input_Outlets A point feature defining points of interest or outlets that should ideally be located on a stream, but may not be exactly on the stream due to the fact that the feature point locations may not have been accurately registered with respect to the stream raster grid.
        */
        public Builder(@NotBlank String Input_D8_Flow_Direction_Grid, @NotBlank String Input_Stream_Raster_Grid, @NotBlank String Input_Outlets){
            this.Input_D8_Flow_Direction_Grid = Input_D8_Flow_Direction_Grid;
            this.Input_Stream_Raster_Grid = Input_Stream_Raster_Grid;
            this.Input_Outlets = Input_Outlets;
        }

        public Builder Input_D8_Flow_Direction_Grid(String val){
            this.Input_D8_Flow_Direction_Grid = val;
            return this;
        }
        public Builder Input_Stream_Raster_Grid(String val){
            this.Input_Stream_Raster_Grid = val;
            return this;
        }
        public Builder Input_Outlets(String val){
            this.Input_Outlets = val;
            return this;
        }
        public Builder Input_Maximum_Distance(Double val){
            this.Input_Maximum_Distance = val;
            return this;
        }
        public Builder Output_Outlets_file(String val){
            this.Output_Outlets_file = val;
            return this;
        }

        public MoveOutletsToStreamsParams build() {
            return new MoveOutletsToStreamsParams(this);
        }
    }
}