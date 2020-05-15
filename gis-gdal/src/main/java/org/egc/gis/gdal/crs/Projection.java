package org.egc.gis.gdal.crs;

import org.gdal.osr.SpatialReference;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2018 /11/2 11:53
 */
public interface Projection {
    /**
     * <pre>
     * Re-project data.
     * https://gdal.org/programs/gdalwarp.html#gdalwarp
     * https://jgomezdans.github.io/gdal_notes/reprojection.html
     * https://pcjericks.github.io/py-gdalogr-cookbook/projection.html
     * </pre>
     *
     * @param srcFile the src file
     * @param dstFile the dst file
     * @param tSRS    the target srs
     * @return the int
     */
    int reproject(String srcFile, String dstFile, SpatialReference tSRS);

    /**
     * Reproject 2 utm.
     *
     * @param srcFile the src file
     * @param dstFile the dst file
     * @return the int
     */
    int reproject2Utm(String srcFile, String dstFile);


}
