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
 * Wrapper of parameters of d8FlowDirections
 *
 * @author houzhiwei
 * @date 2020-06-28T12:02:31+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class D8FlowDirectionsParams implements Params {
    /**
     * <pre>     * pitFilledElevation
     *  </pre>
     *
     * @see Builder#Builder(String)
     */
    public D8FlowDirectionsParams() {
    }

    /**
     * A grid of elevation values.
     */
    @NotNull
    private String pitFilledElevation;


    /**
     * A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     */
    @XmlElement
    private String d8FlowDirection;


    /**
     * A grid giving slope in the D8 flow direction.
     */
    @XmlElement
    private String d8Slope;


    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getD8FlowDirection() {
        // if no output filename provided
        if (StringUtils.isBlank(d8FlowDirection)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(pitFilledElevation, "d8FlowDirection", "Raster Dataset", "tif"));
        }
        return this.d8FlowDirection;
    }

    public String getD8Slope() {
        // if no output filename provided
        if (StringUtils.isBlank(d8Slope)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(pitFilledElevation, "d8Slope", "Raster Dataset", "tif"));
        }
        return this.d8Slope;
    }

    private D8FlowDirectionsParams(Builder builder) {
        this.pitFilledElevation = builder.pitFilledElevation;
        this.d8FlowDirection = builder.d8FlowDirection;
        this.d8Slope = builder.d8Slope;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String pitFilledElevation;
        private String d8FlowDirection;
        private String d8Slope;

        /**
         * @param pitFilledElevation A grid of elevation values.
         */
        public Builder(@NotBlank String pitFilledElevation) {
            this.pitFilledElevation = pitFilledElevation;
        }

        /**
         * @param val A grid of elevation values.
         */
        public Builder pitFilledElevation(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.pitFilledElevation = val;
            }
            return this;
        }

        /**
         * default value is flowDirection
         *
         * @param val A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
         */
        public Builder d8FlowDirection(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.d8FlowDirection = val;
            }
            return this;
        }

        /**
         * default value is slope
         *
         * @param val A grid giving slope in the D8 flow direction.
         */
        public Builder d8Slope(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.d8Slope = val;
            }
            return this;
        }

        public D8FlowDirectionsParams build() {
            return new D8FlowDirectionsParams(this);
        }
    }
}