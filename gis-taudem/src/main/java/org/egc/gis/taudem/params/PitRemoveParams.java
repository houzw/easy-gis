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
 * Wrapper of parameters of PitRemove
 * @author houzhiwei
 * @date 2018-12-17T22:40:56+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PitRemoveParams implements Params {

    /**
     * <pre>
     * Input_Elevation_Grid
     *  </pre>
     * @see Builder#Builder( String)
     */
    public PitRemoveParams(){}
    /**
     * A digital elevation model (DEM) grid to serve as the base input for the terrain analysis and stream delineation.
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_Elevation_Grid;
    /**
    * If this option is selected Fill ensures that the grid is hydrologically conditioned with cell to cell connectivity in only 4 directions (N, S, E or W neighbors).
    */
    private Boolean Fill_Considering_only_4_way_neighbors;
    /**
    * 
    */
    private String Input_Depression_Mask_Grid;
    /**
    * A grid of elevation values with pits removed so that flow is routed off of the domain.
    */
    private String Output_Pit_Removed_Elevation_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_Pit_Removed_Elevation_Grid() {
        if (StringUtils.isBlank(Output_Pit_Removed_Elevation_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_Elevation_Grid, "Output_Pit_Removed_Elevation_Grid", "Raster Dataset", null));
        }
        return this.Output_Pit_Removed_Elevation_Grid;
    }

    private PitRemoveParams(Builder builder){
        this.Input_Elevation_Grid = builder.Input_Elevation_Grid;
        this.Fill_Considering_only_4_way_neighbors = builder.Fill_Considering_only_4_way_neighbors;
        this.Input_Depression_Mask_Grid = builder.Input_Depression_Mask_Grid;
        this.Output_Pit_Removed_Elevation_Grid = builder.Output_Pit_Removed_Elevation_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_Elevation_Grid;
        private Boolean Fill_Considering_only_4_way_neighbors;
        private String Input_Depression_Mask_Grid;
        private String Output_Pit_Removed_Elevation_Grid;

        /**
        * @param Input_Elevation_Grid A digital elevation model (DEM) grid to serve as the base input for the terrain analysis and stream delineation.
        */
        public Builder(@NotBlank String Input_Elevation_Grid){
            this.Input_Elevation_Grid = Input_Elevation_Grid;
        }

        public Builder Input_Elevation_Grid(String val){
            this.Input_Elevation_Grid = val;
            return this;
        }
        public Builder Fill_Considering_only_4_way_neighbors(Boolean val){
            this.Fill_Considering_only_4_way_neighbors = val;
            return this;
        }
        public Builder Input_Depression_Mask_Grid(String val){
            this.Input_Depression_Mask_Grid = val;
            return this;
        }
        public Builder Output_Pit_Removed_Elevation_Grid(String val){
            this.Output_Pit_Removed_Elevation_Grid = val;
            return this;
        }

        public PitRemoveParams build() {
            return new PitRemoveParams(this);
        }
    }
}