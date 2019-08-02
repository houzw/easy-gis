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
 * Wrapper of parameters of ConnectDown
 * @author houzhiwei
 * @date 2018-12-17T22:40:56+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ConnectDownParams implements Params {

    /**
     * <pre>
     * Input_D8_Flow_Direction_Grid
     * Input_D8_Contributing_Area_Grid
     *  </pre>
     * @see Builder#Builder( String,  String)
     */
    public ConnectDownParams(){}
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
    private String Input_D8_Contributing_Area_Grid;
    /**
    * Watershed grid delineated from gage watershed function or streamreachwatershed function.
    */
    private String Input_Watershed_Grid;
    /**
    * Number of grid cells move to downstream based on flow directions.
    */
    private Long Input_Number_of_Grid_Cells;
    /**
    * This output is point a OGR file where each point is created from watershed grid having the largest contributing area for each zone.
    */
    private String Output_Outlets_file;
    /**
    * This output is a point OGR file where each outlet is moved downflow a specified number of grid cells using flow directions.
    */
    private String Output_MovedOutlets_file;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_Outlets_file() {
        if (StringUtils.isBlank(Output_Outlets_file)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_Outlets_file", "File", null));
        }
        return this.Output_Outlets_file;
    }
    public String getOutput_MovedOutlets_file() {
        if (StringUtils.isBlank(Output_MovedOutlets_file)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_D8_Flow_Direction_Grid, "Output_MovedOutlets_file", "File", null));
        }
        return this.Output_MovedOutlets_file;
    }

    private ConnectDownParams(Builder builder){
        this.Input_D8_Flow_Direction_Grid = builder.Input_D8_Flow_Direction_Grid;
        this.Input_D8_Contributing_Area_Grid = builder.Input_D8_Contributing_Area_Grid;
        this.Input_Watershed_Grid = builder.Input_Watershed_Grid;
        this.Input_Number_of_Grid_Cells = builder.Input_Number_of_Grid_Cells;
        this.Output_Outlets_file = builder.Output_Outlets_file;
        this.Output_MovedOutlets_file = builder.Output_MovedOutlets_file;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_D8_Flow_Direction_Grid;
        private String Input_D8_Contributing_Area_Grid;
        private String Input_Watershed_Grid;
        private Long Input_Number_of_Grid_Cells;
        private String Output_Outlets_file;
        private String Output_MovedOutlets_file;

        /**
        * @param Input_D8_Flow_Direction_Grid This input is a grid of flow directions that are encoded using the D8 method where all flow from a cells goes to a single neighboring cell in the direction of steepest descent.
        * @param Input_D8_Contributing_Area_Grid A grid giving the contributing area value in terms of the number of grid cells (or the summation of weights) for each cell taken as its own contribution plus the contribution from upslope neighbors that drain in to it using the D8 algorithm.
        */
        public Builder(@NotBlank String Input_D8_Flow_Direction_Grid, @NotBlank String Input_D8_Contributing_Area_Grid){
            this.Input_D8_Flow_Direction_Grid = Input_D8_Flow_Direction_Grid;
            this.Input_D8_Contributing_Area_Grid = Input_D8_Contributing_Area_Grid;
        }

        public Builder Input_D8_Flow_Direction_Grid(String val){
            this.Input_D8_Flow_Direction_Grid = val;
            return this;
        }
        public Builder Input_D8_Contributing_Area_Grid(String val){
            this.Input_D8_Contributing_Area_Grid = val;
            return this;
        }
        public Builder Input_Watershed_Grid(String val){
            this.Input_Watershed_Grid = val;
            return this;
        }
        public Builder Input_Number_of_Grid_Cells(Long val){
            this.Input_Number_of_Grid_Cells = val;
            return this;
        }
        public Builder Output_Outlets_file(String val){
            this.Output_Outlets_file = val;
            return this;
        }
        public Builder Output_MovedOutlets_file(String val){
            this.Output_MovedOutlets_file = val;
            return this;
        }

        public ConnectDownParams build() {
            return new ConnectDownParams(this);
        }
    }
}