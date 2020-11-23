package org.egc.ows.request.wcs;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.egc.ows.commons.OwsUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * <pre>
 * version 2.0.1
 * https://www.ogc.org/standards/wcs
 * https://mapserver.gis.umn.edu/ogc/wcs_server.html#wcs-2-0
 * https://doc.rasdaman.org/11_cheatsheets.html
 * </pre>
 *
 * @author houzhiwei
 * @date 2020/10/1
 */
@Data
public class GetCoverageRequest20 {
    private String coverageId;

    private String format;
    private WCSRequestBuilder.Version version = WCSRequestBuilder.Version.Version_201;
    /**
     * axis[,crs](low,high): This parameter subsets the coverage on the given axis.
     * This parameter can be given multiple times, but only once for each axis.
     * The optional sub-parameter crs can either be an EPSG definition (like EPSG:4326),
     * an URN or an URI or 'imageCRS' (which is the default). All crs sub-parameters from all SUBSET parameters must be equal.
     * (e.g: you cannot subset one axis in imageCRS and another in EPSG:4326).
     */
    private String subsetX;
    private String subsetY;

    private String subSettingCrs;
    /**
     * This parameter defines in which crs the output image should be expressed in.
     */
    private String outputCrs;

    private String mediaType;
    /**
     * With this parameter (a positive float) the size of the output image can be adjusted.
     * All axes are scaled equally.
     */
    private String scaleFactor;
    /**
     * axis(value)[,axis(value)]: With this parameter, an axis specific scaling can be applied.
     * Any axis not mentioned
     */
    private String scaleAxes;
    /**
     * axis(size)[,axis(size)]: This is similar to the SCALEAXES parameter,
     * but allows an axis specific setting of the absolute pixel size of the returned coverage.
     */
    private String scaleSize;
    /**
     * axis(min:max)[,axis(min:max)]: This parameter is treated like a
     * SCALESIZE parameter with "axis(max-min)".
     */
    private String scaleExtent;

    private String interpolation;
    /**
     * band1[,band2[,...]]: With this parameter, a selection of bands can be made.
     */
    private String rangeSubset;

    private String acceptVersions;
    private String sections;
    private String updateSequence;
    //not a standard parameter
    private String subsetDateTime;

    //    private String acceptFormats;

    private String acceptLanguages;
    private String mapserverMap;

    private GetCoverageRequest20(Builder builder) {
        coverageId = builder.coverageId;
        subsetX = builder.subsetX;
        subsetY = builder.subsetY;
        format = builder.format;
        rangeSubset = builder.rangeSubset;
        subSettingCrs = builder.subSettingCrs;
        interpolation = builder.interpolation;
        outputCrs = builder.outputCrs;
        scaleFactor = builder.scaleFactor;
        scaleAxes = builder.scaleAxes;
        scaleSize = builder.scaleSize;
        scaleExtent = builder.scaleExtent;
        acceptVersions = builder.acceptVersions;
        sections = builder.sections;
        updateSequence = builder.updateSequence;
//        acceptFormats = builder.acceptFormats;
        acceptLanguages = builder.acceptLanguages;
        subsetDateTime = builder.subsetDateTime;
        mapserverMap = builder.mapserverMap;
        mediaType = builder.mediaType;
    }


    public static final class Builder {

        private String coverageId;
        private String mapserverMap;
        private String subsetX;
        private String subsetY;
        private String subsetDateTime;
        private String format;
        private String rangeSubset;
        private String subSettingCrs;
        private String interpolation;
        private String outputCrs;
        private String scaleFactor;
        private String mediaType;

        /**
         * With this parameter (a positive float) the size of the output image can be adjusted.
         * All axes are scaled equally.
         */
        public Builder scaleFactor(String val) {
            this.scaleFactor = val;
            return this;
        }

        /**
         * MapServer map file. <br/>
         * Note: only for MapService WCS
         *
         * @param val map file name
         * @return the builder
         */
        public Builder mapserverMap(String val) {
            this.mapserverMap = val;
            return this;
        }

        /**
         * when multipart XML/image output is desired.<br/>
         * It should be set to true ('multipart/related') ( which is currently the only possible value for this parameter).<br/>
         * (GML header & image/tiff)
         *
         * @param val the val
         * @return builder
         */
        public Builder isSetMediaType(boolean val) {
            if (val) {
                this.mediaType = "multipart/related";
            }
            return this;
        }

        private String scaleAxes;

        /**
         * 缩放
         * axis(value)[,axis(value)]: With this parameter, an axis specific scaling can be applied.
         * Any axis not mentioned
         */
        public Builder scaleAxes(float xScale, float yScale) {
            List<String> axis = new ArrayList<>();
            if (xScale > 0) {
                axis.add("x(" + xScale + ")");
            }
            if (yScale > 0) {
                axis.add("y(" + yScale + ")");
            }
            this.scaleAxes = String.join(",", axis);
            return this;
        }

        private String scaleSize;

        /**
         * axis(size)[,axis(size)]: This is similar to the SCALEAXES parameter,
         * but allows an axis specific setting of the absolute pixel size of the returned coverage.
         */
        public Builder scaleSize(String val) {
            this.scaleSize = val;
            return this;
        }

        private String scaleExtent;

        /**
         * axis(min:max)[,axis(min:max)]: This parameter is treated like a
         * SCALESIZE parameter with "axis(max-min)".
         */
        public Builder scaleExtent(String val) {
            this.scaleExtent = val;
            return this;
        }

        private String acceptVersions;

        public Builder acceptVersions(String val) {
            this.acceptVersions = val;
            return this;
        }

        private String sections;

        public Builder sections(String val) {
            this.sections = val;
            return this;
        }

        private String updateSequence;

        public Builder updateSequence(String val) {
            this.updateSequence = val;
            return this;
        }

        /*
        private String acceptFormats;

        */
        /**
         * Accept formats builder.
         * This parameter is currently ignored.
         *//*
        public Builder acceptFormats(String val) {
            this.acceptFormats = val;
            return this;
        }
        */

        private String acceptLanguages;

        public Builder acceptLanguages(String val) {
            this.acceptLanguages = val;
            return this;
        }

        /**
         * <pre/>
         *
         * @param coverageId     the coverageId
         * @param formatMimeType e.g., image/tiff
         */
        public Builder(String coverageId, String formatMimeType) {
            this.coverageId = coverageId;
            this.format = formatMimeType;
        }

        /**
         * Instantiates a new Builder.
         * default format is image/tiff
         *
         * @param coverageId the coverage id
         */
        public Builder(String coverageId) {
            this.coverageId = coverageId;
            this.format = "image/tiff";
        }


        /**
         * Output format (mime type) of grid product, as stated in the GetCapabilities.
         * <p>
         * netCDF: applications/netcdf <br/>
         * JPEG2000: image/jp2 <br/>
         * GeoTIFF: image/tiff <br/>
         * XML: application/gml+xml <br/>
         * PNG: image/png
         *
         * @param formatMimeType the val {@link org.egc.commons.web.GeoMimeTypes}
         * @return the builder
         */
        public Builder format(String formatMimeType) {
            this.format = formatMimeType;
            return this;
        }

        /**
         * Output crs builder.
         *
         * @param val the val, e.g., http://www.opengis.net/def/crs/EPSG/0/32611
         * @return the builder
         */
        public Builder outputCrs(String val) {
            this.outputCrs = val;
            return this;
        }

        public Builder outputEpsg(int epsg) {
            this.outputCrs = "http://www.opengis.net/def/crs/EPSG/0/" + epsg;
            return this;
        }

        /**
         * Possible values are "NEAREST", "BILINEAR" and "AVERAGE".
         *
         * @param val the val
         * @return the builder
         */
        public Builder interpolation(String val) {
            this.interpolation = val;
            return this;
        }

        public Builder subsetLongLat(double lon, double lat) {
            this.subsetX = "Lat(" + lat + ")";
            this.subsetY = "Long(" + lon + ")";
            return this;
        }

        /**
         * Subset xy builder.
         *
         * @param minx           the minx
         * @param miny           the miny
         * @param maxx           the maxx
         * @param maxy           the maxy
         * @param subSettingEpsg the sub setting epsg.  This parameter defines the crs subsetting all SUBSETs are expressed in,
         *                       * and also the output org.geotools.referencing.CRS if no OUTPUTCRS is specified. By default all
         *                       * subsets are interpreted to be relative to the coverages CRS.
         * @return the builder
         */
        public Builder subsetXY(double minx, double miny, double maxx, double maxy, int subSettingEpsg) {
            this.subsetX = "x(" + minx + "," + maxx + ")";
            this.subsetY = "y(" + miny + "," + maxy + ")";
            this.subSettingCrs = "EPSG:" + subSettingEpsg;
            return this;
        }

        /**
         * Subset xy builder.
         *
         * @param minx          the minx
         * @param miny          the miny
         * @param maxx          the maxx
         * @param maxy          the maxy
         * @param subSettingCrs the sub setting crs. This parameter defines the crs subsetting all SUBSETs are expressed in,
         *                      and also the output org.geotools.referencing.CRS if no OUTPUTCRS is specified. By default all
         *                      subsets are interpreted to be relative to the coverages CRS.
         * @return the builder
         */
        public Builder subsetXY(double minx, double miny, double maxx, double maxy, String subSettingCrs) {
            this.subsetX = "x(" + minx + "," + maxx + ")";
            this.subsetY = "y(" + miny + "," + maxy + ")";
            this.subSettingCrs = subSettingCrs;
            return this;
        }

        /**
         * Subset lat long range builder.
         * srs 同 DescribeCoverage 获得的 CoverageDescriptions 中的 gml:Envelope srsName
         *
         * @param minLat the min lat, miny
         * @param minLon the min lon, minx
         * @param maxLat the max lat, maxy
         * @param maxLon the max lon, maxx
         * @return the builder
         */
        public Builder subsetLatLong(double minLat, double minLon, double maxLat, double maxLon) {
            this.subsetX = "Lat(" + minLat + "," + maxLat + ")";
            this.subsetY = "Long(" + minLon + "," + maxLon + ")";
            return this;
        }

        public Builder subsetLatLong(double minLat, double minLon, double maxLat, double maxLon, int epsg) {
            this.subsetX = "Lat(" + minLat + "," + maxLat + ")";
            this.subsetY = "Long(" + minLon + "," + maxLon + ")";
            this.subSettingCrs = "EPSG:" + epsg;
            return this;
        }

        public Builder subsetTimeRange(Date start, Date end) {
            this.subsetDateTime = "ansi(\"" + OwsUtils.utcDatetimeStr(start) + "\",\"" + OwsUtils.utcDatetimeStr(end) + "\")";
            return this;
        }

        public Builder subsetDateTime(Date dateTime) {
            this.subsetDateTime = "time=(\"" + OwsUtils.utcDatetimeStr(dateTime) + "\"";
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

        public GetCoverageRequest20 build() {
            return new GetCoverageRequest20(this);
        }

    }
}
