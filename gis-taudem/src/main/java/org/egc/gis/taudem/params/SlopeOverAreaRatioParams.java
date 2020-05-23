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
 * Wrapper of parameters of slopeOverAreaRatio
 *
 * @author houzhiwei
 * @date 2020-05-21T14:24:36+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class SlopeOverAreaRatioParams implements Params {
    private static final long serialVersionUID = 6043820173424244273L;

    /**
     * <pre>
     * slope
     * specificCatchmentArea
     *  </pre>
     *
     * @see Builder#Builder(String, String)
     */
    public SlopeOverAreaRatioParams() {
    }

    /**
     * A grid of slope.
     */
    @NotNull
    private String slope;

    /**
     * A grid giving the contributing area value for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it.
     */
    @NotNull
    private String specificCatchmentArea;

    /**
     * A grid of the ratio of slope to specific catchment area (contributing area).
     */
    private String slopeDividedByAreaRatio;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    // if no output filename provided
    public String getSlopeDividedByAreaRatio() {
        if (StringUtils.isBlank(slopeDividedByAreaRatio)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(slope, "slopeDividedByAreaRatio", "Raster Dataset", "tif"));
        }
        return slopeDividedByAreaRatio;
    }

    private SlopeOverAreaRatioParams(Builder builder) {
        slope = builder.slope;
        specificCatchmentArea = builder.specificCatchmentArea;
        slopeDividedByAreaRatio = builder.slopeDividedByAreaRatio;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String slope;
        private String specificCatchmentArea;
        private String slopeDividedByAreaRatio;

        /**
         * @param slope                 A grid of slope.
         * @param specificCatchmentArea A grid giving the contributing area value for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it.
         */
        public Builder(@NotBlank String slope, @NotBlank String specificCatchmentArea) {
            this.slope = slope;
            this.specificCatchmentArea = specificCatchmentArea;
        }

        public Builder slope(String val) {
            slope = val;
            return this;
        }

        public Builder specificCatchmentArea(String val) {
            specificCatchmentArea = val;
            return this;
        }

        public Builder slopeDividedByAreaRatio(String val) {
            slopeDividedByAreaRatio = val;
            return this;
        }

        public SlopeOverAreaRatioParams build() {
            return new SlopeOverAreaRatioParams(this);
        }
    }
}