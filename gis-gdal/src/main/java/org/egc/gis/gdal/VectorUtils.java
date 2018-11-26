package org.egc.gis.gdal;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.gis.gdal.dto.VectorMetadata;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Driver;
import org.gdal.ogr.Layer;
import org.gdal.osr.SpatialReference;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Description:
 * <pre>
 * Vector Utilities
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/11/3 21:37
 */
@Slf4j
public class VectorUtils {

    /**
     * read shapefile and get metadata use OGR
     *
     * @param shapefile
     * @return Metadata
     */
    public static VectorMetadata getShapefileMetadata(String shapefile) {

        DataSource ds = IOFactory.vectorIO().read(shapefile);
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
        // 若没有投影则设置为 wgs 84
        if (sr == null) {
            log.warn("No projection found in {} ! Project to WGS84.", shapefile);
            // 没有参考说明没有prj文件，需要创建一个同名的prj文件
            sr = new SpatialReference();
            sr.ImportFromEPSG(4326);
            //sr.MorphToESRI(); // 这样的话拿不到epsg，但是如果是读取自ESRI的wkt，则必须包含此句，否则报错：不支持的SRS
            File prj = new File(FilenameUtils.removeExtension(shapefile) + ".prj");
            try {
                Files.write(prj.toPath(), sr.ExportToWkt().getBytes());
            } catch (IOException e) {
                log.warn("Create prj file failed.", e);
            }
        }
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
