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
 * Wrapper of parameters of peukerDouglas
 *
 * @author houzhiwei
 * @date 2020-05-21T14:24:35+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PeukerDouglasParams implements Params {
    private static final long serialVersionUID = 5330469577871357499L;

    /**
     * <pre>
     * elevation
     *  </pre>
     *
     * @see Builder#Builder(String)
     */
    public PeukerDouglasParams() {
    }

    /**
     * A grid of elevation values.
     */
    @NotNull
    private String elevation;


    /**
     * default is 0.4d.
     * The center weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
     */
    @XmlElement(defaultValue = "0.4")
    private Double centerSmoothingWeight = 0.4d;


    /**
     * default is 0.1d.
     * The side weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
     */
    @XmlElement(defaultValue = "0.1")
    private Double sideSmoothingWeight = 0.1d;


    /**
     * default is 0.05d.
     * The diagonal weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
     */
    @XmlElement(defaultValue = "0.05")
    private Double diagonalSmoothingWeight = 0.05d;


    /**
     * An indicator grid (1,0) of upward curved grid cells according to the Peuker and Douglas algorithm, and if viewed, resembles a channel network.
     */
    private String streamSource;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    // if no output filename provided
    public String getStreamSource() {
        if (StringUtils.isBlank(streamSource)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(elevation, "streamSource", "Raster Dataset", "tif"));
        }
        return streamSource;
    }

    private PeukerDouglasParams(Builder builder) {
        elevation = builder.elevation;
        centerSmoothingWeight = builder.centerSmoothingWeight;
        sideSmoothingWeight = builder.sideSmoothingWeight;
        diagonalSmoothingWeight = builder.diagonalSmoothingWeight;
        streamSource = builder.streamSource;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String elevation;
        private Double centerSmoothingWeight;
        private Double sideSmoothingWeight;
        private Double diagonalSmoothingWeight;
        private String streamSource;

        /**
         * @param elevation A grid of elevation values.
         */
        public Builder(@NotBlank String elevation) {
            this.elevation = elevation;
        }

        public Builder elevation(String val) {
            elevation = val;
            return this;
        }

        public Builder centerSmoothingWeight(Double val) {
            centerSmoothingWeight = val;
            return this;
        }

        public Builder sideSmoothingWeight(Double val) {
            sideSmoothingWeight = val;
            return this;
        }

        public Builder diagonalSmoothingWeight(Double val) {
            diagonalSmoothingWeight = val;
            return this;
        }

        public Builder streamSource(String val) {
            streamSource = val;
            return this;
        }

        public PeukerDouglasParams build() {
            return new PeukerDouglasParams(this);
        }
    }
}