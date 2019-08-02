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
 * Wrapper of parameters of StreamReachAndWatershed
 * @author houzhiwei
 * @date 2018-12-17T22:40:56+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class StreamReachAndWatershedParams implements Params {

    /**
     * <pre>
     * Input_Pit_Filled_Elevation_Grid
     * Input_D8_Flow_Direction_Grid
     * Input_D8_Drainage_Area
     * Input_Stream_Raster_Grid
     *  </pre>
     * @see Builder#Builder( String,  String,  String,  String)
     */
    public StreamReachAndWatershedParams(){}
    /**
     * This input is a grid of elevation values.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Pit_Filled_Elevation_Grid;
    /**
     * This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_D8_Flow_Direction_Grid;
    /**
     * A grid giving the contributing area value in terms of the number of grid cells (or the summation of weights) for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it using the D8 algorithm.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_D8_Drainage_Area;
    /**
     * An indicator grid indicating streams, by using a grid cell value of 1 on streams and 0 off streams.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Stream_Raster_Grid;
    /**
    * A point feature defining points of interest.
    */
    private String Input_Outlets;
    /**
    * default is false.
    * This option causes the tool to delineate a single watershed by representing the entire area draining to the Stream Network as a single value in the output watershed grid.
    */
    @XmlElement(defaultValue = "false")
    private Boolean Delineate_Single_Watershed = false;

    /**
    * The Stream Order Grid has cells values of streams ordered according to the Strahler order system.
    */
    private String Output_Stream_Order_Grid;
    /**
    * This output is a text file that details the network topological connectivity is stored in the Stream Network Tree file.
    */
    private String Output_Network_Connectivity_Tree;
    /**
    * This output is a text file that contains the coordinates and attributes of points along the stream network.
    */
    private String Output_Network_Coordinates;
    /**
    * This output is a polyline OGR file giving the links in a stream network.
    */
    private String Output_Stream_Reach_file;
    /**
    * This output grid identified each reach watershed with a unique ID number, or in the case where the delineate single watershed option was checked, the entire area draining to the stream network is identified with a single ID.
    */
    private String Output_Watershed_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_Stream_Order_Grid() {
        if (StringUtils.isBlank(Output_Stream_Order_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_Filled_Elevation_Grid, "Output_Stream_Order_Grid", "Raster Dataset", null));
        }
        return this.Output_Stream_Order_Grid;
    }
    public String getOutput_Network_Connectivity_Tree() {
        if (StringUtils.isBlank(Output_Network_Connectivity_Tree)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_Filled_Elevation_Grid, "Output_Network_Connectivity_Tree", "File", null));
        }
        return this.Output_Network_Connectivity_Tree;
    }
    public String getOutput_Network_Coordinates() {
        if (StringUtils.isBlank(Output_Network_Coordinates)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_Filled_Elevation_Grid, "Output_Network_Coordinates", "File", null));
        }
        return this.Output_Network_Coordinates;
    }
    public String getOutput_Stream_Reach_file() {
        if (StringUtils.isBlank(Output_Stream_Reach_file)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_Filled_Elevation_Grid, "Output_Stream_Reach_file", "File", null));
        }
        return this.Output_Stream_Reach_file;
    }
    public String getOutput_Watershed_Grid() {
        if (StringUtils.isBlank(Output_Watershed_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Pit_Filled_Elevation_Grid, "Output_Watershed_Grid", "Raster Dataset", null));
        }
        return this.Output_Watershed_Grid;
    }

    private StreamReachAndWatershedParams(Builder builder){
        this.Input_Pit_Filled_Elevation_Grid = builder.Input_Pit_Filled_Elevation_Grid;
        this.Input_D8_Flow_Direction_Grid = builder.Input_D8_Flow_Direction_Grid;
        this.Input_D8_Drainage_Area = builder.Input_D8_Drainage_Area;
        this.Input_Stream_Raster_Grid = builder.Input_Stream_Raster_Grid;
        this.Input_Outlets = builder.Input_Outlets;
        this.Delineate_Single_Watershed = builder.Delineate_Single_Watershed;
        this.Output_Stream_Order_Grid = builder.Output_Stream_Order_Grid;
        this.Output_Network_Connectivity_Tree = builder.Output_Network_Connectivity_Tree;
        this.Output_Network_Coordinates = builder.Output_Network_Coordinates;
        this.Output_Stream_Reach_file = builder.Output_Stream_Reach_file;
        this.Output_Watershed_Grid = builder.Output_Watershed_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_Pit_Filled_Elevation_Grid;
        private String Input_D8_Flow_Direction_Grid;
        private String Input_D8_Drainage_Area;
        private String Input_Stream_Raster_Grid;
        private String Input_Outlets;
        private Boolean Delineate_Single_Watershed;
        private String Output_Stream_Order_Grid;
        private String Output_Network_Connectivity_Tree;
        private String Output_Network_Coordinates;
        private String Output_Stream_Reach_file;
        private String Output_Watershed_Grid;

        /**
        * @param Input_Pit_Filled_Elevation_Grid This input is a grid of elevation values.
        * @param Input_D8_Flow_Direction_Grid This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
        * @param Input_D8_Drainage_Area A grid giving the contributing area value in terms of the number of grid cells (or the summation of weights) for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it using the D8 algorithm.
        * @param Input_Stream_Raster_Grid An indicator grid indicating streams, by using a grid cell value of 1 on streams and 0 off streams.
        */
        public Builder(@NotBlank String Input_Pit_Filled_Elevation_Grid, @NotBlank String Input_D8_Flow_Direction_Grid, @NotBlank String Input_D8_Drainage_Area, @NotBlank String Input_Stream_Raster_Grid){
            this.Input_Pit_Filled_Elevation_Grid = Input_Pit_Filled_Elevation_Grid;
            this.Input_D8_Flow_Direction_Grid = Input_D8_Flow_Direction_Grid;
            this.Input_D8_Drainage_Area = Input_D8_Drainage_Area;
            this.Input_Stream_Raster_Grid = Input_Stream_Raster_Grid;
        }

        public Builder Input_Pit_Filled_Elevation_Grid(String val){
            this.Input_Pit_Filled_Elevation_Grid = val;
            return this;
        }
        public Builder Input_D8_Flow_Direction_Grid(String val){
            this.Input_D8_Flow_Direction_Grid = val;
            return this;
        }
        public Builder Input_D8_Drainage_Area(String val){
            this.Input_D8_Drainage_Area = val;
            return this;
        }
        public Builder Input_Stream_Raster_Grid(String val){
            this.Input_Stream_Raster_Grid = val;
            return this;
        }
        public Builder Input_Outlets(String val){
            this.Input_Outlets = val;
            return this;
        }
        public Builder Delineate_Single_Watershed(Boolean val){
            this.Delineate_Single_Watershed = val;
            return this;
        }
        public Builder Output_Stream_Order_Grid(String val){
            this.Output_Stream_Order_Grid = val;
            return this;
        }
        public Builder Output_Network_Connectivity_Tree(String val){
            this.Output_Network_Connectivity_Tree = val;
            return this;
        }
        public Builder Output_Network_Coordinates(String val){
            this.Output_Network_Coordinates = val;
            return this;
        }
        public Builder Output_Stream_Reach_file(String val){
            this.Output_Stream_Reach_file = val;
            return this;
        }
        public Builder Output_Watershed_Grid(String val){
            this.Output_Watershed_Grid = val;
            return this;
        }

        public StreamReachAndWatershedParams build() {
            return new StreamReachAndWatershedParams(this);
        }
    }
}