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
 * Wrapper of parameters of streamDropAnalysis
 *
 * @author houzhiwei
 * @date 2020-06-28T12:02:30+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class StreamDropAnalysisParams implements Params {
    /**
     * <pre>     * pitFilledElevation     * d8FlowDirection     * d8ContributingArea     * accumulatedStreamSource     * outlets
     *  </pre>
     *
     * @see Builder#Builder(String, String, String, String)
     */
    public StreamDropAnalysisParams() {
    }

    /**
     * A grid of elevation values.
     */
    @NotNull
    private String pitFilledElevation;

    /**
     * A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     */
    @NotNull
    private String d8FlowDirection;

    /**
     * A grid of contributing area values for each cell that were calculated using the D8 algorithm.
     */
    @NotNull
    private String d8ContributingArea;

    /**
     * This grid must be monotonically increasing along the downslope D8 flow directions.
     */
    @NotNull
    private String accumulatedStreamSource;

    /**
     * A point feature defining the outlets upstream of which drop analysis is performed.
     */
    private String outlets;


    /**
     * default is 5d.
     * This parameter is the lowest end of the range searched for possible threshold values using drop analysis.
     */
    @XmlElement(defaultValue = "5")
    private Double minimumThresholdValue = 5d;


    /**
     * default is 500d.
     * This parameter is the highest end of the range searched for possible threshold values using drop analysis.
     */
    @XmlElement(defaultValue = "500")
    private Double maximumThresholdValue = 500d;


    /**
     * default is 10d.
     * The parameter is the number of steps to divide the search range into when looking for possible threshold values using drop analysis.
     */
    @XmlElement(defaultValue = "10")
    private Double numberOfThresholdValues = 10d;


    /**
     * default is true.
     * This checkbox indicates whether logarithmic or linear spacing should be used when looking for possible threshold values using drop ananlysis.
     */
    @XmlElement(defaultValue = "true")
    private Boolean useLogarithmicSpacingForThresholdValues = true;


    /**
     * This is a comma delimited text file with the following header line: Threshold, DrainDen, NoFirstOrd, NoHighOrd, MeanDFirstOrd, MeanDHighOrd, StdDevFirstOrd, StdDevHighOrd, T The file then contains one line of data for each threshold value examined, and then a summary line that indicates the optimum threshold value.
     */
    @XmlElement
    private String dropAnalysisTextFile;


    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getDropAnalysisTextFile() {
        // if no output filename provided
        if (StringUtils.isBlank(dropAnalysisTextFile)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(pitFilledElevation, "dropAnalysisTextFile", "Text File", "txt"));
        }
        return this.dropAnalysisTextFile;
    }

    private StreamDropAnalysisParams(Builder builder) {
        this.pitFilledElevation = builder.pitFilledElevation;
        this.d8FlowDirection = builder.d8FlowDirection;
        this.d8ContributingArea = builder.d8ContributingArea;
        this.accumulatedStreamSource = builder.accumulatedStreamSource;
        this.outlets = builder.outlets;
        this.minimumThresholdValue = builder.minimumThresholdValue;
        this.maximumThresholdValue = builder.maximumThresholdValue;
        this.numberOfThresholdValues = builder.numberOfThresholdValues;
        this.useLogarithmicSpacingForThresholdValues = builder.useLogarithmicSpacingForThresholdValues;
        this.dropAnalysisTextFile = builder.dropAnalysisTextFile;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String pitFilledElevation;
        private String d8FlowDirection;
        private String d8ContributingArea;
        private String accumulatedStreamSource;
        private String outlets;
        private Double minimumThresholdValue;
        private Double maximumThresholdValue;
        private Double numberOfThresholdValues;
        private Boolean useLogarithmicSpacingForThresholdValues;
        private String dropAnalysisTextFile;

        /**
         * @param pitFilledElevation      A grid of elevation values.
         * @param d8FlowDirection         A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
         * @param d8ContributingArea      A grid of contributing area values for each cell that were calculated using the D8 algorithm.
         * @param accumulatedStreamSource This grid must be monotonically increasing along the downslope D8 flow directions.
         */
        public Builder(@NotBlank String pitFilledElevation, @NotBlank String d8FlowDirection, @NotBlank String d8ContributingArea, @NotBlank String accumulatedStreamSource) {
            this.pitFilledElevation = pitFilledElevation;
            this.d8FlowDirection = d8FlowDirection;
            this.d8ContributingArea = d8ContributingArea;
            this.accumulatedStreamSource = accumulatedStreamSource;
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
         * @param val A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
         */
        public Builder d8FlowDirection(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.d8FlowDirection = val;
            }
            return this;
        }

        /**
         * @param val A grid of contributing area values for each cell that were calculated using the D8 algorithm.
         */
        public Builder d8ContributingArea(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.d8ContributingArea = val;
            }
            return this;
        }

        /**
         * @param val This grid must be monotonically increasing along the downslope D8 flow directions.
         */
        public Builder accumulatedStreamSource(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.accumulatedStreamSource = val;
            }
            return this;
        }

        /**
         * @param val A point feature defining the outlets upstream of which drop analysis is performed.
         */
        public Builder outlets(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.outlets = val;
            }
            return this;
        }

        /**
         * default value is 5d
         *
         * @param val This parameter is the lowest end of the range searched for possible threshold values using drop analysis.
         */
        public Builder minimumThresholdValue(Double val) {

            if (val != null) {
                this.minimumThresholdValue = val;
            }
            return this;
        }

        /**
         * default value is 500d
         *
         * @param val This parameter is the highest end of the range searched for possible threshold values using drop analysis.
         */
        public Builder maximumThresholdValue(Double val) {

            if (val != null) {
                this.maximumThresholdValue = val;
            }
            return this;
        }

        /**
         * default value is 10d
         *
         * @param val The parameter is the number of steps to divide the search range into when looking for possible threshold values using drop analysis.
         */
        public Builder numberOfThresholdValues(Double val) {

            if (val != null) {
                this.numberOfThresholdValues = val;
            }
            return this;
        }

        /**
         * default value is  true
         *
         * @param val This checkbox indicates whether logarithmic or linear spacing should be used when looking for possible threshold values using drop ananlysis.
         */
        public Builder useLogarithmicSpacingForThresholdValues(Boolean val) {

            if (val != null) {
                this.useLogarithmicSpacingForThresholdValues = val;
            }
            return this;
        }

        /**
         * default value is drp
         *
         * @param val This is a comma delimited text file with the following header line: Threshold, DrainDen, NoFirstOrd, NoHighOrd, MeanDFirstOrd, MeanDHighOrd, StdDevFirstOrd, StdDevHighOrd, T The file then contains one line of data for each threshold value examined, and then a summary line that indicates the optimum threshold value.
         */
        public Builder dropAnalysisTextFile(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.dropAnalysisTextFile = val;
            }
            return this;
        }

        public StreamDropAnalysisParams build() {
            return new StreamDropAnalysisParams(this);
        }
    }
}