package org.egc.gis.gdal.test;

import junit.framework.TestCase;
import org.egc.gis.gdal.IOFactory;
import org.egc.gis.gdal.dto.RasterMetadata;
import org.egc.gis.gdal.raster.RasterIO;
import org.egc.gis.gdal.raster.RasterInfo;
import org.egc.gis.gdal.raster.RasterUtils;
import org.gdal.gdal.Dataset;
import org.gdal.gdalconst.gdalconst;
import org.junit.Test;

import java.net.URL;

/**
 * @author houzhiwei
 * @date 2021/3/19 15:30
 */
public class RasterTest extends TestCase {
    public String getFile() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource("raster/Himalaya.tif");
        return resource.getPath().substring(1);
    }

     @Test
    public void testFillNodata() {
        String tif = "F:/data/global_seims/spatial/meichuangjiang_aw3d30_2415-test.tif";
         RasterUtils.fillNodata(tif,1,"F:/data/global_seims/spatial/meichuangjiang_aw3d30_2415-test2.tif");
//         System.out.println(RasterInfo.getMetadata(tif));
    }

    public void testWrite() {
        RasterIO rasterIO = IOFactory.createRasterIO();
        Dataset src = rasterIO.read(getFile());
        float[] band = RasterInfo.readRasterBand(src, 1);
        double[] transform = src.GetGeoTransform();
        RasterMetadata metadata = RasterInfo.getMetadata(src);
        rasterIO.writeGeotiffFile("J:/demos/319/Himalaya_write.tif",
                metadata.getSizeWidth(), metadata.getSizeHeight(),
                band,
                transform,
                metadata.getSrs(), -9999d, gdalconst.GDT_Float32);
    }
}
