package org.egc.ows.test;

import org.apache.commons.io.FileUtils;
import org.egc.ows.client.JavaPSClient;
import org.geotools.data.Base64;
import org.junit.Test;
import org.n52.geoprocessing.wps.client.ExecuteRequestBuilder;
import org.n52.geoprocessing.wps.client.WPSClientException;
import org.n52.geoprocessing.wps.client.model.Format;
import org.n52.geoprocessing.wps.client.model.InputDescription;
import org.n52.geoprocessing.wps.client.model.Process;
import org.n52.geoprocessing.wps.client.model.WPSCapabilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author houzhiwei
 * @date 2020/12/3 23:31
 */
public class WpsTest {
    private static final String MIME_TYPE_TEXT_CSV = "text/csv";
    private static final String MIME_TYPE_TEXT_XML = "text/xml";
    private static final String MIME_TYPE_IMAGE_TIFF = "image/tiff";
    private static final String schema = "http://schemas.opengis.net/gml/3.1.1/base/feature.xsd";

    String wpsURL = "http://geoprocessing.demo.52north.org:8080/wps/WebProcessingService";
    String processID = "org.n52.wps.server.algorithm.test.DummyTestClass";

    @Test
    public void test1() throws WPSClientException, IOException {
        JavaPSClient client = new JavaPSClient(wpsURL, version);
        WPSCapabilities cpbDoc = client.requestGetCapabilities();
//        System.out.println(cpbDoc);
        Process describeProcessDocument = client.requestDescribeProcess(processID);
        ExecuteRequestBuilder builder = new ExecuteRequestBuilder(describeProcessDocument);

        builder.addComplexData("ComplexInputData", "a,b,c", "", "", "MIME_TYPE_TEXT_CSV");
        builder.addLiteralData("LiteralInputData", "0.05", "", "", "MIME_TYPE_TEXT_XML");
       /* BoundingBox boundingBox = new BoundingBox();

            boundingBox.setMinY(50.0);
            boundingBox.setMinX(7.0);
            boundingBox.setMaxY(51.0);
            boundingBox.setMaxX(7.1);

            boundingBox.setCrs("EPSG:4326");

            boundingBox.setDimensions(2);

            builder.addBoundingBoxData("BBOXInputData", boundingBox, "", "", MIME_TYPE_TEXT_XML);
        */
//        builder.addOutput("LiteralOutputData", "", "", MIME_TYPE_TEXT_XML);
//        builder.addOutput("BBOXOutputData", "", "", MIME_TYPE_TEXT_XML);

//            builder.setResponseDocument("ComplexOutputData", "", "", MIME_TYPE_TEXT_CSV);

//        builder.setAsynchronousExecute();

//        WPSClient.execute(url, builder.getExecute(), version);
    }

    String url = "http://localhost:8083/javaps/service";
    String version = "2.0.0";
    String pitremove = "org.egc.javaps.algorithms.PitRemove";

    @Test
    public void testTauDem() throws WPSClientException, IOException {
        JavaPSClient client = new JavaPSClient(url, version);
        WPSCapabilities cpbDoc = client.requestGetCapabilities();
//        System.out.println(cpbDoc);
        System.out.println(pitremove + "3");
        Map<String, List<String>> processIOs = client.getProcessIOs(pitremove + "3");
        System.out.println(processIOs);
        Process describeProcessDocument3 = client.requestDescribeProcess(pitremove + "3");

        for (InputDescription id : describeProcessDocument3.getInputs()) {
            System.out.println(id.getId());
            for (Format format : id.getFormats()) {
                System.out.println(format.getMimeType());
            }
        }

        System.out.println(pitremove + "32");
        Map<String, List<String>> processIOs32 = client.getProcessIOs(pitremove + "32");
        System.out.println(processIOs32);
        Process describeProcessDocument32 = client.requestDescribeProcess(pitremove + "32");

        for (InputDescription id : describeProcessDocument32.getInputs()) {
            System.out.println(id.getId());
            for (Format format : id.getFormats()) {
                System.out.println(format.getMimeType());
            }
        }


        Process describeProcessDocument = client.requestDescribeProcess(pitremove);

        Process describeProcessDocument4 = client.requestDescribeProcess(pitremove + "4");
        //dem
       /* InputDescription inputDescription = describeProcessDocument.getInputs().get(1);
        Format inputFormat = inputDescription.getFormats().get(0);
        System.out.println(inputDescription.getId());
        System.out.println(inputFormat.getSchema());
        System.out.println(inputFormat.getEncoding());
        System.out.println(inputFormat.getMimeType());

      */


//        ExecuteRequestBuilder builder = new ExecuteRequestBuilder(describeProcessDocument32);
        ExecuteRequestBuilder builder = new ExecuteRequestBuilder(describeProcessDocument32);
        File file = new File("J:/demos/zts.tif");
        FileInputStream is = new FileInputStream(file);

       /* MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        String contentType = fileTypeMap.getContentType(file);
        System.out.println(contentType);*/

        byte[] bytes = FileUtils.readFileToByteArray(file);
        String s = Base64.encodeBytes(bytes);
        //is base64  32字节
        byte[] encode = java.util.Base64.getEncoder().encode(bytes);
        builder.addComplexData("dem", Arrays.toString(encode), null, "base64", "image/tiff");

//        OutputDescription outputDescription = describeProcessDocument.getOutputs().get(0);
//        Format format = outputDescription.getFormats().get(0);
       /* System.out.println(format.getEncoding());
        System.out.println(format.getSchema());
        System.out.println(format.getMimeType());*/
        //complex output data

        builder.setResponseDocument("pit_removed_dem", null, "base64", "application/geotiff");
        builder.setAsynchronousExecute();
        client.execute(builder.getExecute());
    }

    @Test
    public void test() throws WPSClientException, IOException {
        testPr2(5);
    }

    public void testPr2(int id) throws WPSClientException, IOException {
        JavaPSClient client = new JavaPSClient(url, version);
        Process describeProcessDocument2;
        if (id != 0) {
            describeProcessDocument2 = client.requestDescribeProcess(pitremove + id);
        } else {
            describeProcessDocument2 = client.requestDescribeProcess(pitremove);
        }
        ExecuteRequestBuilder builder = new ExecuteRequestBuilder(describeProcessDocument2);
        File file = new File("J:/demos/zts.tif");

        byte[] bytes = FileUtils.readFileToByteArray(file);
        String encode = java.util.Base64.getEncoder().encodeToString(bytes);
        String s = org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(bytes);
        builder.addComplexData("dem", s, null, "base64", "image/tiff");
        builder.setResponseDocument("pit_removed_dem", null, "base64", "image/tiff");

        builder.setAsynchronousExecute();
        client.execute(builder.getExecute());
    }

    @Test
    public void testTiff() throws IOException, WPSClientException {
        File file = new File("J:/demos/zts.tif");
        byte[] bytes = FileUtils.readFileToByteArray(file);
        String dataStr = org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(bytes);
        JavaPSClient client = new JavaPSClient(url, version);
        Process describeProcessDocument = client.requestDescribeProcess("org.egc.javaps.algorithms.GeotiffFileAlgorithm");
        ExecuteRequestBuilder builder = new ExecuteRequestBuilder(describeProcessDocument);
        builder.addLiteralData("only4", "false", null, null, MIME_TYPE_TEXT_XML);
        builder.addComplexData("tiff", dataStr, null, "base64", MIME_TYPE_IMAGE_TIFF);
        builder.setResponseDocument("result", null, "base64", MIME_TYPE_IMAGE_TIFF);
        builder.setAsynchronousExecute();
        client.execute(builder.getExecute());
    }

    @Test
    public void testGet() throws WPSClientException {
        JavaPSClient client = new JavaPSClient(url, version);
        client.requestGetCapabilities();
    }

    String url1 = "http://localhost:8080/wps/WebProcessingService";
    String pitremove100 = "org.example.PitRemove";

    @Test
    public void testVersion1() throws WPSClientException, IOException {
        JavaPSClient client = new JavaPSClient(url1, JavaPSClient.VERSION_100);
        client.requestGetCapabilities();

        Map<String, List<String>> processIOs = client.getProcessIOs(pitremove100);
        System.out.println(processIOs);
        Process describeProcessDocument = client.requestDescribeProcess(pitremove100);
        ExecuteRequestBuilder builder = new ExecuteRequestBuilder(describeProcessDocument);
        File file = new File("J:/demos/zts.tif");
        byte[] bytes = FileUtils.readFileToByteArray(file);
        String dataStr = org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(bytes);
        //文本类型设置为 text/xml，包含逗号的设置为 text/csv
        //为什么必须要有literal即使minOccur为0？
        builder.addLiteralData("only4", "false", null, null, MIME_TYPE_TEXT_XML);
        builder.addComplexData("dem", dataStr, null, "base64", MIME_TYPE_IMAGE_TIFF);
        builder.setResponseDocument("pit_removed_dem", null, "base64", MIME_TYPE_IMAGE_TIFF);
        builder.setAsynchronousExecute();
        client.execute(builder.getExecute());
    }
}
