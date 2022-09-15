package org.egc.gis.taudem;

import com.google.common.collect.LinkedHashMultimap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.ExecResult;
import org.egc.gis.taudem.params.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>TauDEMAnalysis
 *
 * @author houzhiwei
 * @date 2020-06-28T12:02:30+08:00
 */
@Slf4j
public class TauDEMAnalysis extends BaseTauDEM {

    /**
     * SingletonHolder is loaded on the first execution of Singleton.getInstance()
     * or the first access to SingletonHolder.INSTANCE, not before.
     * <p>此实现方式可保证単例的延迟加载和线程安全</p>
     */
    private static class SingletonHolder {
        private static final TauDEMAnalysis INSTANCE = new TauDEMAnalysis();
    }

    public static TauDEMAnalysis getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private TauDEMAnalysis() {
    }


    /**
     * peukerDouglas
     * <p> Creates an indicator grid (1,0) of upward curved grid cells according to the Peuker and Douglas algorithm. With this tool, the DEM is first smoothed by a kernel with weights at the center, sides, and diagonals. The Peuker and Douglas (1975) method (also explained in Band, 1986), is then used to identify upwardly curving grid cells. This technique flags the entire grid, then examines in a single pass each quadrant of 4 grid cells, and unflags the highest. The remaining flagged cells are deemed 'upwardly curved', and when viewed, resemble a channel network. This proto-channel network generally lacks connectivity and requires thinning, issues that were discussed in detail by Band (1986). Band, L. E., (1986), "Topographic partition of watersheds with digital elevation models," Water Resources Research, 22(1): 15-24.  Peuker, T. K. and D. H. Douglas, (1975), "Detection of surface-specific points by local parallel processing of discrete terrain elevation data," Comput. Graphics Image Process., 4: 375-387.
     *
     * @param params {@link PeukerDouglasParams}
     * @return {@link ExecResult}
     */
    public ExecResult peukerDouglas(PeukerDouglasParams params) {
        log.debug("Start peukerDouglas...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A grid of elevation values.
        files.put("-fel", params.getElevation());
        // The center weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
        extraParams.put("-par", params.getCenterSmoothingWeight());
        // The side weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
        extraParams.put("-par", params.getSideSmoothingWeight());
        // The diagonal weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
        extraParams.put("-par", params.getDiagonalSmoothingWeight());
        // An indicator grid (1,0) of upward curved grid cells according to the Peuker and Douglas algorithm, and if viewed, resembles a channel network.
        outFiles.put("-ss", params.getStreamSource());
        ExecResult result = run("PeukerDouglas", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("peukerDouglas finished");
        } else {
            log.error("peukerDouglas failed!");
        }
        return result;
    }

    /**
     * moveOutletsToStreams
     * <p> Moves outlet points that are not aligned with a stream cell from a stream raster grid, downslope along the D8 flow direction until a stream raster cell is encountered, the max_dist number of grid cells are examined, or the flow path exits the domain (i.e. a "no data" value is encountered for the D8 flow direction). The output file is a new point OGR file where each point has been moved to coincide with the stream raster grid, if possible. A field 'dist_moved' is added to the new outlets file to indicate the changes made to each point. Points that are already on a stream cell are not moved and their 'dist_moved' field is assigned a value 0. Points that are initially not on a stream cell are moved by sliding them downslope along the D8 flow direction until one of the following occurs: a.) A stream raster grid cell is encountered before traversing the max_dist number of grid cells. In which case, the point is moved and the 'dist_moved' field is assigned a value indicating how many grid cells the point was moved. b.) More than the max_number of grid cells are traversed, or c) the traversal ends up going out of the domain (i.e., a "no data" D8 flow direction value is encountered). In which case, the point is not moved and the 'dist_moved' field is assigned a value of -1.
     *
     * @param params {@link MoveOutletsToStreamsParams}
     * @return {@link ExecResult}
     */
    public ExecResult moveOutletsToStreams(MoveOutletsToStreamsParams params) {
        log.debug("Start moveOutletsToStreams...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
        files.put("-p", params.getD8FlowDirection());
        // This output is an indicator grid (1,0) that indicates the location of streams, with a value of 1 for each of the stream cells and 0 for the remainder of the cells.
        files.put("-src", params.getStreamRaster());
        // A point feature defining points of interest or outlets that should ideally be located on a stream, but may not be exactly on the stream due to the fact that the feature point locations may not have been accurately registered with respect to the stream raster grid.
        files.put("-o", params.getOutlets());
        // This input paramater is the maximum number of grid cells that the points in the input outlet feature  will be moved before they are saved to the output outlet feature.
        extraParams.put("-md", params.getMaximumDistance());
        // A point  OGR file defining points of interest or outlets.
        outFiles.put("-om", params.getOutletsFile());
        ExecResult result = run("MoveOutletsToStreams", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("moveOutletsToStreams finished");
        } else {
            log.error("moveOutletsToStreams failed!");
        }
        return result;
    }

    /**
     * slopeAreaCombination
     * <p> Creates a grid of slope-area values = (S^m)(A^n) based on slope and specific catchment area grid inputs, and parameters m and n. This tool is intended for use as part of the slope-area stream raster delineation method.
     *
     * @param params {@link SlopeAreaCombinationParams}
     * @return {@link ExecResult}
     */
    public ExecResult slopeAreaCombination(SlopeAreaCombinationParams params) {
        log.debug("Start slopeAreaCombination...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // This input is a grid of slope values.
        files.put("-slp", params.getSlope());
        // A grid giving the specific catchment area for each cell taken as its own contribution (grid cell length or summation of weights) plus the proportional contribution from upslope neighbors that drain in to it.
        files.put("-sca", params.getArea());
        // The slope exponent (m) parameter which will be used in the formula: (S^m)(A^n), that is used to create the slope-area grid.
        extraParams.put("-par", params.getSlopeExponentM());
        // The area exponent (n) parameter which will be used in the formula: (S^m)(A^n), that is used to create the slope-area grid.
        extraParams.put("-par", params.getAreaExponentN());
        // A grid of slope-area values = (S^m)(A^n) calculated from the slope grid, specific catchment area grid, m slope exponent parameter, and n area exponent parameter.
        outFiles.put("-sa", params.getSlopeArea());
        ExecResult result = run("SlopeArea", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("slopeAreaCombination finished");
        } else {
            log.error("slopeAreaCombination failed!");
        }
        return result;
    }

    /**
     * streamDefinitionByThreshold
     * <p> Operates on any grid and outputs an indicator (1,0) grid identifing cells with input values >= the threshold value. The standard use is to use an accumulated source area grid to as the input grid to generate a stream raster grid as the output. If you use the optional input mask grid, it limits the domain being evaluated to cells with mask values >= 0 . When you use a D-infinity contributing area grid (*sca) as the mask grid, it functions as an edge contamination mask. The threshold logic is: src = ((ssa >= thresh) & (mask >=0)) ? 1:0 .
     *
     * @param params {@link StreamDefinitionByThresholdParams}
     * @return {@link ExecResult}
     */
    public ExecResult streamDefinitionByThreshold(StreamDefinitionByThresholdParams params) {
        log.debug("Start streamDefinitionByThreshold...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // This grid nominally accumulates some characteristic or combination of characteristics of the watershed.
        files.put("-ssa", params.getAccumulatedStreamSource());
        // This optional input is a grid that is used to mask the domain of interest and output is only provided where this grid is >= 0.
        files.put("-mask", params.getMask());
        // This parameter is compared to the value in the Accumulated Stream Source grid (*ssa) to determine if the cell should be considered a stream cell.
        extraParams.put("-thresh", params.getThreshold());
        // This is an indicator grid (1,0) that indicates the location of streams, with a value of 1 for each of the stream cells and 0 for the remainder of the cells.
        outFiles.put("-src", params.getStreamRaster());
        ExecResult result = run("Threshold", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("streamDefinitionByThreshold finished");
        } else {
            log.error("streamDefinitionByThreshold failed!");
        }
        return result;
    }

    /**
     * lengthAreaStreamSource
     * <p> Creates an indicator grid (1,0) that evaluates A >= (M)(L^y) based on upslope path length, D8 contributing area grid inputs, and parameters M and y. This grid indicates likely stream source grid cells. This is an experimental method with theoretical basis in Hack's law which states that for streams L ~ A^0.6. However for hillslopes with parallel flow L ~ A. So a transition from hillslopes to streams may be represented by L ~ A^0.8 suggesting identifying grid cells as stream cells if A > M (L^(1/0.8)).
     *
     * @param params {@link LengthAreaStreamSourceParams}
     * @return {@link ExecResult}
     */
    public ExecResult lengthAreaStreamSource(LengthAreaStreamSourceParams params) {
        log.debug("Start lengthAreaStreamSource...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A grid of the maximum upslope length for each cell.
        files.put("-plen", params.getLength());
        // A grid of contributing area values for each cell that were calculated using the D8 algorithm.
        files.put("-ad8", params.getContributingArea());
        // The multiplier threshold (M) parameter which is used in the formula: A > (M)(L^y), to identify the beginning of streams.
        extraParams.put("-par", params.getThresholdM());
        // The exponent (y) parameter which is used in the formula: A > (M)(L^y), to identify the beginning of streams.
        extraParams.put("-par", params.getExponentY());
        // An indicator grid (1,0) that evaluates A >= (M)(L^y), based on the maximum upslope path length, the D8 contributing area grid inputs, and parameters M and y.
        outFiles.put("-ss", params.getStreamSource());
        ExecResult result = run("LengthArea", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("lengthAreaStreamSource finished");
        } else {
            log.error("lengthAreaStreamSource failed!");
        }
        return result;
    }

    /**
     * streamDropAnalysis
     * <p> Applies a series of thresholds (determined from the input parameters) to the input accumulated stream source grid (*ssa) grid and outputs the results in the *drp.txt file the stream drop statistics table. This function is designed to aid in the determination of a geomorphologically objective threshold to be used to delineate streams. Drop Analysis attempts to select the right threshold automatically by evaluating a stream network for a range of thresholds and examining the constant drop property of the resulting Strahler streams. Basically it asks the question: Is the mean stream drop for first order streams statistically different from the mean stream drop for higher order streams, using a T-Test. Stream drop is the difference in elevation from the beginning to the end of a stream defined as the sequence of links of the same stream order. If the T test shows a significant difference then the stream network does not obey this "law" so a larger threshold needs to be chosen. The smallest threshold for which the T test does not show a significant difference gives the highest resolution stream network that obeys the constant stream drop "law" from geomorphology, and is the threshold chosen for the "objective" or automatic mapping of streams from the DEM. This function can be used in the development of stream network rasters, where the exact watershed characteristic(s) that were accumulated in the accumulated stream source grid vary based on the method being used to determine the stream network raster.
     *
     * @param params {@link StreamDropAnalysisParams}
     * @return {@link ExecResult}
     */
    public ExecResult streamDropAnalysis(StreamDropAnalysisParams params) {
        log.debug("Start streamDropAnalysis...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A grid of elevation values.
        files.put("-fel", params.getPitFilledElevation());
        // A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        files.put("-p", params.getD8FlowDirection());
        // A grid of contributing area values for each cell that were calculated using the D8 algorithm.
        files.put("-ad8", params.getD8ContributingArea());
        // This grid must be monotonically increasing along the downslope D8 flow directions.
        files.put("-ssa", params.getAccumulatedStreamSource());
        // A point feature defining the outlets upstream of which drop analysis is performed.
        files.put("-o", params.getOutlets());
        // This parameter is the lowest end of the range searched for possible threshold values using drop analysis.
        extraParams.put("-par", params.getMinimumThresholdValue());
        // This parameter is the highest end of the range searched for possible threshold values using drop analysis.
        extraParams.put("-par", params.getMaximumThresholdValue());
        // The parameter is the number of steps to divide the search range into when looking for possible threshold values using drop analysis.
        extraParams.put("-par", params.getNumberOfThresholdValues());
        // This checkbox indicates whether logarithmic or linear spacing should be used when looking for possible threshold values using drop ananlysis.
        extraParams.put("-l", params.getUseLogarithmicSpacingForThresholdValues());
        // This is a comma delimited text file with the following header line: Threshold, DrainDen, NoFirstOrd, NoHighOrd, MeanDFirstOrd, MeanDHighOrd, StdDevFirstOrd, StdDevHighOrd, T The file then contains one line of data for each threshold value examined, and then a summary line that indicates the optimum threshold value.
        outFiles.put("-drp", params.getDropAnalysisTextFile());
        ExecResult result = run("DropAnalysis", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("streamDropAnalysis finished");
        } else {
            log.error("streamDropAnalysis failed!");
        }
        return result;
    }

    /**
     * streamReachAndWatershed
     * <p> This tool produces a vector network and OGR file from the stream raster grid. The flow direction grid is used to connect flow paths along the stream raster. The Strahler order of each stream segment is computed. The subwatershed draining to each stream segment (reach) is also delineated and labeled with the value identifier that corresponds to the WSNO (watershed number) attribute in the Stream Reach OGR file. This tool orders the stream network according to the Strahler ordering system. Streams that don't have any other streams draining in to them are order 1. When two stream reaches of different order join the order of the downstream reach is the order of the highest incoming reach. When two reaches of equal order join the downstream reach order is increased by 1. When more than two reaches join the downstream reach order is calculated as the maximum of the highest incoming reach order or the second highest incoming reach order + 1. This generalizes the common definition to cases where more than two reaches join at a point. The network topological connectivity is stored in the Stream Network Tree file, and coordinates and attributes from each grid cell along the network are stored in the Network Coordinates file. The stream raster grid is used as the source for the stream network, and the flow direction grid is used to trace connections within the stream network. Elevations and contributing area are used to determine the elevation and contributing area attributes in the network coordinate file. Points in the outlets file are used to logically split stream reaches to facilitate representing watersheds upstream and downstream of monitoring points. The program uses the attribute field "id" in the outlets file as identifiers in the Network Tree file. This tool then translates the text file vector network representation in the Network Tree and Coordinates files into a polyline OGR file. Further attributes are also evaluated. The program has an option to delineate a single watershed by representing the entire area draining to the Stream Network as a single value in the output watershed grid.
     *
     * @param params {@link StreamReachAndWatershedParams}
     * @return {@link ExecResult}
     */
    public ExecResult streamReachAndWatershed(StreamReachAndWatershedParams params) {
        log.debug("Start streamReachAndWatershed...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // This input is a grid of elevation values.
        files.put("-fel", params.getPitFilledElevation());
        // This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
        files.put("-p", params.getD8FlowDirection());
        // A grid giving the contributing area value in terms of the number of grid cells (or the summation of weights) for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it using the D8 algorithm.
        files.put("-ad8", params.getD8DrainageArea());
        // An indicator grid indicating streams, by using a grid cell value of 1 on streams and 0 off streams.
        files.put("-src", params.getStreamRaster());
        // A point feature defining points of interest.
        files.put("-o", params.getOutlets());
        // This option causes the tool to delineate a single watershed by representing the entire area draining to the Stream Network as a single value in the output watershed grid.
        extraParams.put("-sw", params.getDelineateSingleWatershed());
        // The Stream Order Grid has cells values of streams ordered according to the Strahler order system.
        outFiles.put("-ord", params.getStreamOrder());
        // This output is a text file that details the network topological connectivity is stored in the Stream Network Tree file.
        outFiles.put("-tree", params.getNetworkConnectivityTree());
        // This output is a text file that contains the coordinates and attributes of points along the stream network.
        outFiles.put("-coord", params.getNetworkCoordinates());
        // This output is a polyline OGR file giving the links in a stream network.
        outFiles.put("-net", params.getStreamReachFile());
        // This output grid identified each reach watershed with a unique ID number, or in the case where the delineate single watershed option was checked, the entire area draining to the stream network is identified with a single ID.
        outFiles.put("-w", params.getWatershed());
        ExecResult result = run("StreamNet", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("streamReachAndWatershed finished");
        } else {
            log.error("streamReachAndWatershed failed!");
        }
        return result;
    }

    /**
     * gagewatershed
     * <p> Calculates Gage watersheds grid. Each grid cell is labeled with the identifier (from column id) of the gage to which it drains directly without passing through any other gages.
     *
     * @param params {@link GagewatershedParams}
     * @return {@link ExecResult}
     */
    public ExecResult gagewatershed(GagewatershedParams params) {
        log.debug("Start gagewatershed...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        files.put("-p", params.getD8FlowDirection());
        // A point feature defining the gages(outlets) to which watersheds will be delineated.
        files.put("-o", params.getGagesFile());
        // This output grid identifies each gage watershed.
        outFiles.put("-gw", params.getGagewatershed());
        // 
        outFiles.put("-id", params.getDownstreamIdentefier());
        ExecResult result = run("GageWatershed", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("gagewatershed finished");
        } else {
            log.error("gagewatershed failed!");
        }
        return result;
    }

    /**
     * d8ExtremeUpslopeValue
     * <p> Evaluates the extreme (either maximum or minimum) upslope value from an input grid based on the D8 flow model. This is intended initially for use in stream raster generation to identify a threshold of the slope times area product that results in an optimum (according to drop analysis) stream network. If the optional outlet point shapefile is used, only the outlet cells and the cells upslope (by the D8 flow model) of them are in the domain to be evaluated. By default, the tool checks for edge contamination. This is defined as the possibility that a result may be underestimated due to grid cells outside of the domain not being counted. This occurs when drainage is inwards from the boundaries or areas with no data values for elevation. The algorithm recognizes this and reports "no data" for the result for these grid cells. It is common to see streaks of "no data" values extending inwards from boundaries along flow paths that enter the domain at a boundary. This is the desired effect and indicates that the result for these grid cells is unknown due to it being dependent on terrain outside of the domain of data available. Edge contamination checking may be turned off in cases where you know this is not an issue or want to ignore these problems, if for example, the DEM has been clipped along a watershed outline.
     *
     * @param params {@link D8ExtremeUpslopeValueParams}
     * @return {@link ExecResult}
     */
    public ExecResult d8ExtremeUpslopeValue(D8ExtremeUpslopeValueParams params) {
        log.debug("Start d8ExtremeUpslopeValue...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        files.put("-p", params.getD8FlowDirection());
        // This is the grid of values of which the maximum or minimum upslope value is selected.
        files.put("-sa", params.getValue());
        // A flag to indicate whether the maximum or minimum upslope value is to be calculated.
        extraParams.put("-max", params.getUseMaximumUpslopeValue());
        // A flag that indicates whether the tool should check for edge contamination.
        extraParams.put("-nc", params.getCheckForEdgeContamination());
        // A point feature defining outlets of interest.
        files.put("-o", params.getOutlets());
        // A grid of the maximum/minimum upslope values.
        outFiles.put("-ssa", params.getExtremeValue());
        ExecResult result = run("D8FlowPathExtremeUp", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("d8ExtremeUpslopeValue finished");
        } else {
            log.error("d8ExtremeUpslopeValue failed!");
        }
        return result;
    }

    /**
     * connectdown
     * <p> For each zone in a raster entered (e.g. HUC converted to grid) it identifies the point with largest AreaD8. This is taken to be the outlet. A OGR file is created.Using flow directions each outlet is moved downflow a specified number of grid cells which is user controllable (Default is 1). The ID of the location the point has moved to is taken as iddown. Two OGR files are created one with the initial points and one with the moved points. Both contain id, iddown and AreaD8.
     *
     * @param params {@link ConnectdownParams}
     * @return {@link ExecResult}
     */
    public ExecResult connectdown(ConnectdownParams params) {
        log.debug("Start connectdown...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
        files.put("-p", params.getD8FlowDirection());
        // A grid giving the contributing area value in terms of the number of grid cells (or the summation of weights) for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it using the D8 algorithm.
        files.put("-ad8", params.getD8ContributingArea());
        // Watershed grid delineated from gage watershed function or streamreachwatershed function.
        files.put("-w", params.getWatershed());
        // Number of grid cells move to downstream based on flow directions.
        extraParams.put("-d", params.getNumberOfGridCells());
        // This output is point a OGR file where each point is created from watershed grid having the largest contributing area for each zone.
        outFiles.put("-o", params.getOutletsFile());
        // This output is a point OGR file where each outlet is moved downflow a specified number of grid cells using flow directions.
        outFiles.put("-od", params.getMovedoutletsFile());
        ExecResult result = run("ConnectDown", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("connectdown finished");
        } else {
            log.error("connectdown failed!");
        }
        return result;
    }

    /**
     * dInfinityReverseAccumulation
     * <p> This works in a similar way to evaluation of weighted Contributing area, except that the accumulation is by propagating the weight loadings upslope along the reverse of the flow directions to accumulate the quantity of weight loading downslope from each grid cell. The function also reports the maximum value of the weight loading downslope from each grid cell in the Maximum Downslope grid.
     *
     * @param params {@link DInfinityReverseAccumulationParams}
     * @return {@link ExecResult}
     */
    public ExecResult dInfinityReverseAccumulation(DInfinityReverseAccumulationParams params) {
        log.debug("Start dInfinityReverseAccumulation...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A grid giving flow direction by the Dinfinity method.
        files.put("-ang", params.getDinfinityFlowDirection());
        // A grid giving weights (loadings) to be used in the accumulation.
        files.put("-wg", params.getWeight());
        // The grid giving the result of the "Reverse Accumulation" function.
        outFiles.put("-racc", params.getReverseAccumulation());
        // The grid giving the maximum of the weight loading grid downslope from each grid cell.
        outFiles.put("-dmax", params.getMaximumDownslope());
        ExecResult result = run("DinfRevAccum", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("dInfinityReverseAccumulation finished");
        } else {
            log.error("dInfinityReverseAccumulation failed!");
        }
        return result;
    }

    /**
     * dInfinityUpslopeDependence
     * <p> The D-Infinity Upslope Dependence tool quantifies the amount each grid cell in the domain contributes to a destination set of grid cells. D-Infinity flow directions proportion flow from each grid cell between multiple downslope grid cells. Following this flow field downslope the amount of flow originating at each grid cell that reaches the destination zone is defined. Upslope influence is evaluated using a downslope recursion, examining grid cells downslope from each grid cell, so that the map produced identifies the area upslope where flow through the destination zone originates, or the area it depends on, for its flow.
     *
     * @param params {@link DInfinityUpslopeDependenceParams}
     * @return {@link ExecResult}
     */
    public ExecResult dInfinityUpslopeDependence(DInfinityUpslopeDependenceParams params) {
        log.debug("Start dInfinityUpslopeDependence...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A grid giving flow direction by the D-Infinity method where the flow direction angle is determined as the direction of the steepest downward slope on the eight triangular facets formed in a 3 x 3 grid cell window centered on the grid cell of interest.
        files.put("-ang", params.getDinfinityFlowDirection());
        // A grid that encodes the destination zone that may receive flow from upslope.
        files.put("-dg", params.getDestination());
        // A grid quantifing the amount each source point in the domain contributes to the zone defined by the destination grid.
        outFiles.put("-dep", params.getUpslopeDependence());
        ExecResult result = run("DinfUpDependence", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("dInfinityUpslopeDependence finished");
        } else {
            log.error("dInfinityUpslopeDependence failed!");
        }
        return result;
    }

    /**
     * d8DistanceToStreams
     * <p> Computes the horizontal distance to stream for each grid cell, moving downslope according to the D8 flow model, until a stream grid cell is encountered.
     *
     * @param params {@link D8DistanceToStreamsParams}
     * @return {@link ExecResult}
     */
    public ExecResult d8DistanceToStreams(D8DistanceToStreamsParams params) {
        log.debug("Start d8DistanceToStreams...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
        files.put("-p", params.getD8FlowDirection());
        // A grid indicating streams.
        files.put("-src", params.getStreamRaster());
        // This value acts as threshold on the Stream Raster Grid to determine the location of streams.
        extraParams.put("-thresh", params.getThreshold());
        // A grid giving the horizontal distance along the flow path as defined by the D8 Flow Directions Grid to the streams in the Stream Raster Grid.
        outFiles.put("-dist", params.getDistanceToStreams());
        ExecResult result = run("D8HDistTostrm", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("d8DistanceToStreams finished");
        } else {
            log.error("d8DistanceToStreams failed!");
        }
        return result;
    }

    /**
     * topographicWetnessIndex
     * <p> Calculates the ratio of the natural log of the specific catchment area (contributing area) to slope, ln(a/S), or ln(a/tan (beta)). This provides an indication of depth to water table. No data values occur in locations where slope is 0 (flat).
     *
     * @param params {@link TopographicWetnessIndexParams}
     * @return {@link ExecResult}
     */
    public ExecResult topographicWetnessIndex(TopographicWetnessIndexParams params) {
        log.debug("Start topographicWetnessIndex...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A grid of specific catchment area which is the contributing area per unit contour length.
        files.put("-sca", params.getSpecificCatchmentArea());
        // A grid of slope.
        files.put("-slp", params.getSlope());
        // A grid of the natural log of the ratio of specific catchment area (contributing area) to slope, ln(a/S).
        outFiles.put("-twi", params.getWetnessIndex());
        ExecResult result = run("TWI", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("topographicWetnessIndex finished");
        } else {
            log.error("topographicWetnessIndex failed!");
        }
        return result;
    }

    /**
     * slopeOverAreaRatio
     * <p> Calculates the ratio of the slope to the specific catchment area (contributing area). This is algebraically related to the more common ln(a/tan beta) wetness index, but contributing area is in the denominator to avoid divide by 0 errors when slope is 0.
     *
     * @param params {@link SlopeOverAreaRatioParams}
     * @return {@link ExecResult}
     */
    public ExecResult slopeOverAreaRatio(SlopeOverAreaRatioParams params) {
        log.debug("Start slopeOverAreaRatio...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A grid of slope.
        files.put("-slp", params.getSlope());
        // A grid giving the contributing area value for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it.
        files.put("-sca", params.getSpecificCatchmentArea());
        // A grid of the ratio of slope to specific catchment area (contributing area).
        outFiles.put("-sar", params.getSlopeDividedByAreaRatio());
        ExecResult result = run("SlopeAreaRatio", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("slopeOverAreaRatio finished");
        } else {
            log.error("slopeOverAreaRatio failed!");
        }
        return result;
    }

    /**
     * dInfinityTransportLimitedAccumulation
     * <p> This function is designed to calculate the transport and deposition of a substance (e.g. sediment) that may be limited by both supply and the capacity of the flow field to transport it. This function accumulates substance flux (e.g. sediment transport) subject to the rule that transport out of any grid cell is the minimum between supply and transport capacity, Tcap. The total supply at a grid cell is calculated as the sum of the transport in from upslope grid cells, Tin, plus the local supply contribution, E (e.g. erosion). This function also outputs deposition, D, calculated as total supply minus actual transport.
     *
     * @param params {@link DInfinityTransportLimitedAccumulationParams}
     * @return {@link ExecResult}
     */
    public ExecResult dInfinityTransportLimitedAccumulation(DInfinityTransportLimitedAccumulationParams params) {
        log.debug("Start dInfinityTransportLimitedAccumulation...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A grid giving flow direction by the D-infinity method.
        files.put("-ang", params.getDinfinityFlowDirection());
        // A grid giving the supply (loading) of material to a transport limited accumulation function.
        files.put("-tsup", params.getSupply());
        // A grid giving the transport capacity at each grid cell for the transport limited accumulation function.
        files.put("-tc", params.getTransportCapacity());
        // A grid giving the concentration of a compound of interest in the supply to the transport limited accumulation function.
        files.put("-cs", params.getConcentration());
        // This optional input is a point feature  defining outlets of interest.
        files.put("-o", params.getOutlets());
        // This checkbox determines whether the tool should check for edge contamination.
        extraParams.put("-nc", params.getCheckForEdgeContamination());
        // This grid is the weighted accumulation of supply accumulated respecting the limitations in transport capacity and reports the transport rate calculated by accumulating the substance flux subject to the rule that the transport out of any grid cell is the minimum of the total supply (local supply plus transport in) to that grid cell and the transport capacity.
        outFiles.put("-tla", params.getTransportLimitedAccumulation());
        // A grid giving the deposition resulting from the transport limited accumulation.
        outFiles.put("-tdep", params.getDeposition());
        // If an input concentation in supply grid is given, then this grid is also output and gives the concentration of a compound (contaminant) adhered or bound to the transported substance (e.
        outFiles.put("-ctpt", params.getOutputConcentration());
        ExecResult result = run("> DinfTransLimAccum", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("dInfinityTransportLimitedAccumulation finished");
        } else {
            log.error("dInfinityTransportLimitedAccumulation failed!");
        }
        return result;
    }

    /**
     * slopeAverageDown
     * <p> This tool computes slope in a D8 downslope direction averaged over a user selected distance. Distance should be specified in horizontal map units.
     *
     * @param params {@link SlopeAverageDownParams}
     * @return {@link ExecResult}
     */
    public ExecResult slopeAverageDown(SlopeAverageDownParams params) {
        log.debug("Start slopeAverageDown...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
        files.put("-p", params.getD8FlowDirection());
        // A grid of elevation values.
        files.put("-fel", params.getPitFilledElevation());
        // Input parameter of downslope distance over which to calculate the slope (in horizontal map units).
        extraParams.put("-dn", params.getDistance());
        // A grid of the ratio of  specific catchment area (contributing area) to slope.
        outFiles.put("-slpd", params.getSlopeAverageDown());
        ExecResult result = run("SlopeAveDown", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("slopeAverageDown finished");
        } else {
            log.error("slopeAverageDown failed!");
        }
        return result;
    }

    /**
     * dInfinityDistanceDown
     * <p> Calculates the distance downslope to a stream using the D-infinity flow model. The D-infinity flow model is a multiple flow direction model, because the outflow from each grid cell is proportioned between up to 2 downslope grid cells. As such, the distance from any grid cell to a stream is not uniquely defined. Flow that originates at a particular grid cell may enter the stream at a number of different cells. The statistical method may be selected as the longest, shortest or weighted average of the flow path distance to the stream. Also one of several ways of measuring distance may be selected: the total straight line path (Pythagoras), the horizontal component of the straight line path, the vertical component of the straight line path, or the total surface flow path.
     *
     * @param params {@link DInfinityDistanceDownParams}
     * @return {@link ExecResult}
     */
    public ExecResult dInfinityDistanceDown(DInfinityDistanceDownParams params) {
        log.debug("Start dInfinityDistanceDown...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A grid giving flow directions by the D-Infinity method.
        files.put("-ang", params.getDinfinityFlowDirection());
        // This input is a grid of elevation values.
        files.put("-fel", params.getPitFilledElevation());
        // A grid indicating streams, by using a grid cell value of 1 on streams and 0 off streams.
        files.put("-src", params.getStreamRaster());
        // Alternative values are ave, max, min, where ave for Average, max for Maximum, min for Minimum Statistical method used to calculate the distance down to the stream.
        extraParams.put("-m", params.getStatisticalMethod());
        // Alternative values are h, v, p, s, where h for Horizontal, v for Vertical, p for Pythagoras, s for Surface Distance method used to calculate the distance down to the stream.
        extraParams.put("-m", params.getDistanceMethod());
        // A flag that determines whether the tool should check for edge contamination.
        extraParams.put("-nc", params.getCheckForEdgeContamination());
        // A grid giving weights (loadings) to be used in the distance calculation.
        files.put("-wg", params.getWeightPath());
        // Creates a grid containing the distance to stream calculated using the D-infinity flow model and the statistical and path methods chosen.
        outFiles.put("-dd", params.getDinfinityDropToStream());
        ExecResult result = run("DinfDistDown", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("dInfinityDistanceDown finished");
        } else {
            log.error("dInfinityDistanceDown failed!");
        }
        return result;
    }

    /**
     * dInfinityDistanceUp
     * <p> This tool calculates the distance from each grid cell up to the ridge cells along the reverse D-infinity flow directions. Ridge cells are defined to be grid cells that have no contribution from grid cells further upslope. Given the convergence of multiple flow paths at any grid cell, any given grid cell can have multiple upslope ridge cells. There are three statictical methods that this tool can use: maximum distance, minimum distance and weighted flow average over these flow paths. A variant on the above is to consider only grid cells that contribute flow with a proportion greater than a user specified threshold (t) to be considered as upslope of any given grid cell. Setting t=0.5 would result in only one flow path from any grid cell and would give the result equivalent to a D8 flow model, rather than D-infinity flow model, where flow is proportioned between two downslope grid cells. There are several different options for the way distance can be measured: the total straight line path (Pythagoras), the horizontal component of the straight line path, the vertical component of the straight line path, or the total surface flow path.
     *
     * @param params {@link DInfinityDistanceUpParams}
     * @return {@link ExecResult}
     */
    public ExecResult dInfinityDistanceUp(DInfinityDistanceUpParams params) {
        log.debug("Start dInfinityDistanceUp...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A grid giving flow directions by the D-Infinity method.
        files.put("-ang", params.getDinfinityFlowDirection());
        // This input is a grid of elevation values.
        files.put("-fel", params.getPitFilledElevation());
        // This input is a grid of slope values.
        files.put("-slp", params.getSlope());
        // The proportion threshold parameter where only grid cells that contribute flow with a proportion greater than this user specified threshold (t) is considered to be upslope of any given grid cell.
        extraParams.put("-thresh", params.getProportionThreshold());
        // Statistical method used to calculate the distance down to the stream.
        extraParams.put("-m", params.getStatisticalMethod());
        // Distance method used to calculate the distance down to the stream.
        extraParams.put("-m", params.getDistanceMethod());
        // A flag that determines whether the tool should check for edge contamination.
        extraParams.put("-nc", params.getCheckForEdgeContamination());
        // 
        outFiles.put("-du", params.getDinfinityDistanceUp());
        ExecResult result = run("DinfDistUp", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("dInfinityDistanceUp finished");
        } else {
            log.error("dInfinityDistanceUp failed!");
        }
        return result;
    }

    /**
     * dInfinityConcentrationLimitedAcccumulation
     * <p> This function applies to the situation where an unlimited supply of a substance is loaded into flow at a concentration or solubility threshold Csol over a region indicated by an indicator grid (dg). It a grid of the concentration of a substance at each location in the domain, where the supply of substance from a supply area is loaded into the flow at a concentration or solubility threshold. The flow is first calculated as a D-infinity weighted contributing area of an input Effective Runoff Weight Grid (notionally excess precipitation). The concentation of substance over the supply area (indicator grid) is at the concentration threshold. As the substance moves downslope with the D-infinity flow field, it is subject to first order decay in moving from cell to cell as well as dilution due to changes in flow. The decay multiplier grid gives the fractional (first order) reduction in quantity in moving from grid cell x to the next downslope cell. If the input outlets is used, the tool only evaluates the part of the domain that contributes flow to the locations given by the outlets. This is useful for a tracking a contaminant or compound from an area with unlimited supply of that compound that is loaded into a flow at a concentration or solubility threshold over a zone and flow from the zone may be subject to decay or attenuation.
     *
     * @param params {@link DInfinityConcentrationLimitedAcccumulationParams}
     * @return {@link ExecResult}
     */
    public ExecResult dInfinityConcentrationLimitedAcccumulation(DInfinityConcentrationLimitedAcccumulationParams params) {
        log.debug("Start dInfinityConcentrationLimitedAcccumulation...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A grid giving flow direction by the D-infinity method.
        files.put("-ang", params.getDinfinityFlowDirection());
        // A grid giving the input quantity (notionally effective runoff or excess precipitation) to be used in the D-infinity weighted contributing area evaluation of Overland Flow Specific Discharge.
        files.put("-wg", params.getEffectiveRunoffWeight());
        // A grid that indicates the source zone of the area of substance supply and must be 1 inside the zone and 0 or "no data" over the rest of the domain.
        files.put("-dg", params.getDisturbanceIndicator());
        // A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
        files.put("-dm", params.getDecayMultiplier());
        // This optional input is a point feature  defining outlets of interest.
        files.put("-o", params.getOutlets());
        // The concentration or solubility threshold.
        extraParams.put("-csol", params.getConcentrationThreshold());
        // This checkbox determines whether the tool should check for edge contamination.
        extraParams.put("-nc", params.getCheckForEdgeContamination());
        // The grid giving the specific discharge of the flow carrying the constituent being loaded at the concentration threshold specified.
        outFiles.put("-q", params.getOverlandFlowSpecificDischarge());
        // A grid giving the resulting concentration of the compound of interest in the flow.
        outFiles.put("-ctpt", params.getConcentration());
        ExecResult result = run("DinfConcLimAccum", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("dInfinityConcentrationLimitedAcccumulation finished");
        } else {
            log.error("dInfinityConcentrationLimitedAcccumulation failed!");
        }
        return result;
    }

    /**
     * gridNetwork
     * <p> Creates 3 grids that contain for each grid cell: 1) the longest path, 2) the total path, and 3) the Strahler order number. These values are derived from the network defined by the D8 flow model. The longest upslope length is the length of the flow path from the furthest cell that drains to each cell. The total upslope path length is the length of the entire grid network upslope of each grid cell. Lengths are measured between cell centers taking into account cell size and whether the direction is adjacent or diagonal. Strahler order is defined as follows: A network of flow paths is defined by the D8 Flow Direction grid. Source flow paths have a Strahler order number of one. When two flow paths of different order join the order of the downstream flow path is the order of the highest incoming flow path. When two flow paths of equal order join the downstream flow path order is increased by 1. When more than two flow paths join the downstream flow path order is calculated as the maximum of the highest incoming flow path order or the second highest incoming flow path order + 1. This generalizes the common definition to cases where more than two flow paths join at a point. Where the optional mask grid and threshold value are input, the function is evaluated only considering grid cells that lie in the domain with mask grid value greater than or equal to the threshold value. Source (first order) grid cells are taken as those that do not have any other grid cells from inside the domain draining in to them, and only when two of these flow paths join is order propagated according to the ordering rules. Lengths are also only evaluated counting paths within the domain greater than or equal to the threshold. If the optional outlet point is used, only the outlet cells and the cells upslope (by the D8 flow model) of them are in the domain to be evaluated.
     *
     * @param params {@link GridNetworkParams}
     * @return {@link ExecResult}
     */
    public ExecResult gridNetwork(GridNetworkParams params) {
        log.debug("Start gridNetwork...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        files.put("-p", params.getD8FlowDirection());
        // A point feature  defining the outlets of interest.
        files.put("-o", params.getOutlets());
        // A grid that is used to determine the domain do be analyzed.
        files.put("-mask", params.getMask());
        // This input parameter is used in the calculation mask grid value >= mask threshold to determine if the grid cell is in the domain to be analyzed.
        extraParams.put("-thresh", params.getMaskThresholdValue());
        // A grid giving the Strahler order number for each cell.
        outFiles.put("-gord", params.getStrahlerNetworkOrder());
        // A grid that gives the length of the longest upslope D8 flow path terminating at each grid cell.
        outFiles.put("-plen", params.getLongestUpslopeLength());
        // The total upslope path length is the length of the entire D8 flow grid network upslope of each grid cell.
        outFiles.put("-tlen", params.getTotalUpslopeLength());
        ExecResult result = run("Gridnet", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("gridNetwork finished");
        } else {
            log.error("gridNetwork failed!");
        }
        return result;
    }

    /**
     * dInfinityDecayingAccumulation
     * <p> The D-Infinity Decaying Accumulation tool creates a grid of the accumulated quantity at each location in the domain where the quantity accumulates with the D-infinity flow field, but is subject to first order decay in moving from cell to cell. By default, the quantity contribution of each grid cell is the cell length to give a per unit width accumulation, but can optionally be expressed with a weight grid. The decay multiplier grid gives the fractional (first order) reduction in quantity in accumulating from grid cell x to the next downslope cell.
     *
     * @param params {@link DInfinityDecayingAccumulationParams}
     * @return {@link ExecResult}
     */
    public ExecResult dInfinityDecayingAccumulation(DInfinityDecayingAccumulationParams params) {
        log.debug("Start dInfinityDecayingAccumulation...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A grid giving flow direction by the D-infinity method.
        files.put("-ang", params.getDinfinityFlowDirection());
        // A grid giving the factor by which flow leaving each grid cell is multiplied before accumulation on downslope grid cells.
        files.put("-dm", params.getDecayMultiplier());
        // A grid giving weights (loadings) to be used in the accumulation.
        files.put("-wg", params.getWeight());
        // This optional input is a point feature  defining outlets of interest.
        files.put("-o", params.getOutlets());
        // This checkbox determines whether the tool should check for edge contamination.
        extraParams.put("-nc", params.getCheckForEdgeContamination());
        // The D-Infinity Decaying Accumulation tool creates a grid of the accumulated mass at each location in the domain where mass moves with the D-infinity flow field, but is subject to first order decay in moving from cell to cell.
        outFiles.put("-sca", params.getDecayedSpecificCatchmentArea());
        ExecResult result = run("DinfDecayAccum", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("dInfinityDecayingAccumulation finished");
        } else {
            log.error("dInfinityDecayingAccumulation failed!");
        }
        return result;
    }

    /**
     * dInfinityAvalancheRunout
     * <p> Identifies an avalanche's affected area and the flow path length to each cell in that affacted area. All cells downslope from each source area cell, up to the point where the slope from the source to the affected area is less than a threshold angle called the Alpha Angle can be in the affected area. This tool uses the D-infinity multiple flow direction method for determining flow direction. This will likely cause very small amounts of flow to be dispersed to some downslope cells that might overstate the affected area, so a threshold proportion can be set to avoid this excess dispersion. The flow path length is the distance from the cell in question to the source cell that has the highest angle. All points downslope from the source area are potentially in the affected area, but not beyond a point where the slope from the source to the affected area is less than a threshold angle called the Alpha Angle.
     *
     * @param params {@link DInfinityAvalancheRunoutParams}
     * @return {@link ExecResult}
     */
    public ExecResult dInfinityAvalancheRunout(DInfinityAvalancheRunoutParams params) {
        log.debug("Start dInfinityAvalancheRunout...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A grid of elevation values.
        files.put("-fel", params.getPitFilledElevation());
        // A grid giving flow directions by the D-Infinity method.
        files.put("-ang", params.getDinfinityFlowDirection());
        // This is a grid of source areas for snow avalanches that are commonly identified manually using a mix of experience and visual interpretation of maps.
        files.put("-ass", params.getAvalancheSourceSite());
        // This value is a threshold proportion that is used to limit the disperson of flow caused by using the D-infinity multiple flow direction method for determining flow direction.
        extraParams.put("-thresh", params.getProportionThreshold());
        // This value is the threshold angle, called the Alpha Angle, that is used to determine which of the cells downslope from the source cells are in the affected area.
        extraParams.put("-alpha", params.getAlphaAngleThreshold());
        // This option selects the method used to measure the distance used to calculate the slope angle.
        extraParams.put("-direct", params.getPathDistanceMethod());
        // This grid Identifies the avalanche's runout zone (affected area) using a runout zone indicator with value 0 to indicate that this grid cell is not in the runout zone and value > 0 to indicate that this grid cell is in the runout zone.
        outFiles.put("-rz", params.getRunoutZone());
        // This is a grid of the flow distance from the source site that has the highest angle to each cell.
        outFiles.put("-dfs", params.getPathDistance());
        ExecResult result = run("DinfAvalanche", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("dInfinityAvalancheRunout finished");
        } else {
            log.error("dInfinityAvalancheRunout failed!");
        }
        return result;
    }

    /**
     * pitRemove
     * <p> This funciton identifies all pits in the DEM and raises their elevation to the level of the lowest pour point around their edge. Pits, also referred to as depressions or sinks, are low elevation areas in digital elevation models (DEMs) that are completely surrounded by higher terrain. They are generally taken to be artifacts that interfere with the routing of flow across DEMs, so are removed by raising their elevationt, or filling them, to the point where they drain off the edge of the domain. The pour point is the lowest point on the boundary of the "watershed" draining to the pit.  This pit removal step is not   essential if you have reason to believe that all the pits in your DEM are real. This   step can be circumvented by using the raw DEM as input to other TauDEM functions in the place where a filled DEM is used, such as by copying the raw DEM source data onto the file with   suffix "fel" to simulate the output of "Pit Remove" without actually removing   the pits.
     *
     * @param params {@link PitRemoveParams}
     * @return {@link ExecResult}
     */
    public ExecResult pitRemove(PitRemoveParams params) {
        log.debug("Start pitRemove...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A digital elevation model (DEM) grid to serve as the base input for the terrain analysis and stream delineation.
        files.put("-z", params.getElevation());
        // If this option is selected Fill ensures that the grid is hydrologically conditioned with cell to cell connectivity in only 4 directions (N, S, E or W neighbors).
        extraParams.put("-4way", params.getFillConsideringOnly4WayNeighbors());
        // 
        files.put("-depmask", params.getDepressionMask());
        // A grid of elevation values with pits removed so that flow is routed off of the domain.
        outFiles.put("-fel", params.getPitFilledElevation());
        ExecResult result = run("PitRemove", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("pitRemove finished");
        } else {
            log.error("pitRemove failed!");
        }
        return result;
    }

    /**
     * d8FlowDirections
     * <p> Creates 2 grids. The first contains the flow direction from each grid cell to one of its adjacent or diagonal neighbors, calculated using the direction of steepest descent. The second contain the slope, as evaluated in the direction of steepest descent, and is reported as drop/distance, i.e. tan of the angle. Flow direction is reported as "no data" for any grid cell adjacent to the edge of the DEM domain, or adjacent to a "no data" value in the DEM. In flat areas, flow directions are assigned away from higher ground and towards lower ground using the method of Garbrecht and Martz (1997). The D8 flow direction algorithm may be applied to a DEM that has not had its pits filled, but it will then result in "no data" values for flow direction and slope at the lowest point of each pit.
     *
     * @param params {@link D8FlowDirectionsParams}
     * @return {@link ExecResult}
     */
    public ExecResult d8FlowDirections(D8FlowDirectionsParams params) {
        log.debug("Start d8FlowDirections...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A grid of elevation values.
        files.put("-fel", params.getPitFilledElevation());
        // A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        outFiles.put("-p", params.getD8FlowDirection());
        // A grid giving slope in the D8 flow direction.
        outFiles.put("-sd8", params.getD8Slope());
        ExecResult result = run("D8FlowDir", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("d8FlowDirections finished");
        } else {
            log.error("d8FlowDirections failed!");
        }
        return result;
    }

    /**
     * dInfinityFlowDirections
     * <p> Assigns a flow direction based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997, "A New Method for the Determination of Flow Directions and Contributing Areas in Grid Digital Elevation Models," Water Resources Research, 33(2): 309-319). Flow direction is defined as steepest downward slope on planar triangular facets on a block centered grid. Flow direction is encoded as an angle in radians counter-clockwise from east as a continuous (floating point) quantity between 0 and 2 pi. The flow direction angle is determined as the direction of the steepest downward slope on the eight triangular facets formed in a 3 x 3 grid cell window centered on the grid cell of interest. The resulting flow in a grid is then usually interpreted as being proportioned between the two neighboring cells that define the triangular facet with the steepest downward slope.
     *
     * @param params {@link DInfinityFlowDirectionsParams}
     * @return {@link ExecResult}
     */
    public ExecResult dInfinityFlowDirections(DInfinityFlowDirectionsParams params) {
        log.debug("Start dInfinityFlowDirections...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A grid of elevation values.
        files.put("-fel", params.getPitFilledElevation());
        // A grid of flow directions based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997, "A New Method for the Determination of Flow Directions and Contributing Areas in Grid Digital Elevation Models," Water Resources Research, 33(2): 309-319).
        outFiles.put("-ang", params.getDinfinityFlowDirection());
        // A grid of slope evaluated using the D-infinity method described in Tarboton, D.
        outFiles.put("-slp", params.getDinfinitySlope());
        ExecResult result = run("DinfFlowDir", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("dInfinityFlowDirections finished");
        } else {
            log.error("dInfinityFlowDirections failed!");
        }
        return result;
    }

    /**
     * d8ContributingArea
     * <p>
     *
     * @param params {@link D8ContributingAreaParams}
     * @return {@link ExecResult}
     */
    public ExecResult d8ContributingArea(D8ContributingAreaParams params) {
        log.debug("Start d8ContributingArea...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        files.put("-p", params.getD8FlowDirection());
        // A point feature defining the outlets of interest.
        files.put("-o", params.getOutlets());
        // A grid giving contribution to flow for each cell.
        files.put("-wg", params.getWeight());
        // A flag that indicates whether the tool should check for edge contamination.
        extraParams.put("-nc", params.getCheckForEdgeContamination());
        // A grid of contributing area values calculated as the cells own contribution plus the contribution from upslope neighbors that drain in to it according to the D8 flow model.
        outFiles.put("-ad8", params.getD8ContributingArea());
        ExecResult result = run("AreaD8", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("d8ContributingArea finished");
        } else {
            log.error("d8ContributingArea failed!");
        }
        return result;
    }

    /**
     * dInfinityContributingArea
     * <p> Calculates a grid of specific catchment area which is the contributing area per unit contour length using the multiple flow direction D-infinity approach. D-infinity flow direction is defined as steepest downward slope on planar triangular facets on a block centered grid. The contribution at each grid cell is taken as the grid cell length (or when the optional weight grid input is used, from the weight grid). The contributing area of each grid cell is then taken as its own contribution plus the contribution from upslope neighbors that have some fraction draining to it according to the D-infinity flow model. The flow from each cell either all drains to one neighbor, if the angle falls along a cardinal (0, pi/2, pi, 3pi/2) or ordinal (pi/4, 3pi/4, 5pi/4, 7pi/4) direction, or is on an angle falling between the direct angle to two adjacent neighbors. In the latter case the flow is proportioned between these two neighbor cells according to how close the flow direction angle is to the direct angle to those cells.  Using the grid cell size as the contribution at each grid cell assumes that grid cell size is the effective contour length in the definition of specific catchment area and does not distinguish any difference in contour length dependent upon the flow direction. The resulting units of the specific catchment area are then length units the same as those of the grid cell size. If the   optional outlet point shapefile is used, only the outlet cells and the   cells upslope (by the D-infinity flow model) of them are in the domain   to be evaluated. By default   Dinfinity specific catchment area is evaluated using checks for edge   contamination. This is defined as the possibility that a contributing   area value may be underestimated due to grid cells outside of the domain  not being counted. This occurs when drainage is inwards from the   boundaries or areas with "no data" values for elevation. The algorithm   recognizes this and reports "no data" for the contributing area. It is   common to see streaks of "no data" values extending inwards from   boundaries along flow paths that enter the domain at a boundary. This is  the desired effect and indicates that contributing area for these grid   cells is unknown due to it being dependent on terrain outside of the   domain of data available. Edge contamination checking may be turned off   in cases where you know it is not an issue or want to ignore these   problems, if for example, the DEM has been clipped along a watershed   outline.
     *
     * @param params {@link DInfinityContributingAreaParams}
     * @return {@link ExecResult}
     */
    public ExecResult dInfinityContributingArea(DInfinityContributingAreaParams params) {
        log.debug("Start dInfinityContributingArea...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // A grid of flow directions based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997, "A New Method for the Determination of Flow Directions and Contributing Areas in Grid Digital Elevation Models," Water Resources Research, 33(2): 309-319).
        files.put("-ang", params.getDinfinityFlowDirection());
        // A point feature defining the outlets of interest.
        files.put("-o", params.getOutlets());
        // A grid giving contribution to flow for each cell.
        files.put("-wg", params.getWeight());
        // A flag that indicates whether the tool should check for edge contamination.
        extraParams.put("-nc", params.getCheckForEdgeContamination());
        // A grid of specific catchment area which is the contributing area per unit contour length using the multiple flow direction D-infinity approach.
        outFiles.put("-sca", params.getDinfinitySpecificCatchmentArea());
        ExecResult result = run("AreaDinf", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("dInfinityContributingArea finished");
        } else {
            log.error("dInfinityContributingArea failed!");
        }
        return result;
    }
}