package org.egc.gis.data.topography.srtm;

import org.egc.commons.util.FileUtil;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * https://github.com/m4gr3d/3DRServices/tree/master/Core/src/org/droidplanner/core/srtm
 *
 * @author houzhiwei
 * @date 2020/5/4 21:45
 */
public class SrtmDownloader {
    static final String URL = "http://dds.cr.usgs.gov/srtm/version2_1/SRTM3/";
    //SRTM30是命名规则不一样
    static final String URL30 = "http://dds.cr.usgs.gov/srtm/version2_1/SRTM30/";

    public SrtmDownloader() {
    }

    public void downloadRegionIndex(int region, String srtmPath) throws IOException {
        String regionIndex = SrtmRegions.REGIONS[region] + ".index.html";
        regionIndex = getIndexPath(srtmPath) + regionIndex;
        File regionIndexFile = new File(regionIndex);
        downloadFile(URL + SrtmRegions.REGIONS[region] + "/", regionIndexFile);
    }

    //"http://dds.cr.usgs.gov/srtm/version2_1/SRTM3/Eurasia/
    public void downloadSrtmFile(String fname, String path) throws Exception {
        File output;
        String region = new SrtmRegions(path).findRegion(fname);
        output = new File(path + "/" + fname + ".zip");
        downloadSrtmFile(fname, output, region);
        FileUtil.unzip(path + "/" + fname + ".zip", path);
        //UnZip.unZipIt(fname, output);
        output.delete();
    }

    private void downloadSrtmFile(String fname, File output, String region) throws IOException {
        try {
            downloadFile(SrtmDownloader.URL + region + "/" + fname + ".zip", output);
        } catch (IOException e) {
            downloadAlternativeSrtmFile(fname, output, region, e);
        }
    }

    private void downloadAlternativeSrtmFile(String fname, File output, String region, IOException e)
            throws IOException {
        // fix SRTM 2.1 naming problem in North America
        if (fname.startsWith("N5") && "North_America".equalsIgnoreCase(region)) {
            downloadFile(SrtmDownloader.URL + region + "/" + fname.replace(".hgt", "hgt") + ".zip", output);
        } else {
            throw e;
        }
    }

    private void downloadFile(String urlAddress, File file) throws IOException {
        URL url = new URL(urlAddress);
        URLConnection connection = url.openConnection();
        connection.connect();
        // this will be useful so that you can show a typical 0-100% progress
        // bar
        long fileLength = connection.getContentLength();

        // download the file
        InputStream input = new BufferedInputStream(url.openStream());
        BufferedOutputStream outputs = new BufferedOutputStream(new FileOutputStream(file));

        byte data[] = new byte[2048];
        long total = 0;
        int count;
        while ((count = input.read(data)) != -1) {
            total += count;
            outputs.write(data, 0, count);
        }

        outputs.flush();
        outputs.close();
        input.close();
    }

    public static String getIndexPath(String srtmPath) {
        return srtmPath + "/Index/";
    }
}
