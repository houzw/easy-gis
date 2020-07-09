package org.egc.gis.gdal.vector;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.gdal.IOFactory;
import org.egc.gis.gdal.crs.ProjectionUtils;
import org.egc.gis.gdal.dto.GDALDriversEnum;
import org.gdal.ogr.*;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

import java.io.File;

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
            FeatureDefn layerDefn = inLyr.GetLayerDefn();
            for (int j = 0; j < layerDefn.GetFieldCount(); j++) {
                outLyr.CreateField(layerDefn.GetFieldDefn(j));
            }
            // get the output inLyr's feature definition
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
                    String type = inFeature.GetFieldDefnRef(k).GetTypeName();
                    String name = outLayerDefn.GetFieldDefn(k).GetNameRef();
                    if ("Integer".equals(type)) {
                        outFeature.SetField(name, inFeature.GetFieldAsInteger(k));
                    } else if ("Real".equals(type)) {
                        outFeature.SetField(name, inFeature.GetFieldAsDouble(k));
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
}
