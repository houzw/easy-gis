package org.egc.gis.gdal.vector;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.gdal.IOFactory;
import org.egc.gis.gdal.crs.ProjectionUtils;
import org.egc.gis.gdal.dto.GDALDriversEnum;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.VectorTranslateOptions;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.ogr.*;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

import java.io.File;
import java.util.Arrays;
import java.util.Vector;

/**
 * @author houzhiwei
 * @date 2020/7/9 16:36
 */
@Slf4j
public class ReprojectVector {

    /**
     * <pre>
     * Re-project data.
     * https://pcjericks.github.io/py-gdalogr-cookbook/projection.html
     * </pre>
     *
     * @param srcFile the src file
     * @param dstFile the dst file
     * @param dstSRS  the target srs
     * @return the int
     */
    public static void reproject(String srcFile, String dstFile, SpatialReference dstSRS) {
        DataSource ds = IOFactory.createVectorIO().read(srcFile);
        SpatialReference srcSRS = ds.GetLayer(0).GetSpatialRef();
        CoordinateTransformation transformation = new CoordinateTransformation(srcSRS, dstSRS);
        Driver driver = ogr.GetDriverByName(GDALDriversEnum.ESRI_Shapefile.getName());
        File dst = new File(dstFile);
        if (dst.exists()) {
            boolean delete = dst.delete();
            log.debug("Old file {} deleted: {}", dstFile, delete);
        }
        DataSource dstSrc = driver.CreateDataSource(dstFile);
        for (int i = 0; i < ds.GetLayerCount(); i++) {
            Layer inLyr = ds.GetLayer(i);
            Layer outLyr = dstSrc.CreateLayer(inLyr.GetName(), dstSRS, inLyr.GetGeomType());
            //add fields
            FeatureDefn inLayerDefn = inLyr.GetLayerDefn();
            for (int j = 0; j < inLayerDefn.GetFieldCount(); j++) {
                outLyr.CreateField(inLayerDefn.GetFieldDefn(j));
            }
            // get the output layer's feature definition
            FeatureDefn outLayerDefn = outLyr.GetLayerDefn();
            Feature inFeature = inLyr.GetNextFeature();
            while (inFeature != null) {
                //get the input geometry
                Geometry geom = inFeature.GetGeometryRef();
                //reproject the geometry
                geom.Transform(transformation);
                //create a new feature
                Feature outFeature = new Feature(outLayerDefn);
                //set the geometry and attribute
                outFeature.SetGeometry(geom);
                for (int k = 0; k < outLayerDefn.GetFieldCount(); k++) {
                    String name = outLayerDefn.GetFieldDefn(k).GetNameRef();
                    String type = outLayerDefn.GetFieldDefn(k).GetTypeName();
                    //                        token.equalsIgnoreCase("Date") ||
                    //                        token.equalsIgnoreCase("Time") ||
                    //                        token.equalsIgnoreCase("DateTime") ||
                    if ("Integer".equalsIgnoreCase(type)) {
                        outFeature.SetField(name, inFeature.GetFieldAsInteger(k));
                    } else if ("IntegerList".equalsIgnoreCase(type)) {
                        outFeature.SetFieldIntegerList(k, inFeature.GetFieldAsIntegerList(k));
                    } else if ("Real".equalsIgnoreCase(type)) {
                        outFeature.SetField(name, inFeature.GetFieldAsDouble(k));
                    } else if ("RealList".equalsIgnoreCase(type)) {
                        outFeature.SetFieldDoubleList(k, inFeature.GetFieldAsDoubleList(k));
                    } else if ("StringList".equalsIgnoreCase(type)) {
                        outFeature.SetFieldStringList(k, new Vector<>(Arrays.asList(inFeature.GetFieldAsStringList(k))));
                    } else if ("Binary".equalsIgnoreCase(type)) {
                        outFeature.SetFieldBinaryFromHexString(name, toHexString(inFeature.GetFieldAsBinary(k)));
                    } else {
                        outFeature.SetField(name, inFeature.GetFieldAsString(k));
                    }
                }
                //add the feature to the shapefile
                outLyr.CreateFeature(outFeature);
                // destroy the features and get the next input feature
                outFeature.delete();
                inFeature.delete();
                inFeature = inLyr.GetNextFeature();
            }
        }
        ds.delete();
        driver.delete();
    }

    public static String toHexString(byte[] byteArray) {
        StringBuilder hexString = new StringBuilder("");
        if (byteArray == null || byteArray.length <= 0) {
            return null;
        }
        for (byte b : byteArray) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                hexString.append(0);
            }
            hexString.append(hv);
        }
        return hexString.toString().toLowerCase();
    }

    /**
     * Reproject.
     *
     * @param srcFile the src file
     * @param dstFile the dst file
     * @param dstEpsg the dst epsg
     */
    public static void reproject(String srcFile, String dstFile, int dstEpsg) {
        SpatialReference dstSRS = ProjectionUtils.createSpatialReference(dstEpsg);
        reproject(srcFile, dstFile, dstSRS);
    }

    //https://gdal.org/programs/ogr2ogr.html
    //https://trac.osgeo.org/gdal/browser/trunk/gdal/swig/java/apps/ogr2ogr_new.java

    public static void reprojectUseOgr(String srcFile, String dstFile, int dstEpsg) {
        SpatialReference dstSRS = ProjectionUtils.createSpatialReference(dstEpsg);
        reprojectUseOgr(srcFile, dstFile, dstSRS);
    }

    /**
     * 矢量数据投影转换。适用于没有 epsg 编码，但有 wkt/proj4 的投影
     *
     * @param srcFile
     * @param dstFile 输出为 shapefile
     * @param dstSRS  目标投影
     */
    public static void reprojectUseOgr(String srcFile, String dstFile, SpatialReference dstSRS) {
        if (gdal.GetDriverCount() == 0) {
            gdal.AllRegister();
        }
        Dataset ds = gdal.OpenEx(srcFile, gdalconst.OF_VECTOR | gdalconst.OF_VERBOSE_ERROR);
        Vector<String> options = new Vector<>();
        options.add("-f");
        options.add("ESRI Shapefile");
        options.add("-t_srs");
        options.add(dstSRS.ExportToProj4());
        options.add("-overwrite");
        gdal.VectorTranslate(dstFile, ds, new VectorTranslateOptions(options));
        ds.delete();
        gdal.GDALDestroyDriverManager();
    }

}
