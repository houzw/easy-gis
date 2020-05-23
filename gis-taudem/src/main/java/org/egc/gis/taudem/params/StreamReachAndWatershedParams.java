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
 * Wrapper of parameters of streamReachAndWatershed
 *
 * @author houzhiwei
 * @date 2020-05-21T14:24:36+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class StreamReachAndWatershedParams implements Params {
    private static final long serialVersionUID = -8730494915989904370L;

    /**
     * <pre>
     * pitFilledElevation
     * d8FlowDirection
     * d8DrainageArea
     * streamRaster
     * outlets
     *  </pre>
     *
     * @see Builder#Builder(String, String, String, String)
     */
    public StreamReachAndWatershedParams() {
    }

    /**
     * This input is a grid of elevation values.
     */
    @NotNull
    private String pitFilledElevation;

    /**
     * This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
     */
    @NotNull
    private String d8FlowDirection;

    /**
     * A grid giving the contributing area value in terms of the number of grid cells (or the summation of weights) for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it using the D8 algorithm.
     */
    @NotNull
    private String d8DrainageArea;

    /**
     * An indicator grid indicating streams, by using a grid cell value of 1 on streams and 0 off streams.
     */
    @NotNull
    private String streamRaster;

    /**
     * A point feature defining points of interest.
     */
    private String outlets;


    /**
     * default is false.
     * This option causes the tool to delineate a single watershed by representing the entire area draining to the Stream Network as a single value in the output watershed grid.
     */
    @XmlElement(defaultValue = "false")
    private Boolean delineateSingleWatershed = false;


    /**
     * The Stream Order Grid has cells values of streams ordered according to the Strahler order system.
     */
    private String streamOrder;

    /**
     * This output is a text file that details the network topological connectivity is stored in the Stream Network Tree file.
     */
    private String networkConnectivityTree;

    /**
     * This output is a text file that contains the coordinates and attributes of points along the stream network.
     */
    private String networkCoordinates;

    /**
     * This output is a polyline OGR file giving the links in a stream network.
     */
    private String streamReachFile;

    /**
     * This output grid identified each reach watershed with a unique ID number, or in the case where the delineate single watershed option was checked, the entire area draining to the stream network is identified with a single ID.
     */
    private String watershed;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    // if no output filename provided
    public String getStreamOrder() {
        if (StringUtils.isBlank(streamOrder)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(pitFilledElevation, "streamOrder", "Raster Dataset", "tif"));
        }
        return streamOrder;
    }

    // if no output filename provided
    public String getNetworkConnectivityTree() {
        if (StringUtils.isBlank(networkConnectivityTree)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(pitFilledElevation, "networkConnectivityTree", "File", "txt"));
        }
        return networkConnectivityTree;
    }

    // if no output filename provided
    public String getNetworkCoordinates() {
        if (StringUtils.isBlank(networkCoordinates)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(pitFilledElevation, "networkCoordinates", "File", "txt"));
        }
        return networkCoordinates;
    }

    // if no output filename provided
    public String getStreamReachFile() {
        if (StringUtils.isBlank(streamReachFile)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(pitFilledElevation, "streamReachFile", "File", "txt"));
        }
        return streamReachFile;
    }

    // if no output filename provided
    public String getWatershed() {
        if (StringUtils.isBlank(watershed)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(pitFilledElevation, "watershed", "Raster Dataset", "tif"));
        }
        return watershed;
    }

    private StreamReachAndWatershedParams(Builder builder) {
        pitFilledElevation = builder.pitFilledElevation;
        d8FlowDirection = builder.d8FlowDirection;
        d8DrainageArea = builder.d8DrainageArea;
        streamRaster = builder.streamRaster;
        outlets = builder.outlets;
        delineateSingleWatershed = builder.delineateSingleWatershed;
        streamOrder = builder.streamOrder;
        networkConnectivityTree = builder.networkConnectivityTree;
        networkCoordinates = builder.networkCoordinates;
        streamReachFile = builder.streamReachFile;
        watershed = builder.watershed;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String pitFilledElevation;
        private String d8FlowDirection;
        private String d8DrainageArea;
        private String streamRaster;
        private String outlets;
        private Boolean delineateSingleWatershed;
        private String streamOrder;
        private String networkConnectivityTree;
        private String networkCoordinates;
        private String streamReachFile;
        private String watershed;

        /**
         * @param pitFilledElevation This input is a grid of elevation values.
         * @param d8FlowDirection    This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
         * @param d8DrainageArea     A grid giving the contributing area value in terms of the number of grid cells (or the summation of weights) for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it using the D8 algorithm.
         * @param streamRaster       An indicator grid indicating streams, by using a grid cell value of 1 on streams and 0 off streams.
         */
        public Builder(@NotBlank String pitFilledElevation, @NotBlank String d8FlowDirection, @NotBlank String d8DrainageArea, @NotBlank String streamRaster) {
            this.pitFilledElevation = pitFilledElevation;
            this.d8FlowDirection = d8FlowDirection;
            this.d8DrainageArea = d8DrainageArea;
            this.streamRaster = streamRaster;
        }

        public Builder pitFilledElevation(String val) {
            pitFilledElevation = val;
            return this;
        }

        public Builder d8FlowDirection(String val) {
            d8FlowDirection = val;
            return this;
        }

        public Builder d8DrainageArea(String val) {
            d8DrainageArea = val;
            return this;
        }

        public Builder streamRaster(String val) {
            streamRaster = val;
            return this;
        }

        public Builder outlets(String val) {
            outlets = val;
            return this;
        }

        public Builder delineateSingleWatershed(Boolean val) {
            delineateSingleWatershed = val;
            return this;
        }

        public Builder streamOrder(String val) {
            streamOrder = val;
            return this;
        }

        public Builder networkConnectivityTree(String val) {
            networkConnectivityTree = val;
            return this;
        }

        public Builder networkCoordinates(String val) {
            networkCoordinates = val;
            return this;
        }

        public Builder streamReachFile(String val) {
            streamReachFile = val;
            return this;
        }

        public Builder watershed(String val) {
            watershed = val;
            return this;
        }

        public StreamReachAndWatershedParams build() {
            return new StreamReachAndWatershedParams(this);
        }
    }
}