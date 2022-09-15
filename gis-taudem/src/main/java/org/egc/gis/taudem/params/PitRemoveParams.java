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
 * Wrapper of parameters of pitRemove
 *
 * @author houzhiwei
 * @date 2020-06-28T12:02:31+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PitRemoveParams implements Params {
    /**
     * <pre>     * elevation     * depressionMask
     *  </pre>
     *
     * @see Builder#Builder(String)
     */
    public PitRemoveParams() {
    }

    /**
     * A digital elevation model (DEM) grid to serve as the base input for the terrain analysis and stream delineation.
     */
    @NotNull
    private String elevation;

    /**
     * If this option is selected Fill ensures that the grid is hydrologically conditioned with cell to cell connectivity in only 4 directions (N, S, E or W neighbors).
     */
    private Boolean fillConsideringOnly4WayNeighbors;

    /**
     *
     */
    private String depressionMask;

    /**
     * A grid of elevation values with pits removed so that flow is routed off of the domain.
     */
    @NotNull
    private String pitFilledElevation;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getPitFilledElevation() {
        // if no output filename provided
        if (StringUtils.isBlank(pitFilledElevation)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(elevation, "pitFilledElevation", "Raster Dataset", "tif"));
        }
        return this.pitFilledElevation;
    }

    private PitRemoveParams(Builder builder) {
        this.elevation = builder.elevation;
        this.fillConsideringOnly4WayNeighbors = builder.fillConsideringOnly4WayNeighbors;
        this.depressionMask = builder.depressionMask;
        this.pitFilledElevation = builder.pitFilledElevation;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String elevation;
        private Boolean fillConsideringOnly4WayNeighbors;
        private String depressionMask;
        private String pitFilledElevation;

        /**
         * @param elevation A digital elevation model (DEM) grid to serve as the base input for the terrain analysis and stream delineation.
         */
        public Builder(@NotBlank String elevation) {
            this.elevation = elevation;
        }

        /**
         * @param val A digital elevation model (DEM) grid to serve as the base input for the terrain analysis and stream delineation.
         */
        public Builder elevation(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.elevation = val;
            }
            return this;
        }

        /**
         * @param val If this option is selected Fill ensures that the grid is hydrologically conditioned with cell to cell connectivity in only 4 directions (N, S, E or W neighbors).
         */
        public Builder fillConsideringOnly4WayNeighbors(Boolean val) {

            if (val != null) {
                this.fillConsideringOnly4WayNeighbors = val;
            }
            return this;
        }

        /**
         * @param val
         */
        public Builder depressionMask(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.depressionMask = val;
            }
            return this;
        }

        /**
         * @param val A grid of elevation values with pits removed so that flow is routed off of the domain.
         */
        public Builder pitFilledElevation(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.pitFilledElevation = val;
            }
            return this;
        }

        public PitRemoveParams build() {
            return new PitRemoveParams(this);
        }
    }
}