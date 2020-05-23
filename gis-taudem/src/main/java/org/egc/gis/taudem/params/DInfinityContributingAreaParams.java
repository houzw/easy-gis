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
 * Wrapper of parameters of dInfinityContributingArea
 *
 * @author houzhiwei
 * @date 2020-05-21T14:24:36+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityContributingAreaParams implements Params {
    private static final long serialVersionUID = 5178914606888747681L;

    /**
     * <pre>
     * dinfinityFlowDirection
     * outlets
     * weight
     *  </pre>
     *
     * @see Builder#Builder(String)
     */
    public DInfinityContributingAreaParams() {
    }

    /**
     * A grid of flow directions based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997, "A New Method for the Determination of Flow Directions and Contributing Areas in Grid Digital Elevation Models," Water Resources Research, 33(2): 309-319).
     */
    @NotNull
    private String dinfinityFlowDirection;

    /**
     * A point feature defining the outlets of interest.
     */
    private String outlets;

    /**
     * A grid giving contribution to flow for each cell.
     */
    private String weight;


    /**
     * default is true.
     * A flag that indicates whether the tool should check for edge contamination.
     */
    @XmlElement(defaultValue = "true")
    private Boolean checkForEdgeContamination = true;


    /**
     * A grid of specific catchment area which is the contributing area per unit contour length using the multiple flow direction D-infinity approach.
     */
    private String dinfinitySpecificCatchmentArea;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    // if no output filename provided
    public String getDinfinitySpecificCatchmentArea() {
        if (StringUtils.isBlank(dinfinitySpecificCatchmentArea)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(dinfinityFlowDirection, "dinfinitySpecificCatchmentArea", "Raster Dataset", "tif"));
        }
        return dinfinitySpecificCatchmentArea;
    }

    private DInfinityContributingAreaParams(Builder builder) {
        dinfinityFlowDirection = builder.dinfinityFlowDirection;
        outlets = builder.outlets;
        weight = builder.weight;
        checkForEdgeContamination = builder.checkForEdgeContamination;
        dinfinitySpecificCatchmentArea = builder.dinfinitySpecificCatchmentArea;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String dinfinityFlowDirection;
        private String outlets;
        private String weight;
        private Boolean checkForEdgeContamination;
        private String dinfinitySpecificCatchmentArea;

        /**
         * @param dinfinityFlowDirection A grid of flow directions based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997, "A New Method for the Determination of Flow Directions and Contributing Areas in Grid Digital Elevation Models," Water Resources Research, 33(2): 309-319).
         */
        public Builder(@NotBlank String dinfinityFlowDirection) {
            this.dinfinityFlowDirection = dinfinityFlowDirection;
        }

        public Builder dinfinityFlowDirection(String val) {
            dinfinityFlowDirection = val;
            return this;
        }

        public Builder outlets(String val) {
            outlets = val;
            return this;
        }

        public Builder weight(String val) {
            weight = val;
            return this;
        }

        public Builder checkForEdgeContamination(Boolean val) {
            checkForEdgeContamination = val;
            return this;
        }

        public Builder dinfinitySpecificCatchmentArea(String val) {
            dinfinitySpecificCatchmentArea = val;
            return this;
        }

        public DInfinityContributingAreaParams build() {
            return new DInfinityContributingAreaParams(this);
        }
    }
}