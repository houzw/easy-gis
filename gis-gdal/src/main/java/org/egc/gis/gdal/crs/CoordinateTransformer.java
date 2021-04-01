package org.egc.gis.gdal.crs;

import org.gdal.gdal.gdal;
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
 * @date 2018 /9/24 16:17
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

    public static double[] transform(SpatialReference source, SpatialReference dest, double x, double y) {
        CoordinateTransformation coordinateTransformation = new CoordinateTransformation(source, dest);
        return coordinateTransformation.TransformPoint(x, y);
    }

    public static double[] transformExtent(SpatialReference src, SpatialReference dst, double minX, double minY, double maxX, double maxY) {
        double[] ll = transform(src, dst, minX, minY);
        double[] ur = transform(src, dst, maxX, maxY);
        return new double[]{ll[0], ll[1], ur[0], ur[1]};
    }

    /**
     * Transform to lat long. <br/>
     * 投影坐标转换为相应的经纬度坐标
     *
     * @param proj the  projection
     * @param x    the x
     * @param y    the y
     * @return the double [ ]
     * @see <a href="https://trac.osgeo.org/gdal/browser/trunk/gdal/swig/java/apps/gdalinfo.java">gdalinfo</a>
     */
    public static double[] transformToLatLong(SpatialReference proj, double x, double y) {
        SpatialReference hLatLong = proj.CloneGeogCS();
        CoordinateTransformation transformation = CoordinateTransformation.CreateCoordinateTransformation(proj, hLatLong);
        proj.delete();
        double[] transPoint = new double[3];
        transformation.TransformPoint(transPoint, x, y, 0);
        hLatLong.delete();
        return transPoint;
    }


    /**
     * Transform to degree, minute, second <br/>
     * 转换为度分秒形式
     *
     * @param val  the val
     * @param axis the axis, "Long" or "Lat
     * @return String, e.g., 113d 0' 0.00"E
     * @see <a href="https://trac.osgeo.org/gdal/browser/trunk/gdal/swig/java/apps/gdalinfo.java">gdalinfo</a>
     */
    public static String decimalToDms(double val, String axis) {
        return gdal.DecToDMS(val, axis, 2);
    }

}
