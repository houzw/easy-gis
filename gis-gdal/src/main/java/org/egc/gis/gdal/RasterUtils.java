package org.egc.gis.gdal;

import org.egc.commons.util.StringUtil;
import org.egc.gis.gdal.dto.RasterMetadata;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.osr.SpatialReference;

/**
 * Description:
 * <pre>
 * Raster Utilities
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/11/13 9:08
 */
public class RasterUtils {
    /**
     * 利用gdal获取栅格数据元数据
     *
     * @param tif
     * @return
     */
    public static RasterMetadata getMetadata(String tif) {
        StringUtil.isNullOrEmptyPrecondition(tif, "Raster file must exists");
        gdal.AllRegister();
        RasterMetadata metadata = new RasterMetadata();
        Dataset dataset = gdal.Open(tif, gdalconstConstants.GA_ReadOnly);
        Driver driver = dataset.GetDriver();
        metadata.setFormat(driver.getShortName());
        SpatialReference sr = new SpatialReference(dataset.GetProjectionRef());
        metadata.setCrsProj4(sr.ExportToProj4());
        metadata.setCrsWkt(sr.ExportToWkt());
        String authorityCode = sr.GetAuthorityCode(null);

        if (authorityCode != null) {
            Integer srid = Integer.parseInt(authorityCode);
            metadata.setSrid(srid);
        }

        if (sr.IsProjected() > 0) {
            String projcs = sr.GetAttrValue("PROJCS");
            metadata.setCrs(projcs);
        } else if (sr.IsGeographic() > 0) {
            String geogcs = sr.GetAttrValue("GEOGCS");
            metadata.setCrs(geogcs);
        }

        metadata.setUnit(sr.GetLinearUnitsName());

        Band band = dataset.GetRasterBand(1);
        Double[] nodataval = new Double[1];
        band.GetNoDataValue(nodataval);
        if (nodataval[0] != null) {
            metadata.setNodata(nodataval[0]);
        } else {
            metadata.setNodata(-9999d);
        }
        double[] min = new double[1], max = new double[1], stddev = new double[1], mean = new double[1];
        band.GetStatistics(true, true, min, max, mean, stddev);
        metadata.setMinValue(min[0]);
        metadata.setMaxValue(max[0]);
        metadata.setMeanValue(mean[0]);
        metadata.setSdev(stddev[0]);

        double[] gt = dataset.GetGeoTransform();
       /*
        adfGeoTransform[0] // top left x
        adfGeoTransform[1] // w-e pixel resolution
        adfGeoTransform[2] // 0
        adfGeoTransform[3] // top left y
        adfGeoTransform[4] // 0
        adfGeoTransform[5] // n-s pixel resolution (negative value)
        */
        metadata.setMinX(gt[0]);
        metadata.setMaxX(gt[0] + gt[1] * dataset.GetRasterXSize());
        metadata.setCenterX(gt[0] + gt[1] * dataset.GetRasterXSize() / 2);
        metadata.setMaxY(gt[3]);
        metadata.setMinY(gt[3] + gt[5] * dataset.GetRasterYSize());
        metadata.setCenterY(gt[3] + gt[5] * dataset.GetRasterYSize() / 2);
        //gt[5]
        metadata.setPixelSize(gt[1]);
        metadata.setWidth(dataset.GetRasterXSize() * gt[1]);
        metadata.setHeight(dataset.GetRasterYSize() * gt[5]);
        metadata.setSizeHeight(dataset.GetRasterYSize());
        metadata.setSizeWidth(dataset.GetRasterXSize());

        dataset.delete();
        gdal.GDALDestroyDriverManager();
        return metadata;
    }
}
