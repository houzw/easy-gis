package org.egc.gis.whitebox.params;

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
 * Wrapper of parameters of breachDepressions
 *
 * @author houzhiwei & wangyijie
 * @date 2020-05-24T17:24:32+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class BreachDepressionsParams implements Params {
    /**
     * <pre>
     * dem
     *  </pre>
     *
     * @see Builder#Builder(String)
     */
    public BreachDepressionsParams() {
    }

    /**
     * Input raster DEM file
     */
    @NotNull
    private String dem;

    /**
     * Output raster file
     */
    private String filledDem;

    /**
     * Optional maximum breach depth (default is Inf)
     */
    private String maxDepth;

    /**
     * Optional maximum breach channel length (in grid cells; default is Inf)
     */
    private String maxLength;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getFilledDem() {
        // if no output filename provided
        if (StringUtils.isBlank(filledDem)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(dem, "filledDem", "tif", "tif"));
        }
        return this.filledDem;
    }

    private BreachDepressionsParams(Builder builder) {
        this.dem = builder.dem;
        this.filledDem = builder.filledDem;
        this.maxDepth = builder.maxDepth;
        this.maxLength = builder.maxLength;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String dem;
        private String filledDem;
        private String maxDepth;
        private String maxLength;

        /**
         * @param dem Input raster DEM file
         */
        public Builder(@NotBlank String dem) {
            this.dem = dem;
        }

        public Builder dem(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.dem = val;
            }
            return this;
        }

        public Builder filledDem(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.filledDem = val;
            }
            return this;
        }

        public Builder maxDepth(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.maxDepth = val;
            }
            return this;
        }

        public Builder maxLength(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.maxLength = val;
            }
            return this;
        }

        public BreachDepressionsParams build() {
            return new BreachDepressionsParams(this);
        }
    }
}