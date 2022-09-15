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
 * Wrapper of parameters of slopeAverageDown
 *
 * @author houzhiwei
 * @date 2020-06-28T12:02:31+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class SlopeAverageDownParams implements Params {
    /**
     * <pre>     * d8FlowDirection     * pitFilledElevation
     *  </pre>
     *
     * @see Builder#Builder(String, String)
     */
    public SlopeAverageDownParams() {
    }

    /**
     * This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
     */
    @NotNull
    private String d8FlowDirection;

    /**
     * A grid of elevation values.
     */
    @NotNull
    private String pitFilledElevation;


    /**
     * default is 50d.
     * Input parameter of downslope distance over which to calculate the slope (in horizontal map units).
     */
    @XmlElement(defaultValue = "50")
    private Double distance = 50d;


    /**
     * A grid of the ratio of  specific catchment area (contributing area) to slope.
     */
    @NotNull
    private String slopeAverageDown;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getSlopeAverageDown() {
        // if no output filename provided
        if (StringUtils.isBlank(slopeAverageDown)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(d8FlowDirection, "slopeAverageDown", "Raster Dataset", "tif"));
        }
        return this.slopeAverageDown;
    }

    private SlopeAverageDownParams(Builder builder) {
        this.d8FlowDirection = builder.d8FlowDirection;
        this.pitFilledElevation = builder.pitFilledElevation;
        this.distance = builder.distance;
        this.slopeAverageDown = builder.slopeAverageDown;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String d8FlowDirection;
        private String pitFilledElevation;
        private Double distance;
        private String slopeAverageDown;

        /**
         * @param d8FlowDirection    This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
         * @param pitFilledElevation A grid of elevation values.
         */
        public Builder(@NotBlank String d8FlowDirection, @NotBlank String pitFilledElevation) {
            this.d8FlowDirection = d8FlowDirection;
            this.pitFilledElevation = pitFilledElevation;
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
         * @param val A grid of elevation values.
         */
        public Builder pitFilledElevation(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.pitFilledElevation = val;
            }
            return this;
        }

        /**
         * default value is 50d
         *
         * @param val Input parameter of downslope distance over which to calculate the slope (in horizontal map units).
         */
        public Builder distance(Double val) {

            if (val != null) {
                this.distance = val;
            }
            return this;
        }

        /**
         * @param val A grid of the ratio of  specific catchment area (contributing area) to slope.
         */
        public Builder slopeAverageDown(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.slopeAverageDown = val;
            }
            return this;
        }

        public SlopeAverageDownParams build() {
            return new SlopeAverageDownParams(this);
        }
    }
}