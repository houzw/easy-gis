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
 * Wrapper of parameters of connectdown
 *
 * @author houzhiwei
 * @date 2020-06-28T12:02:31+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ConnectdownParams implements Params {
    /**
     * <pre>
     * d8FlowDirection
     * d8ContributingArea
     * watershed
     *  </pre>
     *
     * @see Builder#Builder(String, String)
     */
    public ConnectdownParams() {
    }

    /**
     * This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
     */
    @NotNull
    private String d8FlowDirection;

    /**
     * A grid giving the contributing area value in terms of the number of grid cells (or the summation of weights) for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it using the D8 algorithm.
     */
    @NotNull
    private String d8ContributingArea;

    /**
     * Watershed grid delineated from gage watershed function or streamreachwatershed function.
     */
    private String watershed;

    /**
     * Number of grid cells move to downstream based on flow directions.
     */
    private Long numberOfGridCells;

    /**
     * This output is point a OGR file where each point is created from watershed grid having the largest contributing area for each zone.
     */
    @NotNull
    private String outletsFile;

    /**
     * This output is a point OGR file where each outlet is moved downflow a specified number of grid cells using flow directions.
     */
    @NotNull
    private String movedoutletsFile;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getOutletsFile() {
        // if no output filename provided
        if (StringUtils.isBlank(outletsFile)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(d8FlowDirection, "outletsFile", "Feature Layer", "shp"));
        }
        return this.outletsFile;
    }

    public String getMovedoutletsFile() {
        // if no output filename provided
        if (StringUtils.isBlank(movedoutletsFile)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(d8FlowDirection, "movedoutletsFile", "Feature Layer", "shp"));
        }
        return this.movedoutletsFile;
    }

    private ConnectdownParams(Builder builder) {
        this.d8FlowDirection = builder.d8FlowDirection;
        this.d8ContributingArea = builder.d8ContributingArea;
        this.watershed = builder.watershed;
        this.numberOfGridCells = builder.numberOfGridCells;
        this.outletsFile = builder.outletsFile;
        this.movedoutletsFile = builder.movedoutletsFile;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String d8FlowDirection;
        private String d8ContributingArea;
        private String watershed;
        private Long numberOfGridCells;
        private String outletsFile;
        private String movedoutletsFile;

        /**
         * @param d8FlowDirection    This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
         * @param d8ContributingArea A grid giving the contributing area value in terms of the number of grid cells (or the summation of weights) for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it using the D8 algorithm.
         */
        public Builder(@NotBlank String d8FlowDirection, @NotBlank String d8ContributingArea) {
            this.d8FlowDirection = d8FlowDirection;
            this.d8ContributingArea = d8ContributingArea;
        }

        /**
         * @param val This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
         */
        public Builder d8FlowDirection(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.d8FlowDirection = val;
            }
            return this;
        }

        /**
         * @param val A grid giving the contributing area value in terms of the number of grid cells (or the summation of weights) for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it using the D8 algorithm.
         */
        public Builder d8ContributingArea(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.d8ContributingArea = val;
            }
            return this;
        }

        /**
         * @param val Watershed grid delineated from gage watershed function or streamreachwatershed function.
         */
        public Builder watershed(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.watershed = val;
            }
            return this;
        }

        /**
         * @param val Number of grid cells move to downstream based on flow directions.
         */
        public Builder numberOfGridCells(Long val) {

            if (val != null) {
                this.numberOfGridCells = val;
            }
            return this;
        }

        /**
         * @param val This output is point a OGR file where each point is created from watershed grid having the largest contributing area for each zone.
         */
        public Builder outletsFile(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.outletsFile = val;
            }
            return this;
        }

        /**
         * @param val This output is a point OGR file where each outlet is moved downflow a specified number of grid cells using flow directions.
         */
        public Builder movedoutletsFile(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.movedoutletsFile = val;
            }
            return this;
        }

        public ConnectdownParams build() {
            return new ConnectdownParams(this);
        }
    }
}