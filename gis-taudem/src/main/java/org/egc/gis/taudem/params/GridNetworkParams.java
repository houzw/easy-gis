package org.egc.gis.taudem.params;

import lombok.Data;
import lombok.Setter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.Params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;

/**
 * Wrapper of parameters of gridNetwork
 *
 * @author houzhiwei
 * @date 2020-06-28T12:02:31+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class GridNetworkParams implements Params {
    /**
     * <pre>     * d8FlowDirection     * outlets     * mask
     *  </pre>
     *
     * @see Builder#Builder(String)
     */
    public GridNetworkParams() {
    }

    /**
     * A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     */
    @NotNull
    private String d8FlowDirection;

    /**
     * A point feature  defining the outlets of interest.
     */
    private String outlets;

    /**
     * A grid that is used to determine the domain do be analyzed.
     */
    private String mask;


    /**
     * default is 100d.
     * This input parameter is used in the calculation mask grid value >= mask threshold to determine if the grid cell is in the domain to be analyzed.
     */
    @XmlElement(defaultValue = "100")
    private Double maskThresholdValue = 100d;


    /**
     * A grid giving the Strahler order number for each cell.
     */
    @NotNull
    private String strahlerNetworkOrder;

    /**
     * A grid that gives the length of the longest upslope D8 flow path terminating at each grid cell.
     */
    @NotNull
    private String longestUpslopeLength;

    /**
     * The total upslope path length is the length of the entire D8 flow grid network upslope of each grid cell.
     */
    @NotNull
    private String totalUpslopeLength;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getStrahlerNetworkOrder() {
        // if no output filename provided
        if (StringUtils.isBlank(strahlerNetworkOrder)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(d8FlowDirection, "strahlerNetworkOrder", "Raster Dataset", "tif"));
        }
        return this.strahlerNetworkOrder;
    }

    public String getLongestUpslopeLength() {
        // if no output filename provided
        if (StringUtils.isBlank(longestUpslopeLength)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(d8FlowDirection, "longestUpslopeLength", "Raster Dataset", "tif"));
        }
        return this.longestUpslopeLength;
    }

    public String getTotalUpslopeLength() {
        // if no output filename provided
        if (StringUtils.isBlank(totalUpslopeLength)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(d8FlowDirection, "totalUpslopeLength", "Raster Dataset", "tif"));
        }
        return this.totalUpslopeLength;
    }

    private GridNetworkParams(Builder builder) {
        this.d8FlowDirection = builder.d8FlowDirection;
        this.outlets = builder.outlets;
        this.mask = builder.mask;
        this.maskThresholdValue = builder.maskThresholdValue;
        this.strahlerNetworkOrder = builder.strahlerNetworkOrder;
        this.longestUpslopeLength = builder.longestUpslopeLength;
        this.totalUpslopeLength = builder.totalUpslopeLength;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String d8FlowDirection;
        private String outlets;
        private String mask;
        private Double maskThresholdValue;
        private String strahlerNetworkOrder;
        private String longestUpslopeLength;
        private String totalUpslopeLength;

        /**
         * @param d8FlowDirection A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
         */
        public Builder(@NotBlank String d8FlowDirection) {
            this.d8FlowDirection = d8FlowDirection;
        }

        /**
         * @param val A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
         */
        public Builder d8FlowDirection(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.d8FlowDirection = val;
            }
            return this;
        }

        /**
         * @param val A point feature  defining the outlets of interest.
         */
        public Builder outlets(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.outlets = val;
            }
            return this;
        }

        /**
         * @param val A grid that is used to determine the domain do be analyzed.
         */
        public Builder mask(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.mask = val;
            }
            return this;
        }

        /**
         * default value is 100d
         *
         * @param val This input parameter is used in the calculation mask grid value >= mask threshold to determine if the grid cell is in the domain to be analyzed.
         */
        public Builder maskThresholdValue(Double val) {

            if (val != null) {
                this.maskThresholdValue = val;
            }
            return this;
        }

        /**
         * @param val A grid giving the Strahler order number for each cell.
         */
        public Builder strahlerNetworkOrder(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.strahlerNetworkOrder = val;
            }
            return this;
        }

        /**
         * @param val A grid that gives the length of the longest upslope D8 flow path terminating at each grid cell.
         */
        public Builder longestUpslopeLength(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.longestUpslopeLength = val;
            }
            return this;
        }

        /**
         * @param val The total upslope path length is the length of the entire D8 flow grid network upslope of each grid cell.
         */
        public Builder totalUpslopeLength(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.totalUpslopeLength = val;
            }
            return this;
        }

        public GridNetworkParams build() {
            return new GridNetworkParams(this);
        }
    }
}