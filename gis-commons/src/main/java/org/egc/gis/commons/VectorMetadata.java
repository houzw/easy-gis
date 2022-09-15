package org.egc.gis.commons;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.egc.commons.util.Formatter;

import java.io.Serializable;

/**
 * @author houzhiwei
 */
@Slf4j
@Data
public class VectorMetadata implements Serializable {
    private String name;
    private String encoding;
    /**
     * e.g. 1 ogr.wkbPoint
     */
    private int geomType;
    private String geometry;
    private String shapeEncoding;
    /**
     * required in WFS
     */
    private String featureIdField;
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
    private String semantic;
    private String unit;
    private boolean isProjected = false;

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

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
