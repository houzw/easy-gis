package org.egc.gis.proj;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * https://github.com/maptiler/epsg.io
 *
 * @author houzhiwei
 * @date 2020/5/17 15:17
 */
@Slf4j
public class Epsg {

    public static final String EPSG_URI = "https://epsg.io";

    /**
     * fetch epsg info from epsg.io
     *
     * @param epsg epsg code (without "EPSG:")
     * @throws IOException
     */
    public static EpsgInfo fetchEpsgInfo(int epsg) {
        HttpUriRequest request = RequestBuilder.get()
                .setUri(EPSG_URI)
                .addParameter("format", "json")
                .addParameter("q", String.valueOf(epsg))
                .build();
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 & statusCode < 300) {
                String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8.name());
                EpsgResponse epsgResponse = JSON.parseObject(body, EpsgResponse.class);
                if (epsgResponse.getResults().size() > 0) {
                    return epsgResponse.getResults().get(0);
                } else {
                    return null;
                }
            } else {
                log.error("Error，statusCode={}", statusCode);
                return null;
            }
        } catch (IOException e) {
            log.error("fetch EPSG info error", e);
            return null;
        }
    }
}
