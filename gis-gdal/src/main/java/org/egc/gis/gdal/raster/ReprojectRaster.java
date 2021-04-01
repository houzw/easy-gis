package org.egc.gis.gdal.raster;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.egc.gis.gdal.IOFactory;
import org.egc.gis.gdal.crs.ProjectionUtils;
import org.egc.gis.gdal.dto.Consts;
import org.egc.gis.gdal.dto.GDALDriversEnum;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.WarpOptions;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

import java.util.Vector;

/**
 * TODO Test
 *
 * @author houzhiwei
 * @date 2020/7/9 16:36
 */
@Slf4j
public class ReprojectRaster {
    /**
     * Reproject raster.
     * <pre>
     * https://gdal.org/programs/gdalwarp.html#gdalwarp
     * https://jgomezdans.github.io/gdal_notes/reprojection.html
     * 1. Set up the two Spatial Reference systems.
     * 2. Open the original dataset, and get the geotransform
     * 3. Calculate bounds of new geotransform by projecting the UL corners
     * 4. Calculate the number of pixels with the new projection & spacing
     * 5. Create an in-memory raster dataset
     * 6. Perform the projection
     * </pre>
     *
     * @param srcFile the src file
     * @param dstFile the dst file
     * @param dstSRS  the dst srs
     */
    public static void reproject(String srcFile, String dstFile, SpatialReference dstSRS) {
        Dataset ds = IOFactory.createRasterIO().read(srcFile);
        SpatialReference srcSRS = new SpatialReference();
        srcSRS.ImportFromWkt(ds.GetProjection());
        CoordinateTransformation transformation = new CoordinateTransformation(srcSRS, dstSRS);

        // Get the Geotransform vector
        double[] gt = ds.GetGeoTransform();
        double xSize = ds.GetRasterXSize();
        double ySize = ds.GetRasterYSize();
        //Work out the boundaries of the new dataset in the target projection
        //ulx, uly, ulz: upper left
        double[] ulPoint = transformation.TransformPoint(gt[0], gt[3]);
        //lrx, lry, lrz: lower right
        double[] lrPoint = transformation.TransformPoint(gt[0] + gt[1] * xSize, gt[3] + gt[5] * ySize);
        // Now, we create an in-memory raster
        org.gdal.gdal.Driver memDrv = gdal.GetDriverByName("MEM");
        // The size of the raster is given the new projection and pixel spacing
        // Using the values we calculated above. Also, setting it to store one band
        // and to use Float32 data type.
        int pixelSpacing = 5000;
        Dataset dstDs = memDrv.Create("", (int) ((lrPoint[0] - ulPoint[0] / pixelSpacing)),
                (int) ((lrPoint[1] - ulPoint[1] / pixelSpacing)), 1, gdalconst.GDT_Float32);
        //dest = memDrv.Create("", int((lrx - ulx) / pixelSpacing), int ((uly - lry) / pixelSpacing), 1, gdalconst.GDT_Float32);
        // Calculate the new geotransform
        //new_geo = (ulx, pixelSpacing, geo_t[2], uly, geo_t[4], -pixelSpacing )
        // Set the geotransform
        dstDs.SetGeoTransform(new double[]{ulPoint[0], pixelSpacing, gt[2], ulPoint[1], gt[4], -pixelSpacing});
        dstDs.SetProjection(srcSRS.ExportToWkt());
        // Perform the projection/resampling
        gdal.ReprojectImage(ds, dstDs, dstSRS.ExportToWkt(), srcSRS.ExportToWkt(), gdalconst.GRA_Bilinear);
    }

    /**
     * Reproject.
     * use gdal.Warp
     *
     * @param srcFile the src file
     * @param dstFile the dst file
     * @param dstEpsg the dst epsg
     */
    public static void reprojectWithWarp(String srcFile, String dstFile, int dstEpsg) {
        Dataset ds = IOFactory.createRasterIO().read(srcFile);
        Vector<String> v = new Vector<>();
        v.add("-t_srs");
        v.add("EPSG:" + dstEpsg);
        v.add("-overwrite");
        WarpOptions options = new WarpOptions(v);
        Dataset[] datasets = new Dataset[]{ds};
        gdal.Warp(dstFile, datasets, options);
        //关闭数据集
        ds.delete();
    }

    /**
     * Reproject image int.
     * use gdal.ReprojectImage
     *
     * @param srcFile the src file
     * @param dstFile the dst file
     * @param tSRS    the t srs
     * @return the int
     */
    public static int reprojectImage(String srcFile, String dstFile, SpatialReference tSRS) {
        Dataset ds = IOFactory.createRasterIO().read(srcFile);
        //create new raster
        Dataset dst = gdal.GetDriverByName(GDALDriversEnum.GTiff.name()).CreateCopy(dstFile, ds);
        dst.SetGeoTransform(ds.GetGeoTransform());
        //perform re-projection
        return gdal.ReprojectImage(ds, dst, ds.GetProjection(), tSRS.ExportToWkt());
    }

    /**
     * Reproject to utm.
     *
     * @param srcFile the src file
     * @param dstFile the dst file
     * @return the int
     */
    public static int reproject2Utm(String srcFile, String dstFile) {
        Dataset ds = IOFactory.createRasterIO().read(srcFile);
        SpatialReference sSRS = new SpatialReference(ds.GetProjectionRef());
        if (sSRS.IsProjected() > 0) {
            String projcs = sSRS.GetAttrValue(Consts.ATTR_PROJCS);
            int sUtmZone = sSRS.GetUTMZone();
        } else if (sSRS.IsGeographic() > 0) {
            String geogcs = sSRS.GetAttrValue(Consts.ATTR_GEOGCS);
        }

        SpatialReference tSRS = new SpatialReference();
        tSRS.SetWellKnownGeogCS("WGS84");

        CoordinateTransformation transformation = CoordinateTransformation.CreateCoordinateTransformation(sSRS, tSRS);

        double[] gt = ds.GetGeoTransform();
        int utmZone = ProjectionUtils.utmZone(gt[0] + gt[1] * ds.GetRasterXSize() / 2);
        int north = ProjectionUtils.isNorthern(gt[3] + gt[5] * ds.GetRasterYSize() / 2);

        String dstWKT = tSRS.ExportToWkt();
        //create new raster
        Dataset dst = gdal.GetDriverByName(GDALDriversEnum.GTiff.name()).CreateCopy(dstFile, ds);
        dst.SetGeoTransform(gt);
        //perform re-projection
        return gdal.ReprojectImage(ds, dst, ds.GetProjection(), dstWKT);
    }

    /**
     * Reproject with vrt.
     * use gdal.AutoCreateWarpedVRT
     *
     * @param srcFile the src file
     * @param dstFile the dst file
     * @param tSRS    the t srs
     */
    public static void reprojectUseVrt(String srcFile, String dstFile, SpatialReference tSRS) {
        Dataset ds = IOFactory.createRasterIO().read(srcFile);
        // # error threshold --> use same value as in gdalwarp
        double errorThreshold = 0.125;
        int resampling = gdalconst.GRA_Bilinear;
        // Call AutoCreateWarpedVRT() to fetch default values for target raster dimensions and geotransform
        // src_wkt : left to default value --> will use the one from source
        Dataset tmpDs = gdal.AutoCreateWarpedVRT(ds, null, tSRS.ExportToWkt(), resampling, errorThreshold);
        //perform re-projection
        //create new raster
        gdal.GetDriverByName("GTiff").CreateCopy(FilenameUtils.removeExtension(dstFile) + ".tif", tmpDs);
    }

    public static void reproject(String srcFile, String dstFile, int dstEpsg) {
        SpatialReference dstSRS = ProjectionUtils.createSpatialReference(dstEpsg);
        reproject(srcFile, dstFile, dstSRS);
    }
}
