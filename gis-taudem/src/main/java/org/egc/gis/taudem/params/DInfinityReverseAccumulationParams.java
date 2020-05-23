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
 * @date 2020-05-21T14:24:36+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityReverseAccumulationParams implements Params {
    private static final long serialVersionUID = 5235774109759949425L;

    /**
     * <pre>
     * dinfinityFlowDirection
     * weight
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
    private String reverseAccumulation;

    /**
     * The grid giving the maximum of the weight loading grid downslope from each grid cell.
     */
    private String maximumDownslope;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    // if no output filename provided
    public String getReverseAccumulation() {
        if (StringUtils.isBlank(reverseAccumulation)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(dinfinityFlowDirection, "reverseAccumulation", "Raster Dataset", "tif"));
        }
        return reverseAccumulation;
    }

    // if no output filename provided
    public String getMaximumDownslope() {
        if (StringUtils.isBlank(maximumDownslope)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(dinfinityFlowDirection, "maximumDownslope", "Raster Dataset", "tif"));
        }
        return maximumDownslope;
    }

    private DInfinityReverseAccumulationParams(Builder builder) {
        dinfinityFlowDirection = builder.dinfinityFlowDirection;
        weight = builder.weight;
        reverseAccumulation = builder.reverseAccumulation;
        maximumDownslope = builder.maximumDownslope;
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

        public Builder dinfinityFlowDirection(String val) {
            dinfinityFlowDirection = val;
            return this;
        }

        public Builder weight(String val) {
            weight = val;
            return this;
        }

        public Builder reverseAccumulation(String val) {
            reverseAccumulation = val;
            return this;
        }

        public Builder maximumDownslope(String val) {
            maximumDownslope = val;
            return this;
        }

        public DInfinityReverseAccumulationParams build() {
            return new DInfinityReverseAccumulationParams(this);
        }
    }
}