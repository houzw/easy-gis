package org.egc.gis.gdal.raster;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.gdal.Clip;
import org.egc.gis.gdal.IOFactory;
import org.egc.gis.gdal.dto.BoundingBox;
import org.egc.gis.gdal.dto.Consts;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.WarpOptions;
import org.gdal.gdal.gdal;

import java.util.Vector;

/**
 * https://gdal.org/programs/gdalwarp.html#gdalwarp
 * https://joeyklee.github.io/broc-cli-geo/guide/XX_raster_cropping_and_clipping.html
 *
 * @author houzhiwei
 */
@Slf4j
public class RasterClip implements Clip {

    public boolean clipByRaster(String src, String ref){

        return false;
    }

    @Override
    public boolean clipByBoundingBox(String src, String dst, BoundingBox boundingBox, double expand) {
        Dataset ds = IOFactory.createRasterIO().read(src);
        String boundingStr = expandBoundingBox(boundingBox, expand).toMinMaxString();
        log.info("clip {} by bounding box {}", src, boundingStr);
        Vector<String> optionsVector = new Vector<>();
        optionsVector.add("-te");
        optionsVector.add(boundingStr);
        WarpOptions options = new WarpOptions(optionsVector);
        Dataset dataset = gdal.Warp(dst, new Dataset[]{ds}, options);
        ds.delete();
        gdal.GDALDestroyDriverManager();
        return dataset != null;
    }

    @Override
    public boolean clipByBoundingBox(String src, String dst, BoundingBox boundingBox) {
        return this.clipByBoundingBox(src, dst, boundingBox, 0);
    }

    @Override
    public boolean clipByVectorBoundingBox(String src, String dst, String shp) {
        Dataset ds = IOFactory.createRasterIO().read(src);
        log.info("clip {} using bounding box of {}", src, shp);
        Vector<String> optionsVector = new Vector<>();
        //Enable use of a blend cutline from the name OGR support datasource.
        optionsVector.add("-cutline");
        optionsVector.add(shp);
        // Crop the extent of the target dataset to the extent of the cutline.
        optionsVector.add("-crop_to_cutline");
        // Use multithreaded warping implementation.
        optionsVector.add("-multi");
        WarpOptions options = new WarpOptions(optionsVector);
        Dataset dataset = gdal.Warp(dst, new Dataset[]{ds}, options);
        ds.delete();
        gdal.GDALDestroyDriverManager();
        return dataset != null;
    }

    @Override
    public boolean clipByPolygon(String src, String dst, String polygonShp) {
        Dataset ds = IOFactory.createRasterIO().read(src);
        log.info("clip {} using boundary of {}", src, polygonShp);
        Vector<String> optionsVector = new Vector<>();
        optionsVector.add("-dstnodata");
        optionsVector.add(Consts.NODATA);
        optionsVector.add("-cutline");
        optionsVector.add(polygonShp);
        WarpOptions options = new WarpOptions(optionsVector);
        Dataset dataset = gdal.Warp(dst, new Dataset[]{ds}, options);
        ds.delete();
        gdal.GDALDestroyDriverManager();
        return dataset != null;
    }
}
