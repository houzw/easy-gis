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
 * @date 2020-06-28T12:02:30+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PeukerDouglasParams implements Params {
    /**
     * <pre>     * elevation
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
    @NotNull
    private String streamSource;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getStreamSource() {
        // if no output filename provided
        if (StringUtils.isBlank(streamSource)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(elevation, "streamSource", "Raster Dataset", "tif"));
        }
        return this.streamSource;
    }

    private PeukerDouglasParams(Builder builder) {
        this.elevation = builder.elevation;
        this.centerSmoothingWeight = builder.centerSmoothingWeight;
        this.sideSmoothingWeight = builder.sideSmoothingWeight;
        this.diagonalSmoothingWeight = builder.diagonalSmoothingWeight;
        this.streamSource = builder.streamSource;
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

        /**
         * @param val A grid of elevation values.
         */
        public Builder elevation(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.elevation = val;
            }
            return this;
        }

        /**
         * default value is 0.4d
         *
         * @param val The center weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
         */
        public Builder centerSmoothingWeight(Double val) {

            if (val != null) {
                this.centerSmoothingWeight = val;
            }
            return this;
        }

        /**
         * default value is 0.1d
         *
         * @param val The side weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
         */
        public Builder sideSmoothingWeight(Double val) {

            if (val != null) {
                this.sideSmoothingWeight = val;
            }
            return this;
        }

        /**
         * default value is 0.05d
         *
         * @param val The diagonal weight parameter used by a kernel to smooth the DEM before the tool identifies upwardly curved grid cells.
         */
        public Builder diagonalSmoothingWeight(Double val) {

            if (val != null) {
                this.diagonalSmoothingWeight = val;
            }
            return this;
        }

        /**
         * @param val An indicator grid (1,0) of upward curved grid cells according to the Peuker and Douglas algorithm, and if viewed, resembles a channel network.
         */
        public Builder streamSource(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.streamSource = val;
            }
            return this;
        }

        public PeukerDouglasParams build() {
            return new PeukerDouglasParams(this);
        }
    }
}