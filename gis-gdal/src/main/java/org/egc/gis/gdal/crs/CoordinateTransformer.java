package org.egc.gis.gdal.crs;

import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

/**
 * Description:
 * <pre>
 * 坐标转换工具类
 * TODO rest web service
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/9/24 16:17
 */
public class CoordinateTransformer {


    /**
     * 使用 GDAL 转换坐标
     *
     * @param sourceEPSG      源 epsg
     * @param destinationEPSG 目标 epsg
     * @param x               x 坐标
     * @param y               y 坐标
     * @return [x, y]
     * @author lp
     */
    public static double[] transform(int sourceEPSG, int destinationEPSG, double x, double y) {
        SpatialReference source = new SpatialReference();
        source.ImportFromEPSG(sourceEPSG);
        SpatialReference destination = new SpatialReference();
        destination.ImportFromEPSG(destinationEPSG);
        CoordinateTransformation coordinateTransformation = new CoordinateTransformation(source, destination);
        return coordinateTransformation.TransformPoint(x, y);
    }

    public static double[] transformExtent(int srcEpsg, int dstEpsg, double minX, double minY, double maxX, double maxY) {
        double[] ll = transform(srcEpsg, dstEpsg, minX, minY);
        double[] ur = transform(srcEpsg, dstEpsg, maxX, maxY);
        return new double[]{ll[0], ll[1], ur[0], ur[1]};
    }

}
