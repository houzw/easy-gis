package org.egc.gis.gdal;

import org.egc.gis.gdal.raster.RasterIO;
import org.egc.gis.gdal.vector.VectorIO;

/**
 * Description:
 * <pre>
 * gdal/ogr io factory
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/11/13 10:25
 */
public class IOFactory {
    public static RasterIO createRasterIO() {
        return new RasterIO();
    }

    public static VectorIO createVectorIO() {
        return new VectorIO();
    }
}
