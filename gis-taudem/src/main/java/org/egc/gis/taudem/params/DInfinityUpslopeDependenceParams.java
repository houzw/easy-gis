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
 * Wrapper of parameters of dInfinityUpslopeDependence
 *
 * @author houzhiwei
 * @date 2020-05-21T14:24:36+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityUpslopeDependenceParams implements Params {
    private static final long serialVersionUID = 8014027308120049569L;

    /**
     * <pre>
     * dinfinityFlowDirection
     * destination
     *  </pre>
     *
     * @see Builder#Builder(String, String)
     */
    public DInfinityUpslopeDependenceParams() {
    }

    /**
     * A grid giving flow direction by the D-Infinity method where the flow direction angle is determined as the direction of the steepest downward slope on the eight triangular facets formed in a 3 x 3 grid cell window centered on the grid cell of interest.
     */
    @NotNull
    private String dinfinityFlowDirection;

    /**
     * A grid that encodes the destination zone that may receive flow from upslope.
     */
    @NotNull
    private String destination;

    /**
     * A grid quantifing the amount each source point in the domain contributes to the zone defined by the destination grid.
     */
    private String upslopeDependence;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    // if no output filename provided
    public String getUpslopeDependence() {
        if (StringUtils.isBlank(upslopeDependence)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(dinfinityFlowDirection, "upslopeDependence", "Raster Dataset", "tif"));
        }
        return upslopeDependence;
    }

    private DInfinityUpslopeDependenceParams(Builder builder) {
        dinfinityFlowDirection = builder.dinfinityFlowDirection;
        destination = builder.destination;
        upslopeDependence = builder.upslopeDependence;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String dinfinityFlowDirection;
        private String destination;
        private String upslopeDependence;

        /**
         * @param dinfinityFlowDirection A grid giving flow direction by the D-Infinity method where the flow direction angle is determined as the direction of the steepest downward slope on the eight triangular facets formed in a 3 x 3 grid cell window centered on the grid cell of interest.
         * @param destination            A grid that encodes the destination zone that may receive flow from upslope.
         */
        public Builder(@NotBlank String dinfinityFlowDirection, @NotBlank String destination) {
            this.dinfinityFlowDirection = dinfinityFlowDirection;
            this.destination = destination;
        }

        public Builder dinfinityFlowDirection(String val) {
            dinfinityFlowDirection = val;
            return this;
        }

        public Builder destination(String val) {
            destination = val;
            return this;
        }

        public Builder upslopeDependence(String val) {
            upslopeDependence = val;
            return this;
        }

        public DInfinityUpslopeDependenceParams build() {
            return new DInfinityUpslopeDependenceParams(this);
        }
    }
}