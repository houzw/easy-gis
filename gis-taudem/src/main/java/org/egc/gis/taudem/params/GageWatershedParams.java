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
 * Wrapper of parameters of GageWatershed
 * @author houzhiwei
 * @date 2018-12-17T22:40:56+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class GageWatershedParams implements Params {

    /**
     * <pre>
     * Input_D8_Flow_Direction_Grid
     * Input_Gages_file
     *  </pre>
     * @see Builder#Builder( String,  String)
     */
    public GageWatershedParams(){}
    /**
     * A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_D8_Flow_Direction_Grid;
    /**
     * A point feature defining the gages(outlets) to which watersheds will be delineated.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Gages_file;
    /**
    * This output grid identifies each gage watershed.
    */
    private String Output_GageWatershed;
    /**
    * 
    */
    private String Output_Downstream_Identefier;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_GageWatershed() {
        if (StringUtils.isBlank(Output_GageWatershed)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_GageWatershed", "Raster Dataset", null));
        }
        return this.Output_GageWatershed;
    }
    public String getOutput_Downstream_Identefier() {
        if (StringUtils.isBlank(Output_Downstream_Identefier)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_Downstream_Identefier", "File", null));
        }
        return this.Output_Downstream_Identefier;
    }

    private GageWatershedParams(Builder builder){
        this.Input_D8_Flow_Direction_Grid = builder.Input_D8_Flow_Direction_Grid;
        this.Input_Gages_file = builder.Input_Gages_file;
        this.Output_GageWatershed = builder.Output_GageWatershed;
        this.Output_Downstream_Identefier = builder.Output_Downstream_Identefier;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_D8_Flow_Direction_Grid;
        private String Input_Gages_file;
        private String Output_GageWatershed;
        private String Output_Downstream_Identefier;

        /**
        * @param Input_D8_Flow_Direction_Grid A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        * @param Input_Gages_file A point feature defining the gages(outlets) to which watersheds will be delineated.
        */
        public Builder(@NotBlank String Input_D8_Flow_Direction_Grid, @NotBlank String Input_Gages_file){
            this.Input_D8_Flow_Direction_Grid = Input_D8_Flow_Direction_Grid;
            this.Input_Gages_file = Input_Gages_file;
        }

        public Builder Input_D8_Flow_Direction_Grid(String val){
            this.Input_D8_Flow_Direction_Grid = val;
            return this;
        }
        public Builder Input_Gages_file(String val){
            this.Input_Gages_file = val;
            return this;
        }
        public Builder Output_GageWatershed(String val){
            this.Output_GageWatershed = val;
            return this;
        }
        public Builder Output_Downstream_Identefier(String val){
            this.Output_Downstream_Identefier = val;
            return this;
        }

        public GageWatershedParams build() {
            return new GageWatershedParams(this);
        }
    }
}