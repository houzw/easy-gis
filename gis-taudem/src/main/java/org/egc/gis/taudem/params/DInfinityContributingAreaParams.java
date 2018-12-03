package org.egc.gis.taudem.params;

import lombok.Getter;
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
 * Wrapper of parameters of DInfinityContributingArea
 * @author houzhiwei
 * @date 2018-12-03T19:26:15+08:00
 */
@Getter
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityContributingAreaParams implements Params {

    /**
     * <pre>
     * Input_DInfinity_Flow_Direction_Grid
     *  </pre>
     * @see Builder#Builder( String)
     */
    public DInfinityContributingAreaParams(){}
    /**
     * A grid of flow directions based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997, "A New Method for the Determination of Flow Directions and Contributing Areas in Grid Digital Elevation Models," Water Resources Research, 33(2): 309-319).
     */
    @NotNull
    @XmlElement(required = true)
    private String Input_DInfinity_Flow_Direction_Grid;
    /**
    * A point feature defining the outlets of interest.
    */
    private String Input_Outlets;
    /**
    * A grid giving contribution to flow for each cell.
    */
    private String Input_Weight_Grid;
    /**
    * default is true.
    * A flag that indicates whether the tool should check for edge contamination.
    */
    @XmlElement(defaultValue = "true")
    private Boolean Check_for_Edge_Contamination = true;

    /**
    * A grid of specific catchment area which is the contributing area per unit contour length using the multiple flow direction D-infinity approach.
    */
    private String Output_DInfinity_Specific_Catchment_Area_Grid;
    @Setter
    @XmlElement(defaultValue = "./")
    private String outputDir = "./";

    public String getOutput_DInfinity_Specific_Catchment_Area_Grid() {
        if (StringUtils.isBlank(Output_DInfinity_Specific_Catchment_Area_Grid)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(Input_DInfinity_Flow_Direction_Grid, "Output_DInfinity_Specific_Catchment_Area_Grid", "Raster Dataset", null));
        }
        return this.Output_DInfinity_Specific_Catchment_Area_Grid;
    }

    private DInfinityContributingAreaParams(Builder builder){
        this.Input_DInfinity_Flow_Direction_Grid = builder.Input_DInfinity_Flow_Direction_Grid;
        this.Input_Outlets = builder.Input_Outlets;
        this.Input_Weight_Grid = builder.Input_Weight_Grid;
        this.Check_for_Edge_Contamination = builder.Check_for_Edge_Contamination;
        this.Output_DInfinity_Specific_Catchment_Area_Grid = builder.Output_DInfinity_Specific_Catchment_Area_Grid;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String Input_DInfinity_Flow_Direction_Grid;
        private String Input_Outlets;
        private String Input_Weight_Grid;
        private Boolean Check_for_Edge_Contamination;
        private String Output_DInfinity_Specific_Catchment_Area_Grid;

        /**
        * @param Input_DInfinity_Flow_Direction_Grid A grid of flow directions based on the D-infinity flow method using the steepest slope of a triangular facet (Tarboton, 1997, "A New Method for the Determination of Flow Directions and Contributing Areas in Grid Digital Elevation Models," Water Resources Research, 33(2): 309-319).
        */
        public Builder(@NotBlank String Input_DInfinity_Flow_Direction_Grid){
            this.Input_DInfinity_Flow_Direction_Grid = Input_DInfinity_Flow_Direction_Grid;
        }

        public Builder Input_DInfinity_Flow_Direction_Grid(String val){
            this.Input_DInfinity_Flow_Direction_Grid = val;
            return this;
        }
        public Builder Input_Outlets(String val){
            this.Input_Outlets = val;
            return this;
        }
        public Builder Input_Weight_Grid(String val){
            this.Input_Weight_Grid = val;
            return this;
        }
        public Builder Check_for_Edge_Contamination(Boolean val){
            this.Check_for_Edge_Contamination = val;
            return this;
        }
        public Builder Output_DInfinity_Specific_Catchment_Area_Grid(String val){
            this.Output_DInfinity_Specific_Catchment_Area_Grid = val;
            return this;
        }

        public DInfinityContributingAreaParams build() {
            return new DInfinityContributingAreaParams(this);
        }
    }
}