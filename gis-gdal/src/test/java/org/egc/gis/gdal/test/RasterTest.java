package org.egc.gis.gdal.test;

import junit.framework.TestCase;
import org.egc.gis.gdal.IOFactory;
import org.egc.gis.gdal.dto.RasterMetadata;
import org.egc.gis.gdal.raster.RasterIO;
import org.egc.gis.gdal.raster.RasterInfo;
import org.egc.gis.gdal.raster.RasterUtils;
import org.egc.gis.gdal.raster.GDALThumbnail;
import org.gdal.gdal.Dataset;
import org.gdal.gdalconst.gdalconst;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author houzhiwei
 * @date 2021/3/19 15:30
 */
public class RasterTest extends TestCase {

    String demoDir = "D:/data/demos/";

    public String getFile() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource("raster/Himalaya.tif");
        return resource.getPath().substring(1);
    }

    @Test
    public void testFillNodata() {
        String tif = "F:/data/global_seims/spatial/meichuangjiang_aw3d30_2415-test.tif";
        RasterUtils.fillNodata(tif, 1, "F:/data/global_seims/spatial/meichuangjiang_aw3d30_2415-test2.tif");
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

    public void testThumbnail() {
        String data = "D:/data/demos/synthesisBands652.tif";
        GDALThumbnail gdalThumbnail =  new GDALThumbnail();
        //gdalThumbnail.truecolorThumbnail(data, demoDir + "1.png", 1, 2, 3, 30);
        //gdalThumbnail.rasterThumbnail(data, demoDir + "2.png",  30);
        double scale = gdalThumbnail.getScale(data, 512);
        System.out.println(scale);
        //gdalThumbnail.createThumbnail()
    }

    public void testFiles() throws IOException {

        List<Path> paths = IOFactory.listFiles("D:\\data\\landsat 8\\LC81240462019252LGN00",
                new String[]{"LC08_L1TP_124046_20190909_20190917_01_T1_B2.TIF",
                        "LC08_L1TP_124046_20190909_20190917_01_T1_B6.TIF",
                        "LC08_L1TP_124046_20190909_20190917_01_T1_B5.TIF"},
                IOFactory.FilenameFilterEnum.END_WITH);
        List<String> filesList = paths.stream().map(Path::toString).collect(Collectors.toList());
        /*for (Path path : paths) {
            System.out.println(path);
        }*/
        RasterUtils.synthesisBands(filesList,demoDir+"synthesisBands652.tif");
    }
}
