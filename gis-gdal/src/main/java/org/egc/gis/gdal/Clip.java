package org.egc.gis.gdal;

import org.egc.gis.gdal.dto.BoundingBox;

/**
 * clip raster/vector dataset use GDAL
 *
 * @author houzhiwei
 * @date 2020/5/2 18:27
 */
public interface Clip {

    /**
     * Clip by bounding box.
     *
     * @param src         the source file
     * @param dst         the destination file
     * @param boundingBox the bounding box
     * @param expand      To avoid no data around the edges, it is better to round up or down (according to which edge you are
     *                    on) by 0.2° or other values. Make sure you round outwards from your area rather than into it.
     * @return the boolean
     */
    boolean clipByBoundingBox(String src, String dst, BoundingBox boundingBox, double expand);

    /**
     * Clip by bounding box.
     * see {@link #clipByBoundingBox(String, String, BoundingBox, double)}
     *
     * @param src         the src
     * @param dst         the dst
     * @param boundingBox the bounding box
     * @return the boolean
     */
    boolean clipByBoundingBox(String src, String dst, BoundingBox boundingBox);

    /**
     * Clip by the bounding box of a vector file.
     *
     * @param src the src
     * @param dst the dst
     * @param shp the shapefile (vector file)
     * @return the boolean
     */
    boolean clipByVectorBoundingBox(String src, String dst, String shp);

    /**
     * Clip by polygon.
     *
     * @param src        the src
     * @param dst        the dst
     * @param polygonShp the polygon shapefile (vector file)
     * @return the boolean
     */
    boolean clipByPolygon(String src, String dst, String polygonShp);

    /**
     * Expand bounding box.<br/>
     * NOT if expand is 0
     *
     * @param boundingBox the bounding box
     * @param expand      the expand
     * @return the bounding box
     */
    default BoundingBox expandBoundingBox(BoundingBox boundingBox, double expand) {
        return boundingBox.expand(expand);
    }
}
