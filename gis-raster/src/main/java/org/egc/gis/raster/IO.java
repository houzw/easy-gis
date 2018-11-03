package org.egc.gis.raster;

import org.egc.commons.raster.RasterMetadata;

/**
 * Description:
 * <pre>
 * read, write, update raster file(s)
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/10/26 17:02
 */
public interface IO {
    /**
     * Gets raster metadata.
     *
     * @param rasterFile the raster file
     * @return the metadata
     */
    RasterMetadata getMetadata(String rasterFile);

    String vector2Raster(String vectorFile);
}
