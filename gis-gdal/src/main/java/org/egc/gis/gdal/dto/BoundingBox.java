package org.egc.gis.gdal.dto;

/**
 * Description:
 * 左下右上
 * <pre>
 * BoundingBox:
 *  (min_x, min_y, max_x, max_y)
 *  (lower_left_x, lower_left_y, upper_right_x, upper_right_y)
 *  (bottom_left_lon,bottom_left_lat,top_right_lon, top_right_lat)
 * </pre>
 * https://docs.geotools.org/stable/userguide/library/main/envelope.html
 *
 * @author houzhiwei
 * @date 2019 /10/14 22:11
 */
public class BoundingBox {
    private double minX;
    private double minY;
    private double maxX;
    private double maxY;

    private double lowerLeftX;
    private double lowerLeftY;
    private double upperRightX;
    private double upperRightY;

    private double bottomLeftLon;
    private double bottomLeftLat;
    private double topRightLon;
    private double topRightLat;

    // private int epsg = 4326;

    /**
     * Instantiates a new Bounding box.
     *
     * @param minX min_x, lower_left_x, bottom_left_lon
     * @param minY min_y, lower_left_y, bottom_left_lat
     * @param maxX max_x, upper_right_x, top_right_lon
     * @param maxY max_y, upper_right_y, top_right_lat
     */
    public BoundingBox(double minX, double minY, double maxX, double maxY) {
        this.setMinX(minX);
        this.setMinY(minY);
        this.setMaxX(maxX);
        this.setMaxY(maxY);
    }

    /**
     * @return "minX minY maxX maxY"
     */
    public String toMinMaxString() {
        return this.minX + " " + this.minY + " " + this.maxX + " " + this.maxY;
    }

    public double getMinX() {
        return minX;
    }

    /**
     * minX/lowerLeftX/bottomLeftLon
     *
     * @param minX the min x
     */
    public void setMinX(double minX) {
        this.minX = minX;
        this.lowerLeftX = minX;
        this.bottomLeftLon = minX;
    }

    public double getMinY() {
        return minY;
    }


    /**
     * Sets min y.
     *
     * @param minY the min y
     */
    public void setMinY(double minY) {
        this.minY = minY;
        this.upperRightY = minY;
        this.topRightLat = minY;
    }

    public double getMaxX() {
        return maxX;
    }

    /**
     * Sets max x.
     *
     * @param maxX the max x
     */
    public void setMaxX(double maxX) {
        this.maxX = maxX;
        this.upperRightX = maxX;
        this.topRightLon = maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    /**
     * Sets m gax y.
     *
     * @param maxY the max y
     */
    public void setMaxY(double maxY) {
        this.maxY = maxY;
        this.bottomLeftLat = maxY;
        this.lowerLeftY = maxY;
    }

    public double getLowerLeftX() {
        return lowerLeftX;
    }

    /**
     * minX/upperLeftX/topLeftLon
     *
     * @param lowerLeftX the lower left x
     */
    public void setLowerLeftX(double lowerLeftX) {
        this.minX = lowerLeftX;
        this.lowerLeftX = lowerLeftX;
        this.bottomLeftLon = lowerLeftX;
    }

    public double getLowerLeftY() {
        return lowerLeftY;
    }

    /**
     * Sets upper left y.
     *
     * @param lowerLeftY the upper left y
     */
    public void setLowerLeftY(double lowerLeftY) {
        this.maxY = lowerLeftY;
        this.bottomLeftLat = lowerLeftY;
        this.lowerLeftY = lowerLeftY;
    }

    public double getUpperRightX() {
        return upperRightX;
    }

    /**
     * Sets lower right x.
     *
     * @param upperRightX the lower right x
     */
    public void setUpperRightX(double upperRightX) {
        this.upperRightX = upperRightX;
        this.maxX = upperRightX;
        this.topRightLon = upperRightX;
    }

    public double getUpperRightY() {
        return upperRightY;
    }

    /**
     * Sets lower right y.
     *
     * @param upperRightY the lower right y
     */
    public void setUpperRightY(double upperRightY) {
        this.minY = upperRightY;
        this.upperRightY = upperRightY;
        this.topRightLat = upperRightY;
    }

    public double getBottomLeftLon() {
        return bottomLeftLon;
    }

    /**
     * minX/lowerLeftX/bottomLeftLon
     *
     * @param bottomLeftLon the bottom left lon
     */
    public void setBottomLeftLon(double bottomLeftLon) {
        this.minX = bottomLeftLon;
        this.lowerLeftX = bottomLeftLon;
        this.bottomLeftLon = bottomLeftLon;
    }

    public double getBottomLeftLat() {
        return bottomLeftLat;
    }

    /**
     * Sets top left lat.
     *
     * @param bottomLeftLat the top left lat
     */
    public void setBottomLeftLat(double bottomLeftLat) {
        this.maxY = bottomLeftLat;
        this.bottomLeftLat = bottomLeftLat;
        this.lowerLeftY = bottomLeftLat;
    }

    public double getTopRightLon() {
        return topRightLon;
    }

    /**
     * Sets bottom right lon.
     *
     * @param topRightLon the bottom right lon
     */
    public void setTopRightLon(double topRightLon) {
        this.topRightLon = topRightLon;
        this.upperRightX = topRightLon;
        this.maxX = topRightLon;
    }

    /**
     * Gets bottom right lat.
     *
     * @return the bottom right lat
     */
    public double getTopRightLat() {
        return topRightLat;
    }

    /**
     * Sets bottom right lat.
     *
     * @param topRightLat the bottom right lat
     */
    public void setTopRightLat(double topRightLat) {
        this.minY = topRightLat;
        this.upperRightY = topRightLat;
        this.topRightLat = topRightLat;
    }

    /**
     * To avoid no data around the edges, it is better to round up or down (according to which edge you are
     * on) by 0.2° or other values. Make sure you round outwards from your area rather than into it.
     *
     * @param val 0.2 or other values
     * @return bounding box
     */
    public BoundingBox expand(double val) {
        double minx = minX - val;
        double miny = minY - val;
        double maxx = maxX + val;
        double maxy = maxY + val;
        return new BoundingBox(minx, miny, maxx, maxy);
    }

    /**
     * 左上右下
     * Upper-left lower-right
     * <pre>
     * "ulx uly lrx lry"<br/>
     * "upperLeftX upperLeftY lowerRightX lowerRightY"<br/>
     * "minX maxY maxX minY"
     * @return "ulx uly lrx lry"
     */
    public String toUllrString() {
        return this.minX + " " + this.maxY + " " + this.maxX + " " + this.minY;
    }

    /**
     * 左下右上
     * lower-left upper-right
     * @return string
     */
    public String toLlurString() {
        return this.minX + " " + this.minY + " " + this.maxX + " " + this.maxY;
    }
}
