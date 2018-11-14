package org.egc.gis.gdal;

/**
 * Description:
 * <pre>
 * read, write, update raster file(s)
 * </pre>
 *
 * @author houzhiwei
 * @date 2018 /10/26 17:02
 */
public interface IO {

    /**
     * read raster/vector and get data set/data source
     * <p>readonly</p>
     *
     * @param file path to raster/vector file
     * @return dataset
     */
    Object read(String file);

    /**
     * Read raster/vector for update.
     *
     * @param file the raster/vector file
     * @return
     */
    Object read4Update(String file);

}
