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
 * Wrapper of parameters of d8Pointer
 *
 * @author houzhiwei & wangyijie
 * @date 2020-05-24T17:24:32+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class D8PointerParams implements Params {
    /**
     * <pre>
     * filledDem
     *  </pre>
     *
     * @see Builder#Builder(String)
     */
    public D8PointerParams() {
    }

    /**
     * Input raster DEM file
     */
    @NotNull
    private String filledDem;

    /**
     * Output raster file
     */
    private String flowDirectionDem;

    /**
     * D8 pointer uses the ESRI style scheme
     */
    private String esriPntr;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getFlowDirectionDem() {
        // if no output filename provided
        if (StringUtils.isBlank(flowDirectionDem)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(filledDem, "flowDirectionDem", "tif", "tif"));
        }
        return this.flowDirectionDem;
    }

    private D8PointerParams(Builder builder) {
        this.filledDem = builder.filledDem;
        this.flowDirectionDem = builder.flowDirectionDem;
        this.esriPntr = builder.esriPntr;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String filledDem;
        private String flowDirectionDem;
        private String esriPntr;

        /**
         * @param filledDem Input raster DEM file
         */
        public Builder(@NotBlank String filledDem) {
            this.filledDem = filledDem;
        }

        public Builder filledDem(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.filledDem = val;
            }
            return this;
        }

        public Builder flowDirectionDem(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.flowDirectionDem = val;
            }
            return this;
        }

        public Builder esriPntr(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.esriPntr = val;
            }
            return this;
        }

        public D8PointerParams build() {
            return new D8PointerParams(this);
        }
    }
}