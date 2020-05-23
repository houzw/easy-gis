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
 * @date 2020-05-21T14:24:36+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class D8DistanceToStreamsParams implements Params {
    private static final long serialVersionUID = -2093292811600675024L;

    /**
     * <pre>
     * d8FlowDirection
     * streamRaster
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
    private String distanceToStreams;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    // if no output filename provided
    public String getDistanceToStreams() {
        if (StringUtils.isBlank(distanceToStreams)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(d8FlowDirection, "distanceToStreams", "Raster Dataset", "tif"));
        }
        return distanceToStreams;
    }

    private D8DistanceToStreamsParams(Builder builder) {
        d8FlowDirection = builder.d8FlowDirection;
        streamRaster = builder.streamRaster;
        threshold = builder.threshold;
        distanceToStreams = builder.distanceToStreams;
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

        public Builder d8FlowDirection(String val) {
            d8FlowDirection = val;
            return this;
        }

        public Builder streamRaster(String val) {
            streamRaster = val;
            return this;
        }

        public Builder threshold(Double val) {
            threshold = val;
            return this;
        }

        public Builder distanceToStreams(String val) {
            distanceToStreams = val;
            return this;
        }

        public D8DistanceToStreamsParams build() {
            return new D8DistanceToStreamsParams(this);
        }
    }
}