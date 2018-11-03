package org.egc.gis.taudem;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.egc.commons.command.AbstractRunCommand;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Description:
 * <pre>
 * Basic Utilities for TauDEM Analysis
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/10/25 21:32
 */
@Slf4j
public abstract class BaseTauDEM extends AbstractRunCommand {

    @Override
    public CommandLine initCmd() {
        CommandLine cmd = new CommandLine(TauDEMCommands.MPIEXEC);
        cmd.addArgument("-n");
        cmd.addArgument(String.valueOf(RunUtils.processors()));
        return cmd;
    }

    /**
     * 当输出数据名称为空时，根据输入数据设置其名称
     *
     * @param input     输入数据名称
     * @param output    输出数据名称
     * @param paramName 输出数据参数名称（如果不是使用模板生成，则需要通过反射或Spring 的 ParameterNameDiscoverer 获取）
     * @param fileType  输出数据文件类型
     * @return
     */
    protected static String outputNaming(String input, String output, String paramName, String fileType) {
        String extension = "";
        if (StringUtils.isNotBlank(fileType)) {
            switch (fileType) {
                case "Raster Layer":
                    extension = "tif";
                    break;
                case "Raster Dataset":
                    extension = "tif";
                    break;
                case "Feature Layer":
                    extension = "shp";
                    break;
                case "File":
                    extension = "txt";
                    break;
                case "DBFile":
                    extension = "dbf";
                    break;
                default:
                    extension = FilenameUtils.getExtension(input);
                    break;
            }
        }

        String baseName = FilenameUtils.getBaseName(input);
        StringBuilder stringBuilder = new StringBuilder();
        if (input.contains("_") && !input.startsWith("_")) {
            baseName = input.substring(0, input.indexOf("_"));
        }
        if (StringUtils.isBlank(output)) {
            stringBuilder.append(baseName);
            stringBuilder.append("_");
            stringBuilder.append(paramName.replaceFirst("Output_", ""));
            stringBuilder.append(".");
            stringBuilder.append(extension);
            output = stringBuilder.toString();
        }
        if (new File(output).exists()) {
            output = FilenameUtils.getBaseName(output) + "_" +
                    DateUtils.getFragmentInMilliseconds(new Date(), Calendar.MINUTE) + "." + extension;
        }
        return output;
    }


    /**
     * 处理 outlets 图层名称和编号
     *
     * @param params
     * @param layerName
     * @param layerNumber
     * @return
     */
    protected static Map layerNameAndNumber(Map params, String layerName, Integer layerNumber) {
        if (StringUtils.isNotBlank(layerName)) {
            //OGR layer name if outlets are not the first layer in outletfile (optional)
            params.put("-lyrname", layerName);
        } else if (layerNumber != null && layerNumber > -1) {
            //OGR layer number if outlets are not the first layer in outletfile (optional)
            params.put("-lyrno", String.valueOf(layerNumber));
        }
        return params;
    }
}
