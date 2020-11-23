package org.egc.gis.proj;

import org.locationtech.proj4j.*;

/**
 * @author houzhiwei
 * @date 2020/11/22 15:04
 */
public class Transformer {

    /**
     * Transform coordinate.
     *
     * @param srcCoord  the source coordinate
     * @param sourceCrs the source crs: "authority:code"
     * @param targetCrs the target crs: "authority:code"
     * @return the transformed coordinate
     */
    public static ProjCoordinate transform(ProjCoordinate srcCoord, String sourceCrs, String targetCrs) {
        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem srcCrs = factory.createFromName(sourceCrs);
        CoordinateReferenceSystem destCrs = factory.createFromName(targetCrs);
        CoordinateTransform transform = new CoordinateTransformFactory().createTransform(srcCrs, destCrs);
        ProjCoordinate destCoord = new ProjCoordinate();
        transform.transform(srcCoord, destCoord);
        return destCoord;
    }


    /**
     * 坐标点转换
     *
     * @param sourceEPSG 源坐标EPSG码
     * @param targetEPSG 目标坐标EPSG码
     * @param x          源坐标 x
     * @param y          源坐标 y
     * @return 转换后坐标，通过{@link ProjCoordinate#x} 和 {@link ProjCoordinate#y} 获取值
     */
    public static ProjCoordinate transform(int sourceEPSG, int targetEPSG, double x, double y) {
        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CRSFactory csFactory = new CRSFactory();
        CoordinateReferenceSystem source = csFactory.createFromName("EPSG:" + sourceEPSG);
        CoordinateReferenceSystem target = csFactory.createFromName("EPSG:" + targetEPSG);
        CoordinateTransform trans = ctFactory.createTransform(source, target);
        ProjCoordinate sourceP = new ProjCoordinate();
        sourceP.x = x;
        sourceP.y = y;
        ProjCoordinate targetP = new ProjCoordinate();
        trans.transform(sourceP, targetP);
        return targetP;
    }

    /**
     * Transform extent double [].
     *
     * @param srcEpsg the src epsg
     * @param dstEpsg the dst epsg
     * @param minX    the min x
     * @param minY    the min y
     * @param maxX    the max x
     * @param maxY    the max y
     * @return the x,y of the lower left and upper right
     */
    public static double[] transformExtent(int srcEpsg, int dstEpsg, double minX, double minY, double maxX, double maxY) {
        ProjCoordinate ll = transform(srcEpsg, dstEpsg, minX, minY);
        ProjCoordinate ur = transform(srcEpsg, dstEpsg, maxX, maxY);
        return new double[]{ll.x, ll.y, ur.x, ur.y};
    }
}
