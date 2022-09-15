package org.egc.gis.whitebox;

import com.google.common.collect.LinkedHashMultimap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.ExecResult;
import org.egc.gis.whitebox.params.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>WhiteboxTools
 *
 * @author houzhiwei & wangyijie
 * @date 2020-05-24T17:24:32+08:00
 */
@Slf4j
public class WhiteboxTools extends BaseWhitebox {

    /**
     * SingletonHolder is loaded on the first execution of Singleton.getInstance()
     * or the first access to SingletonHolder.INSTANCE, not before.
     * <p>此实现方式可保证単例的延迟加载和线程安全</p>
     */
    private static class SingletonHolder {
        private static final WhiteboxTools INSTANCE = new WhiteboxTools();
    }

    public static WhiteboxTools getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private WhiteboxTools() {
    }


    /**
     * breachDepressions
     * <p> Breaches all of the depressions in a DEM using Lindsay's (2016) algorithm. This should be preferred over depression filling in most cases..
     *
     * @param params {@link BreachDepressionsParams}
     * @return {@link ExecResult}
     */
    public ExecResult breachDepressions(BreachDepressionsParams params) {
        log.debug("Start breachDepressions...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // Input raster DEM file
        files.put("-i=", params.getDem());
        // Output raster file
        outFiles.put("-o=", params.getFilledDem());
        // Optional maximum breach depth (default is Inf)
        extraParams.put("--max_depth=", params.getMaxDepth());
        // Optional maximum breach channel length (in grid cells; default is Inf)
        extraParams.put("--max_length=", params.getMaxLength());
        ExecResult result = run("-r=BreachDepressions", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("breachDepressions finished");
        } else {
            log.error("breachDepressions failed!");
        }
        return result;
    }

    /**
     * d8Pointer
     * <p> Calculates a D8 flow pointer raster from an input DEM..
     *
     * @param params {@link D8PointerParams}
     * @return {@link ExecResult}
     */
    public ExecResult d8Pointer(D8PointerParams params) {
        log.debug("Start d8Pointer...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // Input raster DEM file
        files.put("-i=", params.getFilledDem());
        // Output raster file
        outFiles.put("-o=", params.getFlowDirectionDem());
        // D8 pointer uses the ESRI style scheme
        extraParams.put("--esri_pntr=", params.getEsriPntr());
        ExecResult result = run("-r=D8Pointer", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("d8Pointer finished");
        } else {
            log.error("d8Pointer failed!");
        }
        return result;
    }

    /**
     * fd8FlowAccumulation
     * <p> Calculates an FD8 flow accumulation raster from an input DEM..
     *
     * @param params {@link Fd8FlowAccumulationParams}
     * @return {@link ExecResult}
     */
    public ExecResult fd8FlowAccumulation(Fd8FlowAccumulationParams params) {
        log.debug("Start fd8FlowAccumulation...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // Input raster DEM file
        files.put("-i=", params.getFlowDirectionDem());
        // Output raster file
        outFiles.put("-o=", params.getFlowAccumulationDem());
        // Output type; one of 'cells', 'specific contributing area' (default), and
        extraParams.put("--out_type=", params.getOutType());
        // Optional exponent parameter; default is 1.1
        extraParams.put("--exponent=", params.getExponent());
        // Optional convergence threshold parameter, in grid cells; default is inifinity
        extraParams.put("--threshold=", params.getThreshold());
        // Optional flag to request the output be log-transformed
        extraParams.put("--log", params.getLog());
        // Optional flag to request clipping the display max by 1%
        extraParams.put("--clip", params.getClip());
        ExecResult result = run("-r=FD8FlowAccumulation", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("fd8FlowAccumulation finished");
        } else {
            log.error("fd8FlowAccumulation failed!");
        }
        return result;
    }

    /**
     * extractStreams
     * <p> Extracts stream grid cells from a flow accumulation raster..
     *
     * @param params {@link ExtractStreamsParams}
     * @return {@link ExecResult}
     */
    public ExecResult extractStreams(ExtractStreamsParams params) {
        log.debug("Start extractStreams...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // Input raster D8 flow accumulation file
        files.put("--flow_accum=", params.getFlowAccumulationDem());
        // Output raster file
        outFiles.put("-o=", params.getStreamDem());
        // Threshold in flow accumulation values for channelization
        extraParams.put("--threshold=", params.getThreshold());
        // Flag indicating whether a background value of zero should be used
        extraParams.put("--zero_background", params.getZeroBackground());
        ExecResult result = run("-r=ExtractStreams", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("extractStreams finished");
        } else {
            log.error("extractStreams failed!");
        }
        return result;
    }

    /**
     * hillslopes
     * <p> Identifies the individual hillslopes draining to each link in a stream network..
     *
     * @param params {@link HillslopesParams}
     * @return {@link ExecResult}
     */
    public ExecResult hillslopes(HillslopesParams params) {
        log.debug("Start hillslopes...");
        Map<String, String> files = new LinkedHashMap<>();
        Map<String, String> outFiles = new LinkedHashMap<>();
        LinkedHashMultimap<String, Object> extraParams = LinkedHashMultimap.<String, Object>create();
        if (StringUtils.isNotBlank(workspace)) {
            params.setOutputDir(workspace);
        }
        // Input raster D8 pointer file
        files.put("--d8_pntr=", params.getFlowDirectionDem());
        // Input raster streams file
        files.put("--streams=", params.getStreamDem());
        // Output raster file
        outFiles.put("-o=", params.getHillslopesDem());
        // D8 pointer uses the ESRI style scheme
        extraParams.put("--esri_pntr=", params.getEsriPntr());
        ExecResult result = run("-r=Hillslopes", files, outFiles, extraParams, params.getOutputDir());
        result.setParams(params);
        if (result.getSuccess()) {
            log.debug("hillslopes finished");
        } else {
            log.error("hillslopes failed!");
        }
        return result;
    }
}