package org.egc.gis.gdal.crs;

import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;
import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.CoordinateTransformFactory;
import org.osgeo.proj4j.ProjCoordinate;

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
     * 坐标点转换
     *
     * @param sourceEPSG 源坐标EPSG码
     * @param targetEPSG 目标坐标EPSG码
     * @param x          源坐标 x
     * @param y          源坐标 y
     * @return 转换后坐标，通过{@link ProjCoordinate#x} 和 {@link ProjCoordinate#y} 获取值
     */
    public static ProjCoordinate transformByProj4(int sourceEPSG, int targetEPSG, double x, double y) {
        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CRSFactory csFactory = new CRSFactory();
        CoordinateReferenceSystem source = csFactory.createFromName("EPSG:" + sourceEPSG);
        CoordinateReferenceSystem target = csFactory.createFromName("EPSG:" + targetEPSG);
        org.osgeo.proj4j.CoordinateTransform trans = ctFactory.createTransform(source, target);
        ProjCoordinate sourceP = new ProjCoordinate();
        sourceP.x = x;
        sourceP.y = y;
        ProjCoordinate targetP = new ProjCoordinate();
        trans.transform(sourceP, targetP);
        return targetP;
    }


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
}
