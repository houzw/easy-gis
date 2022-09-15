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
 * Wrapper of parameters of slopeAreaCombination
 *
 * @author houzhiwei
 * @date 2020-06-28T12:02:30+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class SlopeAreaCombinationParams implements Params {
    /**
     * <pre>     * slope     * area
     *  </pre>
     *
     * @see Builder#Builder(String, String)
     */
    public SlopeAreaCombinationParams() {
    }

    /**
     * This input is a grid of slope values.
     */
    @NotNull
    private String slope;

    /**
     * A grid giving the specific catchment area for each cell taken as its own contribution (grid cell length or summation of weights) plus the proportional contribution from upslope neighbors that drain in to it.
     */
    @NotNull
    private String area;


    /**
     * default is 2d.
     * The slope exponent (m) parameter which will be used in the formula: (S^m)(A^n), that is used to create the slope-area grid.
     */
    @XmlElement(defaultValue = "2")
    private Double slopeExponentM = 2d;


    /**
     * default is 1d.
     * The area exponent (n) parameter which will be used in the formula: (S^m)(A^n), that is used to create the slope-area grid.
     */
    @XmlElement(defaultValue = "1")
    private Double areaExponentN = 1d;


    /**
     * A grid of slope-area values = (S^m)(A^n) calculated from the slope grid, specific catchment area grid, m slope exponent parameter, and n area exponent parameter.
     */
    @NotNull
    private String slopeArea;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getSlopeArea() {
        // if no output filename provided
        if (StringUtils.isBlank(slopeArea)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(slope, "slopeArea", "Raster Dataset", "tif"));
        }
        return this.slopeArea;
    }

    private SlopeAreaCombinationParams(Builder builder) {
        this.slope = builder.slope;
        this.area = builder.area;
        this.slopeExponentM = builder.slopeExponentM;
        this.areaExponentN = builder.areaExponentN;
        this.slopeArea = builder.slopeArea;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String slope;
        private String area;
        private Double slopeExponentM;
        private Double areaExponentN;
        private String slopeArea;

        /**
         * @param slope This input is a grid of slope values.
         * @param area  A grid giving the specific catchment area for each cell taken as its own contribution (grid cell length or summation of weights) plus the proportional contribution from upslope neighbors that drain in to it.
         */
        public Builder(@NotBlank String slope, @NotBlank String area) {
            this.slope = slope;
            this.area = area;
        }

        /**
         * @param val This input is a grid of slope values.
         */
        public Builder slope(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.slope = val;
            }
            return this;
        }

        /**
         * @param val A grid giving the specific catchment area for each cell taken as its own contribution (grid cell length or summation of weights) plus the proportional contribution from upslope neighbors that drain in to it.
         */
        public Builder area(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.area = val;
            }
            return this;
        }

        /**
         * default value is 2d
         *
         * @param val The slope exponent (m) parameter which will be used in the formula: (S^m)(A^n), that is used to create the slope-area grid.
         */
        public Builder slopeExponentM(Double val) {

            if (val != null) {
                this.slopeExponentM = val;
            }
            return this;
        }

        /**
         * default value is 1d
         *
         * @param val The area exponent (n) parameter which will be used in the formula: (S^m)(A^n), that is used to create the slope-area grid.
         */
        public Builder areaExponentN(Double val) {

            if (val != null) {
                this.areaExponentN = val;
            }
            return this;
        }

        /**
         * @param val A grid of slope-area values = (S^m)(A^n) calculated from the slope grid, specific catchment area grid, m slope exponent parameter, and n area exponent parameter.
         */
        public Builder slopeArea(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.slopeArea = val;
            }
            return this;
        }

        public SlopeAreaCombinationParams build() {
            return new SlopeAreaCombinationParams(this);
        }
    }
}