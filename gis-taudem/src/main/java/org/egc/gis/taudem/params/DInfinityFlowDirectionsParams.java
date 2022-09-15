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
 * Wrapper of parameters of dInfinityFlowDirections
 *
 * @author houzhiwei
 * @date 2020-06-28T12:02:31+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityFlowDirectionsParams implements Params {
    /**
     * <pre>     * pitFilledElevation
     *  </pre>
     *
     * @see Builder#Builder(String)
     */
    public DInfinityFlowDirectionsParams() {
    }

    /**
     * A grid of elevation values.
     */
    @NotNull
    private String pitFilledElevation;

    /**
     * A grid of flow directions based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997, "A New Method for the Determination of Flow Directions and Contributing Areas in Grid Digital Elevation Models," Water Resources Research, 33(2): 309-319).
     */
    @NotNull
    private String dinfinityFlowDirection;

    /**
     * A grid of slope evaluated using the D-infinity method described in Tarboton, D.
     */
    @NotNull
    private String dinfinitySlope;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getDinfinityFlowDirection() {
        // if no output filename provided
        if (StringUtils.isBlank(dinfinityFlowDirection)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(pitFilledElevation, "dinfinityFlowDirection", "Raster Dataset", "tif"));
        }
        return this.dinfinityFlowDirection;
    }

    public String getDinfinitySlope() {
        // if no output filename provided
        if (StringUtils.isBlank(dinfinitySlope)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(pitFilledElevation, "dinfinitySlope", "Raster Dataset", "tif"));
        }
        return this.dinfinitySlope;
    }

    private DInfinityFlowDirectionsParams(Builder builder) {
        this.pitFilledElevation = builder.pitFilledElevation;
        this.dinfinityFlowDirection = builder.dinfinityFlowDirection;
        this.dinfinitySlope = builder.dinfinitySlope;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String pitFilledElevation;
        private String dinfinityFlowDirection;
        private String dinfinitySlope;

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
         * @param val A grid of flow directions based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997, "A New Method for the Determination of Flow Directions and Contributing Areas in Grid Digital Elevation Models," Water Resources Research, 33(2): 309-319).
         */
        public Builder dinfinityFlowDirection(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.dinfinityFlowDirection = val;
            }
            return this;
        }

        /**
         * @param val A grid of slope evaluated using the D-infinity method described in Tarboton, D.
         */
        public Builder dinfinitySlope(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.dinfinitySlope = val;
            }
            return this;
        }

        public DInfinityFlowDirectionsParams build() {
            return new DInfinityFlowDirectionsParams(this);
        }
    }
}