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
 * Wrapper of parameters of d8ContributingArea
 *
 * @author houzhiwei
 * @date 2020-05-21T14:24:36+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class D8ContributingAreaParams implements Params {
    private static final long serialVersionUID = 3396007760293404491L;

    /**
     * <pre>
     * d8FlowDirection
     * outlets
     * weight
     *  </pre>
     *
     * @see Builder#Builder(String)
     */
    public D8ContributingAreaParams() {
    }

    /**
     * A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     */
    @NotNull
    private String d8FlowDirection;

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
     * A grid of contributing area values calculated as the cells own contribution plus the contribution from upslope neighbors that drain in to it according to the D8 flow model.
     */
    private String d8ContributingArea;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    // if no output filename provided
    public String getD8ContributingArea() {
        if (StringUtils.isBlank(d8ContributingArea)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(d8FlowDirection, "d8ContributingArea", "Raster Dataset", "tif"));
        }
        return d8ContributingArea;
    }

    private D8ContributingAreaParams(Builder builder) {
        d8FlowDirection = builder.d8FlowDirection;
        outlets = builder.outlets;
        weight = builder.weight;
        checkForEdgeContamination = builder.checkForEdgeContamination;
        d8ContributingArea = builder.d8ContributingArea;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String d8FlowDirection;
        private String outlets;
        private String weight;
        private Boolean checkForEdgeContamination;
        private String d8ContributingArea;

        /**
         * @param d8FlowDirection A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
         */
        public Builder(@NotBlank String d8FlowDirection) {
            this.d8FlowDirection = d8FlowDirection;
        }

        public Builder d8FlowDirection(String val) {
            d8FlowDirection = val;
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

        public Builder d8ContributingArea(String val) {
            d8ContributingArea = val;
            return this;
        }

        public D8ContributingAreaParams build() {
            return new D8ContributingAreaParams(this);
        }
    }
}