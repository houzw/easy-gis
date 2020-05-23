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
 * @date 2020-05-21T14:24:36+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class D8ExtremeUpslopeValueParams implements Params {
    private static final long serialVersionUID = 6443168184968635943L;

    /**
     * <pre>
     * d8FlowDirection
     * value
     * outlets
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
    private String extremeValue;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    // if no output filename provided
    public String getExtremeValue() {
        if (StringUtils.isBlank(extremeValue)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(d8FlowDirection, "extremeValue", "Raster Dataset", "tif"));
        }
        return extremeValue;
    }

    private D8ExtremeUpslopeValueParams(Builder builder) {
        d8FlowDirection = builder.d8FlowDirection;
        value = builder.value;
        useMaximumUpslopeValue = builder.useMaximumUpslopeValue;
        checkForEdgeContamination = builder.checkForEdgeContamination;
        outlets = builder.outlets;
        extremeValue = builder.extremeValue;
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

        public Builder d8FlowDirection(String val) {
            d8FlowDirection = val;
            return this;
        }

        public Builder value(String val) {
            value = val;
            return this;
        }

        public Builder useMaximumUpslopeValue(Boolean val) {
            useMaximumUpslopeValue = val;
            return this;
        }

        public Builder checkForEdgeContamination(Boolean val) {
            checkForEdgeContamination = val;
            return this;
        }

        public Builder outlets(String val) {
            outlets = val;
            return this;
        }

        public Builder extremeValue(String val) {
            extremeValue = val;
            return this;
        }

        public D8ExtremeUpslopeValueParams build() {
            return new D8ExtremeUpslopeValueParams(this);
        }
    }
}