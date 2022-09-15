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
 * Wrapper of parameters of d8ContributingArea
 * @author houzhiwei
 * @date 2020-06-28T12:02:31+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class D8ContributingAreaParams implements Params {
    /**
     * <pre>     * d8FlowDirection     * outlets     * weight
     *  </pre>
     * @see Builder#Builder( String)
     */
    public D8ContributingAreaParams(){}
    /**
     * A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
     */
    @NotNull
    private String d8FlowDirection;

    /**
    * A point feature defining the outlets of interest.
    */
    private String outlets;

    /**
    * A grid giving contribution to flow for each cell.
    */
    private String weight;


    /**
    * default is true.
    * A flag that indicates whether the tool should check for edge contamination.
    */
    @XmlElement(defaultValue = "true")
    private Boolean checkForEdgeContamination = true;


    /**
     * A grid of contributing area values calculated as the cells own contribution plus the contribution from upslope neighbors that drain in to it according to the D8 flow model.
     */
    @NotNull
    private String d8ContributingArea;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    public String getD8ContributingArea() {
        // if no output filename provided
        if (StringUtils.isBlank(d8ContributingArea)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(d8FlowDirection, "d8ContributingArea", "Raster Dataset", "tif"));
        }
        return this.d8ContributingArea;
    }

    private D8ContributingAreaParams(Builder builder){
        this.d8FlowDirection = builder.d8FlowDirection;
        this.outlets = builder.outlets;
        this.weight = builder.weight;
        this.checkForEdgeContamination = builder.checkForEdgeContamination;
        this.d8ContributingArea = builder.d8ContributingArea;
    }
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String d8FlowDirection;
        private String outlets;
        private String weight;
        private Boolean checkForEdgeContamination;
        private String d8ContributingArea;

        /**
        * @param d8FlowDirection A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        */
        public Builder(@NotBlank String d8FlowDirection){
            this.d8FlowDirection = d8FlowDirection;
        }
        /**
* 
        * @param val A grid of D8 flow directions which are defined, for each cell, as the direction of the one of its eight adjacent or diagonal neighbors with the steepest downward slope.
        */
        public Builder d8FlowDirection(String val){
            if (StringUtils.isNotBlank(val)) {
                this.d8FlowDirection = val;
            } 
            return this;
        }
        /**
* 
        * @param val A point feature defining the outlets of interest.
        */
        public Builder outlets(String val){
            if (StringUtils.isNotBlank(val)) {
                this.outlets = val;
            } 
            return this;
        }
        /**
* 
        * @param val A grid giving contribution to flow for each cell.
        */
        public Builder weight(String val){
            if (StringUtils.isNotBlank(val)) {
                this.weight = val;
            } 
            return this;
        }
        /**
*  default value is  true
        * @param val A flag that indicates whether the tool should check for edge contamination.
        */
        public Builder checkForEdgeContamination(Boolean val){

            if(val != null){
                this.checkForEdgeContamination = val;
            }
            return this;
        }
        /**
* 
        * @param val A grid of contributing area values calculated as the cells own contribution plus the contribution from upslope neighbors that drain in to it according to the D8 flow model.
        */
        public Builder d8ContributingArea(String val){
            if (StringUtils.isNotBlank(val)) {
                this.d8ContributingArea = val;
            } 
            return this;
        }

        public D8ContributingAreaParams build() {
            return new D8ContributingAreaParams(this);
        }
    }
}