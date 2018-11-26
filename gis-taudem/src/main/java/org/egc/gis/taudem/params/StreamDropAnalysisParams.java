package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of StreamDropAnalysis
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class StreamDropAnalysisParams implements Params {


    /**
     * <pre>
     * Input_Pit_Filled_Elevation_Grid
     * Input_D8_Flow_Direction_Grid
     * Input_D8_Contributing_Area_Grid
     * Input_Accumulated_Stream_Source_Grid
     * Input_Outlets
     *  </pre>
     * @see #StreamDropAnalysisParams( String,  String,  String,  String,  String)
     */
    public StreamDropAnalysisParams(){}
    /**
     * A grid of elevation values.
     */
    @NotNull
    private String Input_Pit_Filled_Elevation_Grid;
    /**
     * A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     */
    @NotNull
    private String Input_D8_Flow_Direction_Grid;
    /**
     * A grid of contributing area values for each cell that were calculated using the D8 algorithm.
     */
    @NotNull
    private String Input_D8_Contributing_Area_Grid;
    /**
     * This grid must be monotonically increasing along the downslope D8 flow directions.
     */
    @NotNull
    private String Input_Accumulated_Stream_Source_Grid;
    /**
     * A point feature defining the outlets upstream of which drop analysis is performed.
     */
    @NotNull
    private String Input_Outlets;
    /**
    * default is 5d.
    * This parameter is the lowest end of the range searched for possible threshold values using drop analysis.
    */
    private Double Minimum_Threshold_Value = 5d;

    /**
    * default is 500d.
    * This parameter is the highest end of the range searched for possible threshold values using drop analysis.
    */
    private Double Maximum_Threshold_Value = 500d;

    /**
    * default is 10d.
    * The parameter is the number of steps to divide the search range into when looking for possible threshold values using drop analysis.
    */
    private Double Number_of_Threshold_Values = 10d;

    /**
    * default is true.
    * This checkbox indicates whether logarithmic or linear spacing should be used when looking for possible threshold values using drop ananlysis.
    */
    private Boolean Use_logarithmic_spacing_for_threshold_values = true;

    /**
    * default is "drp.txt".
    * This is a comma delimited text file with the following header line: Threshold, DrainDen, NoFirstOrd, NoHighOrd, MeanDFirstOrd, MeanDHighOrd, StdDevFirstOrd, StdDevHighOrd, T The file then contains one line of data for each threshold value examined, and then a summary line that indicates the optimum threshold value.
    */
    private String Output_Drop_Analysis_Text_File = "drp.txt";

    private String outputDir = "./";


    /**
    * @param Input_Pit_Filled_Elevation_Grid A grid of elevation values.
    * @param Input_D8_Flow_Direction_Grid A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
    * @param Input_D8_Contributing_Area_Grid A grid of contributing area values for each cell that were calculated using the D8 algorithm.
    * @param Input_Accumulated_Stream_Source_Grid This grid must be monotonically increasing along the downslope D8 flow directions.
    * @param Input_Outlets A point feature defining the outlets upstream of which drop analysis is performed.
    */
    public StreamDropAnalysisParams(@NotBlank String Input_Pit_Filled_Elevation_Grid, @NotBlank String Input_D8_Flow_Direction_Grid, @NotBlank String Input_D8_Contributing_Area_Grid, @NotBlank String Input_Accumulated_Stream_Source_Grid, @NotBlank String Input_Outlets){
        this.Input_Pit_Filled_Elevation_Grid = Input_Pit_Filled_Elevation_Grid;
        this.Input_D8_Flow_Direction_Grid = Input_D8_Flow_Direction_Grid;
        this.Input_D8_Contributing_Area_Grid = Input_D8_Contributing_Area_Grid;
        this.Input_Accumulated_Stream_Source_Grid = Input_Accumulated_Stream_Source_Grid;
        this.Input_Outlets = Input_Outlets;
    }

    public String getOutput_Drop_Analysis_Text_File() {
        if (StringUtils.isBlank(Output_Drop_Analysis_Text_File)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_Filled_Elevation_Grid, "Output_Drop_Analysis_Text_File", "File", null));
        }
        return this.Output_Drop_Analysis_Text_File;
    }
}