package org.egc.gis.wps;

import com.alibaba.fastjson.JSON;
import net.opengis.wps.x100.CapabilitiesDocument;
import net.opengis.wps.x100.ExecuteResponseDocument;
import net.opengis.wps.x100.InputDescriptionType;
import net.opengis.wps.x100.ProcessDescriptionType;
import org.apache.commons.io.FileUtils;
import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;
import org.geotools.data.Base64;
import org.junit.Test;
import org.n52.wps.client.ExecuteRequestBuilder;
import org.n52.wps.client.ExecuteResponseAnalyser;
import org.n52.wps.client.WPSClientException;
import org.n52.wps.client.WPSClientSession;
import org.n52.wps.io.data.IData;
import org.n52.wps.io.data.binding.complex.GeotiffBinding;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.List;

//https://github.com/52North/WPS/blob/dev/52n-wps-webapp/src/test/java/org/n52/wps/test/ExecutePostIT.java
public class PitremoveTest {

    String url = "http://192.168.60.109:8080/wps/WebProcessingService";
    String pitremove = "org.example.PitRemove";// error
    String pitremoveFile = "org.example.PitRemoveFile";
    String pitremoveGeotiff = "org.example.PitRemoveGeotiff";// error
    String testFile = "D:/data/demos/zts.tif";
    String tiff_schema = "http://www.opengis.net/ows/1.1";
    String test_process = "org.n52.wps.server.algorithm.test.EchoProcess";

    @Test
    public void testConn() throws WPSClientException {
        // connect session
        WPSClientSession wpsClient = WPSClientSession.getInstance();
        boolean connected = wpsClient.connect(url);
        System.out.println(connected);
    }

    @Test
    public void testWps1() throws WPSClientException {
        WPSClient wpsClient = new WPSClient(url);
        CapabilitiesDocument capabilitiesDocument = wpsClient.requestGetCapabilities();
        List<String> processIds = wpsClient.getProcessIds(capabilitiesDocument);
        System.out.println(JSON.toJSONString(processIds, true));
    }

    @Test
    public void testProcess() throws WPSClientException, IOException {
        WPSClient wpsClient = new WPSClient(url);
        ProcessDescriptionType descriptionType = wpsClient.requestDescribeProcess(test_process);
        InputDescriptionType[] inputArray = descriptionType.getDataInputs().getInputArray();

        for (InputDescriptionType input : inputArray) {
            System.out.println(input.getIdentifier().getStringValue());
            System.out.println(input.getMinOccurs());
//            System.out.println(input.getTitle().getLang());
        }
    }

    @Test
    public void testExecProcess() throws Exception {
        WPSClient wpsClient = new WPSClient(url);
        File file = new File(testFile);
        byte[] bytes = FileUtils.readFileToByteArray(file);
        String dataStr = org.geotools.data.Base64.encodeBytes(bytes);

        ProcessDescriptionType descriptionType = wpsClient.requestDescribeProcess(pitremoveFile);
//        System.out.println(descriptionType.getIdentifier().getStringValue());

        ExecuteRequestBuilder executeBuilder = new ExecuteRequestBuilder(descriptionType);
        executeBuilder.addLiteralData("only4", "false");
        executeBuilder.addComplexData("dem", dataStr, null, "base64", "image/tiff");
        executeBuilder.setResponseDocument("pit_removed_dem", null, "base64", "image/tiff");// result null
//        executeBuilder.setRawData("pit_removed_dem", null, "base64", "image/tiff"); // error
//        executeBuilder.setStoreSupport("pit_removed_dem", true);
//        executeBuilder.setStatus("pit_removed_dem", true);
        
        Object response = wpsClient.executeRequest(executeBuilder, descriptionType);
        // compare input and output
        if (response instanceof ExecuteResponseDocument) {
            ExecuteResponseDocument responseDoc = (ExecuteResponseDocument) response;
            System.out.println(responseDoc.xmlText());
            XObject data = XPathAPI.eval(responseDoc.getDomNode(), "//wps:ComplexData");
            String output = data.toString();
//            System.out.println(output);
            Base64.decodeToFile(output, "D:/data/demos/wps_result.tif");
        }
//        ExecuteResponseAnalyser analyser = wpsClient.execute(executeBuilder, descriptionType);

//        IData pit_removed_dem = analyser.getComplexData("pit_removed_dem", GTRasterDataBinding.class);// got null
//        GridCoverage2D payload = ((GTRasterDataBinding) result).getPayload();
//        RasterIO.write(payload, "");
    }

    //https://github.com/geoserver/geoserver/blob/c98467248a24701611f1d5d0f20fe5ea075e43a0/src/extension/wps/wps-core/src/test/java/org/geoserver/wps/gs/BinaryProcessingsTest.java
    @Test
    @Deprecated
    public void testExecProcess2() throws Exception {
        WPSClient wpsClient = new WPSClient(url);
        File file = new File(testFile);
        byte[] bytes = FileUtils.readFileToByteArray(file);
//        String dataStr = Base64.encodeBase64URLSafeString(bytes);
        String dataStr = org.geotools.data.Base64.encodeBytes(bytes);
        ProcessDescriptionType descriptionType = wpsClient.requestDescribeProcess(pitremoveGeotiff);
        System.out.println(descriptionType.getIdentifier().getStringValue()); //org.example.PitRemoveGeotiff->geotiffBinding

        ExecuteRequestBuilder executeBuilder = new ExecuteRequestBuilder(descriptionType);
        executeBuilder.addLiteralData("only4", "false");
        //error inputDescription is null
        executeBuilder.addComplexData("dem", dataStr, null, "base64", "image/tiff");
        executeBuilder.setResponseDocument("pit_removed_dem", null, "base64", "image/tiff");
        ExecuteResponseAnalyser analyser = wpsClient.execute(executeBuilder, descriptionType);
        IData result = (IData) analyser.getComplexData("pit_removed_dem", GeotiffBinding.class);
//        IData result2 = (IData) analyser.getComplexDataByIndex(0, GTRasterDataBinding.class);
        File payload = ((GeotiffBinding) result).getPayload();
//        RasterIO.write(payload, "");
    }

    @Test
    public void base64() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("result_base64.txt");
        String base64 = FileUtils.readFileToString(classPathResource.getFile());
//        byte[] decode = Base64.decode(base64);
        Base64.decodeToFile(base64, "D:/data/demos/wps_result2.tif");
//        System.out.println(decode);
    }

}
