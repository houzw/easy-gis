package org.egc.gis.gdal.vector;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.egc.gis.gdal.FormatConversion;
import org.egc.gis.gdal.dto.Consts;
import org.egc.gis.gdal.dto.GDALDriversEnum;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.VectorTranslateOptions;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.ogr;

import java.io.IOException;
import java.util.Vector;

/**
 * Description:
 * <pre>
 * vector data input output
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/11/13 10:18
 */
@Slf4j
public class VectorIO implements FormatConversion {

    /**
     * To geo json string.
     *
     * @param srcFile the src file
     * @return the geojson string
     */
    public String toGeoJSON(String srcFile) {
        return "";
    }

    /**
     * To geojson file.
     *
     * @param srcFile the src file
     * @param dstFile the dst file
     * @return the GeoJSON file
     */
    public String toGeoJSONFile(String srcFile, String dstFile) throws IOException {
        return formatConvert(srcFile, dstFile, GDALDriversEnum.GeoJSON);
    }

    /**
     * To kml file.
     *
     * @param srcFile the src file
     * @return the KML file
     */
    public String toKMLFile(String srcFile, String dstFile) throws IOException {
        return formatConvert(srcFile, dstFile, GDALDriversEnum.KML);
    }

    /**
     * To kml string.
     *
     * @param srcFile the src file
     * @return the KML String
     */
    public String toKML(String srcFile) throws IOException {
        return "";
    }

    /**
     * vector format conversion
     *
     * @param srcFile
     * @param dstFile
     * @throws IOException
     */
    @Override
    public String formatConvert(String srcFile, String dstFile) throws IOException {
        String ext = FilenameUtils.getExtension(dstFile);
        log.info("Convert from {} to {}", srcFile, dstFile);
        GDALDriversEnum driversEnum = GDALDriversEnum.lookupByExtension(ext);
        return formatConvert(srcFile, dstFile, driversEnum);
    }

    /**
     * vector format conversion
     *
     * @param srcFile
     * @param format
     * @throws IOException
     */
    @Override
    public String formatConvert(String srcFile, String dstFile, GDALDriversEnum format) throws IOException {
        gdal.AllRegister();
        Dataset ds = gdal.Open(srcFile);//?
        log.info("Convert from {} to {} format", srcFile, format.name());
        Driver driver = gdal.GetDriverByName(format.name());
        if (driver == null) {
            log.error("Output Format {} Not Supported", format.name());
            throw new IOException("Output Format " + format.name() + " Not Supported");
        }
        Vector<String> vector = new Vector<String>();
        vector.add("-f");
        vector.add(driver.getShortName());
        String ext = format.getExtension();
        String newName = outputName(srcFile, dstFile, ext);
        Dataset dataset = gdal.VectorTranslate(newName, ds, new VectorTranslateOptions(vector));
        ds.delete();
        gdal.GDALDestroyDriverManager();
        return dataset != null ? newName : null;
    }

    public DataSource read(String vector) {
        ogr.RegisterAll();
        gdal.SetConfigOption(Consts.GDAL_FILENAME_IS_UTF8, Consts.YES);
        gdal.SetConfigOption(Consts.SHAPE_ENCODING, Consts.CP936);
        return ogr.Open(vector);
    }

    public DataSource read4Update(String vector) {
        ogr.RegisterAll();
        gdal.SetConfigOption(Consts.GDAL_FILENAME_IS_UTF8, Consts.YES);
        gdal.SetConfigOption(Consts.SHAPE_ENCODING, Consts.CP936);
        return ogr.Open(vector, gdalconst.GA_Update);
    }

    public boolean write(DataSource data, String dstFile) {
        return false;
    }
}
