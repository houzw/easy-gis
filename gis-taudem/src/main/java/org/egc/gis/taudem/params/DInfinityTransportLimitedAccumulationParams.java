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
 * Wrapper of parameters of dInfinityTransportLimitedAccumulation
 *
 * @author houzhiwei
 * @date 2020-05-21T14:24:36+08:00
 */
@Data
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DInfinityTransportLimitedAccumulationParams implements Params {
    private static final long serialVersionUID = 474362311637061617L;

    /**
     * <pre>
     * dinfinityFlowDirection
     * supply
     * transportCapacity
     * concentration
     * outlets
     *  </pre>
     *
     * @see Builder#Builder(String, String, String)
     */
    public DInfinityTransportLimitedAccumulationParams() {
    }

    /**
     * A grid giving flow direction by the D-infinity method.
     */
    @NotNull
    private String dinfinityFlowDirection;

    /**
     * A grid giving the supply (loading) of material to a transport limited accumulation function.
     */
    @NotNull
    private String supply;

    /**
     * A grid giving the transport capacity at each grid cell for the transport limited accumulation function.
     */
    @NotNull
    private String transportCapacity;

    /**
     * A grid giving the concentration of a compound of interest in the supply to the transport limited accumulation function.
     */
    private String concentration;

    /**
     * This optional input is a point feature  defining outlets of interest.
     */
    private String outlets;


    /**
     * default is true.
     * This checkbox determines whether the tool should check for edge contamination.
     */
    @XmlElement(defaultValue = "true")
    private Boolean checkForEdgeContamination = true;


    /**
     * This grid is the weighted accumulation of supply accumulated respecting the limitations in transport capacity and reports the transport rate calculated by accumulating the substance flux subject to the rule that the transport out of any grid cell is the minimum of the total supply (local supply plus transport in) to that grid cell and the transport capacity.
     */
    private String transportLimitedAccumulation;

    /**
     * A grid giving the deposition resulting from the transport limited accumulation.
     */
    private String deposition;

    /**
     * If an input concentation in supply grid is given, then this grid is also output and gives the concentration of a compound (contaminant) adhered or bound to the transported substance (e.
     */
    private String outputConcentration;

    @Setter
    @XmlElement
    private String outputDir = System.getProperty("java.io.tmpdir");

    // if no output filename provided
    public String getTransportLimitedAccumulation() {
        if (StringUtils.isBlank(transportLimitedAccumulation)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(dinfinityFlowDirection, "transportLimitedAccumulation", "Raster Dataset", "tif"));
        }
        return transportLimitedAccumulation;
    }

    // if no output filename provided
    public String getDeposition() {
        if (StringUtils.isBlank(deposition)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(dinfinityFlowDirection, "deposition", "Raster Dataset", "tif"));
        }
        return deposition;
    }

    // if no output filename provided
    public String getOutputConcentration() {
        if (StringUtils.isBlank(outputConcentration)) {
            return FilenameUtils.normalize(outputDir + File.separator + namingOutput(dinfinityFlowDirection, "outputConcentration", "Raster Dataset", "tif"));
        }
        return outputConcentration;
    }

    private DInfinityTransportLimitedAccumulationParams(Builder builder) {
        dinfinityFlowDirection = builder.dinfinityFlowDirection;
        supply = builder.supply;
        transportCapacity = builder.transportCapacity;
        concentration = builder.concentration;
        outlets = builder.outlets;
        checkForEdgeContamination = builder.checkForEdgeContamination;
        transportLimitedAccumulation = builder.transportLimitedAccumulation;
        deposition = builder.deposition;
        outputConcentration = builder.outputConcentration;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Builder {
        private String dinfinityFlowDirection;
        private String supply;
        private String transportCapacity;
        private String concentration;
        private String outlets;
        private Boolean checkForEdgeContamination;
        private String transportLimitedAccumulation;
        private String deposition;
        private String outputConcentration;

        /**
         * @param dinfinityFlowDirection A grid giving flow direction by the D-infinity method.
         * @param supply                 A grid giving the supply (loading) of material to a transport limited accumulation function.
         * @param transportCapacity      A grid giving the transport capacity at each grid cell for the transport limited accumulation function.
         */
        public Builder(@NotBlank String dinfinityFlowDirection, @NotBlank String supply, @NotBlank String transportCapacity) {
            this.dinfinityFlowDirection = dinfinityFlowDirection;
            this.supply = supply;
            this.transportCapacity = transportCapacity;
        }

        public Builder dinfinityFlowDirection(String val) {
            dinfinityFlowDirection = val;
            return this;
        }

        public Builder supply(String val) {
            supply = val;
            return this;
        }

        public Builder transportCapacity(String val) {
            transportCapacity = val;
            return this;
        }

        public Builder concentration(String val) {
            concentration = val;
            return this;
        }

        public Builder outlets(String val) {
            outlets = val;
            return this;
        }

        public Builder checkForEdgeContamination(Boolean val) {
            checkForEdgeContamination = val;
            return this;
        }

        public Builder transportLimitedAccumulation(String val) {
            transportLimitedAccumulation = val;
            return this;
        }

        public Builder deposition(String val) {
            deposition = val;
            return this;
        }

        public Builder outputConcentration(String val) {
            outputConcentration = val;
            return this;
        }

        public DInfinityTransportLimitedAccumulationParams build() {
            return new DInfinityTransportLimitedAccumulationParams(this);
        }
    }
}