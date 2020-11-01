package org.egc.gis.gdal.dto;

/**
 * Description:
 * <pre>
 * BoundingBox:
 *  (min_x, max_y, max_x, min_y)
 *  (upper_left_x, upper_left_y, lower_right_x, lower_right_y)
 *  (top_left_lon, top_left_lat, bottom_right_lon, bottom_right_lat)
 * </pre>
 * https://docs.geotools.org/stable/userguide/library/main/envelope.html
 * @author houzhiwei
 * @date 2019 /10/14 22:11
 */
public class BoundingBox {
    private double minX;
    private double minY;
    private double maxX;
    private double maxY;

    private double upperLeftX;
    private double upperLeftY;
    private double lowerRightX;
    private double lowerRightY;

    private double topLeftLon;
    private double topLeftLat;
    private double bottomRightLon;
    private double bottomRightLat;

    // private int epsg = 4326;

    /**
     * @param minX min_x, upper_left_x, top_left_lon
     * @param maxY max_y, upper_left_y, top_left_lat
     * @param maxX max_x, lower_right_x, bottom_right_lon
     * @param minY min_y, lower_right_y, bottom_right_lat
     */
    public BoundingBox(double minX, double maxY, double maxX, double minY) {
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
     * minX/upperLeftX/topLeftLon
     *
     * @param minX
     */
    public void setMinX(double minX) {
        this.minX = minX;
        this.upperLeftX = minX;
        this.topLeftLon = minX;
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
        this.lowerRightY = minY;
        this.bottomRightLat = minY;
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
        this.lowerRightX = maxX;
        this.bottomRightLon = maxX;
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
        this.topLeftLat = maxY;
        this.upperLeftY = maxY;
    }

    public double getUpperLeftX() {
        return upperLeftX;
    }

    /**
     * minX/upperLeftX/topLeftLon
     *
     * @param upperLeftX
     */
    public void setUpperLeftX(double upperLeftX) {
        this.minX = upperLeftX;
        this.upperLeftX = upperLeftX;
        this.topLeftLon = upperLeftX;
    }

    public double getUpperLeftY() {
        return upperLeftY;
    }

    /**
     * Sets upper left y.
     *
     * @param upperLeftY the upper left y
     */
    public void setUpperLeftY(double upperLeftY) {
        this.maxY = upperLeftY;
        this.topLeftLat = upperLeftY;
        this.upperLeftY = upperLeftY;
    }

    public double getLowerRightX() {
        return lowerRightX;
    }

    /**
     * Sets lower right x.
     *
     * @param lowerRightX the lower right x
     */
    public void setLowerRightX(double lowerRightX) {
        this.lowerRightX = lowerRightX;
        this.maxX = lowerRightX;
        this.bottomRightLon = lowerRightX;
    }

    public double getLowerRightY() {
        return lowerRightY;
    }

    /**
     * Sets lower right y.
     *
     * @param lowerRightY the lower right y
     */
    public void setLowerRightY(double lowerRightY) {
        this.minY = lowerRightY;
        this.lowerRightY = lowerRightY;
        this.bottomRightLat = lowerRightY;
    }

    public double getTopLeftLon() {
        return topLeftLon;
    }

    /**
     * minX/upperLeftX/topLeftLon
     *
     * @param topLeftLon
     */
    public void setTopLeftLon(double topLeftLon) {
        this.minX = topLeftLon;
        this.upperLeftX = topLeftLon;
        this.topLeftLon = topLeftLon;
    }

    public double getTopLeftLat() {
        return topLeftLat;
    }

    /**
     * Sets top left lat.
     *
     * @param topLeftLat the top left lat
     */
    public void setTopLeftLat(double topLeftLat) {
        this.maxY = topLeftLat;
        this.topLeftLat = topLeftLat;
        this.upperLeftY = topLeftLat;
    }

    public double getBottomRightLon() {
        return bottomRightLon;
    }

    /**
     * Sets bottom right lon.
     *
     * @param bottomRightLon the bottom right lon
     */
    public void setBottomRightLon(double bottomRightLon) {
        this.bottomRightLon = bottomRightLon;
        this.lowerRightX = bottomRightLon;
        this.maxX = bottomRightLon;
    }

    /**
     * Gets bottom right lat.
     *
     * @return the bottom right lat
     */
    public double getBottomRightLat() {
        return bottomRightLat;
    }

    /**
     * Sets bottom right lat.
     *
     * @param bottomRightLat the bottom right lat
     */
    public void setBottomRightLat(double bottomRightLat) {
        this.minY = bottomRightLat;
        this.lowerRightY = bottomRightLat;
        this.bottomRightLat = bottomRightLat;
    }

    /**
     * To avoid no data around the edges, it is better to round up or down (according to which edge you are
     * on) by 0.2° or other values. Make sure you round outwards from your area rather than into it.
     *
     * @param val 0.2 or other values
     * @return
     */
    public BoundingBox expand(double val) {
        val = Math.abs(val);
        double newTopLeftLon = topLeftLon - val;
        double newTopLeftLat = topLeftLat + val;
        double newBottomRightLon = bottomRightLon + val;
        double newBottomRightLat = bottomRightLat - val;
        return new BoundingBox(newTopLeftLon, newTopLeftLat, newBottomRightLon, newBottomRightLat);
    }

    /**
     * <pre>
     * "ulx uly lrx lry"<br/>
     * "upperLeftX upperLeftY lowerRightX lowerRightY"<br/>
     * "minX maxY maxX minY"
     * @return "ulx uly lrx lry"
     */
    public String toXYString() {
        return this.upperLeftX + " " + this.upperLeftY + " " + this.lowerRightX + " " + this.lowerRightY;
    }

}
