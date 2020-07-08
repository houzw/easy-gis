package org.egc.gis.gdal.raster;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.util.PathUtil;
import org.egc.commons.util.StringUtil;
import org.egc.gis.gdal.IOFactory;
import org.egc.gis.gdal.dto.BoundingBox;
import org.egc.gis.gdal.dto.Consts;
import org.egc.gis.gdal.dto.RasterMetadata;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.osr.SpatialReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author houzhiwei
 * @date 2020/5/2 17:36
 */
public class RasterInfo {
    /**
     * 利用gdal获取栅格数据元数据
     *
     * @param tif the tif
     * @return metadata
     */
    public static RasterMetadata getMetadata(String tif) {
        StringUtil.isNullOrEmptyPrecondition(tif, "Raster file must exists");
        RasterMetadata metadata = new RasterMetadata();
        Dataset dataset = IOFactory.createRasterIO().read(tif);
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
            String projcs = sr.GetAttrValue(Consts.ATTR_PROJCS);
            metadata.setCrs(projcs);
        } else if (sr.IsGeographic() > 0) {
            String geogcs = sr.GetAttrValue(Consts.ATTR_GEOGCS);
            metadata.setCrs(geogcs);
        }
        metadata.setUnit(sr.GetLinearUnitsName());
        Band band = dataset.GetRasterBand(1);
        Double[] nodataVal = new Double[1];
        band.GetNoDataValue(nodataVal);
        if (nodataVal[0] != null) {
            metadata.setNodata(nodataVal[0]);
        } else {
            metadata.setNodata(-9999d);
        }
        double[] min = new double[1], max = new double[1], mean = new double[1], stddev = new double[1];
        band.GetStatistics(true, true, min, max, mean, stddev);
        metadata.setMinValue(min[0]);
        metadata.setMaxValue(max[0]);
        metadata.setMeanValue(mean[0]);
        metadata.setStdev(stddev[0]);

        double[] gt = dataset.GetGeoTransform();
       /*
        adfGeoTransform[0] // top left x
        adfGeoTransform[1] // w-e(x) pixel resolution
        adfGeoTransform[2] // 0
        adfGeoTransform[3] // top left y
        adfGeoTransform[4] // 0
        adfGeoTransform[5] // n-s(y) pixel resolution (negative value)
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

    /**
     * get raster Statistics
     *
     * @param raster the raster
     * @return [min, max, mean, std_dev]
     */
    public static double[] getRasterStats(String raster) {
        Dataset dataset = IOFactory.createRasterIO().read(raster);
        Band band = dataset.GetRasterBand(1);
        double[] min = new double[1], max = new double[1], mean = new double[1], stddev = new double[1];
        band.GetStatistics(true, true, min, max, mean, stddev);
        return new double[]{min[0], max[0], mean[0], stddev[0]};
    }

    /**
     * Gets bounding box.
     *
     * @param ds the raster dataset
     * @return the bounding box
     */
    public static BoundingBox getBoundingBox(Dataset ds) {
        double[] gt = ds.GetGeoTransform();
        double upperLeftX = gt[0];
        double xres = gt[1];
        double xskew = gt[2];
        double upperLeftY = gt[3];
        double yskew = gt[4];
        double yres = gt[5];
        double lowerRightX = upperLeftX + (ds.GetRasterXSize() * xres);
        double lowerRightY = upperLeftY + (ds.GetRasterYSize() * yres);
        return new BoundingBox(upperLeftX, upperLeftY, lowerRightX, lowerRightY);
    }

    /**
     * get raster Authority<br/>
     * {@link #getMetadata(String)} are recommended
     *
     * @param raster the raster
     * @return authority map with keys: auth, srs_id, prj_name
     */
    public static Map<String, Object> getAuthority(String raster) {
        String utmZone = " / UTM Zone ";
        String srsId = null;
        String prjName = null;
        Map<String, Object> result = new HashMap<>(3);
        try {
            String path = FilenameUtils.normalize(PathUtil.resourcesFilePath("wkt_lookup.uesv"));
            List<String> listOfWkt = FileUtils.readLines(new File(path), "UTF-8");
            Dataset ds = IOFactory.createRasterIO().read(raster);
            String wktInfo = ds.GetProjection();
            SpatialReference prjInfo = new SpatialReference(wktInfo);
            prjName = prjInfo.GetAttrValue(Consts.ATTR_PROJCS, 0);
            String speroid = prjInfo.GetAttrValue(Consts.ATTR_SPHEROID, 0);
            if (prjName.startsWith(Consts.UTM_ZONE)) {
                if (prjName.split(",")[1].startsWith(" N")) {
                    prjName = speroid + utmZone + prjName.split(" ")[2].replace(",", "") + "N";
                }
                if (prjName.split(",")[1].startsWith(" S")) {
                    prjName = speroid + utmZone + prjName.split(" ")[2].replace(",", "") + "S";
                }
            }
            for (String wkt : listOfWkt) {
                if (StringUtils.isNotBlank(prjName)) {
                    break;
                }
                if (wkt.startsWith("epsg_code")) {
                    continue;
                }
                if (wkt.split(";")[1].replace(" ", "").toLowerCase().replace("\n", "")
                        == prjName.replace(" ", "").toLowerCase()) {
                    srsId = wkt.split(";")[0].replace(" ", "").toLowerCase().replace("\n", "");
                    result.put("auth", wkt.split(";")[6]);
                    result.put("srs_id", srsId);
                    result.put("prj_name", prjName);
                    return result;
                }
            }
            result.put("auth", prjInfo.GetAttrValue(Consts.ATTR_AUTHORITY, 1));
            result.put("srs_id", srsId);
            result.put("prj_name", prjName);
            return result;
        } catch (IOException e) {
            result.put("auth", "4326");
            result.put("srs_id", "3452");
            result.put("prj_name", prjName);
            e.printStackTrace();
            return result;
        }
    }

    /**
     * Gets proj 4.
     *
     * @param rasterOrShape the raster or shape
     * @return map with keys: proj4 (string), isProjected (boolean)
     */
    public static Map<String, Object> getProj4(String rasterOrShape) {
        Dataset dt = gdal.Open(rasterOrShape, gdalconst.GA_ReadOnly);
        String projection = dt.GetProjection();
        SpatialReference spatialReference = new SpatialReference();
        spatialReference.ImportFromWkt(projection);
        String proj4 = spatialReference.ExportToProj4();
        boolean projected = spatialReference.IsGeographic() != 0;

        Map<String, Object> r = new HashMap<>(2);
        r.put("proj4", proj4);
        r.put("isProjected", projected);
        return r;
    }

}
