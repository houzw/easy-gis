package org.egc.gis.taudem;

import com.google.common.collect.LinkedHashMultimap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.ExecResult;
import org.egc.gis.taudem.params.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * <p>TauDEMAnalysis
 * @author houzhiwei
 * @date 2018-11-26T16:43:28+08:00
 */
@Slf4j
public class TauDEMAnalysis extends BaseTauDEM{

    //region initialize
    /**
    * SingletonHolder is loaded on the first execution of Singleton.getInstance()
    * or the first access to SingletonHolder.INSTANCE, not before.
    * <p>此实现方式可保证単例的延迟加载和线程安全</p>
    */
    private static class SingletonHolder {
        private static final TauDEMAnalysis INSTANCE = new TauDEMAnalysis();
    }

    public static TauDEMAnalysis getInstance() {  return SingletonHolder.INSTANCE; }

    private TauDEMAnalysis() {}

    private static String WORKSPACE;

    /**
    * initialize workspace/output directory
    */
    public void init(String workspace) { WORKSPACE = workspace;}

    //endregion

    /*******************************************************************************************************
     *                                    Stream Network Analysis                                          *
     *******************************************************************************************************/

    //region Stream Network Analysis
    /**
    * PeukerDouglas
    * <p> Creates an indicator grid (1,0) of upward curved grid cells according to the Peuker and Douglas algorithm.
    * @param params {@link PeukerDouglasParams}
    * @return {@link ExecResult}
    */
    public ExecResult PeukerDouglas(PeukerDouglasParams params){
        log.debug("Start PeukerDouglas...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A grid of elevation values.
        files.put("-fel", params.getInput_Elevation_Grid());
        // The center weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
        extraParams.put("-par", params.getCenter_Smoothing_Weight());
        // The side weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
        extraParams.put("-par", params.getSide_Smoothing_Weight());
        // The diagonal weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
        extraParams.put("-par", params.getDiagonal_Smoothing_Weight());
        // An indicator grid (1,0) of upward curved grid cells according to the Peuker and Douglas algorithm, and if viewed, resembles a channel network.
        outFiles.put("-ss", params.getOutput_Stream_Source_Grid());
        ExecResult result = run("PeukerDouglas", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("PeukerDouglas finished");
        }else{
            log.error("PeukerDouglas failed!");
        }
        return result;
    }
    /**
    * MoveOutletsToStreams
    * <p> Moves outlet points that are not aligned with a stream cell from a stream raster grid, downslope along the D8 flow direction until a stream raster cell is encountered, the max_dist number of grid cells are examined, or the flow path exits the domain (i.
    * @param params {@link MoveOutletsToStreamsParams}
    * @return {@link ExecResult}
    */
    public ExecResult MoveOutletsToStreams(MoveOutletsToStreamsParams params){
        log.debug("Start MoveOutletsToStreams...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
        files.put("-p", params.getInput_D8_Flow_Direction_Grid());
        // This output is an indicator grid (1,0) that indicates the location of streams, with a value of 1 for each of the stream cells and 0 for the remainder of the cells.
        files.put("-src", params.getInput_Stream_Raster_Grid());
        // A point feature defining points of interest or outlets that should ideally be located on a stream, but may not be exactly on the stream due to the fact that the feature point locations may not have been accurately registered with respect to the stream raster grid.
        files.put("-o", params.getInput_Outlets());
        // This input paramater is the maximum number of grid cells that the points in the input outlet feature  will be moved before they are saved to the output outlet feature.
        extraParams.put("-md", params.getInput_Maximum_Distance());
        // A point  OGR file defining points of interest or outlets.
        outFiles.put("-o", params.getOutput_Outlets_file());
        ExecResult result = run("MoveOutletsToStreams", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("MoveOutletsToStreams finished");
        }else{
            log.error("MoveOutletsToStreams failed!");
        }
        return result;
    }
    /**
    * SlopeAreaCombination
    * <p> Creates a grid of slope-area values = (S^m)(A^n) based on slope and specific catchment area grid inputs, and parameters m and n.
    * @param params {@link SlopeAreaCombinationParams}
    * @return {@link ExecResult}
    */
    public ExecResult SlopeAreaCombination(SlopeAreaCombinationParams params){
        log.debug("Start SlopeAreaCombination...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // This input is a grid of slope values.
        files.put("-slp", params.getInput_Slope_Grid());
        // A grid giving the specific catchment area for each cell taken as its own contribution (grid cell length or summation of weights) plus the proportional contribution from upslope neighbors that drain in to it.
        files.put("-sca", params.getInput_Area_Grid());
        // The slope exponent (m) parameter which will be used in the formula: (S^m)(A^n), that is used to create the slope-area grid.
        extraParams.put("-par", params.getSlope_Exponent_m());
        // The area exponent (n) parameter which will be used in the formula: (S^m)(A^n), that is used to create the slope-area grid.
        extraParams.put("-par", params.getArea_Exponent_n());
        // A grid of slope-area values = (S^m)(A^n) calculated from the slope grid, specific catchment area grid, m slope exponent parameter, and n area exponent parameter.
        outFiles.put("-sa", params.getOutput_Slope_Area_Grid());
        ExecResult result = run("SlopeArea", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("SlopeAreaCombination finished");
        }else{
            log.error("SlopeAreaCombination failed!");
        }
        return result;
    }
    /**
    * StreamDefinitionByThreshold
    * <p> Operates on any grid and outputs an indicator (1,0) grid identifing cells with input values >= the threshold value.
    * @param params {@link StreamDefinitionByThresholdParams}
    * @return {@link ExecResult}
    */
    public ExecResult StreamDefinitionByThreshold(StreamDefinitionByThresholdParams params){
        log.debug("Start StreamDefinitionByThreshold...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // This grid nominally accumulates some characteristic or combination of characteristics of the watershed.
        files.put("-ssa", params.getInput_Accumulated_Stream_Source_Grid());
        // This optional input is a grid that is used to mask the domain of interest and output is only provided where this grid is >= 0.
        files.put("-mask", params.getInput_Mask_Grid());
        // This parameter is compared to the value in the Accumulated Stream Source grid (*ssa) to determine if the cell should be considered a stream cell.
        extraParams.put("-thresh", params.getThreshold());
        // This is an indicator grid (1,0) that indicates the location of streams, with a value of 1 for each of the stream cells and 0 for the remainder of the cells.
        outFiles.put("-src", params.getOutput_Stream_Raster_Grid());
        ExecResult result = run("Threshold", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("StreamDefinitionByThreshold finished");
        }else{
            log.error("StreamDefinitionByThreshold failed!");
        }
        return result;
    }
    /**
    * LengthAreaStreamSource
    * <p> Creates an indicator grid (1,0) that evaluates A >= (M)(L^y) based on upslope path length, D8 contributing area grid inputs, and parameters M and y.
    * @param params {@link LengthAreaStreamSourceParams}
    * @return {@link ExecResult}
    */
    public ExecResult LengthAreaStreamSource(LengthAreaStreamSourceParams params){
        log.debug("Start LengthAreaStreamSource...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A grid of the maximum upslope length for each cell.
        files.put("-plen", params.getInput_Length_Grid());
        // A grid of contributing area values for each cell that were calculated using the D8 algorithm.
        files.put("-ad8", params.getInput_Contributing_Area_Grid());
        // The multiplier threshold (M) parameter which is used in the formula: A > (M)(L^y), to identify the beginning of streams.
        extraParams.put("-par", params.getThreshold_M());
        // The exponent (y) parameter which is used in the formula: A > (M)(L^y), to identify the beginning of streams.
        extraParams.put("-par", params.getExponent_y());
        // An indicator grid (1,0) that evaluates A >= (M)(L^y), based on the maximum upslope path length, the D8 contributing area grid inputs, and parameters M and y.
        outFiles.put("-ss", params.getOutput_Stream_Source_Grid());
        ExecResult result = run("LengthArea", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("LengthAreaStreamSource finished");
        }else{
            log.error("LengthAreaStreamSource failed!");
        }
        return result;
    }
    /**
    * StreamDropAnalysis
    * <p> Applies a series of thresholds (determined from the input parameters) to the input accumulated stream source grid (*ssa) grid and outputs the results in the *drp.
    * @param params {@link StreamDropAnalysisParams}
    * @return {@link ExecResult}
    */
    public ExecResult StreamDropAnalysis(StreamDropAnalysisParams params){
        log.debug("Start StreamDropAnalysis...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A grid of elevation values.
        files.put("-fel", params.getInput_Pit_Filled_Elevation_Grid());
        // A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        files.put("-p", params.getInput_D8_Flow_Direction_Grid());
        // A grid of contributing area values for each cell that were calculated using the D8 algorithm.
        files.put("-ad8", params.getInput_D8_Contributing_Area_Grid());
        // This grid must be monotonically increasing along the downslope D8 flow directions.
        files.put("-ssa", params.getInput_Accumulated_Stream_Source_Grid());
        // A point feature defining the outlets upstream of which drop analysis is performed.
        files.put("-o", params.getInput_Outlets());
        // This parameter is the lowest end of the range searched for possible threshold values using drop analysis.
        extraParams.put("-par", params.getMinimum_Threshold_Value());
        // This parameter is the highest end of the range searched for possible threshold values using drop analysis.
        extraParams.put("-par", params.getMaximum_Threshold_Value());
        // The parameter is the number of steps to divide the search range into when looking for possible threshold values using drop analysis.
        extraParams.put("-par", params.getNumber_of_Threshold_Values());
        // This checkbox indicates whether logarithmic or linear spacing should be used when looking for possible threshold values using drop ananlysis.
        extraParams.put("-l", params.getUse_logarithmic_spacing_for_threshold_values());
        // This is a comma delimited text file with the following header line: Threshold, DrainDen, NoFirstOrd, NoHighOrd, MeanDFirstOrd, MeanDHighOrd, StdDevFirstOrd, StdDevHighOrd, T The file then contains one line of data for each threshold value examined, and then a summary line that indicates the optimum threshold value.
        outFiles.put("-drp", params.getOutput_Drop_Analysis_Text_File());
        ExecResult result = run("DropAnalysis", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("StreamDropAnalysis finished");
        }else{
            log.error("StreamDropAnalysis failed!");
        }
        return result;
    }
    /**
    * StreamReachAndWatershed
    * <p> This tool produces a vector network and OGR file from the stream raster grid.
    * @param params {@link StreamReachAndWatershedParams}
    * @return {@link ExecResult}
    */
    public ExecResult StreamReachAndWatershed(StreamReachAndWatershedParams params){
        log.debug("Start StreamReachAndWatershed...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // This input is a grid of elevation values.
        files.put("-fel", params.getInput_Pit_Filled_Elevation_Grid());
        // This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
        files.put("-p", params.getInput_D8_Flow_Direction_Grid());
        // A grid giving the contributing area value in terms of the number of grid cells (or the summation of weights) for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it using the D8 algorithm.
        files.put("-ad8", params.getInput_D8_Drainage_Area());
        // An indicator grid indicating streams, by using a grid cell value of 1 on streams and 0 off streams.
        files.put("-src", params.getInput_Stream_Raster_Grid());
        // A point feature defining points of interest.
        files.put("-o", params.getInput_Outlets());
        // This option causes the tool to delineate a single watershed by representing the entire area draining to the Stream Network as a single value in the output watershed grid.
        extraParams.put("-sw", params.getDelineate_Single_Watershed());
        // The Stream Order Grid has cells values of streams ordered according to the Strahler order system.
        outFiles.put("-ord", params.getOutput_Stream_Order_Grid());
        // This output is a text file that details the network topological connectivity is stored in the Stream Network Tree file.
        outFiles.put("-tree", params.getOutput_Network_Connectivity_Tree());
        // This output is a text file that contains the coordinates and attributes of points along the stream network.
        outFiles.put("-coord", params.getOutput_Network_Coordinates());
        // This output is a polyline OGR file giving the links in a stream network.
        outFiles.put("-net", params.getOutput_Stream_Reach_file());
        // This output grid identified each reach watershed with a unique ID number, or in the case where the delineate single watershed option was checked, the entire area draining to the stream network is identified with a single ID.
        outFiles.put("-w", params.getOutput_Watershed_Grid());
        ExecResult result = run("StreamNet", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("StreamReachAndWatershed finished");
        }else{
            log.error("StreamReachAndWatershed failed!");
        }
        return result;
    }
    /**
    * GageWatershed
    * <p> Calculates Gage watersheds grid.
    * @param params {@link GageWatershedParams}
    * @return {@link ExecResult}
    */
    public ExecResult GageWatershed(GageWatershedParams params){
        log.debug("Start GageWatershed...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        files.put("-p", params.getInput_D8_Flow_Direction_Grid());
        // A point feature defining the gages(outlets) to which watersheds will be delineated.
        files.put("-o", params.getInput_Gages_file());
        // This output grid identifies each gage watershed.
        outFiles.put("-gw", params.getOutput_GageWatershed());
        // 
        outFiles.put("-id", params.getOutput_Downstream_Identefier());
        ExecResult result = run("GageWatershed", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("GageWatershed finished");
        }else{
            log.error("GageWatershed failed!");
        }
        return result;
    }
    /**
    * D8ExtremeUpslopeValue
    * <p> Evaluates the extreme (either maximum or minimum) upslope value from an input grid based on the D8 flow model.
    * @param params {@link D8ExtremeUpslopeValueParams}
    * @return {@link ExecResult}
    */
    public ExecResult D8ExtremeUpslopeValue(D8ExtremeUpslopeValueParams params){
        log.debug("Start D8ExtremeUpslopeValue...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        files.put("-p", params.getInput_D8_Flow_Direction_Grid());
        // This is the grid of values of which the maximum or minimum upslope value is selected.
        files.put("-sa", params.getInput_Value_Grid());
        // A flag to indicate whether the maximum or minimum upslope value is to be calculated.
        extraParams.put("-max", params.getUse_maximum_upslope_value());
        // A flag that indicates whether the tool should check for edge contamination.
        extraParams.put("-nc", params.getCheck_for_edge_contamination());
        // A point feature defining outlets of interest.
        files.put("-o", params.getInput_Outlets());
        // A grid of the maximum/minimum upslope values.
        outFiles.put("-ssa", params.getOutput_Extreme_Value_Grid());
        ExecResult result = run("D8FlowPathExtremeUp", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("D8ExtremeUpslopeValue finished");
        }else{
            log.error("D8ExtremeUpslopeValue failed!");
        }
        return result;
    }
    /**
    * ConnectDown
    * <p> For each zone in a raster entered (e.
    * @param params {@link ConnectDownParams}
    * @return {@link ExecResult}
    */
    public ExecResult ConnectDown(ConnectDownParams params){
        log.debug("Start ConnectDown...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
        files.put("-p", params.getInput_D8_Flow_Direction_Grid());
        // A grid giving the contributing area value in terms of the number of grid cells (or the summation of weights) for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it using the D8 algorithm.
        files.put("-ad8", params.getInput_D8_Contributing_Area_Grid());
        // Watershed grid delineated from gage watershed function or streamreachwatershed function.
        files.put("-w", params.getInput_Watershed_Grid());
        // Number of grid cells move to downstream based on flow directions.
        extraParams.put("-d", params.getInput_Number_of_Grid_Cells());
        // This output is point a OGR file where each point is created from watershed grid having the largest contributing area for each zone.
        outFiles.put("-o", params.getOutput_Outlets_file());
        // This output is a point OGR file where each outlet is moved downflow a specified number of grid cells using flow directions.
        outFiles.put("-od", params.getOutput_MovedOutlets_file());
        ExecResult result = run("ConnectDown", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("ConnectDown finished");
        }else{
            log.error("ConnectDown failed!");
        }
        return result;
    }

    //endregion

    /*******************************************************************************************************
     *                                    Specialized Grid Analysis                                        *
     *******************************************************************************************************/

    //region Specialized Grid Analysis
    /**
    * DInfinityReverseAccumulation
    * <p> This works in a similar way to evaluation of weighted Contributing area, except that the accumulation is by propagating the weight loadings upslope along the reverse of the flow directions to accumulate the quantity of weight loading downslope from each grid cell.
    * @param params {@link DInfinityReverseAccumulationParams}
    * @return {@link ExecResult}
    */
    public ExecResult DInfinityReverseAccumulation(DInfinityReverseAccumulationParams params){
        log.debug("Start DInfinityReverseAccumulation...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A grid giving flow direction by the Dinfinity method.
        files.put("-ang", params.getInput_DInfinity_Flow_Direction_Grid());
        // A grid giving weights (loadings) to be used in the accumulation.
        files.put("-wg", params.getInput_Weight_Grid());
        // The grid giving the result of the "Reverse Accumulation" function.
        outFiles.put("-racc", params.getOutput_Reverse_Accumulation_Grid());
        // The grid giving the maximum of the weight loading grid downslope from each grid cell.
        outFiles.put("-dmax", params.getOutput_Maximum_Downslope_Grid());
        ExecResult result = run("DinfRevAccum", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("DInfinityReverseAccumulation finished");
        }else{
            log.error("DInfinityReverseAccumulation failed!");
        }
        return result;
    }
    /**
    * DInfinityUpslopeDependence
    * <p> The D-Infinity Upslope Dependence tool quantifies the amount each grid cell in the domain contributes to a destination set of grid cells.
    * @param params {@link DInfinityUpslopeDependenceParams}
    * @return {@link ExecResult}
    */
    public ExecResult DInfinityUpslopeDependence(DInfinityUpslopeDependenceParams params){
        log.debug("Start DInfinityUpslopeDependence...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A grid giving flow direction by the D-Infinity method where the flow direction angle is determined as the direction of the steepest downward slope on the eight triangular facets formed in a 3 x 3 grid cell window centered on the grid cell of interest.
        files.put("-ang", params.getInput_DInfinity_Flow_Direction_Grid());
        // A grid that encodes the destination zone that may receive flow from upslope.
        files.put("-dg", params.getInput_Destination_Grid());
        // A grid quantifing the amount each source point in the domain contributes to the zone defined by the destination grid.
        outFiles.put("-dep", params.getOutput_Upslope_Dependence_Grid());
        ExecResult result = run("DinfUpDependence", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("DInfinityUpslopeDependence finished");
        }else{
            log.error("DInfinityUpslopeDependence failed!");
        }
        return result;
    }
    /**
    * D8DistanceToStreams
    * <p> Computes the horizontal distance to stream for each grid cell, moving downslope according to the D8 flow model, until a stream grid cell is encountered.
    * @param params {@link D8DistanceToStreamsParams}
    * @return {@link ExecResult}
    */
    public ExecResult D8DistanceToStreams(D8DistanceToStreamsParams params){
        log.debug("Start D8DistanceToStreams...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
        files.put("-p", params.getInput_D8_Flow_Direction_Grid());
        // A grid indicating streams.
        files.put("-src", params.getInput_Stream_Raster_Grid());
        // This value acts as threshold on the Stream Raster Grid to determine the location of streams.
        extraParams.put("-thresh", params.getThreshold());
        // A grid giving the horizontal distance along the flow path as defined by the D8 Flow Directions Grid to the streams in the Stream Raster Grid.
        outFiles.put("-dist", params.getOutput_Distance_to_Streams_Grid());
        ExecResult result = run("D8HDistTostrm", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("D8DistanceToStreams finished");
        }else{
            log.error("D8DistanceToStreams failed!");
        }
        return result;
    }
    /**
    * TopographicWetnessIndex
    * <p> Calculates the ratio of the natural log of the specific catchment area (contributing area) to slope, ln(a/S), or ln(a/tan (beta)).
    * @param params {@link TopographicWetnessIndexParams}
    * @return {@link ExecResult}
    */
    public ExecResult TopographicWetnessIndex(TopographicWetnessIndexParams params){
        log.debug("Start TopographicWetnessIndex...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A grid of specific catchment area which is the contributing area per unit contour length.
        files.put("-sca", params.getInput_Specific_Catchment_Area_Grid());
        // A grid of slope.
        files.put("-slp", params.getInput_Slope_Grid());
        // A grid of the natural log of the ratio of specific catchment area (contributing area) to slope, ln(a/S).
        outFiles.put("-twi", params.getOutput_Wetness_Index_Grid());
        ExecResult result = run("TWI", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("TopographicWetnessIndex finished");
        }else{
            log.error("TopographicWetnessIndex failed!");
        }
        return result;
    }
    /**
    * SlopeOverAreaRatio
    * <p> Calculates the ratio of the slope to the specific catchment area (contributing area).
    * @param params {@link SlopeOverAreaRatioParams}
    * @return {@link ExecResult}
    */
    public ExecResult SlopeOverAreaRatio(SlopeOverAreaRatioParams params){
        log.debug("Start SlopeOverAreaRatio...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A grid of slope.
        files.put("-slp", params.getInput_Slope_Grid());
        // A grid giving the contributing area value for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it.
        files.put("-sca", params.getInput_Specific_Catchment_Area_Grid());
        // A grid of the ratio of slope to specific catchment area (contributing area).
        outFiles.put("-sar", params.getOutput_Slope_Divided_By_Area_Ratio_Grid());
        ExecResult result = run("SlopeAreaRatio", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("SlopeOverAreaRatio finished");
        }else{
            log.error("SlopeOverAreaRatio failed!");
        }
        return result;
    }
    /**
    * DInfinityTransportLimitedAccumulation
    * <p> This function is designed to calculate the transport and deposition of a substance (e.
    * @param params {@link DInfinityTransportLimitedAccumulationParams}
    * @return {@link ExecResult}
    */
    public ExecResult DInfinityTransportLimitedAccumulation(DInfinityTransportLimitedAccumulationParams params){
        log.debug("Start DInfinityTransportLimitedAccumulation...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A grid giving flow direction by the D-infinity method.
        files.put("-ang", params.getInput_DInfinity_Flow_Direction_Grid());
        // A grid giving the supply (loading) of material to a transport limited accumulation function.
        files.put("-tsup", params.getInput_Supply_Grid());
        // A grid giving the transport capacity at each grid cell for the transport limited accumulation function.
        files.put("-tc", params.getInput_Transport_Capacity_Grid());
        // A grid giving the concentration of a compound of interest in the supply to the transport limited accumulation function.
        files.put("-cs", params.getInput_Concentration_Grid());
        // This optional input is a point feature  defining outlets of interest.
        files.put("-o", params.getInput_Outlets());
        // This checkbox determines whether the tool should check for edge contamination.
        extraParams.put("-nc", params.getCheck_for_Edge_Contamination());
        // This grid is the weighted accumulation of supply accumulated respecting the limitations in transport capacity and reports the transport rate calculated by accumulating the substance flux subject to the rule that the transport out of any grid cell is the minimum of the total supply (local supply plus transport in) to that grid cell and the transport capacity.
        outFiles.put("-tla", params.getOutput_Transport_Limited_Accumulation_Grid());
        // A grid giving the deposition resulting from the transport limited accumulation.
        outFiles.put("-tdep", params.getOutput_Deposition_Grid());
        // If an input concentation in supply grid is given, then this grid is also output and gives the concentration of a compound (contaminant) adhered or bound to the transported substance (e.
        outFiles.put("-ctpt", params.getOutput_Concentration_Grid());
        ExecResult result = run("> DinfTransLimAccum", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("DInfinityTransportLimitedAccumulation finished");
        }else{
            log.error("DInfinityTransportLimitedAccumulation failed!");
        }
        return result;
    }
    /**
    * SlopeAverageDown
    * <p> This tool computes slope in a D8 downslope direction averaged over a user selected distance.
    * @param params {@link SlopeAverageDownParams}
    * @return {@link ExecResult}
    */
    public ExecResult SlopeAverageDown(SlopeAverageDownParams params){
        log.debug("Start SlopeAverageDown...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
        files.put("-p", params.getInput_D8_Flow_Direction_Grid());
        // A grid of elevation values.
        files.put("-fel", params.getInput_Pit_Filled_Elevation_Grid());
        // Input parameter of downslope distance over which to calculate the slope (in horizontal map units).
        extraParams.put("-dn", params.getDistance());
        // A grid of the ratio of  specific catchment area (contributing area) to slope.
        outFiles.put("-slpd", params.getOutput_Slope_Average_Down_Grid());
        ExecResult result = run("SlopeAveDown", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("SlopeAverageDown finished");
        }else{
            log.error("SlopeAverageDown failed!");
        }
        return result;
    }
    /**
    * DInfinityDistanceDown
    * <p> Calculates the distance downslope to a stream using the D-infinity flow model.
    * @param params {@link DInfinityDistanceDownParams}
    * @return {@link ExecResult}
    */
    public ExecResult DInfinityDistanceDown(DInfinityDistanceDownParams params){
        log.debug("Start DInfinityDistanceDown...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A grid giving flow directions by the D-Infinity method.
        files.put("-ang", params.getInput_DInfinity_Flow_Direction_Grid());
        // This input is a grid of elevation values.
        files.put("-fel", params.getInput_Pit_Filled_Elevation_Grid());
        // A grid indicating streams, by using a grid cell value of 1 on streams and 0 off streams.
        files.put("-src", params.getInput_Stream_Raster_Grid());
        // Alternative values are ave, max, min, where ave for Average, max for Maximum, min for Minimum Statistical method used to calculate the distance down to the stream.
        extraParams.put("-m", params.getStatistical_Method());
        // Alternative values are h, v, p, s, where h for Horizontal, v for Vertical, p for Pythagoras, s for Surface Distance method used to calculate the distance down to the stream.
        extraParams.put("-m", params.getDistance_Method());
        // A flag that determines whether the tool should check for edge contamination.
        extraParams.put("-nc", params.getCheck_for_edge_contamination());
        // A grid giving weights (loadings) to be used in the distance calculation.
        files.put("-wg", params.getInput_Weight_Path_Grid());
        // Creates a grid containing the distance to stream calculated using the D-infinity flow model and the statistical and path methods chosen.
        outFiles.put("-dd", params.getOutput_DInfinity_Drop_to_Stream_Grid());
        ExecResult result = run("DinfDistDown", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("DInfinityDistanceDown finished");
        }else{
            log.error("DInfinityDistanceDown failed!");
        }
        return result;
    }
    /**
    * DInfinityDistanceUp
    * <p> This tool calculates the distance from each grid cell up to the ridge cells along the reverse D-infinity flow directions.
    * @param params {@link DInfinityDistanceUpParams}
    * @return {@link ExecResult}
    */
    public ExecResult DInfinityDistanceUp(DInfinityDistanceUpParams params){
        log.debug("Start DInfinityDistanceUp...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A grid giving flow directions by the D-Infinity method.
        files.put("-ang", params.getInput_DInfinity_Flow_Direction_Grid());
        // This input is a grid of elevation values.
        files.put("-fel", params.getInput_Pit_Filled_Elevation_Grid());
        // This input is a grid of slope values.
        files.put("-slp", params.getInput_Slope_Grid());
        // The proportion threshold parameter where only grid cells that contribute flow with a proportion greater than this user specified threshold (t) is considered to be upslope of any given grid cell.
        extraParams.put("-thresh", params.getInput_Proportion_Threshold());
        // Statistical method used to calculate the distance down to the stream.
        extraParams.put("-m", params.getStatistical_Method());
        // Distance method used to calculate the distance down to the stream.
        extraParams.put("-m", params.getDistance_Method());
        // A flag that determines whether the tool should check for edge contamination.
        extraParams.put("-nc", params.getCheck_for_Edge_Contamination());
        // 
        outFiles.put("-du", params.getOutput_DInfinity_Distance_Up_Grid());
        ExecResult result = run("DinfDistUp", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("DInfinityDistanceUp finished");
        }else{
            log.error("DInfinityDistanceUp failed!");
        }
        return result;
    }
    /**
    * DInfinityConcentrationLimitedAcccumulation
    * <p> This function applies to the situation where an unlimited supply of a substance is loaded into flow at a concentration or solubility threshold Csol over a region indicated by an indicator grid (dg).
    * @param params {@link DInfinityConcentrationLimitedAcccumulationParams}
    * @return {@link ExecResult}
    */
    public ExecResult DInfinityConcentrationLimitedAcccumulation(DInfinityConcentrationLimitedAcccumulationParams params){
        log.debug("Start DInfinityConcentrationLimitedAcccumulation...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A grid giving flow direction by the D-infinity method.
        files.put("-ang", params.getInput_DInfinity_Flow_Direction_Grid());
        // A grid giving the input quantity (notionally effective runoff or excess precipitation) to be used in the D-infinity weighted contributing area evaluation of Overland Flow Specific Discharge.
        files.put("-wg", params.getInput_Effective_Runoff_Weight_Grid());
        // A grid that indicates the source zone of the area of substance supply and must be 1 inside the zone and 0 or "no data" over the rest of the domain.
        files.put("-dg", params.getInput_Disturbance_Indicator_Grid());
        // A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
        files.put("-dm", params.getInput_Decay_Multiplier_Grid());
        // This optional input is a point feature  defining outlets of interest.
        files.put("-o", params.getInput_Outlets());
        // The concentration or solubility threshold.
        extraParams.put("-csol", params.getConcentration_Threshold());
        // This checkbox determines whether the tool should check for edge contamination.
        extraParams.put("-nc", params.getCheck_for_Edge_Contamination());
        // The grid giving the specific discharge of the flow carrying the constituent being loaded at the concentration threshold specified.
        outFiles.put("-q", params.getOutput_Overland_Flow_Specific_Discharge_Grid());
        // A grid giving the resulting concentration of the compound of interest in the flow.
        outFiles.put("-ctpt", params.getOutput_Concentration_Grid());
        ExecResult result = run("DinfConcLimAccum", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("DInfinityConcentrationLimitedAcccumulation finished");
        }else{
            log.error("DInfinityConcentrationLimitedAcccumulation failed!");
        }
        return result;
    }

    /**
    * DInfinityDecayingAccumulation
    * <p> The D-Infinity Decaying Accumulation tool creates a grid of the accumulated quantity at each location in the domain where the quantity accumulates with the D-infinity flow field, but is subject to first order decay in moving from cell to cell.
    * @param params {@link DInfinityDecayingAccumulationParams}
    * @return {@link ExecResult}
    */
    public ExecResult DInfinityDecayingAccumulation(DInfinityDecayingAccumulationParams params){
        log.debug("Start DInfinityDecayingAccumulation...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A grid giving flow direction by the D-infinity method.
        files.put("-ang", params.getInput_DInfinity_Flow_Direction_Grid());
        // A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
        files.put("-dm", params.getInput_Decay_Multiplier_Grid());
        // A grid giving weights (loadings) to be used in the accumulation.
        files.put("-wg", params.getInput_Weight_Grid());
        // This optional input is a point feature  defining outlets of interest.
        files.put("-o", params.getInput_Outlets());
        // This checkbox determines whether the tool should check for edge contamination.
        extraParams.put("-nc", params.getCheck_for_Edge_Contamination());
        // The D-Infinity Decaying Accumulation tool creates a grid of the accumulated mass at each location in the domain where mass moves with the D-infinity flow field, but is subject to first order decay in moving from cell to cell.
        outFiles.put("-sca", params.getOutput_Decayed_Specific_Catchment_Area_Grid());
        ExecResult result = run("DinfDecayAccum", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("DInfinityDecayingAccumulation finished");
        }else{
            log.error("DInfinityDecayingAccumulation failed!");
        }
        return result;
    }
    /**
    * DInfinityAvalancheRunout
    * <p> Identifies an avalanche's affected area and the flow path length to each cell in that affacted area.
    * @param params {@link DInfinityAvalancheRunoutParams}
    * @return {@link ExecResult}
    */
    public ExecResult DInfinityAvalancheRunout(DInfinityAvalancheRunoutParams params){
        log.debug("Start DInfinityAvalancheRunout...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A grid of elevation values.
        files.put("-fel", params.getInput_Pit_Filled_Elevation_Grid());
        // A grid giving flow directions by the D-Infinity method.
        files.put("-ang", params.getInput_DInfinity_Flow_Direction_Grid());
        // This is a grid of source areas for snow avalanches that are commonly identified manually using a mix of experience and visual interpretation of maps.
        files.put("-ass", params.getInput_Avalanche_Source_Site_Grid());
        // This value is a threshold proportion that is used to limit the disperson of flow caused by using the D-infinity multiple flow direction method for determining flow direction.
        extraParams.put("-thresh", params.getInput_Proportion_Threshold());
        // This value is the threshold angle, called the Alpha Angle, that is used to determine which of the cells downslope from the source cells are in the affected area.
        extraParams.put("-alpha", params.getInput_Alpha_Angle_Threshold());
        // This option selects the method used to measure the distance used to calculate the slope angle.
        extraParams.put("-direct", params.getPath_Distance_Method());
        // This grid Identifies the avalanche's runout zone (affected area) using a runout zone indicator with value 0 to indicate that this grid cell is not in the runout zone and value > 0 to indicate that this grid cell is in the runout zone.
        outFiles.put("-rz", params.getOutput_Runout_Zone_Grid());
        // This is a grid of the flow distance from the source site that has the highest angle to each cell.
        outFiles.put("-dfs", params.getOutput_Path_Distance_Grid());
        ExecResult result = run("DinfAvalanche", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("DInfinityAvalancheRunout finished");
        }else{
            log.error("DInfinityAvalancheRunout failed!");
        }
        return result;
    }

    //endregion

    /*******************************************************************************************************
     *                                      Basic Grid Analysis                                            *
     *******************************************************************************************************/

    //region Basic Grid Analysis

    /**
    * PitRemove
    * <p> This funciton identifies all pits in the DEM and raises their elevation to the level of the lowest pour point around their edge.
    * @param params {@link PitRemoveParams}
    * @return {@link ExecResult}
    */
    public ExecResult PitRemove(PitRemoveParams params){
        log.debug("Start PitRemove...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A digital elevation model (DEM) grid to serve as the base input for the terrain analysis and stream delineation.
        files.put("-z", params.getInput_Elevation_Grid());
        // If this option is selected Fill ensures that the grid is hydrologically conditioned with cell to cell connectivity in only 4 directions (N, S, E or W neighbors).
        extraParams.put("-4way", params.getFill_Considering_only_4_way_neighbors());
        // 
        files.put("-depmask", params.getInput_Depression_Mask_Grid());
        // A grid of elevation values with pits removed so that flow is routed off of the domain.
        outFiles.put("-fel", params.getOutput_Pit_Removed_Elevation_Grid());
        ExecResult result = run("PitRemove", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("PitRemove finished");
        }else{
            log.error("PitRemove failed!");
        }
        return result;
    }
    /**
    * D8FlowDirections
    * <p> Creates 2 grids.
    * @param params {@link D8FlowDirectionsParams}
    * @return {@link ExecResult}
    */
    public ExecResult D8FlowDirections(D8FlowDirectionsParams params){
        log.debug("Start D8FlowDirections...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A grid of elevation values.
        files.put("-fel", params.getInput_Pit_Filled_Elevation_Grid());
        // A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        outFiles.put("-p", params.getOutput_D8_Flow_Direction_Grid());
        // A grid giving slope in the D8 flow direction.
        outFiles.put("-sd8", params.getOutput_D8_Slope_Grid());
        ExecResult result = run("D8FlowDir", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("D8FlowDirections finished");
        }else{
            log.error("D8FlowDirections failed!");
        }
        return result;
    }
    /**
    * DInfinityFlowDirections
    * <p> Assigns a flow direction based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997, "A New Method for the Determination of Flow Directions and Contributing Areas in Grid Digital Elevation Models," Water Resources Research, 33(2): 309-319).
    * @param params {@link DInfinityFlowDirectionsParams}
    * @return {@link ExecResult}
    */
    public ExecResult DInfinityFlowDirections(DInfinityFlowDirectionsParams params){
        log.debug("Start DInfinityFlowDirections...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A grid of elevation values.
        files.put("-fel", params.getInput_Pit_FIlled_Elevation_Grid());
        // A grid of flow directions based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997, "A New Method for the Determination of Flow Directions and Contributing Areas in Grid Digital Elevation Models," Water Resources Research, 33(2): 309-319).
        outFiles.put("-ang", params.getOutput_DInfinity_Flow_Direction_Grid());
        // A grid of slope evaluated using the D-infinity method described in Tarboton, D.
        outFiles.put("-slp", params.getOutput_DInfinity_Slope_Grid());
        ExecResult result = run("DinfFlowDir", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("DInfinityFlowDirections finished");
        }else{
            log.error("DInfinityFlowDirections failed!");
        }
        return result;
    }
    /**
    * D8ContributingArea
    * <p> 
    * @param params {@link D8ContributingAreaParams}
    * @return {@link ExecResult}
    */
    public ExecResult D8ContributingArea(D8ContributingAreaParams params){
        log.debug("Start D8ContributingArea...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        files.put("-p", params.getInput_D8_Flow_Direction_Grid());
        // A point feature defining the outlets of interest.
        files.put("-o", params.getInput_Outlets());
        // A grid giving contribution to flow for each cell.
        files.put("-wg", params.getInput_Weight_Grid());
        // A flag that indicates whether the tool should check for edge contamination.
        extraParams.put("-nc", params.getCheck_for_edge_contamination());
        // A grid of contributing area values calculated as the cells own contribution plus the contribution from upslope neighbors that drain in to it according to the D8 flow model.
        outFiles.put("-ad8", params.getOutput_D8_Contributing_Area_Grid());
        ExecResult result = run("AreaD8", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("D8ContributingArea finished");
        }else{
            log.error("D8ContributingArea failed!");
        }
        return result;
    }
    /**
    * DInfinityContributingArea
    * <p> Calculates a grid of specific catchment area which is the contributing area per unit contour length using the multiple flow direction D-infinity approach.
    * @param params {@link DInfinityContributingAreaParams}
    * @return {@link ExecResult}
    */
    public ExecResult DInfinityContributingArea(DInfinityContributingAreaParams params){
        log.debug("Start DInfinityContributingArea...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A grid of flow directions based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997, "A New Method for the Determination of Flow Directions and Contributing Areas in Grid Digital Elevation Models," Water Resources Research, 33(2): 309-319).
        files.put("-ang", params.getInput_DInfinity_Flow_Direction_Grid());
        // A point feature defining the outlets of interest.
        files.put("-o", params.getInput_Outlets());
        // A grid giving contribution to flow for each cell.
        files.put("-wg", params.getInput_Weight_Grid());
        // A flag that indicates whether the tool should check for edge contamination.
        extraParams.put("-nc", params.getCheck_for_Edge_Contamination());
        // A grid of specific catchment area which is the contributing area per unit contour length using the multiple flow direction D-infinity approach.
        outFiles.put("-sca", params.getOutput_DInfinity_Specific_Catchment_Area_Grid());
        ExecResult result = run("AreaDinf", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("DInfinityContributingArea finished");
        }else{
            log.error("DInfinityContributingArea failed!");
        }
        return result;
    }
    /**
     * GridNetwork
     * <p> Creates 3 grids that contain for each grid cell: 1) the longest path, 2) the total path, and 3) the Strahler order number.
     * @param params {@link GridNetworkParams}
     * @return {@link ExecResult}
     */
    public ExecResult GridNetwork(GridNetworkParams params){
        log.debug("Start GridNetwork...");
        Map files = new LinkedHashMap();
        Map outFiles = new LinkedHashMap();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(WORKSPACE)) {
            params.setOutputDir(WORKSPACE);
        }
        // A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        files.put("-p", params.getInput_D8_Flow_Direction_Grid());
        // A point feature  defining the outlets of interest.
        files.put("-o", params.getInput_Outlets());
        // A grid that is used to determine the domain do be analyzed.
        files.put("-mask", params.getInput_Mask_Grid());
        // This input parameter is used in the calculation mask grid value >= mask threshold to determine if the grid cell is in the domain to be analyzed.
        extraParams.put("-thresh", params.getInput_Mask_Threshold_Value());
        // A grid giving the Strahler order number for each cell.
        outFiles.put("-gord", params.getOutput_Strahler_Network_Order_Grid());
        // A grid that gives the length of the longest upslope D8 flow path terminating at each grid cell.
        outFiles.put("-plen", params.getOutput_Longest_Upslope_Length_Grid());
        // The total upslope path length is the length of the entire D8 flow grid network upslope of each grid cell.
        outFiles.put("-tlen", params.getOutput_Total_Upslope_Length_Grid());
        ExecResult result = run("Gridnet", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if(result.getSuccess()){
            log.debug("GridNetwork finished");
        }else{
            log.error("GridNetwork failed!");
        }
        return result;
    }

    //endregion
}