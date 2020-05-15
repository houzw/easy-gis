package org.egc.gis.gdal;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.gis.gdal.dto.GDALDriversEnum;

import java.io.File;
import java.io.IOException;

/**
 * GDAL IO
 *
 * @author houzhiwei
 * @date 2020/5/3 21:07
 */
public interface FormatConversion {
    /**
     * Format convert dataset.
     *
     * @param srcFile the src file
     * @param dstFile the dst file
     * @param format  the format
     * @return dstFile if success, else null
     * @throws IOException the io exception
     */
    String formatConvert(String srcFile, String dstFile, GDALDriversEnum format) throws IOException;

    /**
     * Format convert dataset.
     *
     * @param srcFile the src file
     * @param dstFile the dst file
     * @return dstFile if success, else null
     * @throws IOException the io exception
     */
    String formatConvert(String srcFile, String dstFile) throws IOException;

    /**
     * Output name string.
     *
     * @param srcFile the src file
     * @param dstFile the dst file, can be null
     * @param ext     the ext
     * @return the string
     */
    default String outputName(String srcFile, String dstFile, String ext) {
        String newName;
        String inPath = FilenameUtils.getFullPath(srcFile);
        String outPath = FilenameUtils.getFullPath(dstFile);
        if (StringUtils.isBlank(dstFile)) {
            newName = inPath + File.separator + FilenameUtils.getBaseName(srcFile) + "." + ext;
        } else {
            if (StringUtils.isBlank(outPath)) {
                newName = inPath + File.separator + FilenameUtils.getBaseName(dstFile) + "." + ext;
            } else {
                newName = outPath + File.separator + FilenameUtils.getBaseName(dstFile) + "." + ext;
            }
        }
        return newName;
    }
}
