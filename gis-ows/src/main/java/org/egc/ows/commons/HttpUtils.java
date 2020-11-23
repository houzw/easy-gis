package org.egc.ows.commons;

import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.egc.commons.exception.BusinessException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.validation.constraints.NotNull;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.UUID;

/**
 * @author houzhiwei
 * @date 2020/8/25 19:38
 */
@Slf4j
public class HttpUtils {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3";

    /**
     * 获取SSL套接字对象 重点：设置tls协议的版本
     *
     * @return ssl context
     */
    protected static SSLContext createIgnoreVerifySsl() {
        // 创建套接字对象
        SSLContext sslContext = null;
        try {
            //指定TLS版本
            sslContext = SSLContext.getInstance("TLSv1.2");
        } catch (NoSuchAlgorithmException e) {
            log.error("Create SSLContext failed!", e);
        }
        // 实现X509TrustManager接口，用于绕过验证
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        try {
            assert sslContext != null;
            sslContext.init(null, new TrustManager[]{trustManager}, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            log.error("Initialize SSLContext failed!", e);
        }
        return sslContext;
    }

    /**
     * 绕过SSL
     * Gets ssl context.
     *
     * @return the ssl context
     */
    protected static SSLContext getSslContext() {
        try {
            return SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) {
                    return true;
                }
            }).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new BusinessException(e, "get SSLContext failed!");
        }
    }


    /**
     * Gets Pooling HttpClient Connection Manager
     *
     * @return the pooling conn mgr
     */
    public static PoolingHttpClientConnectionManager getPoolingConnMgr() {
        //create a socketfactory in order to use an http connection manager
        PlainConnectionSocketFactory plainSocketFactory = PlainConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> connSocketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", plainSocketFactory)
                .register("https", new SSLConnectionSocketFactory(createIgnoreVerifySsl(), NoopHostnameVerifier.INSTANCE))
//                .register("https", new SSLConnectionSocketFactory(getSSLContext(), NoopHostnameVerifier.INSTANCE))
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(connSocketFactoryRegistry);
        connManager.setMaxTotal(80);
        connManager.setDefaultMaxPerRoute(20);
        return connManager;
    }


    /**
     * Do post string string.
     *
     * @param url the url
     * @return the string
     */
    public static String doPostString(URI url) {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient client = postHttpClient(httpPost, 1000 * 60);
        try (CloseableHttpResponse response = client.execute(httpPost)) {
            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();
            log.debug("status code: " + statusCode);
            if (statusCode != HttpStatus.SC_OK) {
                throw new BusinessException("Failed with HTTP error code : " + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String s = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            //  ensure it is fully consumed
            EntityUtils.consume(entity);
            client.close();
            return s;
        } catch (IOException e) {
            log.error("Cannot convert http entity to string", e);
            return null;
        }
    }

    /**
     * get file from POST Request.
     *
     * @param url      the request url
     * @param filename the filename/dir to save the file
     * @return the filename (could be null)
     */
    public static String doPostFile(URI url, String filename, boolean useOriginalFilename) {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient client = postHttpClient(httpPost, 1000 * 120);
        try (CloseableHttpResponse response = client.execute(httpPost)) {
            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();
            log.debug("status code: " + statusCode);
            if (statusCode != HttpStatus.SC_OK) {
                throw new BusinessException("Failed with HTTP error code : " + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String originalName = getFilename(response);
            filename = checkFilename(filename, originalName, useOriginalFilename);
            if (entity != null) {
                InputStream inputStream = entity.getContent();
                if (inputStream != null) {
                    FileUtils.copyToFile(inputStream, new File(filename));
                }
                //  ensure it is fully consumed
                EntityUtils.consume(entity);
            }
            client.close();
        } catch (IOException e) {
            log.error("Cannot convert http entity to file", e);
            return null;
        }
        return filename;
    }

    public static InputStream doPostStream(URI url) {
        int timeout = 60000;
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient client = postHttpClient(httpPost, timeout);
        try (CloseableHttpResponse response = client.execute(httpPost)) {
            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();
            log.debug("status code: " + statusCode);
            if (statusCode != HttpStatus.SC_OK) {
                throw new BusinessException("Failed with HTTP error code : " + statusCode);
            }
            HttpEntity entity = response.getEntity();
            client.close();
            return new BufferedInputStream(entity.getContent());
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return null;
    }

    public static CloseableHttpClient getHttpClient(HttpGet request, int timeout) {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout)
                .setConnectTimeout(timeout).setConnectionRequestTimeout(30000)
                .build();
        request.setConfig(requestConfig);
        request.addHeader(HttpHeaders.USER_AGENT, USER_AGENT);
        request.addHeader(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate");
        PoolingHttpClientConnectionManager connMgr = HttpUtils.getPoolingConnMgr();
        return HttpClients.custom().setConnectionManager(connMgr).build();
    }

    public static CloseableHttpClient postHttpClient(HttpPost request, int timeout) {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout)
                .setConnectTimeout(timeout).setConnectionRequestTimeout(30000)
                .build();
        request.setConfig(requestConfig);
        request.addHeader(HttpHeaders.USER_AGENT, USER_AGENT);
        request.addHeader(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate");
        PoolingHttpClientConnectionManager connMgr = HttpUtils.getPoolingConnMgr();
        return HttpClients.custom().setConnectionManager(connMgr).build();
    }

    public static CloseableHttpClient getHttpClient(HttpGet request) {
        request.addHeader(HttpHeaders.USER_AGENT, USER_AGENT);
        request.addHeader(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate");
        PoolingHttpClientConnectionManager connMgr = HttpUtils.getPoolingConnMgr();
        return HttpClients.custom().setConnectionManager(connMgr).build();
    }

    public static CloseableHttpClient postHttpClient(HttpPost request) {
        request.addHeader(HttpHeaders.USER_AGENT, USER_AGENT);
        request.addHeader(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate");
        PoolingHttpClientConnectionManager connMgr = HttpUtils.getPoolingConnMgr();
        return HttpClients.custom().setConnectionManager(connMgr).build();
    }

    public static InputStream doGetStream(URI url) {
//        CloseableHttpResponse response = doGet(url);
        int timeout = 60000;
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpClient client = getHttpClient(httpGet, timeout);
        try (CloseableHttpResponse response = client.execute(httpGet)) {
            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();
            log.debug("status code: " + statusCode);
            if (statusCode != HttpStatus.SC_OK) {
                throw new BusinessException("Failed with HTTP error code : " + statusCode);
            }
            HttpEntity entity = response.getEntity();
            client.close();
            return new BufferedInputStream(entity.getContent());
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Do get string.
     *
     * @param url the url
     * @return the string
     */
    public static String doGetString(URI url) {
        int timeout = 60000;
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpClient client = getHttpClient(httpGet, timeout);
        try (CloseableHttpResponse response = client.execute(httpGet)) {
            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();
            log.debug("status code: " + statusCode);
            if (statusCode != HttpStatus.SC_OK) {
                throw new BusinessException("Failed with HTTP error code : " + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String s = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            //  ensure it is fully consumed
            EntityUtils.consume(entity);
            client.close();
            return s;
        } catch (IOException e) {
            log.error("Cannot convert http entity to string", e);
        }
        return null;
    }

    /**
     * get file from GET Request.
     *
     * @param url      the request url
     * @param filename the filename/dir to save the file
     * @return the filename (could be null)
     */
    public static String doGetFile(URI url, String filename, boolean useOriginalFilename) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpClient client = getHttpClient(httpGet, 1000 * 120);
        try (CloseableHttpResponse response = client.execute(httpGet)) {
            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();
            log.debug("status code: " + statusCode);
            HttpEntity entity = response.getEntity();
            if (statusCode != HttpStatus.SC_OK) {
                String err = EntityUtils.toString(entity);
                throw new BusinessException("Failed with HTTP error code: " + statusCode + "\n " + err);
            }
            String originalName = getFilename(response);
            filename = checkFilename(filename, originalName, useOriginalFilename);
            if (entity != null) {
                InputStream inputStream = entity.getContent();
                if (inputStream != null) {
                    FileUtils.copyToFile(inputStream, new File(filename));
                }
                //  ensure it is fully consumed
                EntityUtils.consume(entity);
            }
            client.close();
        } catch (IOException e) {
            log.error("Cannot convert http entity to file", e);
            return null;
        }
        return filename;
    }

    /**
     * Gets filename from response.
     *
     * @param response the response
     * @return the filename
     */
    public static String getFilename(HttpResponse response) {
        String filename = "";
        Header header = response.getFirstHeader("Content-Disposition");
        if (header != null) {
            String dispositionValue = header.getValue();
            int index = dispositionValue.indexOf("filename=");
            if (index > 0) {
                filename = dispositionValue.substring(index + 9);
            }
        }
        return filename;
    }


    /**
     * Write response to file.<br/>
     * https://stackoverflow.com/questions/25893030/download-binary-file-from-okhttp
     * <br/> based on OkHttp
     * @param body       the body
     * @param outputFile the output file
     * @throws IOException the io exception
     */
    public static void writeResponse2File(@NotNull ResponseBody body, File outputFile) throws IOException {
        BufferedSource source = body.source();
        BufferedSink sink = Okio.buffer(Okio.sink(outputFile));

        Buffer sinkBuffer = sink.getBuffer();
        long contentLength = body.contentLength();
        long totalBytesRead = 0;
        int bufferSize = 8 * 1024;
        int progress = 0;
        for (long bytesRead; (bytesRead = source.read(sinkBuffer, bufferSize)) != -1; ) {
            sink.emit();
            totalBytesRead += bytesRead;
            progress = (int) ((totalBytesRead * 100) / contentLength);
            log.info("Write percent: {} %", progress);
        }

        sink.flush();
        sink.close();
        source.close();
    }


    private static final String TEMP_DIR = "java.io.tmpdir";

    /**
     * @param filename            user input filename
     * @param originalName        filename from response
     * @param useOriginalFilename useOriginalFilename
     * @return new filename
     */
    private static String checkFilename(String filename, String originalName, boolean useOriginalFilename) {
        if (StringUtils.isNotBlank(filename) && StringUtils.isBlank(originalName)) {
            log.debug("File saved to {}", filename);
            return filename;
        }
        if (StringUtils.isBlank(filename) && StringUtils.isBlank(originalName)) {
            String tempfile = FilenameUtils.normalize(System.getProperty(TEMP_DIR) + File.separator + UUID.randomUUID().toString());
            log.error("No valid filename! File saved to {}", tempfile);
            return tempfile;
        }
        if (StringUtils.isBlank(filename) && StringUtils.isNotBlank(originalName)) {
            String s = FilenameUtils.normalize(System.getProperty(TEMP_DIR) + File.separator + originalName);
            log.warn("Use original filename. File saved to {}", s);
            return s;
        }
        if (new File(filename).isDirectory()) {
            String s = FilenameUtils.normalize(filename + File.separator + originalName);
            log.warn("Use original filename. File saved to {}", s);
            return s;
        }
        String ext = FilenameUtils.getExtension(originalName);
        if (useOriginalFilename) {
            filename = FilenameUtils.getFullPath(filename) + originalName;
        } else {
            //若后缀不一样，则使用response里面的文件名后缀
            if (StringUtils.isNotBlank(ext) && !ext.equalsIgnoreCase(FilenameUtils.getExtension(filename))) {
                filename = FilenameUtils.removeExtension(filename) + "." + ext;
            }
        }
        return filename;
    }
}
