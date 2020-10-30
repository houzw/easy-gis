package org.egc.ows.request.wcs;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.egc.ows.commons.OwsUtils;

import java.util.Date;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/16 21:13
 */
@Data
public class GetCoverageRequest11 {
    private String identifier;
    private String boundingBox;
    private String format = "image/tiff";
    private WCSRequestBuilder.Version version = WCSRequestBuilder.Version.Version_110;
    private String gridBaseCrs;
    private String gridCs;
    private String gridType;
    private String gridOrigin;
    private String gridOffsets;
    private String rangeSubset;
    private String time;

    private GetCoverageRequest11(Builder builder) {
        identifier = builder.identifier;
        boundingBox = builder.boundingBox;
        format = builder.format;
        gridBaseCrs = builder.gridBaseCrs;
        gridOrigin = builder.gridOrigin;
        rangeSubset = builder.rangeSubset;
        gridOffsets = builder.gridOffsets;
        gridCs = builder.gridCs;
        gridType = builder.gridType;
        time = builder.time;
    }

    public static final class Builder {
        /**
         * 1.1.0+
         */
        private String identifier;
        private String boundingBox;
        private String format;
        private String gridBaseCrs;
        private String gridCs;
        private String gridType = "urn:ogc:def:method:WCS:1.1:2dGridIn2dCrs:";
        private String gridOrigin;
        private String gridOffsets;
        private String rangeSubset;
        private String time;

        /**
         * <pre/>
         * WCS 1.1 follows EPSG defined axis/tuple ordering for geographic coordinate systems.
         * This means that coordinates reported, or provided in urn:ogc:def:EPSG::4326 (WGS84)
         * are actually handled as
         * <b> lat, long, not long, lat. </b>
         *
         * @param identifier     the identifier
         * @param boundingBox    特别注意：<b> 4326 坐标系下坐标为 【纬度, 经度】.而不是通常的 【经度,纬度 】</b>
         * @param formatMimeType e.g., image/tiff
         */
        public Builder(String identifier, String boundingBox, String formatMimeType) {
            this.identifier = identifier;
            this.boundingBox = boundingBox;
            this.format = formatMimeType;
        }

        /**
         * 1.1.0+
         *
         * @param minx   the minx
         * @param miny   the miny
         * @param maxx   the maxx
         * @param maxy   the maxy
         * @param crsURN e.g., urn:ogc:def:crs:EPSG::4326, The CRS is described using a URN.
         *               特别注意：<b> 4326 坐标系下坐标为 【纬度, 经度】.而不是通常的 【经度,纬度 】</b>
         * @return builder
         */
        public Builder boundingBox(double minx, double miny, double maxx, double maxy, String crsURN) {
            this.boundingBox = minx + "," + miny + "," + maxx + "," + maxy + "," + crsURN;
            return this;
        }

        /**
         * default crs: urn:ogc:def:crs:EPSG::4326
         * 特别注意：<b> 4326 坐标系下坐标为 【纬度, 经度】.而不是通常的 【经度,纬度 】</b>
         *
         * @param minx the minx
         * @param miny the miny
         * @param maxx the maxx
         * @param maxy the maxy
         * @return builder
         */
        public Builder boundingBox(double minx, double miny, double maxx, double maxy) {
            this.boundingBox = minx + "," + miny + "," + maxx + "," + maxy + ",urn:ogc:def:crs:EPSG::4326";
            return this;
        }

        /**
         * <pre/>
         * If an alternate spatial resolution is desired,
         * then the following set of keywords must be used to specify the
         * sample origin and step size of the output grid to be produced.
         * The produced grid will be of a number of pixels and lines as can be fit
         * in the BOUNDINGBOX starting at GridOrigin,
         * at GridOffsets resolution.
         * Grid base crs builder.
         *
         * @param val The grid base CRS (URN).
         * @return builder
         */
        public Builder gridBaseCrs(String val) {
            this.gridBaseCrs = val;
            return this;
        }

        public Builder time(Date date) {
            this.time = OwsUtils.utcDatetimeStr(date);
            return this;
        }

        public Builder time(Date start, Date end) {
            this.time = OwsUtils.utcDatetimeStr(start) + "/" + OwsUtils.utcDatetimeStr(end);
            return this;
        }

        /**
         * The grid CRS (URN).
         *
         * @param val the val
         * @return builder
         */
        public Builder gridCs(String val) {
            this.gridCs = val;
            return this;
        }

        /**
         * default value is urn:ogc:def:method:WCS:1.1:2dGridIn2dCrs:
         * This is the only supported value for MapServer.
         *
         * @param val the val
         * @return the builder
         */
        public Builder gridType(String val) {
            this.gridType = val;
            return this;
        }

        /**
         * Output format (mime type) of grid product, as stated in the GetCapabilities.
         *
         * @param val the val
         * @return builder
         */
        public Builder format(String val) {
            this.format = val;
            return this;
        }

        /**
         * The sample point for the top left pixel.
         *
         * @param xOrigin the x origin
         * @param yOrigin the y origin
         * @return builder
         */
        public Builder gridOrigin(double xOrigin, double yOrigin) {
            this.gridOrigin = xOrigin + "," + yOrigin;
            return this;
        }

        /**
         * The x and y step size for grid sampling (resolution). Both are positive.
         */
        public Builder gridOffsets(double xstep, double ystep) {
            this.gridOffsets = xstep + "," + ystep;
            return this;
        }

        /**
         * <pre/>
         * Selects a range subset, and interpolation method.
         * Currently only subsetting on bands are allowed.
         * Depending on rangeset names, this might take the form "BandsName[bands[1]]" to select band 1,
         * or "BandsName:bilinear[bands[1]]" to select band 1 with bilinear interpolation.
         */
        public Builder rangeSubset(String val) {
            this.rangeSubset = val;
            return this;
        }

        /**
         * Selects a range subset, and interpolation method.
         * Currently only subsetting on bands are allowed.
         *
         * @param interpolation interpolation method, e.g., bilinear. can be null or empty
         * @param band          the band index
         * @return the builder
         */
        public Builder rangeSubset(String interpolation, int band) {
            if (StringUtils.isNotBlank(interpolation)) {
                this.rangeSubset = "BandsName:" + interpolation + "[bands[" + band + "]]";
            } else {
                this.rangeSubset = "BandsName[bands[" + band + "]]";
            }
            return this;
        }

        public GetCoverageRequest11 build() {
            return new GetCoverageRequest11(this);
        }
    }
}
