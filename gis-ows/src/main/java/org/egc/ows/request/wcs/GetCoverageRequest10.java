package org.egc.ows.request.wcs;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.exception.BusinessException;
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
public class GetCoverageRequest10 {

    private String coverage;
    private String crs;
    private String bbox;
    private String format="GTiff";

    private String responseCrs;
    private String time;
    private String interpolation;
    private String exceptions;

    private int height;
    private int width;
    private int depth;
    private double resx;
    private double resy;
    private double resz;

    public enum Format {
        NetCDF,
        HDF4,
        GeoTIFF,
        GTiff,
        GEOTIFF_RGB,
        GEOTIFF_INT16,
        JPEG2000
    }


    private GetCoverageRequest10(Builder builder) {
        coverage = builder.coverage;
        crs = builder.crs;
        bbox = builder.bbox;
        format = builder.format;
        responseCrs = builder.responseCrs;
        time = builder.time;
        height = builder.height;
        width = builder.width;
        resx = builder.resx;
        resy = builder.resy;
        resz = builder.resz;
    }


    public static final class Builder {
        private String coverage;
        private String crs;
        private String bbox;
        private String format;
        private String responseCrs;
        private String interpolation;
        private String exceptions;
        private String time;
        private int height;
        private int width;
        private double resx;
        private double resy;
        private double resz;

        /**
         * @param coverage Name of an available coverage, as stated in the GetCapabilities.
         *                 join with a comma (,) if more than one
         * @param format   Output format of map, e.g., NetCDF, as stated in the DescribeCoverage response.
         */
        public Builder(String coverage, String format) {
            this.coverage = coverage;
            this.format = format;
        }

        /**
         * minx,miny,maxx,maxy: Bounding box corners (lower left, upper right) in CRS units.
         * One of BBOX or TIME is required.
         *
         * @param minx the minx
         * @param miny the miny
         * @param maxx the maxx
         * @param maxy the maxy
         * @param epsg epsg_code. Coordinate Reference System in which the request is expressed.
         * @return the builder
         */
        public Builder bbox(double minx, double miny, double maxx, double maxy, int epsg) {
            this.bbox = minx + "," + miny + "," + maxx + "," + maxy;
            this.crs = "EPSG:" + epsg;
            return this;
        }


        /**
         * Response crs builder. optional
         * epsg_code.
         *
         * @param val Coordinate Reference System in which to express coverage responses.
         * @return builder
         */
        public Builder responseCrs(String val) {
            responseCrs = val;
            return this;
        }

        /**
         * "TIME=2016-01-01",
         * "TIME=2016-01-01/2016-02-01/P1D".
         *
         * @param val the val
         * @return builder
         */
        public Builder time(Date val) {
            time = OwsUtils.utcDatetimeStr(val);
            return this;
        }

        public Builder time(Date start, Date end) {
            time = OwsUtils.utcDatetimeStr(start) + "/" + OwsUtils.utcDatetimeStr(end);
            return this;
        }

        /**
         * Height builder.
         *
         * @param val Height in pixels of map picture. One of WIDTH/HEIGHT or RESX/Y is required.
         * @return builder
         */
        public Builder height(int val) {
            height = val;
            return this;
        }

        /**
         * Width builder.
         *
         * @param val Width in pixels of map picture. One of WIDTH/HEIGHT or RESX/Y is required.
         * @return builder
         */
        public Builder width(int val) {
            width = val;
            return this;
        }

        /**
         * Resx builder.
         *
         * @param val When requesting a georectified grid coverage, this requests a subset with a specific spatial resolution.            One of WIDTH/HEIGHT or RESX/Y is required
         * @return builder
         */
        public Builder resx(double val) {
            resx = val;
            return this;
        }

        /**
         * Resy builder.
         *
         * @param val When requesting a georectified grid coverage, this requests a subset with a specific spatial resolution.            One of WIDTH/HEIGHT or RESX/Y is required.
         * @return builder
         */
        public Builder resy(double val) {
            resy = val;
            return this;
        }

       /*
        public Builder resz(double val) {
            resz = val;
            return this;
        }
        */

        public GetCoverageRequest10 build() {
            if (StringUtils.isBlank(this.bbox) && StringUtils.isBlank(this.time)) {
                throw new BusinessException("One of BBOX or TIME is required.");
            }
            if (this.width <= 0 && this.height <= 0 && this.resy <= 0 && this.resx <= 0d) {
                throw new BusinessException("One of WIDTH/HEIGHT or RESX/Y is required.");
            }
            return new GetCoverageRequest10(this);
        }
    }
}
