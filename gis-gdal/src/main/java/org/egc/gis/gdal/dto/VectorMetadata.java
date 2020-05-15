package org.egc.gis.gdal.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.egc.commons.util.Formatter;

import java.io.Serializable;

/**
 * @author houzhiwei
 */
@Slf4j
@Data
@Deprecated
public class VectorMetadata implements Serializable {
    private String name;
    private String encoding;
    /**
     * e.g. 1 ogr.wkbPoint
     */
    private int geomType;
    private String geometry;
    private long featureCount;
    private int layerCount;
    /**
     * Coordinate Reference System Identify
     */
    private String crs;
    private String crsProj4;
    private String crsWkt;
    /**
     * epsg
     */
    private Integer srid;
    private String format;
    private String unit;

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
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
     * left + " " + buttom + " " + right + " " + top
     *
     * @return
     */
    public String getExtent() {
        return Formatter.formatDoubleStr(minX, 4) + " " + Formatter.formatDoubleStr(minY, 4)
                + " " + Formatter.formatDoubleStr(maxX, 4) + " " + Formatter.formatDoubleStr(maxY, 4);
    }

    /**
     * Get upper left .
     *
     * @return [minX, maxY]
     */
    public double[] getUpperLeft() {
        return new double[]{minX, maxY};
    }

    /**
     * Get upper right.
     *
     * @return [maxX, maxY]
     */
    public double[] getUpperRight() {
        return new double[]{maxX, maxY};
    }

    /**
     * Get lower left.
     *
     * @return [minX, minY]
     */
    public double[] getLowerLeft() {
        return new double[]{minX, minY};
    }

    /**
     * Get lower right:  .
     *
     * @return the [minX, minY]
     */
    public double[] getLowerRight() {
        return new double[]{minX, minY};
    }
}
