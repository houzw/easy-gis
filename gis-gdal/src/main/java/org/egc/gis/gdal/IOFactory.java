package org.egc.gis.gdal;

import org.egc.gis.gdal.impl.RasterIO;
import org.egc.gis.gdal.impl.VectorIO;

/**
 * Description:
 * <pre>
 * gdal io factory
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/11/13 10:25
 */
public class IOFactory {
    public static RasterIO rasterIO() {
        return new RasterIO();
    }

    public static VectorIO vectorIO() {
        return new VectorIO();
    }
}
