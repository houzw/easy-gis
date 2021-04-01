package org.egc.gis.data.topography;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import org.apache.http.client.utils.URIBuilder;
import org.egc.ows.commons.HttpUtils;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

/**
 * download data from <a link="https://www.opentopography.org/developers">OpenTopography</a>
 *
 * @author houzhiwei
 * @date 2020/5/5 15:06
 */
@Slf4j
public class OpenTopography {
    //https://portal.opentopography.org/otr/

    private static final String BASE_URL = "https://portal.opentopography.org/";
    private static final String DEM_TYPE = "demtype";
    private static final String WEST = "west";
    private static final String SOUTH = "south";
    private static final String EAST = "east";
    private static final String NORTH = "north";
    private static final String OUTPUT_FORMAT = "outputFormat";

    /* Required Parameters
     ******************************************************************************************************************************************************************************************************************************************
     * demtype 	                |   The global raster dataset - SRTM GL3 (90m) is 'SRTMGL3',
     *                              SRTM GL1 (30m) is 'SRTMGL1', SRTM GL1 (Ellipsoidal) is 'SRTMGL1_E',
     *                              and ALOS World 3D 30m is 'AW3D30',
     *                              ALOS World 3D (Ellipsoidal) is 'AW3D30_E' *
     * west, south, east, north |	WGS 84 bounding box coordinates                                                                                                                                                                           *
     * outputFormat 	        |   outputFormat Output Format (optional) - GTiff for GeoTiff, AAIGrid for Arc ASCII Grid,
     *                              HFA for Erdas Imagine (.IMG). Defaults to GTiff if parameter is not provided                                       *
     ******************************************************************************************************************************************************************************************************************************************/

    /**
     * Build rest url uri.
     *
     * @param demType the dem type
     * @param west    west coordinate, e.g., -120.168457
     * @param south   south coordinate, e.g., 36.738884
     * @param east    east coordinate, e.g., -118.465576
     * @param north   north coordinate, e.g., 38.091337
     * @param format  the format
     * @return url, e.g.,  http://opentopo.sdsc.edu/otr/getdem?demtype=SRTMGL3&west=-120.168457&south=36.738884&east=-118.465576&north=38.091337&outputFormat=GTiff
     */
    private URI buildRestUrl(DemTypesEnum demType, double west, double south, double east, double north, FormatEnum format) {
        URIBuilder uriBuilder = null;
        try {
            uriBuilder = new URIBuilder(BASE_URL);
            uriBuilder.setPathSegments("API", "globaldem");
            uriBuilder.addParameter(DEM_TYPE, demType.name());
            uriBuilder.addParameter(WEST, String.valueOf(west));
            uriBuilder.addParameter(SOUTH, String.valueOf(south));
            uriBuilder.addParameter(EAST, String.valueOf(east));
            uriBuilder.addParameter(NORTH, String.valueOf(north));
            if (format != null) {
                uriBuilder.addParameter(OUTPUT_FORMAT, format.name());
            }
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            log.error("URI syntax error", e);
            return null;
        }
    }

    private HttpUrl buildRequestUrl(DemTypesEnum demType, double west, double south, double east, double north, FormatEnum format) {
        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("https")
                .host("portal.opentopography.org")
                .addPathSegment("API")
                .addPathSegment("globaldem")
                .addQueryParameter(DEM_TYPE, demType.name())
                .addQueryParameter(WEST, String.valueOf(west))
                .addQueryParameter(SOUTH, String.valueOf(south))
                .addQueryParameter(EAST, String.valueOf(east))
                .addQueryParameter(NORTH, String.valueOf(north));
        if (format != null) {
            builder.addQueryParameter(OUTPUT_FORMAT, format.name());
        }
        return builder.build();
    }

    /**
     * Download.
     *
     * @param uri      the uri
     * @param filename PathUtil.getProjectRoot() + name
     */
    public void download(URI uri, String filename) {
        long start = System.currentTimeMillis();
        HttpUtils.doGetFile(uri, filename, false);
        long time = System.currentTimeMillis() - start;
        log.debug("Time used: {}", time);
    }

    /**
     * @param demType        the dem type
     * @param west           west (minx) coordinate, e.g., -120.168457
     * @param south          south (miny) coordinate, e.g., 36.738884
     * @param east           east (maxx) coordinate, e.g., -118.465576
     * @param north          north (maxy) coordinate, e.g., 38.091337
     * @param format         the format
     * @param outputFilename output filename, full path
     */
    public void download(DemTypesEnum demType, double west, double south, double east, double north, FormatEnum format, String outputFilename) {
        URI uri = buildRestUrl(demType, west, south, east, north, format);
        long start = System.currentTimeMillis();
        HttpUtils.doGetFile(uri, outputFilename, false);
        long time = System.currentTimeMillis() - start;
        log.debug("Time used: {}", time);
    }

    /**
     * Download gtiff.
     *
     * @param demType        the dem type
     * @param west           the west (minx)
     * @param south          the south (miny)
     * @param east           the east (maxx)
     * @param north          the north (maxy)
     * @param outputFilename the output filename
     */
    public void downloadGtiff(DemTypesEnum demType, double west, double south, double east, double north, String outputFilename) {
        URI uri = buildRestUrl(demType, west, south, east, north, FormatEnum.GTiff);
        long start = System.currentTimeMillis();
        HttpUtils.doGetFile(uri, outputFilename, false);
        long time = System.currentTimeMillis() - start;
        log.debug("Time used: {}", time);
    }

    private void writeResponse2File(@NotNull ResponseBody body, File outputFile) throws IOException {
        BufferedSource source = body.source();
        if(!outputFile.getParentFile().exists()){
            outputFile.getParentFile().mkdirs();
        }
        BufferedSink sink = Okio.buffer(Okio.sink(outputFile));

        Buffer sinkBuffer = sink.getBuffer();
        long contentLength = body.contentLength();
        long totalBytesRead = 0;
        int bufferSize = 8 * 1024;
        int progress = 0;
        for (long bytesRead; (bytesRead = source.read(sinkBuffer, bufferSize)) != -1; ) {
            sink.emit();
            totalBytesRead += bytesRead;
            // 有问题
            progress = (int) ((totalBytesRead * 100) / contentLength);
            log.info("Write percent: {} %", progress);
        }

        sink.flush();
        sink.close();
        source.close();
    }


    /**
     * Ok download gtiff.
     *
     * @param demType        the dem type
     * @param wsen           the wsen: <b>[west,  south,  east,  north]</b>
     * @param outputFilename the output filename
     * @throws IOException the io exception
     */
    public void okDownloadGtiff(DemTypesEnum demType, double[] wsen, String outputFilename) throws IOException {
        okDownloadGtiff(demType, wsen[0], wsen[1], wsen[2], wsen[3], outputFilename);
    }

    public void okDownloadGtiff(DemTypesEnum demType, double west, double south, double east, double north, String outputFilename) throws IOException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(1, TimeUnit.MINUTES).readTimeout(30, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();
        HttpUrl url = buildRequestUrl(demType, west, south, east, north, FormatEnum.GTiff);
        long start = System.currentTimeMillis();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        assert body != null;
        writeResponse2File(body, new File(outputFilename));
        long time = System.currentTimeMillis() - start;
        log.debug("Time used: {}", time);
    }
}
