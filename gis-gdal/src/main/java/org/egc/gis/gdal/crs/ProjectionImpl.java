package org.egc.gis.gdal.crs;

import lombok.extern.slf4j.Slf4j;
import org.gdal.osr.SpatialReference;

/**
 * @author houzhiwei
 * @date 2020/5/13 20:58
 */
@Slf4j
public class ProjectionImpl implements Projection {
    @Override
    public int reproject(String srcFile, String dstFile, SpatialReference tSRS) {
        return 0;
    }

    @Override
    public int reproject2Utm(String srcFile, String dstFile) {
        return 0;
    }
}
