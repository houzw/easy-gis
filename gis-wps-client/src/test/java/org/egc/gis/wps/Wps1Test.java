package org.egc.gis.wps;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import net.opengis.wps.x100.CapabilitiesDocument;
import net.opengis.wps.x100.ComplexDataDescriptionType;
import net.opengis.wps.x100.InputDescriptionType;
import net.opengis.wps.x100.ProcessDescriptionType;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.n52.wps.client.ExecuteRequestBuilder;
import org.n52.wps.client.ExecuteResponseAnalyser;
import org.n52.wps.client.WPSClientException;
import org.n52.wps.io.data.IData;
import org.n52.wps.io.data.binding.complex.GTRasterDataBinding;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

/**
 * @author houzhiwei
 * @date 2020/12/14 15:20
 */
public class Wps1Test {

    String url = "http://localhost:8080/wps/WebProcessingService";
    String process = "org.example.GeotiffAlgorithm";
    String process2 = "org.example.GenericFileAlgorithm";
    String pitremove = "org.example.PitRemove";
    String testFile = "D:/data/demos/zts.tif";

    @Test
    public void testWps1() throws WPSClientException, URISyntaxException, IOException {
//        HttpClient client = HttpClients.createDefault();
//        HttpGet get = new HttpGet("http://192.168.60.109:8080/wps/WebProcessingService?Request=GetCapabilities&Service=WPS");
//        HttpResponse resp = client.execute(get);
//        System.out.println(resp.getStatusLine().getStatusCode());

        WPSClient wpsClient = new WPSClient(url);
        CapabilitiesDocument capabilitiesDocument = wpsClient.requestGetCapabilities();
        List<String> processIds = wpsClient.getProcessIds(capabilitiesDocument);
        System.out.println(JSON.toJSONString(processIds, true));
    }

    @Test
    public void testProcessDescribe() throws WPSClientException, IOException {
        WPSClient wpsClient = new WPSClient(url);
        ProcessDescriptionType descriptionType = wpsClient.requestDescribeProcess(process);
        InputDescriptionType[] inputList = descriptionType.getDataInputs().getInputArray();
        for (InputDescriptionType input : inputList) {
            System.out.println(input.getIdentifier().getStringValue());
            System.out.println(input.getLiteralData());
            System.out.println(input.getMinOccurs());
            System.out.println(input.getMaxOccurs());
            if (input.getComplexData() != null) {
                System.out.println("---------------getComplexData-------------");
                System.out.println(input.getComplexData().getDefault().getFormat().getMimeType());
                System.out.println(input.getComplexData().getDefault().getFormat().getEncoding());
                ComplexDataDescriptionType[] formatArray = input.getComplexData().getSupported().getFormatArray();
                for (ComplexDataDescriptionType format : formatArray) {
                    System.out.println("---------------getSupported-------------");
                    System.out.println("getEncoding  " + format.getEncoding());
                    System.out.println(format.getMimeType());
                    System.out.println("getSchema  " + format.getSchema());
                }
                System.out.println("---------------getComplexData-------------");
            }
        }
        System.out.println(descriptionType.xmlText());
    }

    @Test
    public void testProcess0() throws Exception {
        WPSClient wpsClient = new WPSClient(url);
        ProcessDescriptionType descriptionType = wpsClient.requestDescribeProcess(process);
        ExecuteRequestBuilder executeBuilder = new ExecuteRequestBuilder(descriptionType);
        File file = new File(testFile);
        byte[] bytes = FileUtils.readFileToByteArray(file);
        String dataStr = Base64.encodeBase64URLSafeString(bytes);
        executeBuilder.addLiteralData("number", String.valueOf(1));
        executeBuilder.addComplexData("tiff", dataStr, null, "base64", "image/tiff");
        executeBuilder.setResponseDocument("result", null, "base64", "image/tiff");
        wpsClient.execute(executeBuilder, descriptionType);
    }

    @Test
    public void testStringProcess() throws Exception {
        WPSClient wpsClient = new WPSClient(url);
        ProcessDescriptionType descriptionType = wpsClient.requestDescribeProcess("org.example.SimpleStringAlgorithm");
        ExecuteRequestBuilder executeBuilder = new ExecuteRequestBuilder(descriptionType);
        executeBuilder.addLiteralData("distance", String.valueOf(1.5));
        executeBuilder.setResponseDocument("result", null, null, "text/xml");
        wpsClient.execute(executeBuilder, descriptionType);
    }

    /*
     ********************************************************
     * */
    @Test
    public void testProcess() throws Exception {
        WPSClient wpsClient = new WPSClient(url);
//        System.out.println(JSON.toJSONString(wpsClient.getProcessIOs(process2), true));
        HashMap<String, WpsInputDTO> data = Maps.newHashMap();
        File file = new File(testFile);
//        Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
//        GeoTiffReader reader = new GeoTiffReader(file, hints);
//        GridCoverage2D coverage = reader.read(null);
        data.put("number", new WpsInputDTO(1));
        data.put("tiff", new WpsInputDTO(file, "image/tiff", "base64"));
        ProcessDescriptionType descriptionType = wpsClient.requestDescribeProcess(process);
        ExecuteRequestBuilder executeRequestBuilder = wpsClient.setExecuteInputs(descriptionType, data);
        wpsClient.setExecuteOutput(executeRequestBuilder, "result", "image/tiff", null);
        ExecuteResponseAnalyser analyser = wpsClient.execute(executeRequestBuilder, descriptionType);
        IData result = (IData) analyser.getComplexDataByIndex(0, GTRasterDataBinding.class);

    }

    @Test
    public void testProcess2() throws Exception {
        WPSClient wpsClient = new WPSClient(url);
//        System.out.println(JSON.toJSONString(wpsClient.getProcessIOs(process2), true));
        HashMap<String, WpsInputDTO> data = Maps.newHashMap();
        File file = new File(testFile);
        data.put("number", new WpsInputDTO(1));
        data.put("tiff", new WpsInputDTO(file, "image/tiff", "base64"));
        ProcessDescriptionType descriptionType = wpsClient.requestDescribeProcess(process2);
        ExecuteRequestBuilder executeRequestBuilder = wpsClient.setExecuteInputs(descriptionType, data);
        wpsClient.setExecuteOutput(executeRequestBuilder, "result", "image/tiff", null);
        ExecuteResponseAnalyser analyser = wpsClient.execute(executeRequestBuilder, descriptionType);
        IData result = (IData) analyser.getComplexDataByIndex(0, GTRasterDataBinding.class);
    }

    @Test
    public void testPitremove() throws WPSClientException, IOException {
        WPSClient wpsClient = new WPSClient(url);
        ProcessDescriptionType descriptionType = wpsClient.requestDescribeProcess(pitremove);
        InputDescriptionType[] inputArray = descriptionType.getDataInputs().getInputArray();
        for (InputDescriptionType input : inputArray) {
            System.out.println(input.getIdentifier().getStringValue());
            System.out.println(input.getMinOccurs());
        }
    }

    @Test
    public void testProcess3() throws Exception {
        WPSClient wpsClient = new WPSClient(url);
        File file = new File(testFile);
        byte[] bytes = FileUtils.readFileToByteArray(file);
        String dataStr = Base64.encodeBase64URLSafeString(bytes);
        ProcessDescriptionType descriptionType = wpsClient.requestDescribeProcess(pitremove);
        System.out.println(descriptionType.getIdentifier().getStringValue());
        ExecuteRequestBuilder executeBuilder = new ExecuteRequestBuilder(descriptionType);
        executeBuilder.addLiteralData("only4", "false");
//        IData data = IData
        executeBuilder.addComplexData("dem", dataStr, null, "base64", "image/tiff");
        executeBuilder.setResponseDocument("pit_removed_dem", null, "base64", "image/tiff");
        ExecuteResponseAnalyser analyser = wpsClient.execute(executeBuilder, descriptionType);
//        IData result = (IData) analyser.getComplexData("pit_removed_dem", GTRasterDataBinding.class);
    }

    @Test
    public void testProcessCoverage() throws Exception {
        WPSClient wpsClient = new WPSClient(url);
//        System.out.println(JSON.toJSONString(wpsClient.getProcessIOs(process2), true));
        HashMap<String, WpsInputDTO> data = Maps.newHashMap();
        File file = new File(testFile);
//        Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
//        GeoTiffReader reader = new GeoTiffReader(file, hints);
//        GridCoverage2D coverage = reader.read((GeneralParameterValue[]) null);
        data.put("number", new WpsInputDTO(1));
//        data.put("tiff", new WpsInputDTO(coverage, "image/tiff", "base64"));
        ProcessDescriptionType descriptionType = wpsClient.requestDescribeProcess(process);
        ExecuteRequestBuilder executeRequestBuilder = wpsClient.setExecuteInputs(descriptionType, data);
        wpsClient.setExecuteOutput(executeRequestBuilder, "result", "image/tiff", null);
        ExecuteResponseAnalyser analyser = wpsClient.execute(executeRequestBuilder, descriptionType);
        IData result = (IData) analyser.getComplexDataByIndex(0, GTRasterDataBinding.class);

    }
}