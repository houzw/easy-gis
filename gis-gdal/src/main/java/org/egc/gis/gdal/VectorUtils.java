package org.egc.gis.gdal;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.gis.gdal.dto.VectorMetadata;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Driver;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;
import org.gdal.osr.SpatialReference;

/**
 * Description:
 * <pre>
 * Vector Utilities
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/11/3 21:37
 */
public class VectorUtils {

    /**
     * read shapefile and get metadata use OGR
     *
     * @param shapefile
     * @return Metadata
     */
    public static VectorMetadata getShapefileMetadata(String shapefile) {

        ogr.RegisterAll();
        DataSource ds = ogr.Open(shapefile, false);
        Driver driver = ds.GetDriver();

        VectorMetadata metadata = new VectorMetadata();
        metadata.setName(FilenameUtils.getName(shapefile));
        metadata.setFormat(driver.GetName());
        metadata.setLayerCount(ds.GetLayerCount());
        Layer layer = ds.GetLayer(0);
        metadata.setGeomType(layer.GetGeomType());
        metadata.setFeatureCount(layer.GetFeatureCount());
        metadata.setGeometry(layer.GetNextFeature().GetGeometryRef().GetGeometryName());

        SpatialReference sr = layer.GetSpatialRef();
        String authorityCode = sr.GetAuthorityCode(null);
        String authorityName = sr.GetAuthorityName(null);
        if (StringUtils.isNotBlank(authorityCode)) {
            metadata.setSrid(Integer.parseInt(authorityCode));
        }
        if (sr.IsProjected() == 1) {
            String projcs = sr.GetAttrValue("PROJCS");
            metadata.setCrs(projcs);
        } else if (sr.IsGeographic() == 1) {
            String geogcs = sr.GetAttrValue("GEOGCS");
            metadata.setCrs(geogcs);
        }
        metadata.setUnit(sr.GetLinearUnitsName());
        metadata.setCrsProj4(sr.ExportToProj4());
        metadata.setCrsWkt(sr.ExportToWkt());

        double[] extent = layer.GetExtent();
        metadata.setMinX(extent[0]);
        metadata.setMaxX(extent[1]);
        metadata.setMinY(extent[2]);
        metadata.setMaxY(extent[3]);

        ds.delete();
        driver.delete();
        return metadata;
    }
}
