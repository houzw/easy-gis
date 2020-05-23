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
 * @date 2020-05-21T14:24:36+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityFlowDirectionsParams implements Params {
    private static final long serialVersionUID = -7496107106353065598L;

    /**
     * <pre>
     * pitFilledElevation
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
    private String dinfinityFlowDirection;

    /**
     * A grid of slope evaluated using the D-infinity method described in Tarboton, D.
     */
    private String dinfinitySlope;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    // if no output filename provided
    public String getDinfinityFlowDirection() {
        if (StringUtils.isBlank(dinfinityFlowDirection)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(pitFilledElevation, "dinfinityFlowDirection", "Raster Dataset", "tif"));
        }
        return dinfinityFlowDirection;
    }

    // if no output filename provided
    public String getDinfinitySlope() {
        if (StringUtils.isBlank(dinfinitySlope)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(pitFilledElevation, "dinfinitySlope", "Raster Dataset", "tif"));
        }
        return dinfinitySlope;
    }

    private DInfinityFlowDirectionsParams(Builder builder) {
        pitFilledElevation = builder.pitFilledElevation;
        dinfinityFlowDirection = builder.dinfinityFlowDirection;
        dinfinitySlope = builder.dinfinitySlope;
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

        public Builder pitFilledElevation(String val) {
            pitFilledElevation = val;
            return this;
        }

        public Builder dinfinityFlowDirection(String val) {
            dinfinityFlowDirection = val;
            return this;
        }

        public Builder dinfinitySlope(String val) {
            dinfinitySlope = val;
            return this;
        }

        public DInfinityFlowDirectionsParams build() {
            return new DInfinityFlowDirectionsParams(this);
        }
    }
}