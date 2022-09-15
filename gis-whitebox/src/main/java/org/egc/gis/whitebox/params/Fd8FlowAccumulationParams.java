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
 * Wrapper of parameters of fd8FlowAccumulation
 *
 * @author houzhiwei & wangyijie
 * @date 2020-05-24T17:24:32+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Fd8FlowAccumulationParams implements Params {
    /**
     * <pre>
     * flowDirectionDem
     *  </pre>
     *
     * @see Builder#Builder(String)
     */
    public Fd8FlowAccumulationParams() {
    }

    /**
     * Input raster DEM file
     */
    @NotNull
    private String flowDirectionDem;

    /**
     * Output raster file
     */
    private String flowAccumulationDem;

    /**
     * Output type; one of 'cells', 'specific contributing area' (default), and
     */
    private String outType;

    /**
     * Optional exponent parameter; default is 1.1
     */
    private String exponent;

    /**
     * Optional convergence threshold parameter, in grid cells; default is inifinity
     */
    private String threshold;

    /**
     * Optional flag to request the output be log-transformed
     */
    private Boolean log;

    /**
     * Optional flag to request clipping the display max by 1%
     */
    private Boolean clip;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getFlowAccumulationDem() {
        // if no output filename provided
        if (StringUtils.isBlank(flowAccumulationDem)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(flowDirectionDem, "flowAccumulationDem", "tif", "tif"));
        }
        return this.flowAccumulationDem;
    }

    private Fd8FlowAccumulationParams(Builder builder) {
        this.flowDirectionDem = builder.flowDirectionDem;
        this.flowAccumulationDem = builder.flowAccumulationDem;
        this.outType = builder.outType;
        this.exponent = builder.exponent;
        this.threshold = builder.threshold;
        this.log = builder.log;
        this.clip = builder.clip;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String flowDirectionDem;
        private String flowAccumulationDem;
        private String outType;
        private String exponent;
        private String threshold;
        private Boolean log;
        private Boolean clip;

        /**
         * @param flowDirectionDem Input raster DEM file
         */
        public Builder(@NotBlank String flowDirectionDem) {
            this.flowDirectionDem = flowDirectionDem;
        }

        public Builder flowDirectionDem(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.flowDirectionDem = val;
            }
            return this;
        }

        public Builder flowAccumulationDem(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.flowAccumulationDem = val;
            }
            return this;
        }

        public Builder outType(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.outType = val;
            }
            return this;
        }

        public Builder exponent(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.exponent = val;
            }
            return this;
        }

        public Builder threshold(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.threshold = val;
            }
            return this;
        }

        public Builder log(Boolean val) {

            if (val != null) {
                this.log = val;
            }
            return this;
        }

        public Builder clip(Boolean val) {

            if (val != null) {
                this.clip = val;
            }
            return this;
        }

        public Fd8FlowAccumulationParams build() {
            return new Fd8FlowAccumulationParams(this);
        }
    }
}