package org.egc.gis.taudem;

import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Description:
 * <pre>
 * TauDEM Basic Grid Analysis
 * generated use Scrapy json result and freemarker, edited by author
 * <b>若 Input_* 和 Output_* 参数已包含路径， *Dir 参数可为 null</b>
 * </pre>
 *
 * @author houzhiwei
 * @date 2018 /10/25 21:50
 */
public class BasicGridAnalysis extends BaseTauDEM {

    private static BasicGridAnalysis ourInstance = new BasicGridAnalysis();

    public static BasicGridAnalysis getInstance() {
        return ourInstance;
    }

    private BasicGridAnalysis() {
    }


    //region  Pit Remove

    /**
     * PitRemove
     * <p> This funciton identifies all pits in the DEM and raises their elevation to the level of the lowest pour point around their edge.
     * <p>http://hydrology.usu.edu/taudem/taudem5/help53/PitRemove.html
     *
     * @param Input_Elevation_Grid              A digital elevation model (DEM) grid to serve as the base input for the terrain analysis and stream delineation.
     * @param Output_Pit_Removed_Elevation_Grid A grid of elevation values with pits removed so that flow is routed off of the domain.
     * @param inputDir                          输入数据所在文件目录。
     * @param outputDir                         输入数据存放目录
     * @return true if succeeded otherwise false
     * @see #PitRemove(String, String, Boolean, String, String, String) #PitRemove(String, String, Boolean, String, String, String)
     */
    public static boolean PitRemove(@NotBlank String Input_Elevation_Grid, String Output_Pit_Removed_Elevation_Grid,
                                    String inputDir, String outputDir)
    {
        return PitRemove(Input_Elevation_Grid, Output_Pit_Removed_Elevation_Grid, false, null, inputDir, outputDir);
    }

    /**
     * PitRemove
     * <p> This funciton identifies all pits in the DEM and raises their elevation to the level of the lowest pour point around their edge.
     *
     * @param Input_Elevation_Grid                  A digital elevation model (DEM) grid to serve as the base input for the terrain analysis and stream delineation.
     * @param Output_Pit_Removed_Elevation_Grid     A grid of elevation values with pits removed so that flow is routed off of the domain.
     * @param Fill_Considering_only_4_way_neighbors If this option is selected Fill ensures that the grid is hydrologically conditioned with cell to cell connectivity in only 4 directions (N, S, E or W neighbors).
     * @param Input_Depression_Mask_Grid            the input depression mask grid
     * @param inputDir                              the input dir
     * @param outputDir                             the output dir
     * @return true if succeeded otherwise false
     */
    public static boolean PitRemove(@NotBlank String Input_Elevation_Grid, String Output_Pit_Removed_Elevation_Grid,
                                    Boolean Fill_Considering_only_4_way_neighbors, String Input_Depression_Mask_Grid,
                                    String inputDir, String outputDir)
    {

        Map files = new LinkedHashMap(2);
        Map outFiles = new LinkedHashMap(1);
        Map params = new LinkedHashMap(1);

        // A digital elevation model (DEM) grid to serve as the base input for the terrain analysis and stream delineation.
        files.put("-z", Input_Elevation_Grid);
        // If this option is selected Fill ensures that the grid is hydrologically conditioned with cell to cell connectivity in only 4 directions (N, S, E or W neighbors).
        params.put("-4way", Fill_Considering_only_4_way_neighbors);
        //
        files.put("-depmask", Input_Depression_Mask_Grid);
        // A grid of elevation values with pits removed so that flow is routed off of the domain.
        if (StringUtils.isBlank(Output_Pit_Removed_Elevation_Grid)) {
            Output_Pit_Removed_Elevation_Grid = outputNaming(Input_Elevation_Grid, Output_Pit_Removed_Elevation_Grid,
                                                             "Output_Pit_Removed_Elevation_Grid", "Raster Dataset");
        }
        outFiles.put("-fel", Output_Pit_Removed_Elevation_Grid);
        return ourInstance.runCommand(TauDEMCommands.PIT_REMOVE, files, outFiles, params, inputDir, outputDir);
    }

    //endregion


    //region D8 Flow Direction


    /**
     * D8FlowDirections
     *
     * @param Input_Pit_Filled_Elevation_Grid A grid of elevation values.
     * @param Output_D8_Flow_Direction_Grid   A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     * @param Output_D8_Slope_Grid            A grid giving slope in the D8 flow direction.
     * @param inputDir                        the input dir
     * @param outputDir                       the output dir
     * @return true if succeeded otherwise false
     */
    public static boolean D8FlowDirections(@NotBlank String Input_Pit_Filled_Elevation_Grid,
                                           String Output_D8_Flow_Direction_Grid, String Output_D8_Slope_Grid,
                                           String inputDir, String outputDir)
    {
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap(2);
        Map params = new LinkedHashMap();

        // A grid of elevation values.
        files.put("-fel", Input_Pit_Filled_Elevation_Grid);
        // A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        if (StringUtils.isBlank(Output_D8_Flow_Direction_Grid)) {
            Output_D8_Flow_Direction_Grid = outputNaming(Input_Pit_Filled_Elevation_Grid, Output_D8_Flow_Direction_Grid,
                                                         "Output_D8_Flow_Direction_Grid", "Raster Dataset");
        }
        outFiles.put("-p", Output_D8_Flow_Direction_Grid);
        // A grid giving slope in the D8 flow direction.
        if (StringUtils.isBlank(Output_D8_Slope_Grid)) {
            Output_D8_Slope_Grid = outputNaming(Input_Pit_Filled_Elevation_Grid, Output_D8_Slope_Grid,
                                                "Output_D8_Slope_Grid", "Raster Dataset");
        }
        //D8 slopes output
        outFiles.put("-sd8", Output_D8_Slope_Grid);
        return ourInstance.runCommand(TauDEMCommands.D8_FLOWDIR, files, outFiles, params, inputDir, outputDir);
    }

    //endregion


    //region D-Infinity Flow Direction

    /**
     * DInfinityFlowDirections
     * <p> Assigns a flow direction based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997).
     *
     * @param Input_Pit_FIlled_Elevation_Grid      A grid of elevation values.
     * @param Output_DInfinity_Flow_Direction_Grid A grid of flow directions based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997).
     * @param Output_DInfinity_Slope_Grid          A grid of slope evaluated using the D-infinity method
     * @param inputDir                             the input dir
     * @param outputDir                            the output dir
     * @return true if succeeded otherwise false
     */
    public static boolean DInfinityFlowDirections(@NotBlank String Input_Pit_FIlled_Elevation_Grid,
                                                  String Output_DInfinity_Flow_Direction_Grid,
                                                  String Output_DInfinity_Slope_Grid, String inputDir, String outputDir)
    {
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap(2);

        // A grid of elevation values.
        files.put("-fel", Input_Pit_FIlled_Elevation_Grid);
        // A grid of flow directions based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997, "A New Method for the Determination of Flow Directions and Contributing Areas in Grid Digital Elevation Models," Water Resources Research, 33(2): 309-319).
        if (StringUtils.isBlank(Output_DInfinity_Flow_Direction_Grid)) {
            Output_DInfinity_Flow_Direction_Grid = outputNaming(Input_Pit_FIlled_Elevation_Grid,
                                                                Output_DInfinity_Flow_Direction_Grid,
                                                                "Output_DInfinity_Flow_Direction_Grid",
                                                                "Raster Dataset");
        }
        outFiles.put("-ang", Output_DInfinity_Flow_Direction_Grid);
        // A grid of slope evaluated using the D-infinity method described in Tarboton, D.
        if (StringUtils.isBlank(Output_DInfinity_Slope_Grid)) {
            Output_DInfinity_Slope_Grid = outputNaming(Input_Pit_FIlled_Elevation_Grid, Output_DInfinity_Slope_Grid,
                                                       "Output_DInfinity_Slope_Grid", "Raster Dataset");
        }
        outFiles.put("-slp", Output_DInfinity_Slope_Grid);
        return ourInstance.runCommand(TauDEMCommands.D_INF_FLOWDIR, files, outFiles, null, inputDir, outputDir);
    }

    //endregion


    //region Grid Network

    /**
     * GridNetwork
     * <p> Creates 3 grids that contain for each grid cell: 1) the longest path, 2) the total path, and 3) the Strahler order number.
     *
     * @param Input_D8_Flow_Direction_Grid       A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     * @param Output_Strahler_Network_Order_Grid A grid giving the Strahler order number for each cell.
     * @param Output_Longest_Upslope_Length_Grid A grid that gives the length of the longest upslope D8 flow path terminating at each grid cell.
     * @param Output_Total_Upslope_Length_Grid   The total upslope path length is the length of the entire D8 flow grid network upslope of each grid cell.
     * @param inputDir                           the input dir
     * @param outputDir                          the output dir
     * @return true if succeeded otherwise false
     * @see #GridNetwork(String, String, String, Double, String, String, String, String, Integer, String, String) #GridNetwork(String, String, String, Double, String, String, String, String, Integer, String, String)
     */
    public static boolean GridNetwork(@NotBlank String Input_D8_Flow_Direction_Grid,
                                      String Output_Strahler_Network_Order_Grid,
                                      String Output_Longest_Upslope_Length_Grid,
                                      String Output_Total_Upslope_Length_Grid, String inputDir, String outputDir)
    {
        return GridNetwork(Input_D8_Flow_Direction_Grid, null, null, null, Output_Strahler_Network_Order_Grid,
                           Output_Longest_Upslope_Length_Grid, Output_Total_Upslope_Length_Grid, null, -1, inputDir,
                           outputDir);
    }

    /**
     * GridNetwork
     * <p> Creates 3 grids that contain for each grid cell: 1) the longest path, 2) the total path, and 3) the Strahler order number.
     *
     * @param Input_D8_Flow_Direction_Grid       A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     * @param Input_Outlets                      A point feature  defining the outlets of interest.
     * @param Input_Mask_Grid                    A grid that is used to determine the domain do be analyzed.
     * @param Input_Mask_Threshold_Value         This input parameter is used in the calculation mask grid value >= mask threshold to determine if the grid cell is in the domain to be analyzed.
     * @param Output_Strahler_Network_Order_Grid A grid giving the Strahler order number for each cell.
     * @param Output_Longest_Upslope_Length_Grid A grid that gives the length of the longest upslope D8 flow path terminating at each grid cell.
     * @param Output_Total_Upslope_Length_Grid   The total upslope path length is the length of the entire D8 flow grid network upslope of each grid cell.
     * @param layerName                          the layer name
     * @param layerNumber                        the layer number
     * @param inputDir                           the input dir
     * @param outputDir                          the output dir
     * @return true if succeeded otherwise false
     */
    public static boolean GridNetwork(@NotBlank String Input_D8_Flow_Direction_Grid, String Input_Outlets,
                                      String Input_Mask_Grid, Double Input_Mask_Threshold_Value,
                                      String Output_Strahler_Network_Order_Grid,
                                      String Output_Longest_Upslope_Length_Grid,
                                      String Output_Total_Upslope_Length_Grid, String layerName,
                                      Integer layerNumber, String inputDir, String outputDir)
    {

        Map files = new LinkedHashMap(3);
        Map outFiles = new LinkedHashMap(3);
        Map params = new LinkedHashMap();

        // A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        files.put("-p", Input_D8_Flow_Direction_Grid);
        // A point feature  defining the outlets of interest.
        files.put("-o", Input_Outlets);
        // A grid that is used to determine the domain do be analyzed.
        files.put("-mask", Input_Mask_Grid);
        // This input parameter is used in the calculation mask grid value >= mask threshold to determine if the grid cell is in the domain to be analyzed.
        params.put("-thresh", Input_Mask_Threshold_Value);
        // A grid giving the Strahler order number for each cell.
        if (StringUtils.isBlank(Output_Strahler_Network_Order_Grid)) {
            Output_Strahler_Network_Order_Grid = outputNaming(Input_D8_Flow_Direction_Grid,
                                                              Output_Strahler_Network_Order_Grid,
                                                              "Output_Strahler_Network_Order_Grid", "Raster Dataset");
        }
        outFiles.put("-gord", Output_Strahler_Network_Order_Grid);
        // A grid that gives the length of the longest upslope D8 flow path terminating at each grid cell.
        if (StringUtils.isBlank(Output_Longest_Upslope_Length_Grid)) {
            Output_Longest_Upslope_Length_Grid = outputNaming(Input_D8_Flow_Direction_Grid,
                                                              Output_Longest_Upslope_Length_Grid,
                                                              "Output_Longest_Upslope_Length_Grid", "Raster Dataset");
        }
        outFiles.put("-plen", Output_Longest_Upslope_Length_Grid);
        // The total upslope path length is the length of the entire D8 flow grid network upslope of each grid cell.
        if (StringUtils.isBlank(Output_Total_Upslope_Length_Grid)) {
            Output_Total_Upslope_Length_Grid = outputNaming(Input_D8_Flow_Direction_Grid,
                                                            Output_Total_Upslope_Length_Grid,
                                                            "Output_Total_Upslope_Length_Grid", "Raster Dataset");
        }
        outFiles.put("-tlen", Output_Total_Upslope_Length_Grid);
        params = layerNameAndNumber(params, layerName, layerNumber);
        return ourInstance.runCommand(TauDEMCommands.GRID_NET, files, outFiles, params, inputDir, outputDir);
    }

    //endregion


    //region   D8 Contributing Area

    /**
     * D8ContributingArea
     *
     * @param Input_D8_Flow_Direction_Grid     A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     * @param Output_D8_Contributing_Area_Grid A grid of contributing area values calculated as the cells own contribution plus the contribution from upslope neighbors that drain in to it according to the D8 flow model.
     * @param inputDir                         the input dir
     * @param outputDir                        the output dir
     * @return true if succeeded otherwise false
     * @see #D8ContributingArea(String, String, String, Boolean, String, String, Integer, String, String) #D8ContributingArea(String, String, String, Boolean, String, String, Integer, String, String)
     */
    public static boolean D8ContributingArea(@NotBlank String Input_D8_Flow_Direction_Grid,
                                             String Output_D8_Contributing_Area_Grid, String inputDir, String outputDir)
    {
        return D8ContributingArea(Input_D8_Flow_Direction_Grid, null, null, false,
                                  Output_D8_Contributing_Area_Grid, null, -1, inputDir, outputDir);
    }

    /**
     * D8ContributingArea
     *
     * @param Input_D8_Flow_Direction_Grid     A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     * @param Input_Outlets                    A point feature defining the outlets of interest.
     * @param Input_Weight_Grid                A grid giving contribution to flow for each cell.
     * @param Check_for_edge_contamination     A flag that indicates whether the tool should check for edge contamination.
     * @param Output_D8_Contributing_Area_Grid A grid of contributing area values calculated as the cells own contribution plus the contribution from upslope neighbors that drain in to it according to the D8 flow model.
     * @param layerName                        the layer name
     * @param layerNumber                      the layer number
     * @param inputDir                         the input dir
     * @param outputDir                        the output dir
     * @return true if succeeded otherwise false
     */
    public static boolean D8ContributingArea(@NotBlank String Input_D8_Flow_Direction_Grid,
                                             String Input_Outlets, String Input_Weight_Grid,
                                             Boolean Check_for_edge_contamination,
                                             String Output_D8_Contributing_Area_Grid,
                                             String layerName, Integer layerNumber, String inputDir, String outputDir)
    {

        Map files = new LinkedHashMap(3);
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap(3);

        // A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        files.put("-p", Input_D8_Flow_Direction_Grid);
        // A point feature defining the outlets of interest.
        files.put("-o", Input_Outlets);
        // A grid giving contribution to flow for each cell.
        files.put("-wg", Input_Weight_Grid);
        // A flag that indicates whether the tool should check for edge contamination.
        params.put("-nc", Check_for_edge_contamination);
        // A grid of contributing area values calculated as the cells own contribution plus the contribution from upslope neighbors that drain in to it according to the D8 flow model.
        if (StringUtils.isBlank(Output_D8_Contributing_Area_Grid)) {
            Output_D8_Contributing_Area_Grid = outputNaming(Input_D8_Flow_Direction_Grid,
                                                            Output_D8_Contributing_Area_Grid,
                                                            "Output_D8_Contributing_Area_Grid", "Raster Dataset");
        }
        outFiles.put("-ad8", Output_D8_Contributing_Area_Grid);
        params = layerNameAndNumber(params, layerName, layerNumber);
        return ourInstance.runCommand("AreaD8", files, outFiles, params, inputDir, outputDir);
    }

    //endregion


    //region   D-Infinity Contributing Area

    /**
     * DInfinityContributingArea
     * <p> Calculates a grid of specific catchment area which is the contributing area per unit contour length using the multiple flow direction D-infinity approach.
     *
     * @param Input_DInfinity_Flow_Direction_Grid           A grid of flow directions based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997).
     * @param Output_DInfinity_Specific_Catchment_Area_Grid A grid of specific catchment area which is the contributing area per unit contour length using the multiple flow direction D-infinity approach.
     * @param inputDir                                      the input dir
     * @param outputDir                                     the output dir
     * @return true if succeeded otherwise false
     * @see #DInfinityContributingArea(String, String, String, Boolean, String, String, Integer, String, String) #DInfinityContributingArea(String, String, String, Boolean, String, String, Integer, String, String)
     */
    public static boolean DInfinityContributingArea(@NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                    String Output_DInfinity_Specific_Catchment_Area_Grid,
                                                    String inputDir, String outputDir)
    {
        return DInfinityContributingArea(Input_DInfinity_Flow_Direction_Grid, null, null, false,
                                         Output_DInfinity_Specific_Catchment_Area_Grid, null, -1, inputDir, outputDir);
    }

    /**
     * DInfinityContributingArea
     * <p> Calculates a grid of specific catchment area which is the contributing area per unit contour length using the multiple flow direction D-infinity approach.
     *
     * @param Input_DInfinity_Flow_Direction_Grid           A grid of flow directions based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997).
     * @param Input_Outlets                                 A point feature defining the outlets of interest.
     * @param Input_Weight_Grid                             A grid giving contribution to flow for each cell.
     * @param Check_for_Edge_Contamination                  A flag that indicates whether the tool should check for edge contamination.
     * @param Output_DInfinity_Specific_Catchment_Area_Grid A grid of specific catchment area which is the contributing area per unit contour length using the multiple flow direction D-infinity approach.
     * @param layerName                                     the layer name
     * @param layerNumber                                   the layer number
     * @param inputDir                                      the input dir
     * @param outputDir                                     the output dir
     * @return true if succeeded otherwise false
     */
    public static boolean DInfinityContributingArea(@NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                    String Input_Outlets, String Input_Weight_Grid,
                                                    Boolean Check_for_Edge_Contamination,
                                                    String Output_DInfinity_Specific_Catchment_Area_Grid,
                                                    String layerName, Integer layerNumber,
                                                    String inputDir, String outputDir)
    {
        Map files = new LinkedHashMap(3);
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap(3);

        // A grid of flow directions based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997, "A New Method for the Determination of Flow Directions and Contributing Areas in Grid Digital Elevation Models," Water Resources Research, 33(2): 309-319).
        files.put("-ang", Input_DInfinity_Flow_Direction_Grid);
        // A point feature defining the outlets of interest.
        files.put("-o", Input_Outlets);
        // A grid giving contribution to flow for each cell.
        files.put("-wg", Input_Weight_Grid);
        // A flag that indicates whether the tool should check for edge contamination.
        params.put("-nc", Check_for_Edge_Contamination);
        // A grid of specific catchment area which is the contributing area per unit contour length using the multiple flow direction D-infinity approach.
        if (StringUtils.isBlank(Output_DInfinity_Specific_Catchment_Area_Grid)) {
            Output_DInfinity_Specific_Catchment_Area_Grid = outputNaming(Input_DInfinity_Flow_Direction_Grid,
                                                                         Output_DInfinity_Specific_Catchment_Area_Grid,
                                                                         "Output_DInfinity_Specific_Catchment_Area_Grid",
                                                                         "Raster Dataset");
        }
        outFiles.put("-sca", Output_DInfinity_Specific_Catchment_Area_Grid);
        params = layerNameAndNumber(params, layerName, layerNumber);
        return ourInstance.runCommand("AreaDinf", files, outFiles, params, inputDir, outputDir);
    }

    //endregion

}
