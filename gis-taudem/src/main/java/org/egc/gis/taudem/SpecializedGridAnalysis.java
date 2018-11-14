package org.egc.gis.taudem;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.ExecResult;

import javax.validation.constraints.NotBlank;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Description:
 * <pre>
 * Specialized Grid Analysis tools
 * </pre>
 *
 * @author houzhiwei
 * @date 2018 /10/12 16:50
 */
@Slf4j
public class SpecializedGridAnalysis extends BaseTauDEM {
    private static SpecializedGridAnalysis ourInstance = new SpecializedGridAnalysis();

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static SpecializedGridAnalysis getInstance() {
        return ourInstance;
    }

    private SpecializedGridAnalysis() {
    }


    // region  D-Infinity Avalanche Runout

    /**
     * D infinity avalanche runout exec result.
     *
     * @param Input_Pit_Filled_Elevation_Grid     the input pit filled elevation grid
     * @param Input_DInfinity_Flow_Direction_Grid the input d infinity flow direction grid
     * @param Input_Avalanche_Source_Site_Grid    the input avalanche source site grid
     * @param outputDir                           the output dir
     * @return the exec result
     * @see #DInfinityAvalancheRunout(String, String, String, Double, Double, String, String, String, String, String)
     */
    public static ExecResult DInfinityAvalancheRunout(@NotBlank String Input_Pit_Filled_Elevation_Grid,
                                                      @NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                      @NotBlank String Input_Avalanche_Source_Site_Grid,
                                                      String outputDir)
    {
        return DInfinityAvalancheRunout(Input_Pit_Filled_Elevation_Grid,
                                        Input_DInfinity_Flow_Direction_Grid,
                                        Input_Avalanche_Source_Site_Grid,
                                        null,
                                        null, null, outputDir);
    }

    /**
     * D infinity avalanche runout exec result.
     *
     * @param Input_Pit_Filled_Elevation_Grid     the input pit filled elevation grid
     * @param Input_DInfinity_Flow_Direction_Grid the input d infinity flow direction grid
     * @param Input_Avalanche_Source_Site_Grid    the input avalanche source site grid
     * @param Output_Runout_Zone_Grid             the output runout zone grid
     * @param Output_Path_Distance_Grid           the output path distance grid
     * @param inputDir                            the input dir
     * @param outputDir                           the output dir
     * @return the exec result
     * @see #DInfinityAvalancheRunout(String, String, String, Double, Double, String, String, String, String, String)
     */
    public static ExecResult DInfinityAvalancheRunout(@NotBlank String Input_Pit_Filled_Elevation_Grid,
                                                      @NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                      @NotBlank String Input_Avalanche_Source_Site_Grid,
                                                      String Output_Runout_Zone_Grid, String Output_Path_Distance_Grid,
                                                      String inputDir, String outputDir)
    {
        return DInfinityAvalancheRunout(Input_Pit_Filled_Elevation_Grid,
                                        Input_DInfinity_Flow_Direction_Grid,
                                        Input_Avalanche_Source_Site_Grid,
                                        null,
                                        null, null,
                                        Output_Runout_Zone_Grid, Output_Path_Distance_Grid,
                                        inputDir, outputDir);
    }

    /**
     * DInfinityAvalancheRunout
     * <p> Identifies an avalanche's affected area and the flow path length to each cell in that affacted area.
     *
     * @param Input_Pit_Filled_Elevation_Grid     A grid of elevation values.
     * @param Input_DInfinity_Flow_Direction_Grid A grid giving flow directions by the D-Infinity method.
     * @param Input_Avalanche_Source_Site_Grid    This is a grid of source areas for snow avalanches that are commonly identified manually using a mix of experience and visual interpretation of maps.
     * @param Input_Proportion_Threshold          This value is a threshold proportion that is used to limit the disperson of flow caused by using the D-infinity multiple flow direction method for determining flow direction.
     * @param Input_Alpha_Angle_Threshold         This value is the threshold angle, called the Alpha Angle, that is used to determine which of the cells downslope from the source cells are in the affected area.
     * @param Path_Distance_Method                This option selects the method used to measure the distance used to calculate the slope angle.
     * @param Output_Runout_Zone_Grid             This grid Identifies the avalanche's runout zone (affected area) using a runout zone indicator with value 0 to indicate that this grid cell is not in the runout zone and value > 0 to indicate that this grid cell is in the runout zone.
     * @param Output_Path_Distance_Grid           This is a grid of the flow distance from the source site that has the highest angle to each cell.
     * @param inputDir                            the input dir
     * @param outputDir                           the output dir
     * @return true if succeeded otherwise false
     */
    public static ExecResult DInfinityAvalancheRunout(@NotBlank String Input_Pit_Filled_Elevation_Grid,
                                                      @NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                      @NotBlank String Input_Avalanche_Source_Site_Grid,
                                                      Double Input_Proportion_Threshold,
                                                      Double Input_Alpha_Angle_Threshold, String Path_Distance_Method,
                                                      String Output_Runout_Zone_Grid, String Output_Path_Distance_Grid,
                                                      String inputDir, String outputDir)
    {
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // A grid of elevation values.
        files.put("-fel", Input_Pit_Filled_Elevation_Grid);
        // A grid giving flow directions by the D-Infinity method.
        files.put("-ang", Input_DInfinity_Flow_Direction_Grid);
        // This is a grid of source areas for snow avalanches that are commonly identified manually using a mix of experience and visual interpretation of maps.
        files.put("-ass", Input_Avalanche_Source_Site_Grid);
        // This value is a threshold proportion that is used to limit the disperson of flow caused by using the D-infinity multiple flow direction method for determining flow direction.
        params.put("-thresh", Input_Proportion_Threshold);
        // This value is the threshold angle, called the Alpha Angle, that is used to determine which of the cells downslope from the source cells are in the affected area.
        params.put("-alpha", Input_Alpha_Angle_Threshold);
        // This option selects the method used to measure the distance used to calculate the slope angle.
        params.put("-direct", Path_Distance_Method);
        // This grid Identifies the avalanche's runout zone (affected area) using a runout zone indicator with value 0 to indicate that this grid cell is not in the runout zone and value > 0 to indicate that this grid cell is in the runout zone.
        if (StringUtils.isBlank(Output_Runout_Zone_Grid)) {
            Output_Runout_Zone_Grid = outputNaming(Input_Pit_Filled_Elevation_Grid, Output_Runout_Zone_Grid,
                                                   "Output_Runout_Zone_Grid", "Raster Dataset");
        }
        outFiles.put("-rz", Output_Runout_Zone_Grid);
        // This is a grid of the flow distance from the source site that has the highest angle to each cell.
        if (StringUtils.isBlank(Output_Path_Distance_Grid)) {
            Output_Path_Distance_Grid = outputNaming(Input_Pit_Filled_Elevation_Grid, Output_Path_Distance_Grid,
                                                     "Output_Path_Distance_Grid", "Raster Dataset");
        }
        outFiles.put("-dfs", Output_Path_Distance_Grid);
        return ourInstance.runCommand(TauDEMCommands.D_INFINITY_AVALANCHE_RUNOUT, files, outFiles, params, inputDir,
                                      outputDir);
    }

    //endregion


    // region  D-Infinity Concentration Limited Accumulation

    /**
     * DInfinityConcentrationLimitedAcccumulation
     *
     * @param Input_DInfinity_Flow_Direction_Grid the input d infinity flow direction grid
     * @param Input_Effective_Runoff_Weight_Grid  the input effective runoff weight grid
     * @param Input_Disturbance_Indicator_Grid    the input disturbance indicator grid
     * @param Input_Decay_Multiplier_Grid         the input decay multiplier grid
     * @param outputDir                           the output dir
     * @return the exec result
     * @see #DInfinityConcentrationLimitedAcccumulation(String, String, String, String, String, Double, Boolean, String, String, String, String)
     */
    public static ExecResult DInfinityConcentrationLimitedAcccumulation(
            @NotBlank String Input_DInfinity_Flow_Direction_Grid, @NotBlank String Input_Effective_Runoff_Weight_Grid,
            @NotBlank String Input_Disturbance_Indicator_Grid, @NotBlank String Input_Decay_Multiplier_Grid,
            String outputDir)
    {
        return DInfinityConcentrationLimitedAcccumulation(Input_DInfinity_Flow_Direction_Grid,
                                                          Input_Effective_Runoff_Weight_Grid,
                                                          Input_Disturbance_Indicator_Grid, Input_Decay_Multiplier_Grid,
                                                          null, null,
                                                          null, outputDir);
    }

    /**
     * DInfinityConcentrationLimitedAcccumulation
     *
     * @param Input_DInfinity_Flow_Direction_Grid          the input d infinity flow direction grid
     * @param Input_Effective_Runoff_Weight_Grid           the input effective runoff weight grid
     * @param Input_Disturbance_Indicator_Grid             the input disturbance indicator grid
     * @param Input_Decay_Multiplier_Grid                  the input decay multiplier grid
     * @param Output_Overland_Flow_Specific_Discharge_Grid the output overland flow specific discharge grid
     * @param Output_Concentration_Grid                    the output concentration grid
     * @param inputDir                                     the input dir
     * @param outputDir                                    the output dir
     * @return the exec result
     * @see  #DInfinityConcentrationLimitedAcccumulation(String, String, String, String, String, Double, Boolean, String, String, String, String)
     */
    public static ExecResult DInfinityConcentrationLimitedAcccumulation(
            @NotBlank String Input_DInfinity_Flow_Direction_Grid, @NotBlank String Input_Effective_Runoff_Weight_Grid,
            @NotBlank String Input_Disturbance_Indicator_Grid, @NotBlank String Input_Decay_Multiplier_Grid,
            String Output_Overland_Flow_Specific_Discharge_Grid, String Output_Concentration_Grid, String inputDir,
            String outputDir)
    {
        return DInfinityConcentrationLimitedAcccumulation(Input_DInfinity_Flow_Direction_Grid,
                                                          Input_Effective_Runoff_Weight_Grid,
                                                          Input_Disturbance_Indicator_Grid, Input_Decay_Multiplier_Grid,
                                                          null, null, false,
                                                          Output_Overland_Flow_Specific_Discharge_Grid,
                                                          Output_Concentration_Grid, inputDir, outputDir);
    }

    /**
     * DInfinityConcentrationLimitedAcccumulation
     * <p> This function applies to the situation where an unlimited supply of a substance is loaded into flow at a concentration or solubility threshold Csol over a region indicated by an indicator grid (dg).
     *
     * @param Input_DInfinity_Flow_Direction_Grid          A grid giving flow direction by the D-infinity method.
     * @param Input_Effective_Runoff_Weight_Grid           A grid giving the input quantity (notionally effective runoff or excess precipitation) to be used in the D-infinity weighted contributing area evaluation of Overland Flow Specific Discharge.
     * @param Input_Disturbance_Indicator_Grid             A grid that indicates the source zone of the area of substance supply and must be 1 inside the zone and 0 or "no data" over the rest of the domain.
     * @param Input_Decay_Multiplier_Grid                  A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
     * @param Input_Outlets                                This optional input is a point feature  defining outlets of interest.
     * @param Concentration_Threshold                      The concentration or solubility threshold.
     * @param Check_for_Edge_Contamination                 This checkbox determines whether the tool should check for edge contamination.
     * @param Output_Overland_Flow_Specific_Discharge_Grid The grid giving the specific discharge of the flow carrying the constituent being loaded at the concentration threshold specified.
     * @param Output_Concentration_Grid                    A grid giving the resulting concentration of the compound of interest in the flow.
     * @param inputDir                                     the input dir
     * @param outputDir                                    the output dir
     * @return true if succeeded otherwise false
     */
    public static ExecResult DInfinityConcentrationLimitedAcccumulation(
            @NotBlank String Input_DInfinity_Flow_Direction_Grid, @NotBlank String Input_Effective_Runoff_Weight_Grid,
            @NotBlank String Input_Disturbance_Indicator_Grid, @NotBlank String Input_Decay_Multiplier_Grid,
            String Input_Outlets, Double Concentration_Threshold, Boolean Check_for_Edge_Contamination,
            String Output_Overland_Flow_Specific_Discharge_Grid, String Output_Concentration_Grid, String inputDir,
            String outputDir)
    {

        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // A grid giving flow direction by the D-infinity method.
        files.put("-ang", Input_DInfinity_Flow_Direction_Grid);
        // A grid giving the input quantity (notionally effective runoff or excess precipitation) to be used in the D-infinity weighted contributing area evaluation of Overland Flow Specific Discharge.
        files.put("-wg", Input_Effective_Runoff_Weight_Grid);
        // A grid that indicates the source zone of the area of substance supply and must be 1 inside the zone and 0 or "no data" over the rest of the domain.
        files.put("-dg", Input_Disturbance_Indicator_Grid);
        // A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
        files.put("-dm", Input_Decay_Multiplier_Grid);
        // This optional input is a point feature  defining outlets of interest.
        files.put("-o", Input_Outlets);
        // The concentration or solubility threshold.
        params.put("-csol", Concentration_Threshold);
        // This checkbox determines whether the tool should check for edge contamination.
        params.put("-nc", Check_for_Edge_Contamination);
        // The grid giving the specific discharge of the flow carrying the constituent being loaded at the concentration threshold specified.
        if (StringUtils.isBlank(Output_Overland_Flow_Specific_Discharge_Grid)) {
            Output_Overland_Flow_Specific_Discharge_Grid = outputNaming(Input_DInfinity_Flow_Direction_Grid,
                                                                        Output_Overland_Flow_Specific_Discharge_Grid,
                                                                        "Output_Overland_Flow_Specific_Discharge_Grid",
                                                                        "Raster Dataset");
        }
        outFiles.put("-q", Output_Overland_Flow_Specific_Discharge_Grid);
        // A grid giving the resulting concentration of the compound of interest in the flow.
        if (StringUtils.isBlank(Output_Concentration_Grid)) {
            Output_Concentration_Grid = outputNaming(Input_DInfinity_Flow_Direction_Grid, Output_Concentration_Grid,
                                                     "Output_Concentration_Grid", "Raster Dataset");
        }
        outFiles.put("-ctpt", Output_Concentration_Grid);
        return ourInstance.runCommand(TauDEMCommands.D_INFINITY_CONCENTRATION_LIMITED_ACCCUMULATION, files, outFiles,
                                      params, inputDir, outputDir);
    }
    // endregion


    // region  D-Infinity Decaying Accumulation

    /**
     * DInfinityDecayingAccumulation
     *
     * @param Input_DInfinity_Flow_Direction_Grid the input d infinity flow direction grid
     * @param Input_Decay_Multiplier_Grid         the input decay multiplier grid
     * @param outputDir                           the output dir
     * @return the exec result
     * @see #DInfinityDecayingAccumulation(String, String, String, String, Boolean, String, String, Integer, String, String)
     */
    public static ExecResult DInfinityDecayingAccumulation(@NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                           @NotBlank String Input_Decay_Multiplier_Grid,
                                                           String outputDir)
    {
        return DInfinityDecayingAccumulation(Input_DInfinity_Flow_Direction_Grid, Input_Decay_Multiplier_Grid,
                                             null, null,
                                             outputDir);
    }

    /**
     * DInfinityDecayingAccumulation
     *
     * @param Input_DInfinity_Flow_Direction_Grid         the input d infinity flow direction grid
     * @param Input_Decay_Multiplier_Grid                 the input decay multiplier grid
     * @param Output_Decayed_Specific_Catchment_Area_Grid the output decayed specific catchment area grid
     * @param inputDir                                    the input dir
     * @param outputDir                                   the output dir
     * @return the exec result
     * @see #DInfinityDecayingAccumulation(String, String, String, String, Boolean, String, String, Integer, String, String)
     */
    public static ExecResult DInfinityDecayingAccumulation(@NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                           @NotBlank String Input_Decay_Multiplier_Grid,
                                                           String Output_Decayed_Specific_Catchment_Area_Grid,
                                                           String inputDir, String outputDir)
    {
        return DInfinityDecayingAccumulation(Input_DInfinity_Flow_Direction_Grid, Input_Decay_Multiplier_Grid, null,
                                             null, false,
                                             Output_Decayed_Specific_Catchment_Area_Grid, null, -1, inputDir,
                                             outputDir);
    }

    /**
     * DInfinityDecayingAccumulation
     * <p> The D-Infinity Decaying Accumulation tool creates a grid of the accumulated quantity at each location in the domain where the quantity accumulates with the D-infinity flow field, but is subject to first order decay in moving from cell to cell.
     *
     * @param Input_DInfinity_Flow_Direction_Grid         A grid giving flow direction by the D-infinity method.
     * @param Input_Decay_Multiplier_Grid                 A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
     * @param Input_Weight_Grid                           A grid giving weights (loadings) to be used in the accumulation.
     * @param Input_Outlets                               This optional input is a point feature  defining outlets of interest.
     * @param Check_for_Edge_Contamination                This checkbox determines whether the tool should check for edge contamination.
     * @param Output_Decayed_Specific_Catchment_Area_Grid The D-Infinity Decaying Accumulation tool creates a grid of the accumulated mass at each location in the domain where mass moves with the D-infinity flow field, but is subject to first order decay in moving from cell to cell.
     * @param layerName                                   the layer name
     * @param layerNumber                                 the layer number
     * @param inputDir                                    the input dir
     * @param outputDir                                   the output dir
     * @return true if succeeded otherwise false
     */
    public static ExecResult DInfinityDecayingAccumulation(@NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                           @NotBlank String Input_Decay_Multiplier_Grid,
                                                           @NotBlank String Input_Weight_Grid,
                                                           String Input_Outlets,
                                                           Boolean Check_for_Edge_Contamination,
                                                           String Output_Decayed_Specific_Catchment_Area_Grid,
                                                           String layerName, Integer layerNumber, String inputDir,
                                                           String outputDir)
    {

        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // A grid giving flow direction by the D-infinity method.
        files.put("-ang", Input_DInfinity_Flow_Direction_Grid);
        // A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
        files.put("-dm", Input_Decay_Multiplier_Grid);
        // A grid giving weights (loadings) to be used in the accumulation.
        files.put("-wg", Input_Weight_Grid);
        // This optional input is a point feature  defining outlets of interest.
        files.put("-o", Input_Outlets);
        // This checkbox determines whether the tool should check for edge contamination.
        params.put("-nc", Check_for_Edge_Contamination);
        // The D-Infinity Decaying Accumulation tool creates a grid of the accumulated mass at each location in the domain where mass moves with the D-infinity flow field, but is subject to first order decay in moving from cell to cell.
        if (StringUtils.isBlank(Output_Decayed_Specific_Catchment_Area_Grid)) {
            Output_Decayed_Specific_Catchment_Area_Grid = outputNaming(Input_DInfinity_Flow_Direction_Grid,
                                                                       Output_Decayed_Specific_Catchment_Area_Grid,
                                                                       "Output_Decayed_Specific_Catchment_Area_Grid",
                                                                       "Raster Dataset");
        }
        outFiles.put("-sca", Output_Decayed_Specific_Catchment_Area_Grid);
        params = layerNameAndNumber(params, layerName, layerNumber);

        return ourInstance.runCommand(TauDEMCommands.D_INFINITY_DECAYING_ACCUMULATION, files, outFiles, params,
                                      inputDir, outputDir);
    }

    // endregion


    // region   D-Infinity Distance Down

    /**
     * DInfinityDistanceDown
     *
     * @param Input_DInfinity_Flow_Direction_Grid the input d infinity flow direction grid
     * @param Input_Pit_Filled_Elevation_Grid     the input pit filled elevation grid
     * @param Input_Stream_Raster_Grid            the input stream raster grid
     * @param outputDir                           the output dir
     * @return the exec result
     * @see  #DInfinityDistanceDown(String, String, String, String, String, Boolean, String, String, String, String)
     */
    public static ExecResult DInfinityDistanceDown(@NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                   @NotBlank String Input_Pit_Filled_Elevation_Grid,
                                                   @NotBlank String Input_Stream_Raster_Grid,
                                                   String outputDir)
    {
        return DInfinityDistanceDown(Input_DInfinity_Flow_Direction_Grid, Input_Pit_Filled_Elevation_Grid,
                                     Input_Stream_Raster_Grid, null, null,
                                     outputDir);
    }


    /**
     * DInfinityDistanceDown
     *
     * @param Input_DInfinity_Flow_Direction_Grid  the input d infinity flow direction grid
     * @param Input_Pit_Filled_Elevation_Grid      the input pit filled elevation grid
     * @param Input_Stream_Raster_Grid             the input stream raster grid
     * @param Output_DInfinity_Drop_to_Stream_Grid the output d infinity drop to stream grid
     * @param inputDir                             the input dir
     * @param outputDir                            the output dir
     * @return the exec result
     * @see #DInfinityDistanceDown(String, String, String, String, String, Boolean, String, String, String, String)
     */
    public static ExecResult DInfinityDistanceDown(@NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                   @NotBlank String Input_Pit_Filled_Elevation_Grid,
                                                   @NotBlank String Input_Stream_Raster_Grid,
                                                   String Output_DInfinity_Drop_to_Stream_Grid, String inputDir,
                                                   String outputDir)
    {
        return DInfinityDistanceDown(Input_DInfinity_Flow_Direction_Grid, Input_Pit_Filled_Elevation_Grid,
                                     Input_Stream_Raster_Grid, "ave", "h",
                                     false, null, Output_DInfinity_Drop_to_Stream_Grid, inputDir,
                                     outputDir);
    }


    /**
     * DInfinityDistanceDown
     * <p> Calculates the distance downslope to a stream using the D-infinity flow model.
     *
     * @param Input_DInfinity_Flow_Direction_Grid  A grid giving flow directions by the D-Infinity method.
     * @param Input_Pit_Filled_Elevation_Grid      This input is a grid of elevation values.
     * @param Input_Stream_Raster_Grid             A grid indicating streams, by using a grid cell value of 1 on streams and 0 off streams.
     * @param Statistical_Method                   Statistical method used to calculate the distance down to the stream. [ave =average, min = minimum, and max = maximum]
     * @param Distance_Method                      Distance method used to calculate the distance down to the stream.  [h = horizontal, v = vertical, p = Pythagoras, s = surface]
     * @param Check_for_edge_contamination         A flag that determines whether the tool should check for edge contamination.
     * @param Input_Weight_Path_Grid               A grid giving weights (loadings) to be used in the distance calculation.
     * @param Output_DInfinity_Drop_to_Stream_Grid Creates a grid containing the distance to stream calculated using the D-infinity flow model and the statistical and path methods chosen.
     * @param inputDir                             the input dir
     * @param outputDir                            the output dir
     * @return true if succeeded otherwise false
     */
    public static ExecResult DInfinityDistanceDown(@NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                   @NotBlank String Input_Pit_Filled_Elevation_Grid,
                                                   @NotBlank String Input_Stream_Raster_Grid, String Statistical_Method,
                                                   String Distance_Method, Boolean Check_for_edge_contamination,
                                                   @NotBlank String Input_Weight_Path_Grid,
                                                   String Output_DInfinity_Drop_to_Stream_Grid, String inputDir,
                                                   String outputDir)
    {

        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // A grid giving flow directions by the D-Infinity method.
        files.put("-ang", Input_DInfinity_Flow_Direction_Grid);
        // This input is a grid of elevation values.
        files.put("-fel", Input_Pit_Filled_Elevation_Grid);
        // A grid indicating streams, by using a grid cell value of 1 on streams and 0 off streams.
        files.put("-src", Input_Stream_Raster_Grid);
        // Statistical method used to calculate the distance down to the stream.
        params.put("-m", Statistical_Method);
        // Distance method used to calculate the distance down to the stream.
        params.put("-m", Distance_Method);
        // A flag that determines whether the tool should check for edge contamination.
        params.put("-nc", Check_for_edge_contamination);
        // A grid giving weights (loadings) to be used in the distance calculation.
        files.put("-wg", Input_Weight_Path_Grid);
        // Creates a grid containing the distance to stream calculated using the D-infinity flow model and the statistical and path methods chosen.
        if (StringUtils.isBlank(Output_DInfinity_Drop_to_Stream_Grid)) {
            Output_DInfinity_Drop_to_Stream_Grid = outputNaming(Input_DInfinity_Flow_Direction_Grid,
                                                                Output_DInfinity_Drop_to_Stream_Grid,
                                                                "Output_DInfinity_Drop_to_Stream_Grid",
                                                                "Raster Dataset");
        }
        outFiles.put("-dd", Output_DInfinity_Drop_to_Stream_Grid);
        return ourInstance.runCommand(TauDEMCommands.D_INFINITY_DISTANCE_DOWN, files, outFiles, params, inputDir,
                                      outputDir);
    }

    // endregion


    // region  D-Infinity Distance Up

    /**
     * DInfinityDistanceUp
     *
     * @param Input_DInfinity_Flow_Direction_Grid the input d infinity flow direction grid
     * @param Input_Pit_Filled_Elevation_Grid     the input pit filled elevation grid
     * @param Input_Slope_Grid                    the input slope grid
     * @param outputDir                           the output dir
     * @return the exec result
     * @see  #DInfinityDistanceUp(String, String, String, Double, String, String, Boolean, String, String, String)
     */
    public static ExecResult DInfinityDistanceUp(@NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                 @NotBlank String Input_Pit_Filled_Elevation_Grid,
                                                 @NotBlank String Input_Slope_Grid,
                                                 String outputDir)
    {
        return DInfinityDistanceUp(Input_DInfinity_Flow_Direction_Grid,
                                   Input_Pit_Filled_Elevation_Grid,
                                   Input_Slope_Grid, null,
                                   null, outputDir);

    }

    /**
     * DInfinityDistanceUp
     *
     * @param Input_DInfinity_Flow_Direction_Grid the input d infinity flow direction grid
     * @param Input_Pit_Filled_Elevation_Grid     the input pit filled elevation grid
     * @param Input_Slope_Grid                    the input slope grid
     * @param Output_DInfinity_Distance_Up_Grid   the output d infinity distance up grid
     * @param inputDir                            the input dir
     * @param outputDir                           the output dir
     * @return the exec result
     * @see  #DInfinityDistanceUp(String, String, String, Double, String, String, Boolean, String, String, String)
     */
    public static ExecResult DInfinityDistanceUp(@NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                 @NotBlank String Input_Pit_Filled_Elevation_Grid,
                                                 @NotBlank String Input_Slope_Grid,
                                                 String Output_DInfinity_Distance_Up_Grid, String inputDir,
                                                 String outputDir)
    {
        return DInfinityDistanceUp(Input_DInfinity_Flow_Direction_Grid,
                                   Input_Pit_Filled_Elevation_Grid,
                                   Input_Slope_Grid, null,
                                   "ave", "h", false,
                                   Output_DInfinity_Distance_Up_Grid, inputDir,
                                   outputDir);

    }

    /**
     * DInfinityDistanceUp
     * <p> This tool calculates the distance from each grid cell up to the ridge cells along the reverse D-infinity flow directions.
     *
     * @param Input_DInfinity_Flow_Direction_Grid A grid giving flow directions by the D-Infinity method.
     * @param Input_Pit_Filled_Elevation_Grid     This input is a grid of elevation values.
     * @param Input_Slope_Grid                    This input is a grid of slope values.
     * @param Input_Proportion_Threshold          The proportion threshold parameter where only grid cells that contribute flow with a proportion greater than this user specified threshold (t) is considered to be upslope of any given grid cell.
     * @param Statistical_Method                  Statistical method used to calculate the distance down to the stream.
     * @param Distance_Method                     Distance method used to calculate the distance down to the stream.
     * @param Check_for_Edge_Contamination        A flag that determines whether the tool should check for edge contamination.
     * @param Output_DInfinity_Distance_Up_Grid   the output d infinity distance up grid
     * @param inputDir                            the input dir
     * @param outputDir                           the output dir
     * @return true if succeeded otherwise false
     */
    public static ExecResult DInfinityDistanceUp(@NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                 @NotBlank String Input_Pit_Filled_Elevation_Grid,
                                                 @NotBlank String Input_Slope_Grid, Double Input_Proportion_Threshold,
                                                 String Statistical_Method, String Distance_Method,
                                                 Boolean Check_for_Edge_Contamination,
                                                 String Output_DInfinity_Distance_Up_Grid, String inputDir,
                                                 String outputDir)
    {

        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // A grid giving flow directions by the D-Infinity method.
        files.put("-ang", Input_DInfinity_Flow_Direction_Grid);
        // This input is a grid of elevation values.
        files.put("-fel", Input_Pit_Filled_Elevation_Grid);
        // This input is a grid of slope values.
        files.put("-slp", Input_Slope_Grid);
        // The proportion threshold parameter where only grid cells that contribute flow with a proportion greater than this user specified threshold (t) is considered to be upslope of any given grid cell.
        params.put("-thresh", Input_Proportion_Threshold);
        // Statistical method used to calculate the distance down to the stream.
        params.put("-m", Statistical_Method);
        // Distance method used to calculate the distance down to the stream.
        params.put("-m", Distance_Method);
        // A flag that determines whether the tool should check for edge contamination.
        params.put("-nc", Check_for_Edge_Contamination);
        //
        if (StringUtils.isBlank(Output_DInfinity_Distance_Up_Grid)) {
            Output_DInfinity_Distance_Up_Grid = outputNaming(Input_DInfinity_Flow_Direction_Grid,
                                                             Output_DInfinity_Distance_Up_Grid,
                                                             "Output_DInfinity_Distance_Up_Grid", "Raster Dataset");
        }
        outFiles.put("-du", Output_DInfinity_Distance_Up_Grid);
        return ourInstance.runCommand(TauDEMCommands.D_INFINITY_DISTANCE_UP, files, outFiles, params, inputDir,
                                      outputDir);
    }

    // endregion


    // region  D-Infinity Reverse Accumulation

    /**
     * DInfinityReverseAccumulation
     *
     * @param Input_DInfinity_Flow_Direction_Grid the input d infinity flow direction grid
     * @param Input_Weight_Grid                   the input weight grid
     * @param outputDir                           the output dir
     * @return the exec result
     * @see #DInfinityReverseAccumulation(String, String, String, String, String, String)
     */
    public static ExecResult DInfinityReverseAccumulation(@NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                          @NotBlank String Input_Weight_Grid,
                                                          String outputDir)
    {

        return DInfinityReverseAccumulation(Input_DInfinity_Flow_Direction_Grid, Input_Weight_Grid,
                                            null,
                                            null, null, outputDir);
    }


    /**
     * DInfinityReverseAccumulation
     * <p> This works in a similar way to evaluation of weighted Contributing area, except that the accumulation is by propagating the weight loadings upslope along the reverse of the flow directions to accumulate the quantity of weight loading downslope from each grid cell.
     *
     * @param Input_DInfinity_Flow_Direction_Grid A grid giving flow direction by the Dinfinity method.
     * @param Input_Weight_Grid                   A grid giving weights (loadings) to be used in the accumulation.
     * @param Output_Reverse_Accumulation_Grid    The grid giving the result of the "Reverse Accumulation" function.
     * @param Output_Maximum_Downslope_Grid       The grid giving the maximum of the weight loading grid downslope from each grid cell.
     * @param inputDir                            the input dir
     * @param outputDir                           the output dir
     * @return true if succeeded otherwise false
     */
    public static ExecResult DInfinityReverseAccumulation(@NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                          @NotBlank String Input_Weight_Grid,
                                                          String Output_Reverse_Accumulation_Grid,
                                                          String Output_Maximum_Downslope_Grid, String inputDir,
                                                          String outputDir)
    {

        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // A grid giving flow direction by the Dinfinity method.
        files.put("-ang", Input_DInfinity_Flow_Direction_Grid);
        // A grid giving weights (loadings) to be used in the accumulation.
        files.put("-wgwg", Input_Weight_Grid);
        // The grid giving the result of the "Reverse Accumulation" function.
        if (StringUtils.isBlank(Output_Reverse_Accumulation_Grid)) {
            Output_Reverse_Accumulation_Grid = outputNaming(Input_DInfinity_Flow_Direction_Grid,
                                                            Output_Reverse_Accumulation_Grid,
                                                            "Output_Reverse_Accumulation_Grid", "Raster Dataset");
        }
        outFiles.put("-racc", Output_Reverse_Accumulation_Grid);
        // The grid giving the maximum of the weight loading grid downslope from each grid cell.
        if (StringUtils.isBlank(Output_Maximum_Downslope_Grid)) {
            Output_Maximum_Downslope_Grid = outputNaming(Input_DInfinity_Flow_Direction_Grid,
                                                         Output_Maximum_Downslope_Grid, "Output_Maximum_Downslope_Grid",
                                                         "Raster Dataset");
        }
        outFiles.put("-dmax", Output_Maximum_Downslope_Grid);
        return ourInstance.runCommand(TauDEMCommands.D_INFINITY_REVERSE_ACCUMULATION, files, outFiles, params, inputDir,
                                      outputDir);
    }

    // endregion


    // region  D-Infinity Transport Limited Accumulation

    /**
     * DInfinityTransportLimitedAccumulation
     *
     * @param Input_DInfinity_Flow_Direction_Grid the input d infinity flow direction grid
     * @param Input_Supply_Grid                   the input supply grid
     * @param Input_Transport_Capacity_Grid       the input transport capacity grid
     * @param outputDir                           the output dir
     * @return the exec result
     * @see #DInfinityTransportLimitedAccumulation(String, String, String, String, String, Boolean, String, String, String, String, Integer, String, String)
     */
    public static ExecResult DInfinityTransportLimitedAccumulation(@NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                                   @NotBlank String Input_Supply_Grid,
                                                                   @NotBlank String Input_Transport_Capacity_Grid,
                                                                   String outputDir)
    {
        return DInfinityTransportLimitedAccumulation(Input_DInfinity_Flow_Direction_Grid, Input_Supply_Grid,
                                                     Input_Transport_Capacity_Grid, null,
                                                     null, null, outputDir);
    }

    /**
     * DInfinityTransportLimitedAccumulation
     *
     * @param Input_DInfinity_Flow_Direction_Grid        the input d infinity flow direction grid
     * @param Input_Supply_Grid                          the input supply grid
     * @param Input_Transport_Capacity_Grid              the input transport capacity grid
     * @param Output_Transport_Limited_Accumulation_Grid the output transport limited accumulation grid
     * @param Output_Deposition_Grid                     the output deposition grid
     * @param inputDir                                   the input dir
     * @param outputDir                                  the output dir
     * @return the exec result
     * @see #DInfinityTransportLimitedAccumulation(String, String, String, String, String, Boolean, String, String, String, String, Integer, String, String)
     */
    public static ExecResult DInfinityTransportLimitedAccumulation(@NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                                   @NotBlank String Input_Supply_Grid,
                                                                   @NotBlank String Input_Transport_Capacity_Grid,
                                                                   String Output_Transport_Limited_Accumulation_Grid,
                                                                   String Output_Deposition_Grid, String inputDir,
                                                                   String outputDir)
    {
        return DInfinityTransportLimitedAccumulation(Input_DInfinity_Flow_Direction_Grid, Input_Supply_Grid,
                                                     Input_Transport_Capacity_Grid, null, null,
                                                     false,
                                                     Output_Transport_Limited_Accumulation_Grid, Output_Deposition_Grid,
                                                     null, null, -1, inputDir, outputDir);
    }

    /**
     * DInfinityTransportLimitedAccumulation
     * <p> This function is designed to calculate the transport and deposition of a substance (e.
     *
     * @param Input_DInfinity_Flow_Direction_Grid        A grid giving flow direction by the D-infinity method.
     * @param Input_Supply_Grid                          A grid giving the supply (loading) of material to a transport limited accumulation function.
     * @param Input_Transport_Capacity_Grid              A grid giving the transport capacity at each grid cell for the transport limited accumulation function.
     * @param Input_Concentration_Grid                   A grid giving the concentration of a compound of interest in the supply to the transport limited accumulation function.
     * @param Input_Outlets                              This optional input is a point feature  defining outlets of interest.
     * @param Check_for_Edge_Contamination               This checkbox determines whether the tool should check for edge contamination.
     * @param Output_Transport_Limited_Accumulation_Grid This grid is the weighted accumulation of supply accumulated respecting the limitations in transport capacity                                                   and reports the transport rate calculated by accumulating the substance flux subject to the rule that the transport out of any grid cell is the minimum of the total supply (local supply plus transport in) to that grid cell and the transport capacity.
     * @param Output_Deposition_Grid                     A grid giving the deposition resulting from the transport limited accumulation.
     * @param Output_Concentration_Grid                  If an input concentation in supply grid is given, then this grid is also output and gives the concentration of a compound (contaminant) adhered or bound to the transported substance (e.
     * @param layerName                                  the layer name
     * @param layerNumber                                the layer number
     * @param inputDir                                   the input dir
     * @param outputDir                                  the output dir
     * @return true if succeeded otherwise false
     */
    public static ExecResult DInfinityTransportLimitedAccumulation(@NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                                   @NotBlank String Input_Supply_Grid,
                                                                   @NotBlank String Input_Transport_Capacity_Grid,
                                                                   String Input_Concentration_Grid,
                                                                   String Input_Outlets,
                                                                   Boolean Check_for_Edge_Contamination,
                                                                   String Output_Transport_Limited_Accumulation_Grid,
                                                                   String Output_Deposition_Grid,
                                                                   String Output_Concentration_Grid, String layerName,
                                                                   Integer layerNumber, String inputDir,
                                                                   String outputDir)
    {

        Map files = new LinkedHashMap(5);
        Map outFiles = new LinkedHashMap(3);
        Map params = new LinkedHashMap();

        // A grid giving flow direction by the D-infinity method.
        files.put("-ang", Input_DInfinity_Flow_Direction_Grid);
        // A grid giving the supply (loading) of material to a transport limited accumulation function.
        files.put("-tsup", Input_Supply_Grid);
        // A grid giving the transport capacity at each grid cell for the transport limited accumulation function.
        files.put("-tc", Input_Transport_Capacity_Grid);
        // A grid giving the concentration of a compound of interest in the supply to the transport limited accumulation function.
        files.put("-cs", Input_Concentration_Grid);
        // This optional input is a point feature  defining outlets of interest.
        files.put("-o", Input_Outlets);
        // This checkbox determines whether the tool should check for edge contamination.
        params.put("-nc", Check_for_Edge_Contamination);
        // This grid is the weighted accumulation of supply accumulated respecting the limitations in transport capacity and reports the transport rate calculated by accumulating the substance flux subject to the rule that the transport out of any grid cell is the minimum of the total supply (local supply plus transport in) to that grid cell and the transport capacity.
        if (StringUtils.isBlank(Output_Transport_Limited_Accumulation_Grid)) {
            Output_Transport_Limited_Accumulation_Grid = outputNaming(Input_DInfinity_Flow_Direction_Grid,
                                                                      Output_Transport_Limited_Accumulation_Grid,
                                                                      "Output_Transport_Limited_Accumulation_Grid",
                                                                      "Raster Dataset");
        }
        outFiles.put("-tla", Output_Transport_Limited_Accumulation_Grid);
        // A grid giving the deposition resulting from the transport limited accumulation.
        if (StringUtils.isBlank(Output_Deposition_Grid)) {
            Output_Deposition_Grid = outputNaming(Input_DInfinity_Flow_Direction_Grid, Output_Deposition_Grid,
                                                  "Output_Deposition_Grid", "Raster Dataset");
        }
        outFiles.put("-tdep", Output_Deposition_Grid);
        // If an input concentation in supply grid is given, then this grid is also output and gives the concentration of a compound (contaminant) adhered or bound to the transported substance (e.
        if (StringUtils.isBlank(Output_Concentration_Grid)) {
            Output_Concentration_Grid = outputNaming(Input_DInfinity_Flow_Direction_Grid, Output_Concentration_Grid,
                                                     "Output_Concentration_Grid", "Raster Dataset");
        }
        outFiles.put("-ctpt", Output_Concentration_Grid);
        params = layerNameAndNumber(params, layerName, layerNumber);

        return ourInstance.runCommand(TauDEMCommands.D_INFINITY_TRANSPORT_LIMITED_ACCUMULATION, files, outFiles, params,
                                      inputDir, outputDir);
    }

    // endregion


    // region  D-Infinity Upslope Dependence

    /**
     * DInfinityUpslopeDependence
     *
     * @param Input_DInfinity_Flow_Direction_Grid the input d infinity flow direction grid
     * @param Input_Destination_Grid              the input destination grid
     * @param outputDir                           the output dir
     * @return the exec result
     * @see  #DInfinityUpslopeDependence(String, String, String, String, String)
     */
    public static ExecResult DInfinityUpslopeDependence(@NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                        @NotBlank String Input_Destination_Grid,
                                                        String outputDir)
    {

        return DInfinityUpslopeDependence(Input_DInfinity_Flow_Direction_Grid,
                                          Input_Destination_Grid,
                                          null, null, outputDir);
    }


    /**
     * DInfinityUpslopeDependence
     * <p> The D-Infinity Upslope Dependence tool quantifies the amount each grid cell in the domain contributes to a destination set of grid cells.
     *
     * @param Input_DInfinity_Flow_Direction_Grid A grid giving flow direction by the D-Infinity method where the flow direction angle is determined as the direction of the steepest downward slope on the eight triangular facets formed in a 3 x 3 grid cell window centered on the grid cell of interest.
     * @param Input_Destination_Grid              A grid that encodes the destination zone that may receive flow from upslope.
     * @param Output_Upslope_Dependence_Grid      A grid quantifing the amount each source point in the domain contributes to the zone defined by the destination grid.
     * @param inputDir                            the input dir
     * @param outputDir                           the output dir
     * @return true if succeeded otherwise false
     */
    public static ExecResult DInfinityUpslopeDependence(@NotBlank String Input_DInfinity_Flow_Direction_Grid,
                                                        @NotBlank String Input_Destination_Grid,
                                                        String Output_Upslope_Dependence_Grid, String inputDir,
                                                        String outputDir)
    {

        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // A grid giving flow direction by the D-Infinity method where the flow direction angle is determined as the direction of the steepest downward slope on the eight triangular facets formed in a 3 x 3 grid cell window centered on the grid cell of interest.
        files.put("-ang", Input_DInfinity_Flow_Direction_Grid);
        // A grid that encodes the destination zone that may receive flow from upslope.
        files.put("-dg", Input_Destination_Grid);
        // A grid quantifing the amount each source point in the domain contributes to the zone defined by the destination grid.
        if (StringUtils.isBlank(Output_Upslope_Dependence_Grid)) {
            Output_Upslope_Dependence_Grid = outputNaming(Input_DInfinity_Flow_Direction_Grid,
                                                          Output_Upslope_Dependence_Grid,
                                                          "Output_Upslope_Dependence_Grid", "Raster Dataset");
        }
        outFiles.put("-dep", Output_Upslope_Dependence_Grid);
        return ourInstance.runCommand(TauDEMCommands.D_INFINITY_UPSLOPE_DEPENDENCE, files, outFiles, params, inputDir,
                                      outputDir);
    }
    // endregion


    // region   D8 Distance To Streams

    /**
     * D8DistanceToStreams
     *
     * @param Input_D8_Flow_Direction_Grid the input d 8 flow direction grid
     * @param Input_Stream_Raster_Grid     the input stream raster grid
     * @param outputDir                    the output dir
     * @return the exec result
     * @see  #D8DistanceToStreams(String, String, Integer, String, String, String)
     */
    public static ExecResult D8DistanceToStreams(@NotBlank String Input_D8_Flow_Direction_Grid,
                                                 @NotBlank String Input_Stream_Raster_Grid,
                                                 String outputDir)
    {
        return D8DistanceToStreams(Input_D8_Flow_Direction_Grid,
                                   Input_Stream_Raster_Grid, 1,
                                   null, null, outputDir);
    }

    /**
     * D8DistanceToStreams
     *
     * @param Input_D8_Flow_Direction_Grid    the input d 8 flow direction grid
     * @param Input_Stream_Raster_Grid        the input stream raster grid
     * @param Output_Distance_to_Streams_Grid the output distance to streams grid
     * @param inputDir                        the input dir
     * @param outputDir                       the output dir
     * @return the exec result
     * @see #D8DistanceToStreams(String, String, Integer, String, String, String)
     */
    public static ExecResult D8DistanceToStreams(@NotBlank String Input_D8_Flow_Direction_Grid,
                                                 @NotBlank String Input_Stream_Raster_Grid,
                                                 String Output_Distance_to_Streams_Grid, String inputDir,
                                                 String outputDir)
    {
        return D8DistanceToStreams(Input_D8_Flow_Direction_Grid,
                                   Input_Stream_Raster_Grid, 1,
                                   Output_Distance_to_Streams_Grid, inputDir, outputDir);
    }

    /**
     * D8DistanceToStreams
     * <p> Computes the horizontal distance to stream for each grid cell, moving downslope according to the D8 flow model, until a stream grid cell is encountered.
     *
     * @param Input_D8_Flow_Direction_Grid    This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
     * @param Input_Stream_Raster_Grid        A grid indicating streams.
     * @param Threshold                       This value acts as threshold on the Stream Raster Grid to determine the location of streams.
     * @param Output_Distance_to_Streams_Grid A grid giving the horizontal distance along the flow path as defined by the D8 Flow Directions Grid to the streams in the Stream Raster Grid.
     * @param inputDir                        the input dir
     * @param outputDir                       the output dir
     * @return true if succeeded otherwise false
     */
    public static ExecResult D8DistanceToStreams(@NotBlank String Input_D8_Flow_Direction_Grid,
                                                 @NotBlank String Input_Stream_Raster_Grid, Integer Threshold,
                                                 String Output_Distance_to_Streams_Grid, String inputDir,
                                                 String outputDir)
    {

        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
        files.put("-p", Input_D8_Flow_Direction_Grid);
        // A grid indicating streams.
        files.put("-src", Input_Stream_Raster_Grid);
        // This value acts as threshold on the Stream Raster Grid to determine the location of streams.
        params.put("-thresh", Threshold);
        // A grid giving the horizontal distance along the flow path as defined by the D8 Flow Directions Grid to the streams in the Stream Raster Grid.
        if (StringUtils.isBlank(Output_Distance_to_Streams_Grid)) {
            Output_Distance_to_Streams_Grid = outputNaming(Input_D8_Flow_Direction_Grid,
                                                           Output_Distance_to_Streams_Grid,
                                                           "Output_Distance_to_Streams_Grid", "Raster Dataset");
        }
        outFiles.put("-dist", Output_Distance_to_Streams_Grid);
        return ourInstance.runCommand(TauDEMCommands.D8_DISTANCE_TO_STREAMS, files, outFiles, params, inputDir,
                                      outputDir);
    }

    // endregion


    // region   Slope Average Down

    /**
     * SlopeAverageDown
     *
     * @param Input_D8_Flow_Direction_Grid    the input d 8 flow direction grid
     * @param Input_Pit_Filled_Elevation_Grid the input pit filled elevation grid
     * @param outputDir                       the output dir
     * @return the exec result
     * @see #SlopeAverageDown(String, String, Double, String, String, String)
     */
    public static ExecResult SlopeAverageDown(@NotBlank String Input_D8_Flow_Direction_Grid,
                                              @NotBlank String Input_Pit_Filled_Elevation_Grid,
                                              String outputDir)
    {

        return SlopeAverageDown(Input_D8_Flow_Direction_Grid,
                                Input_Pit_Filled_Elevation_Grid, null,
                                null, outputDir);

    }


    /**
     * SlopeAverageDown
     *
     * @param Input_D8_Flow_Direction_Grid    the input d 8 flow direction grid
     * @param Input_Pit_Filled_Elevation_Grid the input pit filled elevation grid
     * @param Output_Slope_Average_Down_Grid  the output slope average down grid
     * @param inputDir                        the input dir
     * @param outputDir                       the output dir
     * @return the exec result
     * @see #SlopeAverageDown(String, String, Double, String, String, String)
     */
    public static ExecResult SlopeAverageDown(@NotBlank String Input_D8_Flow_Direction_Grid,
                                              @NotBlank String Input_Pit_Filled_Elevation_Grid,
                                              String Output_Slope_Average_Down_Grid, String inputDir, String outputDir)
    {

        return SlopeAverageDown(Input_D8_Flow_Direction_Grid,
                                Input_Pit_Filled_Elevation_Grid, null,
                                Output_Slope_Average_Down_Grid, inputDir, outputDir);

    }

    /**
     * SlopeAverageDown
     * <p> This tool computes slope in a D8 downslope direction averaged over a user selected distance.
     *
     * @param Input_D8_Flow_Direction_Grid    This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
     * @param Input_Pit_Filled_Elevation_Grid A grid of elevation values.
     * @param Distance                        Input parameter of downslope distance over which to calculate the slope (in horizontal map units).
     * @param Output_Slope_Average_Down_Grid  A grid of the ratio of  specific catchment area (contributing area) to slope.
     * @param inputDir                        the input dir
     * @param outputDir                       the output dir
     * @return true if succeeded otherwise false
     */
    public static ExecResult SlopeAverageDown(@NotBlank String Input_D8_Flow_Direction_Grid,
                                              @NotBlank String Input_Pit_Filled_Elevation_Grid, Double Distance,
                                              String Output_Slope_Average_Down_Grid, String inputDir, String outputDir)
    {

        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
        files.put("-p", Input_D8_Flow_Direction_Grid);
        // A grid of elevation values.
        files.put("-fel", Input_Pit_Filled_Elevation_Grid);
        // Input parameter of downslope distance over which to calculate the slope (in horizontal map units).
        params.put("-dn", Distance);
        // A grid of the ratio of  specific catchment area (contributing area) to slope.
        if (StringUtils.isBlank(Output_Slope_Average_Down_Grid)) {
            Output_Slope_Average_Down_Grid = outputNaming(Input_D8_Flow_Direction_Grid, Output_Slope_Average_Down_Grid,
                                                          "Output_Slope_Average_Down_Grid", "Raster Dataset");
        }
        outFiles.put("-slpd", Output_Slope_Average_Down_Grid);
        return ourInstance.runCommand(TauDEMCommands.SLOPE_AVERAGE_DOWN, files, outFiles, params, inputDir, outputDir);
    }

    // endregion


    // region  Slope Over Area Ratio

    /**
     * SlopeOverAreaRatio
     *
     * @param Input_Slope_Grid                   the input slope grid
     * @param Input_Specific_Catchment_Area_Grid the input specific catchment area grid
     * @param outputDir                          the output dir
     * @return the exec result
     * @see #SlopeOverAreaRatio(String, String, String, String, String)
     */
    public static ExecResult SlopeOverAreaRatio(@NotBlank String Input_Slope_Grid,
                                                @NotBlank String Input_Specific_Catchment_Area_Grid,
                                                String outputDir)
    {
        return SlopeOverAreaRatio(Input_Slope_Grid,
                                  Input_Specific_Catchment_Area_Grid,
                                  null, null, outputDir);
    }

    /**
     * SlopeOverAreaRatio
     * <p> Calculates the ratio of the slope to the specific catchment area (contributing area).
     *
     * @param Input_Slope_Grid                        A grid of slope.
     * @param Input_Specific_Catchment_Area_Grid      A grid giving the contributing area value for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it.
     * @param Output_Slope_Divided_By_Area_Ratio_Grid A grid of the ratio of slope to specific catchment area (contributing area).
     * @param inputDir                                the input dir
     * @param outputDir                               the output dir
     * @return true if succeeded otherwise false
     */
    public static ExecResult SlopeOverAreaRatio(@NotBlank String Input_Slope_Grid,
                                                @NotBlank String Input_Specific_Catchment_Area_Grid,
                                                String Output_Slope_Divided_By_Area_Ratio_Grid, String inputDir,
                                                String outputDir)
    {

        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // A grid of slope.
        files.put("-slp", Input_Slope_Grid);
        // A grid giving the contributing area value for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it.
        files.put("-sca", Input_Specific_Catchment_Area_Grid);
        // A grid of the ratio of slope to specific catchment area (contributing area).
        if (StringUtils.isBlank(Output_Slope_Divided_By_Area_Ratio_Grid)) {
            Output_Slope_Divided_By_Area_Ratio_Grid = outputNaming(Input_Slope_Grid,
                                                                   Output_Slope_Divided_By_Area_Ratio_Grid,
                                                                   "Output_Slope_Divided_By_Area_Ratio_Grid",
                                                                   "Raster Dataset");
        }
        outFiles.put("-sar", Output_Slope_Divided_By_Area_Ratio_Grid);
        return ourInstance.runCommand(TauDEMCommands.SLOPE_OVER_AREA_RATIO, files, outFiles, params, inputDir,
                                      outputDir);
    }

    // endregion


    // region  Topographic Wetness Index

    /**
     * TopographicWetnessIndex
     *
     * @param Input_Specific_Catchment_Area_Grid the input specific catchment area grid
     * @param Input_Slope_Grid                   the input slope grid
     * @param outputDir                          the output dir
     * @return the exec result
     * @see #TopographicWetnessIndex(String, String, String, String, String)
     */
    public static ExecResult TopographicWetnessIndex(@NotBlank String Input_Specific_Catchment_Area_Grid,
                                                     @NotBlank String Input_Slope_Grid,
                                                     String outputDir)
    {

        return TopographicWetnessIndex(Input_Specific_Catchment_Area_Grid, Input_Slope_Grid,
                                       null, null, outputDir);
    }

    /**
     * TopographicWetnessIndex
     * <p> Calculates the ratio of the natural log of the specific catchment area (contributing area) to slope, ln(a/S), or ln(a/tan (beta)).
     *
     * @param Input_Specific_Catchment_Area_Grid A grid of specific catchment area which is the contributing area per unit contour length.                                           If D8 or some other contributing area evaluated using the number of cells is used here                                           it should first be scaled by cell size to give specific catchment area in length units.
     * @param Input_Slope_Grid                   A grid of slope.
     * @param Output_Wetness_Index_Grid          A grid of the natural log of the ratio of specific catchment area (contributing area) to slope, ln(a/S).
     * @param inputDir                           the input dir
     * @param outputDir                          the output dir
     * @return true if succeeded otherwise false
     */
    public static ExecResult TopographicWetnessIndex(@NotBlank String Input_Specific_Catchment_Area_Grid,
                                                     @NotBlank String Input_Slope_Grid,
                                                     String Output_Wetness_Index_Grid,
                                                     String inputDir, String outputDir)
    {

        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        Map params = new LinkedHashMap();

        // A grid of specific catchment area which is the contributing area per unit contour length.
        files.put("-sca", Input_Specific_Catchment_Area_Grid);
        // A grid of slope.
        files.put("-slp", Input_Slope_Grid);
        // A grid of the natural log of the ratio of specific catchment area (contributing area) to slope, ln(a/S).
        if (StringUtils.isBlank(Output_Wetness_Index_Grid)) {
            Output_Wetness_Index_Grid = outputNaming(Input_Specific_Catchment_Area_Grid, Output_Wetness_Index_Grid,
                                                     "Output_Wetness_Index_Grid", "Raster Dataset");
        }
        outFiles.put("-twi", Output_Wetness_Index_Grid);
        return ourInstance.runCommand(TauDEMCommands.TWI, files, outFiles, params, inputDir, outputDir);
    }

    // endregion

}
