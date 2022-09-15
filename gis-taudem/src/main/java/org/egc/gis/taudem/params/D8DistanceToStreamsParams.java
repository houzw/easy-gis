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
 * Wrapper of parameters of d8DistanceToStreams
 *
 * @author houzhiwei
 * @date 2020-06-28T12:02:31+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class D8DistanceToStreamsParams implements Params {
    /**
     * <pre>     * d8FlowDirection     * streamRaster
     *  </pre>
     *
     * @see Builder#Builder(String, String)
     */
    public D8DistanceToStreamsParams() {
    }

    /**
     * This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
     */
    @NotNull
    private String d8FlowDirection;

    /**
     * A grid indicating streams.
     */
    @NotNull
    private String streamRaster;


    /**
     * default is 50d.
     * This value acts as threshold on the Stream Raster Grid to determine the location of streams.
     */
    @XmlElement(defaultValue = "50")
    private Double threshold = 50d;


    /**
     * A grid giving the horizontal distance along the flow path as defined by the D8 Flow Directions Grid to the streams in the Stream Raster Grid.
     */
    @NotNull
    private String distanceToStreams;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getDistanceToStreams() {
        // if no output filename provided
        if (StringUtils.isBlank(distanceToStreams)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(d8FlowDirection, "distanceToStreams", "Raster Dataset", "tif"));
        }
        return this.distanceToStreams;
    }

    private D8DistanceToStreamsParams(Builder builder) {
        this.d8FlowDirection = builder.d8FlowDirection;
        this.streamRaster = builder.streamRaster;
        this.threshold = builder.threshold;
        this.distanceToStreams = builder.distanceToStreams;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String d8FlowDirection;
        private String streamRaster;
        private Double threshold;
        private String distanceToStreams;

        /**
         * @param d8FlowDirection This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
         * @param streamRaster    A grid indicating streams.
         */
        public Builder(@NotBlank String d8FlowDirection, @NotBlank String streamRaster) {
            this.d8FlowDirection = d8FlowDirection;
            this.streamRaster = streamRaster;
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
         * @param val A grid indicating streams.
         */
        public Builder streamRaster(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.streamRaster = val;
            }
            return this;
        }

        /**
         * default value is 50d
         *
         * @param val This value acts as threshold on the Stream Raster Grid to determine the location of streams.
         */
        public Builder threshold(Double val) {

            if (val != null) {
                this.threshold = val;
            }
            return this;
        }

        /**
         * @param val A grid giving the horizontal distance along the flow path as defined by the D8 Flow Directions Grid to the streams in the Stream Raster Grid.
         */
        public Builder distanceToStreams(String val) {
            if (StringUtils.isNotBlank(val)) {
                this.distanceToStreams = val;
            }
            return this;
        }

        public D8DistanceToStreamsParams build() {
            return new D8DistanceToStreamsParams(this);
        }
    }
}