package org.egc.gis.gdal.raster;

import org.egc.gis.gdal.IOFactory;
import org.egc.gis.gdal.crs.Projection;
import org.egc.gis.gdal.crs.ProjectionUtils;
import org.egc.gis.gdal.dto.Consts;
import org.egc.gis.gdal.dto.GDALDriversEnum;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

/**
 * Description:
 * <pre>
 * Raster files re-projection
 * </pre>
 *
 * @author houzhiwei
 * @date 2018 /11/13 9:48
 */
public class RasterProjection implements Projection {

    @Override
    public int reproject(String srcFile, String dstFile, SpatialReference tSRS) {
        Dataset ds = IOFactory.createRasterIO().read(srcFile);
        String dstWKT = tSRS.ExportToWkt();
        //create new raster
        Dataset dst = gdal.GetDriverByName(GDALDriversEnum.GTiff.name()).CreateCopy(dstFile, ds);
        dst.SetGeoTransform(ds.GetGeoTransform());
        //perform re-projection
        return gdal.ReprojectImage(ds, dst, ds.GetProjection(), dstWKT);
    }

    @Override
    public int reproject2Utm(String srcFile, String dstFile) {

        Dataset ds = IOFactory.createRasterIO().read(srcFile);
        SpatialReference sSRS = new SpatialReference(ds.GetProjectionRef());
        if (sSRS.IsProjected() > 0) {
            String projcs = sSRS.GetAttrValue(Consts.ATTR_PROJCS);
            int s_utmZone = sSRS.GetUTMZone();
        }else if (sSRS.IsGeographic() > 0) {
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

   /* int project(String srcFile, String dstFile, SpatialReference tSRS){
        Dataset ds = IOFactory.rasterIO().read(srcFile);
        String dstWKT = tSRS.ExportToWkt();
        // # error threshold --> use same value as in gdalwarp
        double error_threshold = 0.125;
        ds.GetGeoTransform();
        ds.GetProjection();
        int resampling = gdalconst.GRA_NearestNeighbour;
        // Call AutoCreateWarpedVRT() to fetch default values for target raster dimensions and geotransform
        // src_wkt : left to default value --> will use the one from source
        Dataset tmp_ds = gdal.AutoCreateWarpedVRT(ds, null, dstWKT, resampling, error_threshold);
        //perform re-projection
        //create new raster
        Dataset dst = gdal.GetDriverByName(GdalDriversEnum.GTiff.name()).CreateCopy(dstFile, ds);

    }*/
}
