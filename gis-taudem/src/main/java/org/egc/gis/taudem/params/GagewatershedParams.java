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
 * Wrapper of parameters of gagewatershed
 *
 * @author houzhiwei
 * @date 2020-06-28T12:02:31+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class GagewatershedParams implements Params {
    /**
     * <pre>     * d8FlowDirection     * gagesFile
     *  </pre>
     *
     * @see Builder#Builder(String, String)
     */
    public GagewatershedParams() {
    }

    /**
     * A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     */
    @NotNull
    private String d8FlowDirection;

    /**
     * A point feature defining the gages(outlets) to which watersheds will be delineated.
     */
    @NotNull
    private String gagesFile;

    /**
     * This output grid identifies each gage watershed.
     */
    @NotNull
    private String gagewatershed;

    /**
     *
     */
    private String downstreamIdentefier;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getGagewatershed() {
        // if no output filename provided
        if (StringUtils.isBlank(gagewatershed)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(d8FlowDirection, "gagewatershed", "Raster Dataset", "tif"));
        }
        return this.gagewatershed;
    }

    public String getDownstreamIdentefier() {
        // if no output filename provided
        if (StringUtils.isBlank(downstreamIdentefier)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(d8FlowDirection, "downstreamIdentefier", "File", "txt"));
        }
        return this.downstreamIdentefier;
    }

    private GagewatershedParams(Builder builder) {
        this.d8FlowDirection = builder.d8FlowDirection;
        this.gagesFile = builder.gagesFile;
        this.gagewatershed = builder.gagewatershed;
        this.downstreamIdentefier = builder.downstreamIdentefier;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String d8FlowDirection;
        private String gagesFile;
        private String gagewatershed;
        private String downstreamIdentefier;

        /**
         * @param d8FlowDirection A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
         * @param gagesFile       A point feature defining the gages(outlets) to which watersheds will be delineated.
         */
        public Builder(@NotBlank String d8FlowDirection, @NotBlank String gagesFile) {
            this.d8FlowDirection = d8FlowDirection;
            this.gagesFile = gagesFile;
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
         * @param val A point feature defining the gages(outlets) to which watersheds will be delineated.
         */
        public Builder gagesFile(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.gagesFile = val;
            }
            return this;
        }

        /**
         * @param val This output grid identifies each gage watershed.
         */
        public Builder gagewatershed(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.gagewatershed = val;
            }
            return this;
        }

        /**
         * @param val
         */
        public Builder downstreamIdentefier(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.downstreamIdentefier = val;
            }
            return this;
        }

        public GagewatershedParams build() {
            return new GagewatershedParams(this);
        }
    }
}