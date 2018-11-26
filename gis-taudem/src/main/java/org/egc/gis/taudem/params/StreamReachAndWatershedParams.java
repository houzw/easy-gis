package org.egc.gis.taudem.params;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Parameters wrapper of StreamReachAndWatershed
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Data
public class StreamReachAndWatershedParams implements Params {


    /**
     * <pre>
     * Input_Pit_Filled_Elevation_Grid
     * Input_D8_Flow_Direction_Grid
     * Input_D8_Drainage_Area
     * Input_Stream_Raster_Grid
     *  </pre>
     * @see #StreamReachAndWatershedParams( String,  String,  String,  String)
     */
    public StreamReachAndWatershedParams(){}
    /**
     * This input is a grid of elevation values.
     */
    @NotNull
    private String Input_Pit_Filled_Elevation_Grid;
    /**
     * This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
     */
    @NotNull
    private String Input_D8_Flow_Direction_Grid;
    /**
     * A grid giving the contributing area value in terms of the number of grid cells (or the summation of weights) for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it using the D8 algorithm.
     */
    @NotNull
    private String Input_D8_Drainage_Area;
    /**
     * An indicator grid indicating streams, by using a grid cell value of 1 on streams and 0 off streams.
     */
    @NotNull
    private String Input_Stream_Raster_Grid;
    /**
    * A point feature defining points of interest.
    */
    private String Input_Outlets;
    /**
    * default is false.
    * This option causes the tool to delineate a single watershed by representing the entire area draining to the Stream Network as a single value in the output watershed grid.
    */
    private Boolean Delineate_Single_Watershed = false;

    /**
    * The Stream Order Grid has cells values of streams ordered according to the Strahler order system.
    */
    private String Output_Stream_Order_Grid;
    /**
    * This output is a text file that details the network topological connectivity is stored in the Stream Network Tree file.
    */
    private String Output_Network_Connectivity_Tree;
    /**
    * This output is a text file that contains the coordinates and attributes of points along the stream network.
    */
    private String Output_Network_Coordinates;
    /**
    * This output is a polyline OGR file giving the links in a stream network.
    */
    private String Output_Stream_Reach_file;
    /**
    * This output grid identified each reach watershed with a unique ID number, or in the case where the delineate single watershed option was checked, the entire area draining to the stream network is identified with a single ID.
    */
    private String Output_Watershed_Grid;
    private String outputDir = "./";


    /**
    * @param Input_Pit_Filled_Elevation_Grid This input is a grid of elevation values.
    * @param Input_D8_Flow_Direction_Grid This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
    * @param Input_D8_Drainage_Area A grid giving the contributing area value in terms of the number of grid cells (or the summation of weights) for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it using the D8 algorithm.
    * @param Input_Stream_Raster_Grid An indicator grid indicating streams, by using a grid cell value of 1 on streams and 0 off streams.
    */
    public StreamReachAndWatershedParams(@NotBlank String Input_Pit_Filled_Elevation_Grid, @NotBlank String Input_D8_Flow_Direction_Grid, @NotBlank String Input_D8_Drainage_Area, @NotBlank String Input_Stream_Raster_Grid){
        this.Input_Pit_Filled_Elevation_Grid = Input_Pit_Filled_Elevation_Grid;
        this.Input_D8_Flow_Direction_Grid = Input_D8_Flow_Direction_Grid;
        this.Input_D8_Drainage_Area = Input_D8_Drainage_Area;
        this.Input_Stream_Raster_Grid = Input_Stream_Raster_Grid;
    }

    public String getOutput_Stream_Order_Grid() {
        if (StringUtils.isBlank(Output_Stream_Order_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_Filled_Elevation_Grid, "Output_Stream_Order_Grid", "Raster Dataset", null));
        }
        return this.Output_Stream_Order_Grid;
    }
    public String getOutput_Network_Connectivity_Tree() {
        if (StringUtils.isBlank(Output_Network_Connectivity_Tree)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_Filled_Elevation_Grid, "Output_Network_Connectivity_Tree", "File", null));
        }
        return this.Output_Network_Connectivity_Tree;
    }
    public String getOutput_Network_Coordinates() {
        if (StringUtils.isBlank(Output_Network_Coordinates)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_Filled_Elevation_Grid, "Output_Network_Coordinates", "File", null));
        }
        return this.Output_Network_Coordinates;
    }
    public String getOutput_Stream_Reach_file() {
        if (StringUtils.isBlank(Output_Stream_Reach_file)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_Filled_Elevation_Grid, "Output_Stream_Reach_file", "File", null));
        }
        return this.Output_Stream_Reach_file;
    }
    public String getOutput_Watershed_Grid() {
        if (StringUtils.isBlank(Output_Watershed_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_Filled_Elevation_Grid, "Output_Watershed_Grid", "Raster Dataset", null));
        }
        return this.Output_Watershed_Grid;
    }
}