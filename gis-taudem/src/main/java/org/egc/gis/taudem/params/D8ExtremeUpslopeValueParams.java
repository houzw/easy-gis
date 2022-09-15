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
 * Wrapper of parameters of d8ExtremeUpslopeValue
 *
 * @author houzhiwei
 * @date 2020-06-28T12:02:31+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class D8ExtremeUpslopeValueParams implements Params {
    /**
     * <pre>     * d8FlowDirection     * value     * outlets
     *  </pre>
     *
     * @see Builder#Builder(String, String)
     */
    public D8ExtremeUpslopeValueParams() {
    }

    /**
     * A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     */
    @NotNull
    private String d8FlowDirection;

    /**
     * This is the grid of values of which the maximum or minimum upslope value is selected.
     */
    @NotNull
    private String value;


    /**
     * default is true.
     * A flag to indicate whether the maximum or minimum upslope value is to be calculated.
     */
    @XmlElement(defaultValue = "true")
    private Boolean useMaximumUpslopeValue = true;


    /**
     * default is true.
     * A flag that indicates whether the tool should check for edge contamination.
     */
    @XmlElement(defaultValue = "true")
    private Boolean checkForEdgeContamination = true;


    /**
     * A point feature defining outlets of interest.
     */
    private String outlets;

    /**
     * A grid of the maximum/minimum upslope values.
     */
    @NotNull
    private String extremeValue;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getExtremeValue() {
        // if no output filename provided
        if (StringUtils.isBlank(extremeValue)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(d8FlowDirection, "extremeValue", "Raster Dataset", "tif"));
        }
        return this.extremeValue;
    }

    private D8ExtremeUpslopeValueParams(Builder builder) {
        this.d8FlowDirection = builder.d8FlowDirection;
        this.value = builder.value;
        this.useMaximumUpslopeValue = builder.useMaximumUpslopeValue;
        this.checkForEdgeContamination = builder.checkForEdgeContamination;
        this.outlets = builder.outlets;
        this.extremeValue = builder.extremeValue;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String d8FlowDirection;
        private String value;
        private Boolean useMaximumUpslopeValue;
        private Boolean checkForEdgeContamination;
        private String outlets;
        private String extremeValue;

        /**
         * @param d8FlowDirection A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
         * @param value           This is the grid of values of which the maximum or minimum upslope value is selected.
         */
        public Builder(@NotBlank String d8FlowDirection, @NotBlank String value) {
            this.d8FlowDirection = d8FlowDirection;
            this.value = value;
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
         * @param val This is the grid of values of which the maximum or minimum upslope value is selected.
         */
        public Builder value(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.value = val;
            }
            return this;
        }

        /**
         * default value is  true
         *
         * @param val A flag to indicate whether the maximum or minimum upslope value is to be calculated.
         */
        public Builder useMaximumUpslopeValue(Boolean val) {

            if (val != null) {
                this.useMaximumUpslopeValue = val;
            }
            return this;
        }

        /**
         * default value is  true
         *
         * @param val A flag that indicates whether the tool should check for edge contamination.
         */
        public Builder checkForEdgeContamination(Boolean val) {

            if (val != null) {
                this.checkForEdgeContamination = val;
            }
            return this;
        }

        /**
         * @param val A point feature defining outlets of interest.
         */
        public Builder outlets(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.outlets = val;
            }
            return this;
        }

        /**
         * @param val A grid of the maximum/minimum upslope values.
         */
        public Builder extremeValue(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.extremeValue = val;
            }
            return this;
        }

        public D8ExtremeUpslopeValueParams build() {
            return new D8ExtremeUpslopeValueParams(this);
        }
    }
}