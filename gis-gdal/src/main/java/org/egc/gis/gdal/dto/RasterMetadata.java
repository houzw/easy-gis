package org.egc.gis.gdal.dto;

import lombok.Data;
import org.egc.commons.util.Formatter;
import org.gdal.osr.SpatialReference;

import java.io.Serializable;

/**
 * 栅格数据元数据
 *
 * @author houzhiwei
 * @date 2018 /8/28 18:55
 */
@Data
public class RasterMetadata implements Serializable {

    private static final long serialVersionUID = -634650075152957495L;
    /**
     * Coordinate Reference System Identify
     */
    private String crs;
    private SpatialReference srs;
    /**
     * Coordinate Reference System string in PROJ.4 format
     */
    private String crsProj4;
    /**
     * Coordinate Reference System string in WKT format
     */
    private String crsWkt;
    /**
     * 空间引用标识符 (spatial reference identifier)，
     * 通常为EPSG代码，如4326。
     * 参考：http://epsg.io
     */
    private Integer srid;
    private Integer epsg;
    private int bands;
    private double nodata;
    private String format;
    private String semantic;
    private double maxValue;
    private double minValue;
    private double meanValue;
    private String quantileBreaks;
    private String uniqueValues;
    private boolean isProjected = false;
    /**
     * sample standard deviation
     */
    private double stdev;
    /**
     * left/west
     */
    private double minX;
    /**
     * right/east
     */
    private double maxX;

    /**
     * left/west
     *
     * @return minx
     */
    public double getMinX() {
        return minX;
    }

    /**
     * right/east
     */
    public double getMaxX() {
        return maxX;
    }

    /**
     * bottom/south
     */
    public double getMinY() {
        return minY;
    }

    /**
     * upper/north
     */
    public double getMaxY() {
        return maxY;
    }

    /**
     * bottom/south
     */
    private double minY;
    /**
     * upper/north
     */
    private double maxY;
    private double centerX;
    private double centerY;
    private double pixelSize;
    /**
     * 高度
     */
    private double height;

    private double width;

    /**
     * @return rasterYSize
     */
    public int getSizeHeight() {
        return sizeHeight;
    }

    /**
     * @return rasterXSize
     */
    public int getSizeWidth() {
        return sizeWidth;
    }

    /**
     * 像素个数
     */
    private int sizeHeight;
    private int sizeWidth;

    private String unit;

    /**
     * extent of the data
     *
     * @return left(minx) + " " + buttom (miny) + " " + right(maxx) + " " + top(maxy)
     */
    public String getExtent() {
        return Formatter.formatDoubleStr(minX, 4) + " " + Formatter.formatDoubleStr(minY, 4)
                + " " + Formatter.formatDoubleStr(maxX, 4) + " " + Formatter.formatDoubleStr(maxY, 4);
    }

    private String extent;
    private double[] upperLeft;
    private double[] upperRight;
    private double[] lowerLeft;
    private double[] lowerRight;

    /**
     * Get upper left .
     *
     * @return [minX, maxY]
     */
    public double[] getUpperLeft() {
        upperLeft = new double[]{minX, maxY};
        return upperLeft;
    }

    /**
     * Get upper right.
     *
     * @return [maxX, maxY]
     */
    public double[] getUpperRight() {
        upperRight = new double[]{maxX, maxY};
        return upperRight;
    }

    /**
     * Get lower left.
     *
     * @return [minX, minY]
     */
    public double[] getLowerLeft() {
        lowerLeft = new double[]{minX, minY};
        return lowerLeft;
    }

    /**
     * Get lower right:  .
     *
     * @return the [minX, minY]
     */
    public double[] getLowerRight() {
        lowerRight = new double[]{minX, minY};
        return lowerRight;
    }

}
