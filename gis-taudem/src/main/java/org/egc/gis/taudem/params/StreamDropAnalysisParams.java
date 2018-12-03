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
 * Wrapper of parameters of StreamDropAnalysis
 * @author houzhiwei
 * @date 2018-12-03T19:26:14+08:00
 */
@Getter
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class StreamDropAnalysisParams implements Params {

    /**
     * <pre>
     * Input_Pit_Filled_Elevation_Grid
     * Input_D8_Flow_Direction_Grid
     * Input_D8_Contributing_Area_Grid
     * Input_Accumulated_Stream_Source_Grid
     *  </pre>
     * @see Builder#Builder( String,  String,  String,  String)
     */
    public StreamDropAnalysisParams(){}
    /**
     * A grid of elevation values.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Pit_Filled_Elevation_Grid;
    /**
     * A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_D8_Flow_Direction_Grid;
    /**
     * A grid of contributing area values for each cell that were calculated using the D8 algorithm.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_D8_Contributing_Area_Grid;
    /**
     * This grid must be monotonically increasing along the downslope D8 flow directions.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Accumulated_Stream_Source_Grid;
    /**
    * A point feature defining the outlets upstream of which drop analysis is performed.
    */
    private String Input_Outlets;
    /**
    * default is 5d.
    * This parameter is the lowest end of the range searched for possible threshold values using drop analysis.
    */
    @XmlElement(defaultValue = "5")
    private Double Minimum_Threshold_Value = 5d;

    /**
    * default is 500d.
    * This parameter is the highest end of the range searched for possible threshold values using drop analysis.
    */
    @XmlElement(defaultValue = "500")
    private Double Maximum_Threshold_Value = 500d;

    /**
    * default is 10d.
    * The parameter is the number of steps to divide the search range into when looking for possible threshold values using drop analysis.
    */
    @XmlElement(defaultValue = "10")
    private Double Number_of_Threshold_Values = 10d;

    /**
    * default is true.
    * This checkbox indicates whether logarithmic or linear spacing should be used when looking for possible threshold values using drop ananlysis.
    */
    @XmlElement(defaultValue = "true")
    private Boolean Use_logarithmic_spacing_for_threshold_values = true;

    /**
    * default is "drp.txt".
    * This is a comma delimited text file with the following header line: Threshold, DrainDen, NoFirstOrd, NoHighOrd, MeanDFirstOrd, MeanDHighOrd, StdDevFirstOrd, StdDevHighOrd, T The file then contains one line of data for each threshold value examined, and then a summary line that indicates the optimum threshold value.
    */
    @XmlElement(defaultValue = "drp.txt")
    private String Output_Drop_Analysis_Text_File = "drp.txt";

    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_Drop_Analysis_Text_File() {
        if (StringUtils.isBlank(Output_Drop_Analysis_Text_File)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_Filled_Elevation_Grid, "Output_Drop_Analysis_Text_File", "File", null));
        }
        return this.Output_Drop_Analysis_Text_File;
    }

    private StreamDropAnalysisParams(Builder builder){
        this.Input_Pit_Filled_Elevation_Grid = builder.Input_Pit_Filled_Elevation_Grid;
        this.Input_D8_Flow_Direction_Grid = builder.Input_D8_Flow_Direction_Grid;
        this.Input_D8_Contributing_Area_Grid = builder.Input_D8_Contributing_Area_Grid;
        this.Input_Accumulated_Stream_Source_Grid = builder.Input_Accumulated_Stream_Source_Grid;
        this.Input_Outlets = builder.Input_Outlets;
        this.Minimum_Threshold_Value = builder.Minimum_Threshold_Value;
        this.Maximum_Threshold_Value = builder.Maximum_Threshold_Value;
        this.Number_of_Threshold_Values = builder.Number_of_Threshold_Values;
        this.Use_logarithmic_spacing_for_threshold_values = builder.Use_logarithmic_spacing_for_threshold_values;
        this.Output_Drop_Analysis_Text_File = builder.Output_Drop_Analysis_Text_File;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_Pit_Filled_Elevation_Grid;
        private String Input_D8_Flow_Direction_Grid;
        private String Input_D8_Contributing_Area_Grid;
        private String Input_Accumulated_Stream_Source_Grid;
        private String Input_Outlets;
        private Double Minimum_Threshold_Value;
        private Double Maximum_Threshold_Value;
        private Double Number_of_Threshold_Values;
        private Boolean Use_logarithmic_spacing_for_threshold_values;
        private String Output_Drop_Analysis_Text_File;

        /**
        * @param Input_Pit_Filled_Elevation_Grid A grid of elevation values.
        * @param Input_D8_Flow_Direction_Grid A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        * @param Input_D8_Contributing_Area_Grid A grid of contributing area values for each cell that were calculated using the D8 algorithm.
        * @param Input_Accumulated_Stream_Source_Grid This grid must be monotonically increasing along the downslope D8 flow directions.
        */
        public Builder(@NotBlank String Input_Pit_Filled_Elevation_Grid, @NotBlank String Input_D8_Flow_Direction_Grid, @NotBlank String Input_D8_Contributing_Area_Grid, @NotBlank String Input_Accumulated_Stream_Source_Grid){
            this.Input_Pit_Filled_Elevation_Grid = Input_Pit_Filled_Elevation_Grid;
            this.Input_D8_Flow_Direction_Grid = Input_D8_Flow_Direction_Grid;
            this.Input_D8_Contributing_Area_Grid = Input_D8_Contributing_Area_Grid;
            this.Input_Accumulated_Stream_Source_Grid = Input_Accumulated_Stream_Source_Grid;
        }

        public Builder Input_Pit_Filled_Elevation_Grid(String val){
            this.Input_Pit_Filled_Elevation_Grid = val;
            return this;
        }
        public Builder Input_D8_Flow_Direction_Grid(String val){
            this.Input_D8_Flow_Direction_Grid = val;
            return this;
        }
        public Builder Input_D8_Contributing_Area_Grid(String val){
            this.Input_D8_Contributing_Area_Grid = val;
            return this;
        }
        public Builder Input_Accumulated_Stream_Source_Grid(String val){
            this.Input_Accumulated_Stream_Source_Grid = val;
            return this;
        }
        public Builder Input_Outlets(String val){
            this.Input_Outlets = val;
            return this;
        }
        public Builder Minimum_Threshold_Value(Double val){
            this.Minimum_Threshold_Value = val;
            return this;
        }
        public Builder Maximum_Threshold_Value(Double val){
            this.Maximum_Threshold_Value = val;
            return this;
        }
        public Builder Number_of_Threshold_Values(Double val){
            this.Number_of_Threshold_Values = val;
            return this;
        }
        public Builder Use_logarithmic_spacing_for_threshold_values(Boolean val){
            this.Use_logarithmic_spacing_for_threshold_values = val;
            return this;
        }
        public Builder Output_Drop_Analysis_Text_File(String val){
            this.Output_Drop_Analysis_Text_File = val;
            return this;
        }

        public StreamDropAnalysisParams build() {
            return new StreamDropAnalysisParams(this);
        }
    }
}