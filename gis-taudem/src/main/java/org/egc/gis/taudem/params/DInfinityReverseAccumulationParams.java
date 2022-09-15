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
 * Wrapper of parameters of dInfinityReverseAccumulation
 *
 * @author houzhiwei
 * @date 2020-06-28T12:02:31+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityReverseAccumulationParams implements Params {
    /**
     * <pre>     * dinfinityFlowDirection     * weight
     *  </pre>
     *
     * @see Builder#Builder(String, String)
     */
    public DInfinityReverseAccumulationParams() {
    }

    /**
     * A grid giving flow direction by the Dinfinity method.
     */
    @NotNull
    private String dinfinityFlowDirection;

    /**
     * A grid giving weights (loadings) to be used in the accumulation.
     */
    @NotNull
    private String weight;

    /**
     * The grid giving the result of the "Reverse Accumulation" function.
     */
    @NotNull
    private String reverseAccumulation;

    /**
     * The grid giving the maximum of the weight loading grid downslope from each grid cell.
     */
    @NotNull
    private String maximumDownslope;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getReverseAccumulation() {
        // if no output filename provided
        if (StringUtils.isBlank(reverseAccumulation)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(dinfinityFlowDirection, "reverseAccumulation", "Raster Dataset", "tif"));
        }
        return this.reverseAccumulation;
    }

    public String getMaximumDownslope() {
        // if no output filename provided
        if (StringUtils.isBlank(maximumDownslope)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(dinfinityFlowDirection, "maximumDownslope", "Raster Dataset", "tif"));
        }
        return this.maximumDownslope;
    }

    private DInfinityReverseAccumulationParams(Builder builder) {
        this.dinfinityFlowDirection = builder.dinfinityFlowDirection;
        this.weight = builder.weight;
        this.reverseAccumulation = builder.reverseAccumulation;
        this.maximumDownslope = builder.maximumDownslope;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String dinfinityFlowDirection;
        private String weight;
        private String reverseAccumulation;
        private String maximumDownslope;

        /**
         * @param dinfinityFlowDirection A grid giving flow direction by the Dinfinity method.
         * @param weight                 A grid giving weights (loadings) to be used in the accumulation.
         */
        public Builder(@NotBlank String dinfinityFlowDirection, @NotBlank String weight) {
            this.dinfinityFlowDirection = dinfinityFlowDirection;
            this.weight = weight;
        }

        /**
         * @param val A grid giving flow direction by the Dinfinity method.
         */
        public Builder dinfinityFlowDirection(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.dinfinityFlowDirection = val;
            }
            return this;
        }

        /**
         * @param val A grid giving weights (loadings) to be used in the accumulation.
         */
        public Builder weight(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.weight = val;
            }
            return this;
        }

        /**
         * @param val The grid giving the result of the "Reverse Accumulation" function.
         */
        public Builder reverseAccumulation(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.reverseAccumulation = val;
            }
            return this;
        }

        /**
         * @param val The grid giving the maximum of the weight loading grid downslope from each grid cell.
         */
        public Builder maximumDownslope(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.maximumDownslope = val;
            }
            return this;
        }

        public DInfinityReverseAccumulationParams build() {
            return new DInfinityReverseAccumulationParams(this);
        }
    }
}