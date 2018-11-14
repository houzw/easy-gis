package org.egc.gis.geotools;

import lombok.extern.slf4j.Slf4j;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.gce.geotiff.GeoTiffWriteParams;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;

import java.io.File;
import java.io.IOException;

/**
 * Description:
 * <pre>
 * 读写geotiff
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/11/13 18:59
 */
@Slf4j
public class GeoTiffUtils {
    /**
     * https://www.programcreek.com/java-api-examples/?class=org.geotools.gce.geotiff.GeoTiffWriter&method=write
     *
     * @param coverage2D
     * @param dstFile
     * @return
     */
    public boolean write(GridCoverage2D coverage2D, String dstFile) {
        try {
            final GeoTiffWriteParams wp = new GeoTiffWriteParams();
            wp.setCompressionMode(GeoTiffWriteParams.MODE_DEFAULT);

//            wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
//            wp.setCompressionType("LZW");

            wp.setTilingMode(GeoToolsWriteParams.MODE_DEFAULT);
            final GeoTiffFormat format = new GeoTiffFormat();
            final ParameterValueGroup paramWrite = format.getWriteParameters();

            paramWrite.parameter(AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString()).setValue(wp);
            GeoTiffWriter gtw = (GeoTiffWriter) format.getWriter(new File(dstFile));
            gtw.write(coverage2D, (GeneralParameterValue[]) paramWrite.values().toArray(new GeneralParameterValue[1]));
            return true;
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            return false;
        }
    }

    public GridCoverage2D read(String file) {
        Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        GeoTiffReader reader = null;
        GridCoverage2D coverage = null;
        try {
            reader = new GeoTiffReader(file, hints);
            coverage = (GridCoverage2D) reader.read(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Read GeoTiff failed", e);
        }
        return coverage;
    }
}
