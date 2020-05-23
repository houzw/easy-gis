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
 * Wrapper of parameters of topographicWetnessIndex
 *
 * @author houzhiwei
 * @date 2020-05-21T14:24:36+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class TopographicWetnessIndexParams implements Params {
    private static final long serialVersionUID = -8524078692617939530L;

    /**
     * <pre>
     * specificCatchmentArea
     * slope
     *  </pre>
     *
     * @see Builder#Builder(String, String)
     */
    public TopographicWetnessIndexParams() {
    }

    /**
     * A grid of specific catchment area which is the contributing area per unit contour length.
     */
    @NotNull
    private String specificCatchmentArea;

    /**
     * A grid of slope.
     */
    @NotNull
    private String slope;

    /**
     * A grid of the natural log of the ratio of specific catchment area (contributing area) to slope, ln(a/S).
     */
    private String wetnessIndex;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    // if no output filename provided
    public String getWetnessIndex() {
        if (StringUtils.isBlank(wetnessIndex)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(specificCatchmentArea, "wetnessIndex", "Raster Dataset", "tif"));
        }
        return wetnessIndex;
    }

    private TopographicWetnessIndexParams(Builder builder) {
        specificCatchmentArea = builder.specificCatchmentArea;
        slope = builder.slope;
        wetnessIndex = builder.wetnessIndex;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String specificCatchmentArea;
        private String slope;
        private String wetnessIndex;

        /**
         * @param specificCatchmentArea A grid of specific catchment area which is the contributing area per unit contour length.
         * @param slope                 A grid of slope.
         */
        public Builder(@NotBlank String specificCatchmentArea, @NotBlank String slope) {
            this.specificCatchmentArea = specificCatchmentArea;
            this.slope = slope;
        }

        public Builder specificCatchmentArea(String val) {
            specificCatchmentArea = val;
            return this;
        }

        public Builder slope(String val) {
            slope = val;
            return this;
        }

        public Builder wetnessIndex(String val) {
            wetnessIndex = val;
            return this;
        }

        public TopographicWetnessIndexParams build() {
            return new TopographicWetnessIndexParams(this);
        }
    }
}