package org.egc.gis.geotools.utils;

import it.geosolutions.jaiext.range.NoDataContainer;
import lombok.extern.slf4j.Slf4j;
import org.egc.commons.exception.BusinessException;
import org.egc.commons.gis.RasterMetadata;
import org.egc.gis.geotools.RasterIO;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.classify.ClassificationMethod;
import org.geotools.process.raster.CoverageClassStats;
import org.geotools.referencing.CRS;
import org.jaitools.numeric.Statistic;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.operation.TransformException;
import org.osgeo.proj4j.CRSFactory;

import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Description:
 * <pre>
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/11/13 18:59
 */
@Slf4j
public class GridCoverageUtils {

    /**
     * <pre>
     * Get coverage metadata.
     * slower than gdal
     * </pre>
     * TODO geogcs,projcs,isProjected
     *
     * @param coverage the coverage
     * @return the raster metadata
     */
    public static RasterMetadata getCoverageMetadata(GridCoverage2D coverage) {
        RasterMetadata metadata = new RasterMetadata();
        metadata.setFormat(coverage.getName().toString());
        NoDataContainer noDataProperty = CoverageUtilities.getNoDataProperty(coverage);
        //default no data value
        double nodata = -9999d;
        if (noDataProperty != null) {
            nodata = noDataProperty.getAsSingleValue();
        }
        metadata.setNodata(nodata);
        CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem();

        //获取wkt格式的投影信息
        metadata.setCrs(crs.getName().getCode());
        metadata.setCrsWkt(crs.toWKT());
        metadata.setUnit(crs.getCoordinateSystem().getAxis(0).getUnit().toString());
        if (crs instanceof ProjectedCRS) {
            metadata.setProjected(true);
        }
        try {
            Integer epsg = CRS.lookupEpsgCode(crs, true);
            metadata.setEpsg(epsg);
            metadata.setSrid(epsg);
            CRSFactory csFactory = new CRSFactory();
            if (epsg != null) {
                org.osgeo.proj4j.CoordinateReferenceSystem crsProj = csFactory.createFromName("EPSG:" + epsg);
                //获取proj4格式的投影信息
                metadata.setCrsProj4(crsProj.getProjection().getPROJ4Description());
            }
        } catch (FactoryException e) {
            throw new BusinessException(e, "Error occured while searching for the CRS srid");
        }

        Envelope2D envelope2D = coverage.getEnvelope2D();
        metadata.setHeight(envelope2D.height);
        metadata.setWidth(envelope2D.width);
        metadata.setMaxX(envelope2D.getMaxX());
        metadata.setMinX(envelope2D.getMinX());
        metadata.setMaxY(envelope2D.getMaxY());
        metadata.setMinY(envelope2D.getMinY());
        metadata.setCenterX(envelope2D.getCenterX());
        metadata.setCenterY(envelope2D.getCenterY());
        RenderedImage image = coverage.getRenderedImage();
        metadata.setSizeHeight(image.getHeight());
        metadata.setSizeWidth(image.getWidth());
        metadata.setPixelSize((envelope2D.getMaxY() - envelope2D.getMinY()) / image.getHeight());
        CoverageClassStats rasterProcess = new CoverageClassStats();
        Set<Statistic> set = new HashSet();
        set.add(Statistic.MAX);
        set.add(Statistic.MIN);
        set.add(Statistic.MEAN);
        set.add(Statistic.SDEV);
        int band_i = CoverageUtilities.getVisibleBand(image);
        //classes 分段数
        CoverageClassStats.Results results = null;
        try {
            results = rasterProcess.execute(coverage, set, band_i, 1,
                    ClassificationMethod.QUANTILE, nodata, null);
        } catch (IOException e) {
            throw new BusinessException(e, "Process raster file statistics failed");
        }
        metadata.setMinValue(results.value(0, Statistic.MIN));
        metadata.setMaxValue(results.value(0, Statistic.MAX));
        metadata.setMeanValue(results.value(0, Statistic.MEAN));
        metadata.setSdev(results.value(0, Statistic.SDEV));
        return metadata;
    }

    /**
     * Read geotiff and gets metadata. <br/>
     * Recommend to use gdal <br/>
     * 暂不包含 UniqueValues 及 Quantile values
     *
     * @param tif the geotiff
     * @return the raster metadata
     */
    public static RasterMetadata getMetadata(String tif) {
        GridCoverage2D coverage2D = RasterIO.readGeoTiff(tif);
        RasterMetadata metadata = getCoverageMetadata(coverage2D);
        metadata.setFormat("GeoTIFF");
        return metadata;
    }

    /**
     * clip coverage 2d
     * https://gis.stackexchange.com/questions/246374/cropping-geotiff-with-geotools-using-integer-indexes
     *
     * @param coverage coverage
     * @param r        Rectangle will have it's origin in the top left hand corner.
     * @return clipped GridCoverage2D
     */
    public static GridCoverage2D clip(GridCoverage2D coverage, Rectangle r) {

        ReferencedEnvelope envelope = new ReferencedEnvelope(coverage.getCoordinateReferenceSystem());
        GridGeometry2D grid = coverage.getGridGeometry();
        GridEnvelope2D genv = new GridEnvelope2D(r);
        try {
            Envelope2D e = grid.gridToWorld(genv);
            envelope.expandToInclude(e.getLowerCorner());
            envelope.expandToInclude(e.getUpperCorner());
        } catch (TransformException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return clip(coverage, envelope);
    }

    public static GridCoverage2D clip(GridCoverage2D coverage, ReferencedEnvelope envelope) {
        CoverageProcessor processor = CoverageProcessor.getInstance();

        // An example of manually creating the operation and parameters we want
        final ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(coverage);
        param.parameter("Envelope").setValue(envelope);

        return (GridCoverage2D) processor.doOperation(param);

    }
}
