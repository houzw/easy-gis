package org.egc.gis.taudem;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.ExecResult;

import javax.validation.constraints.NotBlank;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * TODO
 * Description:
 * <pre>
 * Stream Network Analysis tools
 * </pre>
 *
 * @author houzhiwei
 * @date 2018 /10/12 16:50
 */
@Slf4j
public class StreamNetworkAnalysis extends BaseTauDEM {

    private static StreamNetworkAnalysis ourInstance = new StreamNetworkAnalysis();

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static StreamNetworkAnalysis getInstance() {
        return ourInstance;
    }

    private StreamNetworkAnalysis() {
    }

    //    region ConnectDown

    /**
     * ConnectDown
     * <p> For each zone in a raster entered it identifies the point with largest AreaD8.
     *
     * @param Input_D8_Flow_Direction_Grid    the input d 8 flow direction grid
     * @param Input_D8_Contributing_Area_Grid the input d 8 contributing area grid
     * @param Input_Watershed_Grid            the input watershed grid
     * @param outputDir                       the output dir
     * @return the exec result
     * @see #ConnectDown(String, String, String, Long, String, String, String, String, String, String)
     */
    public static ExecResult ConnectDown(@NotBlank String Input_D8_Flow_Direction_Grid,
                                         @NotBlank String Input_D8_Contributing_Area_Grid,
                                         @NotBlank String Input_Watershed_Grid,
                                         String outputDir)
    {

        return ConnectDown(Input_D8_Flow_Direction_Grid, Input_D8_Contributing_Area_Grid, Input_Watershed_Grid,
                           -1L, null, null, null, null, null, outputDir);

    }


    /**
     * ConnectDown
     * <p> For each zone in a raster entered it identifies the point with largest AreaD8.
     *
     * @param Input_D8_Flow_Direction_Grid    the input d 8 flow direction grid
     * @param Input_D8_Contributing_Area_Grid the input d 8 contributing area grid
     * @param Input_Watershed_Grid            the input watershed grid
     * @param Output_Outlets_file             the output outlets file
     * @param Output_MovedOutlets_file        the output moved outlets file
     * @return the exec result
     * @see #ConnectDown(String, String, String, Long, String, String, String, String, String, String)
     */
    public static ExecResult ConnectDown(@NotBlank String Input_D8_Flow_Direction_Grid,
                                         @NotBlank String Input_D8_Contributing_Area_Grid,
                                         @NotBlank String Input_Watershed_Grid, String Output_Outlets_file,
                                         String Output_MovedOutlets_file)
    {

        return ConnectDown(Input_D8_Flow_Direction_Grid, Input_D8_Contributing_Area_Grid, Input_Watershed_Grid,
                           -1L, Output_Outlets_file, Output_MovedOutlets_file, null, null, null, null);

    }

    /**
     * ConnectDown
     * <p> For each zone in a raster entered it identifies the point with largest AreaD8.
     *
     * @param Input_D8_Flow_Direction_Grid    This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
     * @param Input_D8_Contributing_Area_Grid A grid giving the contributing area value in terms of the number of grid cells (or the summation of weights) for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it using the D8 algorithm.
     * @param Input_Watershed_Grid            Watershed grid delineated from gage watershed function or stream reach watershed function.
     * @param Input_Number_of_Grid_Cells      Number of grid cells move to downstream based on flow directions.
     * @param Output_Outlets_file             This output is point a OGR file where each point is created from watershed grid having the largest contributing area for each zone.
     * @param Output_MovedOutlets_file        This output is a point OGR file where each outlet is moved downflow a specified number of grid cells using flow directions.
     * @param outletlayername                 OGR layer name in outletfile (optional)
     * @param movedOutletLayerName            OGR layer name in moved outletfile (optional)
     * @param inputDir                        the input dir
     * @param outputDir                       the output dir
     * @return exec result
     */
    public static ExecResult ConnectDown(@NotBlank String Input_D8_Flow_Direction_Grid,
                                         @NotBlank String Input_D8_Contributing_Area_Grid,
                                         @NotBlank String Input_Watershed_Grid, Long Input_Number_of_Grid_Cells,
                                         String Output_Outlets_file, String Output_MovedOutlets_file,
                                         String outletlayername, String movedOutletLayerName, String inputDir,
                                         String outputDir)
    {
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
        files.put("-p", Input_D8_Flow_Direction_Grid);
        // A grid giving the contributing area value in terms of the number of grid cells (or the summation of weights) for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it using the D8 algorithm.
        files.put("-ad8", Input_D8_Contributing_Area_Grid);
        // Watershed grid delineated from gage watershed function or streamreachwatershed function.
        files.put("-w", Input_Watershed_Grid);
        // Number of grid cells move to downstream based on flow directions.
        if (Input_Number_of_Grid_Cells > 0) {
            params.put("-d", Input_Number_of_Grid_Cells);
        }
        // This output is point a OGR file where each point is created from watershed grid having the largest contributing area for each zone.
        if (StringUtils.isBlank(Output_Outlets_file)) {
            Output_Outlets_file = outputNaming(Input_D8_Flow_Direction_Grid, Output_Outlets_file, "Output_Outlets_file",
                                               "File");
        }
        outFiles.put("-o", Output_Outlets_file);
        // This output is a point OGR file where each outlet is moved downflow a specified number of grid cells using flow directions.
        if (StringUtils.isBlank(Output_MovedOutlets_file)) {
            Output_MovedOutlets_file = outputNaming(Input_D8_Flow_Direction_Grid, Output_MovedOutlets_file,
                                                    "Output_MovedOutlets_file", "DBFile");
        }
        outFiles.put("-od", Output_MovedOutlets_file);
        params.put("-odlyr", movedOutletLayerName);
        return ourInstance.runCommand(TauDEMCommands.CONNECT_DOWN, files, outFiles, params, inputDir, outputDir);
    }


    //    endregion

    //    region D8 Extreme Upslope Value

    /**
     * D8ExtremeUpslopeValue
     * <p> Evaluates the extreme (either maximum or minimum) upslope value from an input grid based on the D8 flow model.
     *
     * @param Input_D8_Flow_Direction_Grid A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     * @param Input_Value_Grid             This is the grid of values of which the maximum or minimum upslope value is selected.
     * @param inputDir                     the input dir
     * @param outputDir                    the output dir
     * @return exec result
     * @see #D8ExtremeUpslopeValue(String, String, Boolean, Boolean, String, String, String, Integer, String, String)
     */
    public static ExecResult D8ExtremeUpslopeValue(@NotBlank String Input_D8_Flow_Direction_Grid,
                                                   @NotBlank String Input_Value_Grid,
                                                   String inputDir, String outputDir)
    {
        return D8ExtremeUpslopeValue(Input_D8_Flow_Direction_Grid, Input_Value_Grid, true, false, null, null, null, -1,
                                     inputDir,
                                     outputDir);
    }

    /**
     * D8ExtremeUpslopeValue
     * <p> Evaluates the extreme (either maximum or minimum) upslope value from an input grid based on the D8 flow model.
     *
     * @param Input_D8_Flow_Direction_Grid A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     * @param Input_Value_Grid             This is the grid of values of which the maximum or minimum upslope value is selected.
     * @param Use_maximum_upslope_value    A flag to indicate whether the maximum or minimum upslope value is to be calculated.
     * @param inputDir                     the input dir
     * @param outputDir                    the output dir
     * @return exec result
     * @see #D8ExtremeUpslopeValue(String, String, Boolean, Boolean, String, String, String, Integer, String, String)
     */
    public static ExecResult D8ExtremeUpslopeValue(@NotBlank String Input_D8_Flow_Direction_Grid,
                                                   @NotBlank String Input_Value_Grid, Boolean Use_maximum_upslope_value,
                                                   String inputDir, String outputDir)
    {
        return D8ExtremeUpslopeValue(Input_D8_Flow_Direction_Grid, Input_Value_Grid, Use_maximum_upslope_value, false,
                                     null, null, null, -1, inputDir,
                                     outputDir);
    }

    /**
     * D8ExtremeUpslopeValue
     * <p> Evaluates the extreme (either maximum or minimum) upslope value from an input grid based on the D8 flow model.
     *
     * @param Input_D8_Flow_Direction_Grid A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     * @param Input_Value_Grid             This is the grid of values of which the maximum or minimum upslope value is selected.
     * @param Use_maximum_upslope_value    A flag to indicate whether the maximum or minimum upslope value is to be calculated.
     * @param Check_for_edge_contamination A flag that indicates whether the tool should check for edge contamination.
     * @param Input_Outlets                A point feature defining outlets of interest.
     * @param Output_Extreme_Value_Grid    A grid of the maximum/minimum upslope values.
     * @param layerName                    OGR layer name if outlets are not the first layer in outletfile (optional)
     * @param layerNumber                  OGR layer number if outlets are not the first layer in outletfile (optional)
     * @param inputDir                     the input dir
     * @param outputDir                    the output dir
     * @return exec result
     */
    public static ExecResult D8ExtremeUpslopeValue(@NotBlank String Input_D8_Flow_Direction_Grid,
                                                   @NotBlank String Input_Value_Grid, Boolean Use_maximum_upslope_value,
                                                   Boolean Check_for_edge_contamination, String Input_Outlets,
                                                   String Output_Extreme_Value_Grid,
                                                   String layerName, Integer layerNumber, String inputDir,
                                                   String outputDir)
    {
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        files.put("-p", Input_D8_Flow_Direction_Grid);
        // This is the grid of values of which the maximum or minimum upslope value is selected.
        files.put("-sa", Input_Value_Grid);
        // A flag to indicate whether the maximum or minimum upslope value is to be calculated.
        params.put("-max", Use_maximum_upslope_value);
        // A flag that indicates whether the tool should check for edge contamination.
        params.put("-nc", Check_for_edge_contamination);
        // A point feature defining outlets of interest.
        files.put("-o", Input_Outlets);
        // A grid of the maximum/minimum upslope values.
        if (StringUtils.isBlank(Output_Extreme_Value_Grid)) {
            String paramName = "Output_Extreme_Value_Grid";
            if (Use_maximum_upslope_value != null) {
                if (Use_maximum_upslope_value) {
                    paramName = "Output_Max_Extreme_Value_Grid";
                } else {
                    paramName = "Output_Min_Extreme_Value_Grid";
                }
            }
            Output_Extreme_Value_Grid = outputNaming(Input_D8_Flow_Direction_Grid, Output_Extreme_Value_Grid,
                                                     paramName, "Raster Dataset");
        }
        outFiles.put("-ssa", Output_Extreme_Value_Grid);

        params = layerNameAndNumber(params, layerName, layerNumber);
        return ourInstance.runCommand(TauDEMCommands.D8_FLOWPATH_EXTREME_UP, files, outFiles, params, inputDir,
                                      outputDir);
    }

    //    endregion

    //    region Gage Watershed

    /**
     * GageWatershed
     * <p> Calculates Gage watersheds grid.
     *
     * @param Input_D8_Flow_Direction_Grid the input d 8 flow direction grid
     * @param Input_Gages_file             the input gages file
     * @param outputDir                    the output dir
     * @return the exec result
     * @see #GageWatershed(String, String, String, String, String, Integer, String, String)
     */
    public static ExecResult GageWatershed(@NotBlank String Input_D8_Flow_Direction_Grid,
                                           @NotBlank String Input_Gages_file, String outputDir)
    {
        return GageWatershed(Input_D8_Flow_Direction_Grid, Input_Gages_file, null, null, outputDir);
    }

    /**
     * GageWatershed
     * <p> Calculates Gage watersheds grid.
     *
     * @param Input_D8_Flow_Direction_Grid the input d 8 flow direction grid
     * @param Input_Gages_file             the input gages file
     * @param Output_GageWatershed         the output gage watershed
     * @param inputDir                     the input dir
     * @param outputDir                    the output dir
     * @return the exec result
     * @see #GageWatershed(String, String, String, String, String, Integer, String, String)
     */
    public static ExecResult GageWatershed(@NotBlank String Input_D8_Flow_Direction_Grid,
                                           @NotBlank String Input_Gages_file, String Output_GageWatershed,
                                           String inputDir,
                                           String outputDir)
    {
        return GageWatershed(Input_D8_Flow_Direction_Grid, Input_Gages_file, Output_GageWatershed, null, null, -1,
                             inputDir,
                             outputDir);
    }

    /**
     * GageWatershed
     * <p> Calculates Gage watersheds grid.
     *
     * @param Input_D8_Flow_Direction_Grid A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     * @param Input_Gages_file             A point feature defining the gages to which watersheds will be delineated.
     * @param Output_GageWatershed         This output grid identifies each gage watershed.
     * @param Output_Downstream_Identefier the output downstream identefier
     * @param layerName                    the layer name
     * @param layerNumber                  the layer number
     * @param inputDir                     the input dir
     * @param outputDir                    the output dir
     * @return exec result
     */
    public static ExecResult GageWatershed(@NotBlank String Input_D8_Flow_Direction_Grid,
                                           @NotBlank String Input_Gages_file, String Output_GageWatershed,
                                           String Output_Downstream_Identefier,
                                           String layerName, Integer layerNumber, String inputDir, String outputDir)
    {
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        files.put("-p", Input_D8_Flow_Direction_Grid);
        // A point feature defining the gages to which watersheds will be delineated.
        files.put("-o", Input_Gages_file);
        // This output grid identifies each gage watershed.
        if (StringUtils.isBlank(Output_GageWatershed)) {
            Output_GageWatershed = outputNaming(Input_D8_Flow_Direction_Grid, Output_GageWatershed,
                                                "Output_GageWatershed", "Raster Dataset");
        }
        outFiles.put("-gw", Output_GageWatershed);
        //
        if (StringUtils.isBlank(Output_Downstream_Identefier)) {
            Output_Downstream_Identefier = outputNaming(Input_D8_Flow_Direction_Grid, Output_Downstream_Identefier,
                                                        "Output_Downstream_Identefier", "File");
        }
        outFiles.put("-id", Output_Downstream_Identefier);

        params = layerNameAndNumber(params, layerName, layerNumber);

        return ourInstance.runCommand(TauDEMCommands.GAGE_WATERSHED, files, outFiles, params, inputDir, outputDir);
    }
    //    endregion

    //    region Length Area

    /**
     * LengthAreaStreamSource
     * <p> Creates an indicator grid (1,0) that evaluates A >= (M)(L^y) based on upslope path length,
     *
     * @param Input_Length_Grid            the input length grid
     * @param Input_Contributing_Area_Grid the input contributing area grid
     * @param outputDir                    the output dir
     * @return the exec result
     * @see #LengthAreaStreamSource(String, String, Double, Double, String, String, String)
     */
    public static ExecResult LengthAreaStreamSource(@NotBlank String Input_Length_Grid,
                                                    @NotBlank String Input_Contributing_Area_Grid, String outputDir)
    {
        return LengthAreaStreamSource(Input_Length_Grid, Input_Contributing_Area_Grid, null, null,
                                      outputDir);
    }

    /**
     * LengthAreaStreamSource
     * <p> Creates an indicator grid (1,0) that evaluates A >= (M)(L^y) based on upslope path length,
     *
     * @param Input_Length_Grid            the input length grid
     * @param Input_Contributing_Area_Grid the input contributing area grid
     * @param Output_Stream_Source_Grid    the output stream source grid
     * @param inputDir                     the input dir
     * @param outputDir                    the output dir
     * @return the exec result
     * @see #LengthAreaStreamSource(String, String, Double, Double, String, String, String)
     */
    public static ExecResult LengthAreaStreamSource(@NotBlank String Input_Length_Grid,
                                                    @NotBlank String Input_Contributing_Area_Grid,
                                                    String Output_Stream_Source_Grid, String inputDir,
                                                    String outputDir)
    {
        return LengthAreaStreamSource(Input_Length_Grid, Input_Contributing_Area_Grid, 0.03,
                                      1.3, Output_Stream_Source_Grid, inputDir,
                                      outputDir);
    }

    /**
     * LengthAreaStreamSource
     * <p> Creates an indicator grid (1,0) that evaluates A >= (M)(L^y) based on upslope path length,
     * D8 contributing area grid inputs, and parameters M and y.
     *
     * @param Input_Length_Grid            A grid of the maximum upslope length for each cell.
     * @param Input_Contributing_Area_Grid A grid of contributing area values for each cell that were calculated using the D8 algorithm.
     * @param Threshold_M                  The multiplier threshold (M) parameter which is used in the formula: A > (M)(L^y), to identify the beginning of streams.
     * @param Exponent_y                   The exponent (y) parameter which is used in the formula: A > (M)(L^y), to identify the beginning of streams.
     * @param Output_Stream_Source_Grid    An indicator grid (1,0) that evaluates A >= (M)(L^y), based on the maximum upslope path length, the D8 contributing area grid inputs, and parameters M and y.
     * @param inputDir                     the input dir
     * @param outputDir                    the output dir
     * @return exec result
     */
    public static ExecResult LengthAreaStreamSource(@NotBlank String Input_Length_Grid,
                                                    @NotBlank String Input_Contributing_Area_Grid, Double Threshold_M,
                                                    Double Exponent_y, String Output_Stream_Source_Grid,
                                                    String inputDir,
                                                    String outputDir)
    {

        Map files = new LinkedHashMap(2);
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // A grid of the maximum upslope length for each cell.
        files.put("-plen", Input_Length_Grid);
        // A grid of contributing area values for each cell that were calculated using the D8 algorithm.
        files.put("-ad8", Input_Contributing_Area_Grid);
        // The multiplier threshold (M) parameter which is used in the formula: A > (M)(L^y), to identify the beginning of streams.
        // The exponent (y) parameter which is used in the formula: A > (M)(L^y), to identify the beginning of streams.
        if (Threshold_M == null) {
            Threshold_M = 0.03;
        }
        if (Exponent_y == null) {
            Exponent_y = 1.3;
        }
        params.put("-par", Threshold_M + " " + Exponent_y);
        // An indicator grid (1,0) that evaluates A >= (M)(L^y), based on the maximum upslope path length, the D8 contributing area grid inputs, and parameters M and y.
        if (StringUtils.isBlank(Output_Stream_Source_Grid)) {
            Output_Stream_Source_Grid = outputNaming(Input_Length_Grid, Output_Stream_Source_Grid,
                                                     "Output_Stream_Source_Grid", "Raster Dataset");
        }
        outFiles.put("-ss", Output_Stream_Source_Grid);
        return ourInstance.runCommand(TauDEMCommands.LENGTH_AREA_STREAM_SOURCE, files, outFiles, params, inputDir,
                                      outputDir);
    }

    //    endregion

    //    region Move Outlets To Streams

    /**
     * MoveOutletsToStreams
     *
     * @param Input_D8_Flow_Direction_Grid the input d 8 flow direction grid
     * @param Input_Stream_Raster_Grid     the input stream raster grid
     * @param Input_outlets                the input outlets
     * @param Output_moved_outlets         the output moved outlets
     * @param inputDir                     the input dir
     * @param outputDir                    the output dir
     * @return the exec result
     * @see #MoveOutletsToStreams(String, String, String, Double, String, String, String, Integer, String, String)
     */
    public static ExecResult MoveOutletsToStreams(String Input_D8_Flow_Direction_Grid, String Input_Stream_Raster_Grid,
                                                  String Input_outlets, String Output_moved_outlets, String inputDir,
                                                  String outputDir)
    {
        return MoveOutletsToStreams(Input_D8_Flow_Direction_Grid, Input_Stream_Raster_Grid, Input_outlets, null,
                                    Output_moved_outlets, null, null, -1, inputDir,
                                    outputDir);
    }

    /**
     * MoveOutletsToStreams
     *
     * @param Input_D8_Flow_Direction_Grid the input d 8 flow direction grid
     * @param Input_Stream_Raster_Grid     the input stream raster grid
     * @param Input_Outlets                the input outlets
     * @param inputDir                     the input dir
     * @param outputDir                    the output dir
     * @return the exec result
     * @see #MoveOutletsToStreams(String, String, String, Double, String, String, String, Integer, String, String)
     */
    public static ExecResult MoveOutletsToStreams(String Input_D8_Flow_Direction_Grid, String Input_Stream_Raster_Grid,
                                                  String Input_Outlets, String inputDir, String outputDir)
    {
        return MoveOutletsToStreams(Input_D8_Flow_Direction_Grid, Input_Stream_Raster_Grid, Input_Outlets, null, null,
                                    null, null, -1,
                                    inputDir, outputDir);
    }


    /**
     * MoveOutletsToStreams
     * <p> Moves outlet points that are not aligned with a stream cell from a stream raster grid, downslope along the D8 flow direction until a stream raster cell is encountered, the max_dist number of grid cells are examined, or the flow path exits the domain (i.
     *
     * @param Input_D8_Flow_Direction_Grid This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
     * @param Input_Stream_Raster_Grid     This output is an indicator grid (1,0) that indicates the location of streams, with a value of 1 for each of the stream cells and 0 for the remainder of the cells.
     * @param Input_Outlets                A point feature defining points of interest or outlets that should ideally be located on a stream, but may not be exactly on the stream due to the fact that the feature point locations may not have been accurately registered with respect to the stream raster grid.
     * @param Input_Maximum_Distance       This input paramater is the maximum number of grid cells that the points in the input outlet feature  will be moved before they are saved to the output outlet feature.
     * @param Output_Moved_Outlets         the output moved outlets
     * @param omlayername                  layer name in movedoutletsfile (Optional)
     * @param layerName                    the layer name
     * @param layerNumber                  the layer number
     * @param inputDir                     the input dir
     * @param outputDir                    the output dir
     * @return exec result
     */
    public static ExecResult MoveOutletsToStreams(@NotBlank String Input_D8_Flow_Direction_Grid,
                                                  @NotBlank String Input_Stream_Raster_Grid,
                                                  @NotBlank String Input_Outlets, Double Input_Maximum_Distance,
                                                  String Output_Moved_Outlets, String omlayername,
                                                  String layerName, Integer layerNumber, String inputDir,
                                                  String outputDir)
    {
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
        files.put("-p", Input_D8_Flow_Direction_Grid);
        // This output is an indicator grid (1,0) that indicates the location of streams, with a value of 1 for each of the stream cells and 0 for the remainder of the cells.
        files.put("-src", Input_Stream_Raster_Grid);
        // A point feature defining points of interest or outlets that should ideally be located on a stream, but may not be exactly on the stream due to the fact that the feature point locations may not have been accurately registered with respect to the stream raster grid.
        files.put("-o", Input_Outlets);
        // This input paramater is the maximum number of grid cells that the points in the input outlet feature  will be moved before they are saved to the output outlet feature.
        params.put("-md", Input_Maximum_Distance);
        // A point  OGR file defining points of interest or outlets.
        if (StringUtils.isBlank(Output_Moved_Outlets)) {
            Output_Moved_Outlets = outputNaming(Input_D8_Flow_Direction_Grid, Output_Moved_Outlets,
                                                "Output_Outlets_file",
                                                "File");
        }
        outFiles.put("-om", Output_Moved_Outlets);
        params = layerNameAndNumber(params, layerName, layerNumber);
        params.put("-omlyr", omlayername);

        return ourInstance.runCommand(TauDEMCommands.MOVE_OUTLETS_TO_STREAMS, files, outFiles, params, inputDir,
                                      outputDir);
    }
    //  endregion

    //    region Peuker Douglas


    /**
     * PeukerDouglas
     *
     * @param Input_Elevation_Grid the input elevation grid
     * @param outputDir            the output dir
     * @return the exec result
     * @see #PeukerDouglas(String, Double, Double, Double, String, String, String)
     */
    public static ExecResult PeukerDouglas(@NotBlank String Input_Elevation_Grid, String outputDir)
    {
        return PeukerDouglas(Input_Elevation_Grid, null, null,
                             outputDir);
    }


    /**
     * PeukerDouglas
     *
     * @param Input_Elevation_Grid      the input elevation grid
     * @param Output_Stream_Source_Grid the output stream source grid
     * @param inputDir                  the input dir
     * @param outputDir                 the output dir
     * @return the exec result
     * @see #PeukerDouglas(String, Double, Double, Double, String, String, String)
     */
    public static ExecResult PeukerDouglas(@NotBlank String Input_Elevation_Grid,
                                           String Output_Stream_Source_Grid, String inputDir, String outputDir)
    {
        return PeukerDouglas(Input_Elevation_Grid, null, null, null,
                             Output_Stream_Source_Grid, inputDir, outputDir);
    }


    /**
     * PeukerDouglas
     * <p> Creates an indicator grid (1,0) of upward curved grid cells according to the Peuker and Douglas algorithm.
     *
     * @param Input_Elevation_Grid      A grid of elevation values.
     * @param Center_Smoothing_Weight   The center weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
     * @param Side_Smoothing_Weight     The side weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
     * @param Diagonal_Smoothing_Weight The diagonal weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
     * @param Output_Stream_Source_Grid An indicator grid (1,0) of upward curved grid cells according to the Peuker and Douglas algorithm, and if viewed, resembles a channel network.
     * @param inputDir                  the input dir
     * @param outputDir                 the output dir
     * @return exec result
     */
    public static ExecResult PeukerDouglas(@NotBlank String Input_Elevation_Grid, Double Center_Smoothing_Weight,
                                           Double Side_Smoothing_Weight, Double Diagonal_Smoothing_Weight,
                                           String Output_Stream_Source_Grid, String inputDir, String outputDir)
    {

        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // A grid of elevation values.
        files.put("-fel", Input_Elevation_Grid);
        // The center weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
        // The side weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
        // The diagonal weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
        if (Center_Smoothing_Weight == null) {
            Center_Smoothing_Weight = 0.4;
        }
        if (Side_Smoothing_Weight == null) {
            Side_Smoothing_Weight = 0.1;
        }
        if (Diagonal_Smoothing_Weight == null) {
            Diagonal_Smoothing_Weight = 0.05;
        }
        params.put("-par", Center_Smoothing_Weight + " " + Side_Smoothing_Weight + " " + Diagonal_Smoothing_Weight);

        // An indicator grid (1,0) of upward curved grid cells according to the Peuker and Douglas algorithm, and if viewed, resembles a channel network.
        if (StringUtils.isBlank(Output_Stream_Source_Grid)) {
            Output_Stream_Source_Grid = outputNaming(Input_Elevation_Grid, Output_Stream_Source_Grid,
                                                     "Output_Stream_Source_Grid", "Raster Dataset");
        }
        outFiles.put("-ss", Output_Stream_Source_Grid);
        return ourInstance.runCommand(TauDEMCommands.PEUKER_DOUGLAS, files, outFiles, params, inputDir, outputDir);
    }

    //    endregion

    //    region Slope Area Combination

    /**
     * SlopeAreaCombination
     *
     * @param Input_Slope_Grid the input slope grid
     * @param Input_Area_Grid  the input area grid
     * @param outputDir        the output dir
     * @return the exec result
     * @see #SlopeAreaCombination(String, String, Double, Double, String, String, String)
     */
    public static ExecResult SlopeAreaCombination(@NotBlank String Input_Slope_Grid, @NotBlank String Input_Area_Grid,
                                                  String outputDir)
    {
        return SlopeAreaCombination(Input_Slope_Grid, Input_Area_Grid, null, null, outputDir);

    }

    /**
     * SlopeAreaCombination
     *
     * @param Input_Slope_Grid       the input slope grid
     * @param Input_Area_Grid        the input area grid
     * @param Output_Slope_Area_Grid the output slope area grid
     * @param inputDir               the input dir
     * @param outputDir              the output dir
     * @return the exec result
     * @see #SlopeAreaCombination(String, String, Double, Double, String, String, String)
     */
    public static ExecResult SlopeAreaCombination(@NotBlank String Input_Slope_Grid, @NotBlank String Input_Area_Grid,
                                                  String Output_Slope_Area_Grid, String inputDir, String outputDir)
    {
        return SlopeAreaCombination(Input_Slope_Grid, Input_Area_Grid,
                                    null, null,
                                    Output_Slope_Area_Grid, inputDir, outputDir);

    }


    /**
     * SlopeAreaCombination
     * <p> Creates a grid of slope-area values = (S^m)(A^n) based on slope and specific catchment area grid inputs, and parameters m and n.
     *
     * @param Input_Slope_Grid       This input is a grid of slope values.
     * @param Input_Area_Grid        A grid giving the specific catchment area for each cell taken as its own contribution (grid cell length or summation of weights) plus the proportional contribution from upslope neighbors that drain in to it.
     * @param Slope_Exponent_m       The slope exponent (m) parameter which will be used in the formula: (S^m)(A^n), that is used to create the slope-area grid.
     * @param Area_Exponent_n        The area exponent (n) parameter which will be used in the formula: (S^m)(A^n), that is used to create the slope-area grid.
     * @param Output_Slope_Area_Grid A grid of slope-area values = (S^m)(A^n) calculated from the slope grid, specific catchment area grid, m slope exponent parameter, and n area exponent parameter.
     * @param inputDir               the input dir
     * @param outputDir              the output dir
     * @return exec result
     */
    public static ExecResult SlopeAreaCombination(@NotBlank String Input_Slope_Grid, @NotBlank String Input_Area_Grid,
                                                  Double Slope_Exponent_m, Double Area_Exponent_n,
                                                  String Output_Slope_Area_Grid, String inputDir, String outputDir)
    {

        Map files = new LinkedHashMap(2);
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // This input is a grid of slope values.
        files.put("-slp", Input_Slope_Grid);
        // A grid giving the specific catchment area for each cell taken as its own contribution (grid cell length or summation of weights) plus the proportional contribution from upslope neighbors that drain in to it.
        files.put("-sca", Input_Area_Grid);
        // The slope exponent (m) parameter which will be used in the formula: (S^m)(A^n), that is used to create the slope-area grid.
        // The area exponent (n) parameter which will be used in the formula: (S^m)(A^n), that is used to create the slope-area grid.
        if (Slope_Exponent_m == null) {
            Slope_Exponent_m = 2d;
        }
        if (Area_Exponent_n == null) {
            Area_Exponent_n = 1d;
        }
        params.put("-par", Slope_Exponent_m + " " + Area_Exponent_n);
        // A grid of slope-area values = (S^m)(A^n) calculated from the slope grid, specific catchment area grid, m slope exponent parameter, and n area exponent parameter.
        if (StringUtils.isBlank(Output_Slope_Area_Grid)) {
            Output_Slope_Area_Grid = outputNaming(Input_Slope_Grid, Output_Slope_Area_Grid, "Output_Slope_Area_Grid",
                                                  "Raster Dataset");
        }
        outFiles.put("-sa", Output_Slope_Area_Grid);
        return ourInstance.runCommand(TauDEMCommands.SLOPE_AREA_COMBINATION, files, outFiles, params, inputDir,
                                      outputDir);
    }

    //    endregion

    //    region Stream Definition By Threshold

    /**
     * Stream definition by threshold exec result.
     *
     * @param baseFilename is the name of the base digital elevation model without suffixes
     *                     for simple input. Suffixes 'ssa' and 'src' will be appended.
     * @param inputDir     the input dir
     * @param outputDir    the output dir
     * @return the exec result
     * @see #StreamDefinitionByThreshold(String, String, Double, String, String, String)
     */
    public static ExecResult StreamDefinitionByThreshold(@NotBlank String baseFilename,
                                                         String inputDir, String outputDir)
    {
        Map files = new LinkedHashMap(1);
        files.put(null, baseFilename);
        return ourInstance.runCommand(TauDEMCommands.STREAM_DEFINITION_BY_THRESHOLD, files, null, null, inputDir,
                                      outputDir);
    }


    /**
     * StreamDefinitionByThreshold
     *
     * @param Input_Accumulated_Stream_Source_Grid the input accumulated stream source grid
     * @param Threshold                            the threshold
     * @param outputDir                            the output dir
     * @return the exec result
     * @see #StreamDefinitionByThreshold(String, String, Double, String, String, String)
     */
    public static ExecResult StreamDefinitionByThreshold(@NotBlank String Input_Accumulated_Stream_Source_Grid,
                                                         Double Threshold, String outputDir)
    {
        return StreamDefinitionByThreshold(Input_Accumulated_Stream_Source_Grid, null, Threshold,
                                           null, null, outputDir);
    }

    /**
     * StreamDefinitionByThreshold
     *
     * @param Input_Accumulated_Stream_Source_Grid the input accumulated stream source grid
     * @param Threshold                            the threshold
     * @param Output_Stream_Raster_Grid            the output stream raster grid
     * @param inputDir                             the input dir
     * @param outputDir                            the output dir
     * @return the exec result
     * @see #StreamDefinitionByThreshold(String, String, Double, String, String, String)
     */
    public static ExecResult StreamDefinitionByThreshold(@NotBlank String Input_Accumulated_Stream_Source_Grid,
                                                         Double Threshold, String Output_Stream_Raster_Grid,
                                                         String inputDir, String outputDir)
    {
        return StreamDefinitionByThreshold(Input_Accumulated_Stream_Source_Grid, null, Threshold,
                                           Output_Stream_Raster_Grid, inputDir, outputDir);
    }

    /**
     * StreamDefinitionByThreshold
     * <p> Operates on any grid and outputs an indicator (1,0) grid identifing cells with input values >= the threshold value.
     *
     * @param Input_Accumulated_Stream_Source_Grid This grid nominally accumulates some characteristic or combination of characteristics of the watershed.
     * @param Input_Mask_Grid                      This optional input is a grid that is used to mask the domain of interest and output is only provided where this grid is >= 0.
     * @param Threshold                            This parameter is compared to the value in the Accumulated Stream Source grid (*ssa) to determine if the cell should be considered a stream cell.
     * @param Output_Stream_Raster_Grid            This is an indicator grid (1,0) that indicates the location of streams, with a value of 1 for each of the stream cells and 0 for the remainder of the cells.
     * @param inputDir                             the input dir
     * @param outputDir                            the output dir
     * @return exec result
     */
    public static ExecResult StreamDefinitionByThreshold(@NotBlank String Input_Accumulated_Stream_Source_Grid,
                                                         String Input_Mask_Grid, Double Threshold,
                                                         String Output_Stream_Raster_Grid, String inputDir,
                                                         String outputDir)
    {

        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // This grid nominally accumulates some characteristic or combination of characteristics of the watershed.
        files.put("-ssa", Input_Accumulated_Stream_Source_Grid);
        // This optional input is a grid that is used to mask the domain of interest and output is only provided where this grid is >= 0.
        files.put("-mask", Input_Mask_Grid);
        // This parameter is compared to the value in the Accumulated Stream Source grid (*ssa) to determine if the cell should be considered a stream cell.
        if (Threshold == null) {
            Threshold = 100.0;
        }
        params.put("-thresh", Threshold);
        // This is an indicator grid (1,0) that indicates the location of streams, with a value of 1 for each of the stream cells and 0 for the remainder of the cells.
        if (StringUtils.isBlank(Output_Stream_Raster_Grid)) {
            Output_Stream_Raster_Grid = outputNaming(Input_Accumulated_Stream_Source_Grid, Output_Stream_Raster_Grid,
                                                     "Output_Stream_Raster_Grid", "Raster Dataset");
        }
        outFiles.put("-src", Output_Stream_Raster_Grid);
        return ourInstance.runCommand(TauDEMCommands.STREAM_DEFINITION_BY_THRESHOLD, files, outFiles, params, inputDir,
                                      outputDir);
    }

    //    endregion

    //    region Stream Definition With Drop Analysis

    /**
     * StreamDropAnalysis
     *
     * @param Input_Pit_Filled_Elevation_Grid      the input pit filled elevation grid
     * @param Input_D8_Flow_Direction_Grid         the input d 8 flow direction grid
     * @param Input_D8_Contributing_Area_Grid      the input d 8 contributing area grid
     * @param Input_Accumulated_Stream_Source_Grid the input accumulated stream source grid
     * @param Input_Outlets                        the input outlets
     * @param outputDir                            the output dir
     * @return the exec result
     * @see #StreamDropAnalysis(String, String, String, String, String, Double, Double, Double, Boolean, String, String, Integer, String, String)
     */
    public static ExecResult StreamDropAnalysis(@NotBlank String Input_Pit_Filled_Elevation_Grid,
                                                @NotBlank String Input_D8_Flow_Direction_Grid,
                                                @NotBlank String Input_D8_Contributing_Area_Grid,
                                                @NotBlank String Input_Accumulated_Stream_Source_Grid,
                                                @NotBlank String Input_Outlets, String outputDir)
    {
        return StreamDropAnalysis(Input_Pit_Filled_Elevation_Grid, Input_D8_Flow_Direction_Grid,
                                  Input_D8_Contributing_Area_Grid, Input_Accumulated_Stream_Source_Grid,
                                  Input_Outlets, null, null, outputDir);

    }

    /**
     * StreamDropAnalysis
     *
     * @param Input_Pit_Filled_Elevation_Grid      the input pit filled elevation grid
     * @param Input_D8_Flow_Direction_Grid         the input d 8 flow direction grid
     * @param Input_D8_Contributing_Area_Grid      the input d 8 contributing area grid
     * @param Input_Accumulated_Stream_Source_Grid the input accumulated stream source grid
     * @param Input_Outlets                        the input outlets
     * @param Output_Drop_Analysis_Text_File       the output drop analysis text file
     * @param inputDir                             the input dir
     * @param outputDir                            the output dir
     * @return the exec result
     * @see #StreamDropAnalysis(String, String, String, String, String, Double, Double, Double, Boolean, String, String, Integer, String, String)
     */
    public static ExecResult StreamDropAnalysis(@NotBlank String Input_Pit_Filled_Elevation_Grid,
                                                @NotBlank String Input_D8_Flow_Direction_Grid,
                                                @NotBlank String Input_D8_Contributing_Area_Grid,
                                                @NotBlank String Input_Accumulated_Stream_Source_Grid,
                                                @NotBlank String Input_Outlets,
                                                String Output_Drop_Analysis_Text_File, String inputDir,
                                                String outputDir)
    {
        return StreamDropAnalysis(Input_Pit_Filled_Elevation_Grid, Input_D8_Flow_Direction_Grid,
                                  Input_D8_Contributing_Area_Grid, Input_Accumulated_Stream_Source_Grid,
                                  Input_Outlets, 5d, 500d, 10d,
                                  true,
                                  Output_Drop_Analysis_Text_File, null, -1, inputDir, outputDir);

    }

    /**
     * StreamDropAnalysis
     * <p> Applies a series of thresholds (determined from the input parameters) to the input accumulated stream source grid (*ssa) grid and outputs the results in the *drp.
     *
     * @param Input_Pit_Filled_Elevation_Grid              A grid of elevation values.
     * @param Input_D8_Flow_Direction_Grid                 A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     * @param Input_D8_Contributing_Area_Grid              A grid of contributing area values for each cell that were calculated using the D8 algorithm.
     * @param Input_Accumulated_Stream_Source_Grid         This grid must be monotonically increasing along the downslope D8 flow directions.
     * @param Input_Outlets                                A point feature defining the outlets upstream of which drop analysis is performed.
     * @param Minimum_Threshold_Value                      This parameter is the lowest end of the range searched for possible threshold values using drop analysis.
     * @param Maximum_Threshold_Value                      This parameter is the highest end of the range searched for possible threshold values using drop analysis.
     * @param Number_of_Threshold_Values                   The parameter is the number of steps to divide the search range into when looking for possible threshold values using drop analysis.
     * @param Use_logarithmic_spacing_for_threshold_values This checkbox indicates whether logarithmic or linear spacing should be used when looking for possible threshold values using drop ananlysis.
     * @param Output_Drop_Analysis_Text_File               This is a comma delimited text file with the following header line: Threshold, DrainDen, NoFirstOrd, NoHighOrd, MeanDFirstOrd, MeanDHighOrd, StdDevFirstOrd, StdDevHighOrd, T The file then contains one line of data for each threshold value examined, and then a summary line that indicates the optimum threshold value.
     * @param layerName                                    the layer name
     * @param layerNumber                                  the layer number
     * @param inputDir                                     the input dir
     * @param outputDir                                    the output dir
     * @return exec result
     */
    public static ExecResult StreamDropAnalysis(@NotBlank String Input_Pit_Filled_Elevation_Grid,
                                                @NotBlank String Input_D8_Flow_Direction_Grid,
                                                @NotBlank String Input_D8_Contributing_Area_Grid,
                                                @NotBlank String Input_Accumulated_Stream_Source_Grid,
                                                @NotBlank String Input_Outlets, Double Minimum_Threshold_Value,
                                                Double Maximum_Threshold_Value, Double Number_of_Threshold_Values,
                                                Boolean Use_logarithmic_spacing_for_threshold_values,
                                                String Output_Drop_Analysis_Text_File,
                                                String layerName, Integer layerNumber, String inputDir,
                                                String outputDir)
    {

        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // A grid of elevation values.
        files.put("-fel", Input_Pit_Filled_Elevation_Grid);
        // A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        files.put("-p", Input_D8_Flow_Direction_Grid);
        // A grid of contributing area values for each cell that were calculated using the D8 algorithm.
        files.put("-ad8", Input_D8_Contributing_Area_Grid);
        // This grid must be monotonically increasing along the downslope D8 flow directions.
        files.put("-ssa", Input_Accumulated_Stream_Source_Grid);
        // A point feature defining the outlets upstream of which drop analysis is performed.
        files.put("-o", Input_Outlets);
        // This parameter is the lowest end of the range searched for possible threshold values using drop analysis.
        // This parameter is the highest end of the range searched for possible threshold values using drop analysis.
        // The parameter is the number of steps to divide the search range into when looking for possible threshold values using drop analysis.
        // This checkbox indicates whether logarithmic or linear spacing should be used when looking for possible threshold values using drop ananlysis.
        int l = Use_logarithmic_spacing_for_threshold_values ? 0 : 1;

        if (Minimum_Threshold_Value == null) {
            Minimum_Threshold_Value = 5.0;
        }
        if (Maximum_Threshold_Value == null) {
            Maximum_Threshold_Value = 500.0;
        }
        if (Number_of_Threshold_Values == null) {
            Number_of_Threshold_Values = 10.0;
        }

        params.put("-par",
                   Minimum_Threshold_Value + " " + Maximum_Threshold_Value + " " + Number_of_Threshold_Values + " " + l);

        // This is a comma delimited text file with the following header line: Threshold, DrainDen, NoFirstOrd, NoHighOrd, MeanDFirstOrd, MeanDHighOrd, StdDevFirstOrd, StdDevHighOrd, T The file then contains one line of data for each threshold value examined, and then a summary line that indicates the optimum threshold value.
        if (StringUtils.isBlank(Output_Drop_Analysis_Text_File)) {
            Output_Drop_Analysis_Text_File = outputNaming(Input_Pit_Filled_Elevation_Grid,
                                                          Output_Drop_Analysis_Text_File,
                                                          "Output_Drop_Analysis_Text_File", "File");
        }
        outFiles.put("-drp", Output_Drop_Analysis_Text_File);
        params = layerNameAndNumber(params, layerName, layerNumber);

        return ourInstance.runCommand(TauDEMCommands.STREAM_DROP_ANALYSIS, files, outFiles, params, inputDir,
                                      outputDir);
    }
    //    endregion

    //    region  Stream Reach And Watershed


    /**
     * StreamReachAndWatershed
     *
     * @param Input_Pit_Removed_Elevation_Grid the input pit removed elevation grid
     * @param Input_D8_Flow_Direction_Grid     the input d 8 flow direction grid
     * @param Input_D8_Contributing_Area_Grid  the input d 8 contributing area grid
     * @param Input_Stream_Raster_Grid         the input stream raster grid
     * @param Input_outlets                    the input outlets
     * @param Delineate_Single_Watershed       the delineate single watershed
     * @param Output_Watershed_Grid            the output watershed grid
     * @param inputDir                         the input dir
     * @param outputDir                        the output dir
     * @return the exec result
     * @see #StreamReachAndWatershed(String, String, String, String, String, Boolean, String, String, String, String, String, String, String, Integer, String, String)
     */
    public static ExecResult StreamReachAndWatershed(String Input_Pit_Removed_Elevation_Grid,
                                                     String Input_D8_Flow_Direction_Grid,
                                                     String Input_D8_Contributing_Area_Grid,
                                                     String Input_Stream_Raster_Grid, String Input_outlets,
                                                     Boolean Delineate_Single_Watershed, String Output_Watershed_Grid,
                                                     String inputDir, String outputDir)
    {
        return StreamReachAndWatershed(Input_Pit_Removed_Elevation_Grid, Input_D8_Flow_Direction_Grid,
                                       Input_D8_Contributing_Area_Grid, Input_Stream_Raster_Grid, Input_outlets,
                                       Delineate_Single_Watershed,
                                       null,
                                       null,
                                       null,
                                       null, Output_Watershed_Grid, null, null, -1,
                                       inputDir, outputDir);
    }


    /**
     * StreamReachAndWatershed
     *
     * @param baseFilename the base filename
     * @param inputDir     the input dir
     * @param outputDir    the output dir
     * @return the exec result
     * @see #StreamReachAndWatershed(String, String, String, String, String, Boolean, String, String, String, String, String, String, String, Integer, String, String)
     */
    public static ExecResult StreamReachAndWatershed(@NotBlank String baseFilename, String inputDir, String outputDir)
    {
        Map files = new LinkedHashMap();
        files.put(null, baseFilename);
        return ourInstance.runCommand(TauDEMCommands.STREAM_REACH_AND_WATERSHED, files, null, null, inputDir,
                                      outputDir);
    }


    /**
     * StreamReachAndWatershed
     *
     * @param Input_Pit_Filled_Elevation_Grid the input pit filled elevation grid
     * @param Input_D8_Flow_Direction_Grid    the input d 8 flow direction grid
     * @param Input_D8_Drainage_Area          the input d 8 drainage area
     * @param Input_Stream_Raster_Grid        the input stream raster grid
     * @param outputDir                       the output dir
     * @return the exec result
     * @see #StreamReachAndWatershed(String, String, String, String, String, Boolean, String, String, String, String, String, String, String, Integer, String, String)
     */
    public static ExecResult StreamReachAndWatershed(@NotBlank String Input_Pit_Filled_Elevation_Grid,
                                                     @NotBlank String Input_D8_Flow_Direction_Grid,
                                                     @NotBlank String Input_D8_Drainage_Area,
                                                     @NotBlank String Input_Stream_Raster_Grid, String outputDir)
    {
        return StreamReachAndWatershed(Input_Pit_Filled_Elevation_Grid, Input_D8_Flow_Direction_Grid,
                                       Input_D8_Drainage_Area, Input_Stream_Raster_Grid, null, null,
                                       null, null, null, null,
                                       outputDir);
    }

    /**
     * StreamReachAndWatershed
     *
     * @param Input_Pit_Filled_Elevation_Grid  the input pit filled elevation grid
     * @param Input_D8_Flow_Direction_Grid     the input d 8 flow direction grid
     * @param Input_D8_Drainage_Area           the input d 8 drainage area
     * @param Input_Stream_Raster_Grid         the input stream raster grid
     * @param Output_Stream_Order_Grid         the output stream order grid
     * @param Output_Network_Connectivity_Tree the output network connectivity tree
     * @param Output_Network_Coordinates       the output network coordinates
     * @param Output_Stream_Reach_file         the output stream reach file
     * @param Output_Watershed_Grid            the output watershed grid
     * @param inputDir                         the input dir
     * @param outputDir                        the output dir
     * @return the exec result
     * @see #StreamReachAndWatershed(String, String, String, String, String, Boolean, String, String, String, String, String, String, String, Integer, String, String)
     */
    public static ExecResult StreamReachAndWatershed(@NotBlank String Input_Pit_Filled_Elevation_Grid,
                                                     @NotBlank String Input_D8_Flow_Direction_Grid,
                                                     @NotBlank String Input_D8_Drainage_Area,
                                                     @NotBlank String Input_Stream_Raster_Grid,
                                                     String Output_Stream_Order_Grid,
                                                     String Output_Network_Connectivity_Tree,
                                                     String Output_Network_Coordinates, String Output_Stream_Reach_file,
                                                     String Output_Watershed_Grid, String inputDir, String outputDir)
    {
        return StreamReachAndWatershed(Input_Pit_Filled_Elevation_Grid, Input_D8_Flow_Direction_Grid,
                                       Input_D8_Drainage_Area, Input_Stream_Raster_Grid, null,
                                       false, Output_Stream_Order_Grid,
                                       Output_Network_Connectivity_Tree, Output_Network_Coordinates,
                                       Output_Stream_Reach_file, Output_Watershed_Grid, null, null, -1, inputDir,
                                       outputDir);
    }

    /**
     * StreamReachAndWatershed
     * <p> StreamNet
     * <p> This tool produces a vector network and OGR file from the stream raster grid.
     *
     * @param Input_Pit_Filled_Elevation_Grid  This input is a grid of elevation values.
     * @param Input_D8_Flow_Direction_Grid     This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
     * @param Input_D8_Drainage_Area           A grid giving the contributing area value in terms of the number of grid cells (or the summation of weights) for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it using the D8 algorithm.
     * @param Input_Stream_Raster_Grid         An indicator grid indicating streams, by using a grid cell value of 1 on streams and 0 off streams.
     * @param Input_Outlets                    A point feature defining points of interest.
     * @param Delineate_Single_Watershed       This option causes the tool to delineate a single watershed by representing the entire area draining to the Stream Network as a single value in the output watershed grid.
     * @param Output_Stream_Order_Grid         The Stream Order Grid has cells values of streams ordered according to the Strahler order system.
     * @param Output_Network_Connectivity_Tree This output is a text file that details the network topological connectivity is stored in the Stream Network Tree file.
     * @param Output_Network_Coordinates       This output is a text file that contains the coordinates and attributes of points along the stream network.
     * @param Output_Stream_Reach_file         This output is a polyline OGR file giving the links in a stream network.
     * @param Output_Watershed_Grid            This output grid identified each reach watershed with a unique ID number, or in the case where the delineate single watershed option was checked, the entire area draining to the stream network is identified with a single ID.
     * @param netlayername                     the netlayername
     * @param layerName                        the layer name
     * @param layerNumber                      the layer number
     * @param inputDir                         the input dir
     * @param outputDir                        the output dir
     * @return exec result
     */
    public static ExecResult StreamReachAndWatershed(@NotBlank String Input_Pit_Filled_Elevation_Grid,
                                                     @NotBlank String Input_D8_Flow_Direction_Grid,
                                                     @NotBlank String Input_D8_Drainage_Area,
                                                     @NotBlank String Input_Stream_Raster_Grid,
                                                     String Input_Outlets, Boolean Delineate_Single_Watershed,
                                                     String Output_Stream_Order_Grid,
                                                     String Output_Network_Connectivity_Tree,
                                                     String Output_Network_Coordinates, String Output_Stream_Reach_file,
                                                     String Output_Watershed_Grid, String netlayername,
                                                     String layerName, Integer layerNumber, String inputDir,
                                                     String outputDir)
    {

        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // This input is a grid of elevation values.
        files.put("-fel", Input_Pit_Filled_Elevation_Grid);
        // This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
        files.put("-p", Input_D8_Flow_Direction_Grid);
        // A grid giving the contributing area value in terms of the number of grid cells (or the summation of weights) for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it using the D8 algorithm.
        files.put("-ad8", Input_D8_Drainage_Area);
        // An indicator grid indicating streams, by using a grid cell value of 1 on streams and 0 off streams.
        files.put("-src", Input_Stream_Raster_Grid);
        // A point feature defining points of interest.
        files.put("-o", Input_Outlets);
        // This option causes the tool to delineate a single watershed by representing the entire area draining to the Stream Network as a single value in the output watershed grid.
        params.put("-sw", Delineate_Single_Watershed);
        // The Stream Order Grid has cells values of streams ordered according to the Strahler order system.
        if (StringUtils.isBlank(Output_Stream_Order_Grid)) {
            Output_Stream_Order_Grid = outputNaming(Input_Pit_Filled_Elevation_Grid, Output_Stream_Order_Grid,
                                                    "Output_Stream_Order_Grid", "Raster Dataset");
        }
        outFiles.put("-ord", Output_Stream_Order_Grid);
        // This output is a text file that details the network topological connectivity is stored in the Stream Network Tree file.
        if (StringUtils.isBlank(Output_Network_Connectivity_Tree)) {
            Output_Network_Connectivity_Tree = outputNaming(Input_Pit_Filled_Elevation_Grid,
                                                            Output_Network_Connectivity_Tree,
                                                            "Output_Network_Connectivity_Tree", "File");
        }
        outFiles.put("-tree", Output_Network_Connectivity_Tree);
        // This output is a text file that contains the coordinates and attributes of points along the stream network.
        if (StringUtils.isBlank(Output_Network_Coordinates)) {
            Output_Network_Coordinates = outputNaming(Input_Pit_Filled_Elevation_Grid, Output_Network_Coordinates,
                                                      "Output_Network_Coordinates", "File");
        }
        outFiles.put("-coord", Output_Network_Coordinates);
        // This output is a polyline OGR file giving the links in a stream network.
        if (StringUtils.isBlank(Output_Stream_Reach_file)) {
            Output_Stream_Reach_file = outputNaming(Input_Pit_Filled_Elevation_Grid, Output_Stream_Reach_file,
                                                    "Output_Stream_Reach_file", "File");
        }
        outFiles.put("-net", Output_Stream_Reach_file);
        // This output grid identified each reach watershed with a unique ID number, or in the case where the delineate single watershed option was checked, the entire area draining to the stream network is identified with a single ID.
        if (StringUtils.isBlank(Output_Watershed_Grid)) {
            Output_Watershed_Grid = outputNaming(Input_Pit_Filled_Elevation_Grid, Output_Watershed_Grid,
                                                 "Output_Watershed_Grid", "Raster Dataset");
        }
        outFiles.put("-w", Output_Watershed_Grid);

        params.put("-netlyr", netlayername);
        params = layerNameAndNumber(params, layerName, layerNumber);

        return ourInstance.runCommand(TauDEMCommands.STREAM_REACH_AND_WATERSHED, files, outFiles, params, inputDir,
                                      outputDir);
    }
    //    endregion
}
