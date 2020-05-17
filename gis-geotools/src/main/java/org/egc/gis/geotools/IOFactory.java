package org.egc.gis.geotools;

/**
 * Description:
 * <pre>
 * geotools io factory
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
