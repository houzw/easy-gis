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
 * Wrapper of parameters of moveOutletsToStreams
 *
 * @author houzhiwei
 * @date 2020-06-28T12:02:30+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class MoveOutletsToStreamsParams implements Params {
    /**
     * <pre>     * d8FlowDirection     * streamRaster     * outlets
     *  </pre>
     *
     * @see Builder#Builder(String, String, String)
     */
    public MoveOutletsToStreamsParams() {
    }

    /**
     * This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
     */
    @NotNull
    private String d8FlowDirection;

    /**
     * This output is an indicator grid (1,0) that indicates the location of streams, with a value of 1 for each of the stream cells and 0 for the remainder of the cells.
     */
    @NotNull
    private String streamRaster;

    /**
     * A point feature defining points of interest or outlets that should ideally be located on a stream, but may not be exactly on the stream due to the fact that the feature point locations may not have been accurately registered with respect to the stream raster grid.
     */
    @NotNull
    private String outlets;


    /**
     * default is 50d.
     * This input paramater is the maximum number of grid cells that the points in the input outlet feature  will be moved before they are saved to the output outlet feature.
     */
    @XmlElement(defaultValue = "50")
    private Double maximumDistance = 50d;


    /**
     * A point  OGR file defining points of interest or outlets.
     */
    @NotNull
    private String outletsFile;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getOutletsFile() {
        // if no output filename provided
        if (StringUtils.isBlank(outletsFile)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(d8FlowDirection, "outletsFile", "Feature Layer", "shp"));
        }
        return this.outletsFile;
    }

    private MoveOutletsToStreamsParams(Builder builder) {
        this.d8FlowDirection = builder.d8FlowDirection;
        this.streamRaster = builder.streamRaster;
        this.outlets = builder.outlets;
        this.maximumDistance = builder.maximumDistance;
        this.outletsFile = builder.outletsFile;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String d8FlowDirection;
        private String streamRaster;
        private String outlets;
        private Double maximumDistance;
        private String outletsFile;

        /**
         * @param d8FlowDirection This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
         * @param streamRaster    This output is an indicator grid (1,0) that indicates the location of streams, with a value of 1 for each of the stream cells and 0 for the remainder of the cells.
         * @param outlets         A point feature defining points of interest or outlets that should ideally be located on a stream, but may not be exactly on the stream due to the fact that the feature point locations may not have been accurately registered with respect to the stream raster grid.
         */
        public Builder(@NotBlank String d8FlowDirection, @NotBlank String streamRaster, @NotBlank String outlets) {
            this.d8FlowDirection = d8FlowDirection;
            this.streamRaster = streamRaster;
            this.outlets = outlets;
        }

        /**
         * @param val This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
         */
        public Builder d8FlowDirection(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.d8FlowDirection = val;
            }
            return this;
        }

        /**
         * @param val This output is an indicator grid (1,0) that indicates the location of streams, with a value of 1 for each of the stream cells and 0 for the remainder of the cells.
         */
        public Builder streamRaster(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.streamRaster = val;
            }
            return this;
        }

        /**
         * @param val A point feature defining points of interest or outlets that should ideally be located on a stream, but may not be exactly on the stream due to the fact that the feature point locations may not have been accurately registered with respect to the stream raster grid.
         */
        public Builder outlets(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.outlets = val;
            }
            return this;
        }

        /**
         * default value is 50d
         *
         * @param val This input paramater is the maximum number of grid cells that the points in the input outlet feature  will be moved before they are saved to the output outlet feature.
         */
        public Builder maximumDistance(Double val) {

            if (val != null) {
                this.maximumDistance = val;
            }
            return this;
        }

        /**
         * @param val A point  OGR file defining points of interest or outlets.
         */
        public Builder outletsFile(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.outletsFile = val;
            }
            return this;
        }

        public MoveOutletsToStreamsParams build() {
            return new MoveOutletsToStreamsParams(this);
        }
    }
}