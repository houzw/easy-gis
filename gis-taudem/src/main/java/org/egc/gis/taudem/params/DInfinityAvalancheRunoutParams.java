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
 * Wrapper of parameters of dInfinityAvalancheRunout
 *
 * @author houzhiwei
 * @date 2020-05-21T14:24:36+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityAvalancheRunoutParams implements Params {
    private static final long serialVersionUID = -6027299520652204039L;

    /**
     * <pre>
     * pitFilledElevation
     * dinfinityFlowDirection
     * avalancheSourceSite
     *  </pre>
     *
     * @see Builder#Builder(String, String, String)
     */
    public DInfinityAvalancheRunoutParams() {
    }

    /**
     * A grid of elevation values.
     */
    @NotNull
    private String pitFilledElevation;

    /**
     * A grid giving flow directions by the D-Infinity method.
     */
    @NotNull
    private String dinfinityFlowDirection;

    /**
     * This is a grid of source areas for snow avalanches that are commonly identified manually using a mix of experience and visual interpretation of maps.
     */
    @NotNull
    private String avalancheSourceSite;


    /**
     * default is 0.2d.
     * This value is a threshold proportion that is used to limit the disperson of flow caused by using the D-infinity multiple flow direction method for determining flow direction.
     */
    @XmlElement(defaultValue = "0.2")
    private Double proportionThreshold = 0.2d;


    /**
     * default is 20d.
     * This value is the threshold angle, called the Alpha Angle, that is used to determine which of the cells downslope from the source cells are in the affected area.
     */
    @XmlElement(defaultValue = "20")
    private Double alphaAngleThreshold = 20d;


    /**
     * This option selects the method used to measure the distance used to calculate the slope angle.
     */
    private String pathDistanceMethod;

    /**
     * This grid Identifies the avalanche's runout zone (affected area) using a runout zone indicator with value 0 to indicate that this grid cell is not in the runout zone and value > 0 to indicate that this grid cell is in the runout zone.
     */
    private String runoutZone;

    /**
     * This is a grid of the flow distance from the source site that has the highest angle to each cell.
     */
    private String pathDistance;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    // if no output filename provided
    public String getRunoutZone() {
        if (StringUtils.isBlank(runoutZone)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(pitFilledElevation, "runoutZone", "Raster Dataset", "tif"));
        }
        return runoutZone;
    }

    // if no output filename provided
    public String getPathDistance() {
        if (StringUtils.isBlank(pathDistance)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(pitFilledElevation, "pathDistance", "Raster Dataset", "tif"));
        }
        return pathDistance;
    }

    private DInfinityAvalancheRunoutParams(Builder builder) {
        pitFilledElevation = builder.pitFilledElevation;
        dinfinityFlowDirection = builder.dinfinityFlowDirection;
        avalancheSourceSite = builder.avalancheSourceSite;
        proportionThreshold = builder.proportionThreshold;
        alphaAngleThreshold = builder.alphaAngleThreshold;
        pathDistanceMethod = builder.pathDistanceMethod;
        runoutZone = builder.runoutZone;
        pathDistance = builder.pathDistance;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String pitFilledElevation;
        private String dinfinityFlowDirection;
        private String avalancheSourceSite;
        private Double proportionThreshold;
        private Double alphaAngleThreshold;
        private String pathDistanceMethod;
        private String runoutZone;
        private String pathDistance;

        /**
         * @param pitFilledElevation     A grid of elevation values.
         * @param dinfinityFlowDirection A grid giving flow directions by the D-Infinity method.
         * @param avalancheSourceSite    This is a grid of source areas for snow avalanches that are commonly identified manually using a mix of experience and visual interpretation of maps.
         */
        public Builder(@NotBlank String pitFilledElevation, @NotBlank String dinfinityFlowDirection, @NotBlank String avalancheSourceSite) {
            this.pitFilledElevation = pitFilledElevation;
            this.dinfinityFlowDirection = dinfinityFlowDirection;
            this.avalancheSourceSite = avalancheSourceSite;
        }

        public Builder pitFilledElevation(String val) {
            pitFilledElevation = val;
            return this;
        }

        public Builder dinfinityFlowDirection(String val) {
            dinfinityFlowDirection = val;
            return this;
        }

        public Builder avalancheSourceSite(String val) {
            avalancheSourceSite = val;
            return this;
        }

        public Builder proportionThreshold(Double val) {
            proportionThreshold = val;
            return this;
        }

        public Builder alphaAngleThreshold(Double val) {
            alphaAngleThreshold = val;
            return this;
        }

        public Builder pathDistanceMethod(String val) {
            pathDistanceMethod = val;
            return this;
        }

        public Builder runoutZone(String val) {
            runoutZone = val;
            return this;
        }

        public Builder pathDistance(String val) {
            pathDistance = val;
            return this;
        }

        public DInfinityAvalancheRunoutParams build() {
            return new DInfinityAvalancheRunoutParams(this);
        }
    }
}