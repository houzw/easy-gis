package org.egc.gis.gdal.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 栅格数据元数据
 *
 * @author houzhiwei
 * @date 2018 /8/28 18:55
 */
@Data
@Deprecated
public class RasterMetadata implements Serializable {

    /**
     * Coordinate Reference System Identify
     */
    private String crs;
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
    private double nodata;
    private String format;
    private double maxValue;
    private double minValue;
    private double meanValue;
    /**
     * sample standard deviation
     */
    private double stdev;

    /**
     * upper_left_x
     */
    private double minX;
    /**
     * upper_left_y
     */
    private double maxY;

    /**
     * lower_right_x
     */
    private double maxX;
    /**
     * lower_right_y
     */
    private double minY;

    /**
     * minx
     */
    private double upperLeftX;
    /**
     * maxx
     */
    private double lowerRightX;
    /**
     * maxy
     */
    private double upperLeftY;

    /**
     * miny
     */
    private double lowerRightY;

    private double centerX;
    private double centerY;
    private double pixelSize;
    /**
     * 高度
     */
    private double height;

    private double width;
    /**
     * 像素个数
     */
    private int sizeHeight;
    private int sizeWidth;

    private String unit;
    private double[] upperLeft;
    private double[] upperRight;
    private double[] lowerLeft;
    private double[] lowerRight;

    public void setMinX(double minX) {
        this.minX = minX;
        this.upperLeftX = minX;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
        this.upperLeftY = maxY;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
        this.lowerRightX = maxX;
    }

    public void setMinY(double minY) {
        this.minY = minY;
        this.lowerRightY = minY;
    }

    public void setUpperLeftX(double upperLeftX) {
        this.upperLeftX = upperLeftX;
        this.minX = upperLeftX;
    }

    public void setLowerRightX(double lowerRightX) {
        this.lowerRightX = lowerRightX;
        this.maxX = lowerRightX;
    }

    public void setUpperLeftY(double upperLeftY) {
        this.upperLeftY = upperLeftY;
        this.maxY = upperLeftY;
    }

    public void setLowerRightY(double lowerRightY) {
        this.lowerRightY = lowerRightY;
        this.minY = lowerRightY;
    }

    /**
     * Get upper left .
     *
     * @return [minX, maxY]
     */
    public double[] getUpperLeft() {
        this.upperLeft = new double[]{minX, maxY};
        return upperLeft;
    }

    /**
     * Get upper right.
     *
     * @return [maxX, maxY]
     */
    public double[] getUpperRight() {
        this.upperRight = new double[]{maxX, maxY};
        return upperRight;
    }

    /**
     * Get lower left.
     *
     * @return [minX, minY]
     */
    public double[] getLowerLeft() {
        this.lowerLeft = new double[]{minX, minY};
        return lowerLeft;
    }

    /**
     * Get lower right:  .
     *
     * @return the [minX, minY]
     */
    public double[] getLowerRight() {
        this.lowerRight = new double[]{minX, minY};
        return lowerRight;
    }

}
