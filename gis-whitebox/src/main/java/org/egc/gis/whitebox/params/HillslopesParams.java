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
 * Wrapper of parameters of hillslopes
 *
 * @author houzhiwei & wangyijie
 * @date 2020-05-24T17:24:32+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class HillslopesParams implements Params {
    /**
     * <pre>
     * flowDirectionDem
     * streamDem
     *  </pre>
     *
     * @see Builder#Builder(String, String)
     */
    public HillslopesParams() {
    }

    /**
     * Input raster D8 pointer file
     */
    @NotNull
    private String flowDirectionDem;

    /**
     * Input raster streams file
     */
    @NotNull
    private String streamDem;

    /**
     * Output raster file
     */
    private String hillslopesDem;

    /**
     * D8 pointer uses the ESRI style scheme
     */
    private String esriPntr;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getHillslopesDem() {
        // if no output filename provided
        if (StringUtils.isBlank(hillslopesDem)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(flowDirectionDem, "hillslopesDem", "tif", "tif"));
        }
        return this.hillslopesDem;
    }

    private HillslopesParams(Builder builder) {
        this.flowDirectionDem = builder.flowDirectionDem;
        this.streamDem = builder.streamDem;
        this.hillslopesDem = builder.hillslopesDem;
        this.esriPntr = builder.esriPntr;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String flowDirectionDem;
        private String streamDem;
        private String hillslopesDem;
        private String esriPntr;

        /**
         * @param flowDirectionDem Input raster D8 pointer file
         * @param streamDem        Input raster streams file
         */
        public Builder(@NotBlank String flowDirectionDem, @NotBlank String streamDem) {
            this.flowDirectionDem = flowDirectionDem;
            this.streamDem = streamDem;
        }

        public Builder flowDirectionDem(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.flowDirectionDem = val;
            }
            return this;
        }

        public Builder streamDem(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.streamDem = val;
            }
            return this;
        }

        public Builder hillslopesDem(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.hillslopesDem = val;
            }
            return this;
        }

        public Builder esriPntr(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.esriPntr = val;
            }
            return this;
        }

        public HillslopesParams build() {
            return new HillslopesParams(this);
        }
    }
}