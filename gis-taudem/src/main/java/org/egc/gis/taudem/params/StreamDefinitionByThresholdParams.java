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
 * Wrapper of parameters of StreamDefinitionByThreshold
 * @author houzhiwei
 * @date 2018-12-17T22:40:56+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class StreamDefinitionByThresholdParams implements Params {

    /**
     * <pre>
     * Input_Accumulated_Stream_Source_Grid
     *  </pre>
     * @see Builder#Builder( String)
     */
    public StreamDefinitionByThresholdParams(){}
    /**
     * This grid nominally accumulates some characteristic or combination of characteristics of the watershed.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Accumulated_Stream_Source_Grid;
    /**
    * This optional input is a grid that is used to mask the domain of interest and output is only provided where this grid is >= 0.
    */
    private String Input_Mask_Grid;
    /**
    * default is 100d.
    * This parameter is compared to the value in the Accumulated Stream Source grid (*ssa) to determine if the cell should be considered a stream cell.
    */
    @XmlElement(defaultValue = "100")
    private Double Threshold = 100d;

    /**
    * This is an indicator grid (1,0) that indicates the location of streams, with a value of 1 for each of the stream cells and 0 for the remainder of the cells.
    */
    private String Output_Stream_Raster_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_Stream_Raster_Grid() {
        if (StringUtils.isBlank(Output_Stream_Raster_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Accumulated_Stream_Source_Grid, "Output_Stream_Raster_Grid", "Raster Dataset", null));
        }
        return this.Output_Stream_Raster_Grid;
    }

    private StreamDefinitionByThresholdParams(Builder builder){
        this.Input_Accumulated_Stream_Source_Grid = builder.Input_Accumulated_Stream_Source_Grid;
        this.Input_Mask_Grid = builder.Input_Mask_Grid;
        this.Threshold = builder.Threshold;
        this.Output_Stream_Raster_Grid = builder.Output_Stream_Raster_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_Accumulated_Stream_Source_Grid;
        private String Input_Mask_Grid;
        private Double Threshold;
        private String Output_Stream_Raster_Grid;

        /**
        * @param Input_Accumulated_Stream_Source_Grid This grid nominally accumulates some characteristic or combination of characteristics of the watershed.
        */
        public Builder(@NotBlank String Input_Accumulated_Stream_Source_Grid){
            this.Input_Accumulated_Stream_Source_Grid = Input_Accumulated_Stream_Source_Grid;
        }

        public Builder Input_Accumulated_Stream_Source_Grid(String val){
            this.Input_Accumulated_Stream_Source_Grid = val;
            return this;
        }
        public Builder Input_Mask_Grid(String val){
            this.Input_Mask_Grid = val;
            return this;
        }
        public Builder Threshold(Double val){
            this.Threshold = val;
            return this;
        }
        public Builder Output_Stream_Raster_Grid(String val){
            this.Output_Stream_Raster_Grid = val;
            return this;
        }

        public StreamDefinitionByThresholdParams build() {
            return new StreamDefinitionByThresholdParams(this);
        }
    }
}