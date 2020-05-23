package org.egc.gis.taudem;

import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.ExecResult;
import org.egc.commons.exception.BusinessException;
import org.egc.gis.taudem.params.*;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 * TODO 测试
 * Description:
 * <pre>
 * shortcut to watershed delineation and twi et.at.
 * </pre>
 *
 * @author houzhiwei
 * @date 2018 /11/12 16:48
 */
@Slf4j
public class TauDEMWorkflow {

    /**
     * 参考 TauDEM5DelineationASingleWatershed.pdf
     * <p>Delineation of channels and watersheds using a constant support area threshold
     * <p> Pit Remove -> D8 Flow Directions
     * -> D8 Contributing Area -> Stream Definition by Threshold -> Move Outlets To Streams
     * -> Peuker Douglas -> Stream Reach and Watershed
     * </p>
     *
     * @param elevationGrid the elevation grid
     * @param outlets       the outlets
     * @param threshold     the threshold, if null, use Stream Drop Analysis to find the optimal threshold
     * @param outputDir     the output dir
     * @return Watershed_Grid.tif
     */
    public static String watershedDelineation(@NotBlank String elevationGrid, String outlets, Double threshold, String outputDir) {
        TauDEMAnalysis tauDEMAnalysis = TauDEMAnalysis.getInstance();
        tauDEMAnalysis.init(outputDir);

        long start = System.currentTimeMillis();
        log.info("------------------Watershed Delineation Begin---------------");

        String inputDir = outputDir;

        log.debug("-----------generating hydrologically correct DEM-----------");
        // pit remove/fill
        PitRemoveParams felParams = new PitRemoveParams.Builder(elevationGrid).build();

        tauDEMAnalysis.pitRemove(felParams);
        String pitFilledElevation = felParams.getPitFilledElevation();

        // D8 Flow Directions
        D8FlowDirectionsParams d8FlowDirParams = new D8FlowDirectionsParams.Builder(pitFilledElevation).build();
        tauDEMAnalysis.d8FlowDirections(d8FlowDirParams);

        String d8FlowDirection = d8FlowDirParams.getD8FlowDirection();
        String d8Slope = d8FlowDirParams.getD8Slope();

        // D8 Contributing Area
        D8ContributingAreaParams ad8Params = new D8ContributingAreaParams.Builder(d8FlowDirection).outlets(outlets).build();
        tauDEMAnalysis.d8ContributingArea(ad8Params);
        String d8ContributingArea = ad8Params.getD8ContributingArea();

        // Stream Drop Analysis
        // 使用 drop analysis 自动获取优化的阈值
        if (threshold == null || threshold <= 0) {
            String drp = "drp.txt";
            StreamDropAnalysisParams dropParams = new StreamDropAnalysisParams
                    .Builder(pitFilledElevation, d8FlowDirection, d8ContributingArea,
                    d8ContributingArea).outlets(outlets)
                    .dropAnalysisTextFile(drp).build();
            log.debug("----------------executing drop analysis to select optimal threshold------------");
            tauDEMAnalysis.streamDropAnalysis(dropParams);
            try {
                List<String> strings = Files.readLines(new File(outputDir + drp), Charset.forName("UTF-8"));
                // 取最后一行，格式参考 CSDMS_tauDEM_workshop.ppt Optimal Contributing Area Threshold
                threshold = Double.parseDouble(strings.get(strings.size() - 1).split(": ")[1]);
            } catch (Exception e) {
                log.error("Output_Drop_Analysis_Text_File: drp.txt not found");
                throw new BusinessException("Output_Drop_Analysis_Text_File: drp.txt not found");
            }
        }
        // Stream Definition By Threshold
        StreamDefinitionByThresholdParams thresholdParams =
                new StreamDefinitionByThresholdParams.Builder(d8ContributingArea).build();
        log.debug("threshold value: {} ", threshold);
        tauDEMAnalysis.streamDefinitionByThreshold(thresholdParams);
        String streamRaster = thresholdParams.getStreamRaster();

        if (StringUtils.isBlank(outlets)) {
            // Connect Down
            ConnectdownParams connectDownParams = new ConnectdownParams
                    .Builder(d8FlowDirection, d8ContributingArea).build();
            tauDEMAnalysis.connectdown(connectDownParams);
            outlets = connectDownParams.getOutletsFile();
        }
        // Move Outlets To Streams
        String movedOutlets = "moved_outlets_file.shp";
        MoveOutletsToStreamsParams toStreamsParams =
                new MoveOutletsToStreamsParams.Builder(d8FlowDirection, streamRaster,
                        outlets).outletsFile(movedOutlets).build();
        tauDEMAnalysis.moveOutletsToStreams(toStreamsParams);

        // Peuker Douglas
        PeukerDouglasParams peukerParams = new PeukerDouglasParams.Builder(elevationGrid).build();
        tauDEMAnalysis.peukerDouglas(peukerParams);
        String streamSource = peukerParams.getStreamSource();
        // Stream Reach And Watershed
        String watershedGrid = FilenameUtils.getBaseName(elevationGrid) + "_watershed_grid.tif";
        StreamReachAndWatershedParams watershedParams = new StreamReachAndWatershedParams.Builder(
                pitFilledElevation, d8FlowDirection, d8ContributingArea,
                streamSource).watershed(watershedGrid).build();
        ExecResult result = tauDEMAnalysis.streamReachAndWatershed(watershedParams);

        log.info("---Watershed Delineation done in:  " + String.valueOf(System.currentTimeMillis() - start) + " ms---");
        // 与 Watershed_Grid 不同的是包含了目录
        return watershedParams.getWatershed();
    }

    /**
     * Topographic Wetness Index Calculation
     * <p>Pit Remove  -> D-Infinity Flow Directions (and Slopes)
     * -> D-Infinity Contributing Area -> twi
     * </p>
     *
     * @param elevationGrid
     * @param outputDir
     * @return
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
        log.info("------------Topographic Wetness Index Calculation done in: " + String.valueOf(
                System.currentTimeMillis() - start) + " ms--------------");
        return twiParams.getWetnessIndex();
    }
}
