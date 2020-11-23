package org.egc.gis.gdal.dto;

/**
 * @author houzhiwei
 */
public class ResamplingMethods {
    /**
     * nearest neighbour resampling (default, fastest algorithm, worst interpolation quality).
     */
    public static final String NEAR = "near";
    /**
     * bilinear resampling.
     */
    public static final String BILINEAR = "bilinear";
    /**
     * cubic resampling.
     */
    public static final String CUBIC = "cubic";
    /**
     * cubic spline resampling.
     */
    public static final String CUBIC_SPLINE = "cubicspline";
    /**
     * Lanczos windowed sinc resampling.
     */
    public static final String LANCZOS = "lanczos";
    /**
     * average resampling, computes the weighted average of all non-NODATA contributing pixels.
     */
    public static final String AVERAGE = "average";
    /**
     * mode resampling, selects the value which appears most often of all the sampled points.
     */
    public static final String MODE = "mode";
    /**
     * maximum resampling, selects the maximum value from all non-NODATA contributing pixels.
     */
    public static final String MAX = "max";
    /**
     * minimum resampling, selects the minimum value from all non-NODATA contributing pixels.
     */
    public static final String MIN = "min";
    /**
     * median resampling, selects the median value of all non-NODATA contributing pixels.
     */
    public static final String MED = "med";
    /**
     * first quartile resampling, selects the first quartile value of all non-NODATA contributing pixels.
     */
    public static final String Q1 = "q1";
    /**
     * third quartile resampling, selects the third quartile value of all non-NODATA contributing pixels.
     */
    public static final String Q3 = "q3";
    /**
     * compute the weighted sum of all non-NODATA contributing pixels (since GDAL 3.1)
     */
    public static final String SUM = "sum";
}
