package org.egc.gis.gdal;

import lombok.extern.slf4j.Slf4j;
import org.egc.commons.exception.BusinessException;
import org.egc.gis.gdal.raster.RasterIO;
import org.egc.gis.gdal.vector.VectorIO;
import org.gdal.gdal.Dataset;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description:
 * <pre>
 * gdal/ogr io factory
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/11/13 10:25
 */
@Slf4j
public class IOFactory {
    public static RasterIO createRasterIO() {
        return new RasterIO();
    }

    public static VectorIO createVectorIO() {
        return new VectorIO();
    }

    public static List<Path> listFiles(String fileDir, String[] filenameFilters) throws IOException {
        List<Path> collect;
        try (Stream<Path> stream = Files.walk(Paths.get(fileDir))) {
            collect = stream.map(Path::normalize)
                    .filter(Files::isRegularFile)
                    .filter(path -> {
                        boolean b = false;
                        for (String filter : filenameFilters) {
                            b = path.getFileName().toString().toLowerCase().contains(filter.toLowerCase());
                            if (b) {
                                return b;
                            }
                        }
                        return b;
                    })
                    .collect(Collectors.toList());
        }
        return collect;
    }

    /**
     * list all files in a given directory and all of its sub-directories.
     *
     * @param dir                the given directory
     * @param filenameFilters    filename filter conditions
     * @param filenameFilterEnum how to check file name: startWith, endWith, contains
     * @return all file paths
     * @throws IOException
     */
    public static List<Path> listFiles(String dir, String[] filenameFilters, FilenameFilterEnum filenameFilterEnum) throws IOException {
        try (Stream<Path> stream = Files.walk(Paths.get(dir))) {
            return stream.map(Path::normalize)
                    .filter(Files::isRegularFile)
                    .filter(path -> {
                        boolean b = false;
                        for (String filter : filenameFilters) {
                            switch (filenameFilterEnum) {
                                case END_WITH:
                                    b = path.getFileName().toString().toLowerCase().endsWith(filter.toLowerCase());
                                    break;
                                case START_WITH:
                                    b = path.getFileName().toString().toLowerCase().startsWith(filter.toLowerCase());
                                    break;
                                case CONTAINS:
                                default:
                                    b = path.getFileName().toString().toLowerCase().contains(filter.toLowerCase());
                            }
                            if (b) {
                                return b;
                            }
                        }
                        return b;
                    }).collect(Collectors.toList());
        }
    }

    public enum FilenameFilterEnum {
        START_WITH, END_WITH, CONTAINS
    }

    /**
     * Closes the given {@link Dataset}.
     *
     * @param ds {@link Dataset} to close.
     */
    public static void closeDataSet(Dataset ds) {
        if (ds == null) {
            throw new NullPointerException("The provided dataset is null");
        }
        try {
            ds.delete();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            throw new BusinessException(e.getLocalizedMessage());
        }
    }
}
