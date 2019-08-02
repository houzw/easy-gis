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
 * Wrapper of parameters of PeukerDouglas
 * @author houzhiwei
 * @date 2018-12-17T22:40:56+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PeukerDouglasParams implements Params {

    /**
     * <pre>
     * Input_Elevation_Grid
     *  </pre>
     * @see Builder#Builder( String)
     */
    public PeukerDouglasParams(){}
    /**
     * A grid of elevation values.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Elevation_Grid;
    /**
    * default is 0.4d.
    * The center weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
    */
    @XmlElement(defaultValue = "0.4")
    private Double Center_Smoothing_Weight = 0.4d;

    /**
    * default is 0.1d.
    * The side weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
    */
    @XmlElement(defaultValue = "0.1")
    private Double Side_Smoothing_Weight = 0.1d;

    /**
    * default is 0.05d.
    * The diagonal weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
    */
    @XmlElement(defaultValue = "0.05")
    private Double Diagonal_Smoothing_Weight = 0.05d;

    /**
    * An indicator grid (1,0) of upward curved grid cells according to the Peuker and Douglas algorithm, and if viewed, resembles a channel network.
    */
    private String Output_Stream_Source_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_Stream_Source_Grid() {
        if (StringUtils.isBlank(Output_Stream_Source_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Elevation_Grid, "Output_Stream_Source_Grid", "Raster Dataset", null));
        }
        return this.Output_Stream_Source_Grid;
    }

    private PeukerDouglasParams(Builder builder){
        this.Input_Elevation_Grid = builder.Input_Elevation_Grid;
        this.Center_Smoothing_Weight = builder.Center_Smoothing_Weight;
        this.Side_Smoothing_Weight = builder.Side_Smoothing_Weight;
        this.Diagonal_Smoothing_Weight = builder.Diagonal_Smoothing_Weight;
        this.Output_Stream_Source_Grid = builder.Output_Stream_Source_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_Elevation_Grid;
        private Double Center_Smoothing_Weight;
        private Double Side_Smoothing_Weight;
        private Double Diagonal_Smoothing_Weight;
        private String Output_Stream_Source_Grid;

        /**
        * @param Input_Elevation_Grid A grid of elevation values.
        */
        public Builder(@NotBlank String Input_Elevation_Grid){
            this.Input_Elevation_Grid = Input_Elevation_Grid;
        }

        public Builder Input_Elevation_Grid(String val){
            this.Input_Elevation_Grid = val;
            return this;
        }
        public Builder Center_Smoothing_Weight(Double val){
            this.Center_Smoothing_Weight = val;
            return this;
        }
        public Builder Side_Smoothing_Weight(Double val){
            this.Side_Smoothing_Weight = val;
            return this;
        }
        public Builder Diagonal_Smoothing_Weight(Double val){
            this.Diagonal_Smoothing_Weight = val;
            return this;
        }
        public Builder Output_Stream_Source_Grid(String val){
            this.Output_Stream_Source_Grid = val;
            return this;
        }

        public PeukerDouglasParams build() {
            return new PeukerDouglasParams(this);
        }
    }
}