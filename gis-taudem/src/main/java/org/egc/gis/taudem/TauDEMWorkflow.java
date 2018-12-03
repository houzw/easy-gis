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
     * @param Elevation_Grid the elevation grid
     * @param outlets        the outlets
     * @param threshold      the threshold, if null, use Stream Drop Analysis to find the optimal threshold
     * @param outputDir      the output dir
     * @return Watershed_Grid.tif
     */
    public static String watershedDelineation(@NotBlank String Elevation_Grid, String outlets, Double threshold,
                                              String outputDir)
    {
        TauDEMAnalysis tauDEMAnalysis = TauDEMAnalysis.getInstance();
        tauDEMAnalysis.init(outputDir);

        long start = System.currentTimeMillis();
        log.info("------------------Watershed Delineation Begin---------------");

        String inputDir = outputDir;

        log.debug("-----------generating hydrologically correct DEM-----------");
        // pit remove/fill
        PitRemoveParams felParams = new PitRemoveParams.Builder(Elevation_Grid).build();

        tauDEMAnalysis.PitRemove(felParams);
        String Pit_Removed_Elevation_Grid = felParams.getOutput_Pit_Removed_Elevation_Grid();

        // D8 Flow Directions
        D8FlowDirectionsParams d8FlowDirParams = new D8FlowDirectionsParams.Builder(Pit_Removed_Elevation_Grid).build();
        tauDEMAnalysis.D8FlowDirections(d8FlowDirParams);

        String D8_Flow_Direction_Grid = d8FlowDirParams.getOutput_D8_Flow_Direction_Grid();
        String D8_Slope_Grid = d8FlowDirParams.getOutput_D8_Slope_Grid();

        // D8 Contributing Area
        D8ContributingAreaParams ad8Params = new D8ContributingAreaParams.Builder(D8_Flow_Direction_Grid).Input_Outlets(
                outlets).build();
        tauDEMAnalysis.D8ContributingArea(ad8Params);
        String D8_Contributing_Area_Grid = ad8Params.getOutput_D8_Contributing_Area_Grid();

        // Stream Drop Analysis
        // 使用 drop analysis 自动获取优化的阈值
        if (threshold == null || threshold <= 0) {
            String drp = "drp.txt";
            StreamDropAnalysisParams dropParams = new StreamDropAnalysisParams
                    .Builder(Pit_Removed_Elevation_Grid, D8_Flow_Direction_Grid, D8_Contributing_Area_Grid,
                             D8_Contributing_Area_Grid).Input_Outlets(outlets)
                    .Output_Drop_Analysis_Text_File(drp).build();
            log.debug("----------------executing drop analysis to select optimal threshold------------");
            tauDEMAnalysis.StreamDropAnalysis(dropParams);
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
                new StreamDefinitionByThresholdParams.Builder(D8_Contributing_Area_Grid).build();
        log.debug("threshold value: {} ", threshold);
        tauDEMAnalysis.StreamDefinitionByThreshold(thresholdParams);
        String Stream_Raster_Grid = thresholdParams.getOutput_Stream_Raster_Grid();

        if (StringUtils.isBlank(outlets)) {
            // Connect Down
            ConnectDownParams connectDownParams = new ConnectDownParams
                    .Builder(D8_Flow_Direction_Grid, D8_Contributing_Area_Grid).build();
            tauDEMAnalysis.ConnectDown(connectDownParams);
            outlets = connectDownParams.getOutput_Outlets_file();
        }
        // Move Outlets To Streams
        String moved_outlets = "moved_outlets_file.shp";
        MoveOutletsToStreamsParams toStreamsParams =
                new MoveOutletsToStreamsParams.Builder(D8_Flow_Direction_Grid, Stream_Raster_Grid,
                                                       outlets).Output_Outlets_file(moved_outlets).build();
        tauDEMAnalysis.MoveOutletsToStreams(toStreamsParams);

        // Peuker Douglas
        PeukerDouglasParams peukerParams = new PeukerDouglasParams.Builder(Elevation_Grid).build();
        tauDEMAnalysis.PeukerDouglas(peukerParams);
        String Stream_Source_Grid = peukerParams.getOutput_Stream_Source_Grid();
        // Stream Reach And Watershed
        String Watershed_Grid = FilenameUtils.getBaseName(Elevation_Grid) + "_Watershed_Grid.tif";
        StreamReachAndWatershedParams watershedParams =
                new StreamReachAndWatershedParams.Builder(Pit_Removed_Elevation_Grid, D8_Flow_Direction_Grid,
                                                          D8_Contributing_Area_Grid,
                                                          Stream_Source_Grid).Output_Watershed_Grid(
                        Watershed_Grid).build();
        ExecResult result = tauDEMAnalysis
                .StreamReachAndWatershed(watershedParams);

        log.info("---Watershed Delineation done in:  " + String.valueOf(System.currentTimeMillis() - start) + " ms---");
        // 与 Watershed_Grid 不同的是包含了目录
        return watershedParams.getOutput_Watershed_Grid();
    }

    /**
     * Topographic Wetness Index Calculation
     * <p>Pit Remove  -> D-Infinity Flow Directions (and Slopes)
     * -> D-Infinity Contributing Area -> twi
     * </p>
     *
     * @param Elevation_Grid
     * @param outputDir
     * @return
     */
    public static String twi(@NotBlank String Elevation_Grid, String outputDir)
    {
        TauDEMAnalysis tauDEMAnalysis = TauDEMAnalysis.getInstance();
        tauDEMAnalysis.init(outputDir);

        long start = System.currentTimeMillis();
        log.info("----------Topographic Wetness Index Calculation Begin------------");

        String inputDir = outputDir;
        log.debug("----------------generating hydrologically correct DEM------------");
        PitRemoveParams felParams = new PitRemoveParams.Builder(Elevation_Grid).build();
        ;
        tauDEMAnalysis.PitRemove(felParams);
        String Pit_Removed_Grid = felParams.getOutput_Pit_Removed_Elevation_Grid();

        //generating D-Infinity Flow Directions
        DInfinityFlowDirectionsParams dInfFlowDirParams = new DInfinityFlowDirectionsParams.Builder(
                Pit_Removed_Grid).build();
        tauDEMAnalysis.DInfinityFlowDirections(dInfFlowDirParams);
        String Dinf_Flow_Direction_Grid = dInfFlowDirParams.getOutput_DInfinity_Flow_Direction_Grid();
        String DInf_Slope_Grid = dInfFlowDirParams.getOutput_DInfinity_Slope_Grid();

        //calculating D-Infinity Contributing Area
        DInfinityContributingAreaParams dInfCAParams = new DInfinityContributingAreaParams.Builder(
                Dinf_Flow_Direction_Grid).build();
        tauDEMAnalysis.DInfinityContributingArea(dInfCAParams);
        String DInf_SCArea = dInfCAParams.getOutput_DInfinity_Specific_Catchment_Area_Grid();


        //calculating Topographic Wetness Index
        TopographicWetnessIndexParams twiParams = new TopographicWetnessIndexParams.Builder(DInf_SCArea,
                                                                                            DInf_Slope_Grid).build();
        tauDEMAnalysis.TopographicWetnessIndex(twiParams);
        log.info("------------Topographic Wetness Index Calculation done in: " + String.valueOf(
                System.currentTimeMillis() - start) + " ms--------------");
        return twiParams.getOutput_Wetness_Index_Grid();
    }
}
