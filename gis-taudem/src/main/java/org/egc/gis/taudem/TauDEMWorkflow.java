package org.egc.gis.taudem;

import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.ExecResult;
import org.egc.commons.exception.BusinessException;
import org.egc.commons.gis.GeoTiffUtils;
import org.egc.commons.gis.RasterMetadata;
import org.egc.gis.taudem.params.*;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * TODO 测试
 * Description:
 * <pre>
 * shortcut to watershed delineation and twi et.at.
 * 参考 https://github.com/lreis2415/PyGeoC/blob/master/pygeoc/TauDEM.py 等
 * </pre>
 *
 * @author houzhiwei
 * @date 2018 /11/12 16:48
 */
@Slf4j
public class TauDEMWorkflow {

    /**
     * TODO 根据pygeoc中对应的功能进行改进
     * 参考 TauDEM5DelineationASingleWatershed.pdf
     * <p>Delineation of channels and watersheds using a constant support area threshold
     * <p> Pit Remove -> D8 Flow Directions
     * -> D8 Contributing Area -> Stream Definition by Threshold -> Move Outlets To Streams
     * -> Peuker Douglas -> Stream Reach and Watershed
     * </p>
     *
     * @param dem       the dem grid
     * @param outlets   the outlets
     * @param threshold the threshold, if null, use Stream Drop Analysis to find the optimal threshold
     * @param outputDir the output dir
     * @return Watershed_Grid.tif
     */
    public static String watershedDelineation(@NotBlank String dem, String outlets, double threshold, boolean singleBasin, String outputDir) {
        TauDEMAnalysis tauDEMAnalysis = TauDEMAnalysis.getInstance();
        tauDEMAnalysis.init(outputDir);

        long start = System.currentTimeMillis();
        log.info("------------------Watershed Delineation Begin---------------");

        String inputDir = outputDir;

        log.debug("-----------generating hydrologically correct DEM-----------");
        // pit remove/fill
        PitRemoveParams felParams = new PitRemoveParams.Builder(dem).build();

        tauDEMAnalysis.pitRemove(felParams);
        String pitFilledElevation = felParams.getPitFilledElevation();

        // D8 Flow Directions
        D8FlowDirectionsParams d8FlowDirParams = new D8FlowDirectionsParams.Builder(pitFilledElevation).build();
        tauDEMAnalysis.d8FlowDirections(d8FlowDirParams);

        String d8FlowDirection = d8FlowDirParams.getD8FlowDirection();
        String d8Slope = d8FlowDirParams.getD8Slope();

        //  DInfinity Flow Directions
        //DInfinityFlowDirectionsParams infinityFlowDirectionsParams = new DInfinityFlowDirectionsParams.Builder(pitFilledElevation).build();

        // D8 Contributing Area
        D8ContributingAreaParams ad8Params =
                new D8ContributingAreaParams.Builder(d8FlowDirection).outlets(outlets).build();
        tauDEMAnalysis.d8ContributingArea(ad8Params);
        String d8ContributingArea = ad8Params.getD8ContributingArea();

        // Stream Definition By Threshold
        StreamDefinitionByThresholdParams thresholdParams =
                new StreamDefinitionByThresholdParams.Builder(d8ContributingArea).build();
        log.debug("threshold value is : {} ", threshold);
        tauDEMAnalysis.streamDefinitionByThreshold(thresholdParams);
        String streamRaster = thresholdParams.getStreamRaster();

        String defaultOutlet = "outlet.shp";
        String movedOutlets = "moved_outlets.shp";

        // Connect Down
        if (StringUtils.isBlank(outlets)) {
            ConnectdownParams connectDownParams = new ConnectdownParams
                    .Builder(d8FlowDirection, d8ContributingArea)
                    .movedoutletsFile(movedOutlets)
                    .outletsFile(defaultOutlet).build();
            tauDEMAnalysis.connectdown(connectDownParams);
            outlets = connectDownParams.getMovedoutletsFile();
        }

        // Move Outlets To Streams
        MoveOutletsToStreamsParams toStreamsParams =
                new MoveOutletsToStreamsParams.Builder(d8FlowDirection, streamRaster,
                        outlets).outletsFile(movedOutlets).build();
        tauDEMAnalysis.moveOutletsToStreams(toStreamsParams);

        // Peuker Douglas
        PeukerDouglasParams peukerParams = new PeukerDouglasParams.Builder(dem).build();
        tauDEMAnalysis.peukerDouglas(peukerParams);
        //streamSkeleton
        String streamSource = peukerParams.getStreamSource();

        String tempOutlets = null;
        if (singleBasin) {
            tempOutlets = outlets;
        }
        // D8 Contributing Area with new outlets
        D8ContributingAreaParams ad8Params2 = new D8ContributingAreaParams.Builder(d8FlowDirection)
                .outlets(tempOutlets).weight(streamSource)
                .checkForEdgeContamination(false).build();
        tauDEMAnalysis.d8ContributingArea(ad8Params2);
        //acc
        String d8AreaWeight = ad8Params2.getD8ContributingArea();

        // Stream Drop Analysis
        // 使用 drop analysis 自动获取优化的阈值
        if (threshold <= 0) {
            String drp = "drp.txt";
            double minThreshold = 0d;
            double maxThreshold = 0d;
            RasterMetadata metadata = GeoTiffUtils.getMetadataByGDAL(d8AreaWeight);
            double mean = metadata.getMeanValue();
            double stdev = metadata.getSdev();
            if ((metadata.getMeanValue() - metadata.getSdev()) < 0) {
                minThreshold = mean;
            } else {
                minThreshold = mean - stdev;
            }
            maxThreshold = mean + stdev;

            StreamDropAnalysisParams dropParams = new StreamDropAnalysisParams
                    .Builder(pitFilledElevation, d8FlowDirection, d8AreaWeight,
                    d8AreaWeight).outlets(outlets).minimumThresholdValue(minThreshold)
                    .maximumThresholdValue(maxThreshold)
                    .numberOfThresholdValues(20d)
                    .dropAnalysisTextFile(drp).build();
            log.debug("----------------executing drop analysis to select optimal threshold------------");
            tauDEMAnalysis.streamDropAnalysis(dropParams);
            try {
                List<String> strings = Files.readLines(new File(outputDir + drp), StandardCharsets.UTF_8);
                // 取最后一行，格式参考 CSDMS_tauDEM_workshop.ppt Optimal Contributing Area Threshold
                threshold = Double.parseDouble(strings.get(strings.size() - 1).split(": ")[1]);
                log.debug("threshold is {}", threshold);
            } catch (Exception e) {
                log.error("Output_Drop_Analysis_Text_File: drp.txt not found");
                throw new BusinessException("Output_Drop_Analysis_Text_File: drp.txt not found");
            }
        }

        StreamDefinitionByThresholdParams thresholdParams2 =
                new StreamDefinitionByThresholdParams.Builder(d8AreaWeight)
                        .streamRaster(streamRaster)
                        .threshold(threshold)
                        .build();
        log.debug("threshold value: {} ", threshold);
        tauDEMAnalysis.streamDefinitionByThreshold(thresholdParams2);
        String streamRaster2 = thresholdParams2.getStreamRaster();

        // Stream Reach And Watershed
        String watershedGrid = FilenameUtils.getBaseName(dem) + "_watershed_grid.tif";
        StreamReachAndWatershedParams watershedParams = new StreamReachAndWatershedParams.Builder(
                pitFilledElevation, d8FlowDirection, d8AreaWeight, streamRaster2)
                .outlets(outlets)
                .watershed(watershedGrid).build();
        ExecResult result = tauDEMAnalysis.streamReachAndWatershed(watershedParams);

        //?
        D8DistanceToStreamsParams d8DistanceToStreamsParams = new D8DistanceToStreamsParams.Builder(d8FlowDirection, streamRaster2)
                .threshold(1d).build();
        tauDEMAnalysis.d8DistanceToStreams(d8DistanceToStreamsParams);
        String distanceToStreams = d8DistanceToStreamsParams.getDistanceToStreams();

        log.info("---Watershed Delineation done in:  " + (System.currentTimeMillis() - start) + " ms---");
        // 与 Watershed_Grid 不同的是包含了目录
        watershedParams.getStreamReachFile();
        return watershedParams.getWatershed();
    }

    /**
     * Topographic Wetness Index Calculation
     * <p>Pit Remove  -> D-Infinity Flow Directions (and Slopes)
     * -> D-Infinity Contributing Area -> Slope Over Area Ratio -> twi
     * </p>
     *
     * @param elevationGrid the elevation grid
     * @param outputDir     the output dir
     * @return string
     */
    public static String twi(@NotBlank String elevationGrid, String outputDir) {
        TauDEMAnalysis tauDEMAnalysis = TauDEMAnalysis.getInstance();
        tauDEMAnalysis.init(outputDir);

        long start = System.currentTimeMillis();
        log.info("----------Topographic Wetness Index Calculation Begin------------");

        String inputDir = outputDir;
        log.debug("----------------generating hydrologically correct DEM------------");
        PitRemoveParams felParams = new PitRemoveParams.Builder(elevationGrid).build();
        tauDEMAnalysis.pitRemove(felParams);
        String pitFilledElevation = felParams.getPitFilledElevation();

        //generating D-Infinity Flow Directions
        DInfinityFlowDirectionsParams dInfFlowDirParams = new DInfinityFlowDirectionsParams.Builder(
                pitFilledElevation).build();
        tauDEMAnalysis.dInfinityFlowDirections(dInfFlowDirParams);
        String dinfinityFlowDirection = dInfFlowDirParams.getDinfinityFlowDirection();
        String dinfinitySlope = dInfFlowDirParams.getDinfinitySlope();

        //calculating D-Infinity Contributing Area
        DInfinityContributingAreaParams dInfCAParams = new DInfinityContributingAreaParams.Builder(
                dinfinityFlowDirection).build();
        tauDEMAnalysis.dInfinityContributingArea(dInfCAParams);
        String dinfinitySpecificCatchmentArea = dInfCAParams.getDinfinitySpecificCatchmentArea();


        //calculating Topographic Wetness Index
        TopographicWetnessIndexParams twiParams = new TopographicWetnessIndexParams.Builder(dinfinitySpecificCatchmentArea,
                dinfinitySlope).build();
        tauDEMAnalysis.topographicWetnessIndex(twiParams);
        log.info("------------Topographic Wetness Index Calculation done in: " + (System.currentTimeMillis() - start) + " ms--------------");
        return twiParams.getWetnessIndex();
    }
}
