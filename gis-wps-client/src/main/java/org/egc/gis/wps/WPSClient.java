package org.egc.gis.wps;

import net.opengis.wps.x100.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.feature.FeatureCollection;
import org.n52.wps.client.ExecuteRequestBuilder;
import org.n52.wps.client.ExecuteResponseAnalyser;
import org.n52.wps.client.WPSClientException;
import org.n52.wps.client.WPSClientSession;
import org.n52.wps.io.data.GenericFileDataWithGT;
import org.n52.wps.io.data.IData;
import org.n52.wps.io.data.binding.complex.GTRasterDataBinding;
import org.n52.wps.io.data.binding.complex.GTVectorDataBinding;
import org.n52.wps.io.data.binding.complex.GenericFileDataWithGTBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * https://wiki.52north.org/Geoprocessing/TutorialClientAPI
 *
 * @author houzhiwei
 * @date 2020/8/25 19:40
 */
public class WPSClient {

    private static final Logger log = LoggerFactory.getLogger(WPSClient.class);

    public static final String VERSION_100 = "1.0.0";
    public static final String DESCRIBE_PROCESS = "DescribeProcess";
    public static final String EXECUTE = "Execute";
    public static final String SCHEMA_FEATURE_311 = "http://schemas.opengis.net/gml/3.1.1/base/feature.xsd";
    private WPSClientSession wpsClient;
    private String serviceEndpoint;
    ProcessBriefType[] processList;

    /**
     * Instantiates a new WPS client.
     * <br/> only 1.0.0
     *
     * @param serviceEndpoint the service endpoint, e.g., "http://localhost:8080/wps/service";
     * @throws WPSClientException the wps client exception
     */
    public WPSClient(String serviceEndpoint) throws WPSClientException {
        this.serviceEndpoint = serviceEndpoint;
        wpsClient = WPSClientSession.getInstance();
        wpsClient.connect(serviceEndpoint);
    }

    public CapabilitiesDocument requestGetCapabilities() {
        return wpsClient.getWPSCaps(serviceEndpoint);
    }

    public List<String> getProcessIds(CapabilitiesDocument capabilities) {
        processList = capabilities.getCapabilities()
                .getProcessOfferings().getProcessArray();
        List<String> ids = new ArrayList<>();
        for (ProcessBriefType process : processList) {
            ids.add(process.getIdentifier().getStringValue());
        }
        return ids;
    }

    /**
     * Gets process inputs、outputs.
     *
     * @param processId the process id
     * @return map with keys: inputs、outputs
     */
    public Map<String, List<String>> getProcessIOs(String processId) throws IOException {
        Map<String, List<String>> ios = new HashMap<>(2);
        ProcessDescriptionType processDescription = requestDescribeProcess(processId);
        InputDescriptionType[] inputList = processDescription.getDataInputs().getInputArray();
        OutputDescriptionType[] outputList = processDescription.getProcessOutputs().getOutputArray();
        ios.put("inputs", Arrays.stream(inputList).map(InputDescriptionType::getIdentifier).map(XmlAnySimpleType::getStringValue).collect(Collectors.toList()));
        List<String> outputs = new ArrayList<>();
        for (OutputDescriptionType output : outputList) {
            outputs.add(output.getIdentifier().getStringValue());
        }
        ios.put("outputs", outputs);
        return ios;
    }

    public ProcessDescriptionType requestDescribeProcess(String processId) throws IOException {
        return wpsClient.getProcessDescription(serviceEndpoint, processId);
    }

    /**
     * Sets execute inputs.
     * 请自行设置输入, 例如 Wps1Test#testProcess0()。该方法无法应对各种不同的情况
     *
     * @param processDescription the process description
     * @param inputs             the inputs： inputId, value
     * @return the execute inputs
     * @throws Exception the exception
     */
    @Deprecated
    public ExecuteRequestBuilder setExecuteInputs(ProcessDescriptionType processDescription, HashMap<String, WpsInputDTO> inputs) throws Exception {
        ExecuteRequestBuilder executeBuilder = new ExecuteRequestBuilder(processDescription);
        for (InputDescriptionType input : processDescription.getDataInputs().getInputArray()) {
            String inputName = input.getIdentifier().getStringValue();
            WpsInputDTO inputDTO = inputs.get(inputName);
            Object inputValue = inputDTO.getInputValue();
            String schema = inputDTO.getSchema();
            String encoding = inputDTO.getEncoding();
            String mimeType = inputDTO.getMimeType();

            if (input.getLiteralData() != null) {
                executeBuilder.addLiteralData(inputName, String.valueOf(inputValue));
            } else if (input.getComplexData() != null) {
                if (inputValue instanceof File) {
//                    ShapefileBinding (zipped)
//                    GeotiffBinding
                    IData data = new GenericFileDataWithGTBinding((new GenericFileDataWithGT((File) inputValue, mimeType)));
                    executeBuilder.addComplexData(inputName, data, null, encoding, mimeType);
                } else if (inputValue instanceof GridCoverage2D) {
                    IData data = new GTRasterDataBinding((GridCoverage2D) inputValue);
                    executeBuilder.addComplexData(inputName, data, null, encoding, mimeType);
                } else {
                    mimeType = StringUtils.isBlank(mimeType) ? "text/xml" : mimeType;
                    schema = StringUtils.isBlank(schema) ? SCHEMA_FEATURE_311 : schema;
                    // Complexdata by value
                    if (inputValue instanceof FeatureCollection) {
                        IData data = new GTVectorDataBinding((FeatureCollection) inputValue);
                        executeBuilder.addComplexData(inputName, data, schema, encoding, mimeType);
                    }
                    // Complexdata Reference
                    if (inputValue instanceof String) {
                        executeBuilder.addComplexDataReference(inputName, (String) inputValue, schema, encoding, mimeType);
                    }
                }
                if (inputValue == null && input.getMinOccurs().intValue() > 0) {
                    throw new IOException("Property not set, but mandatory: " + inputName);
                }
            }
        }
        return executeBuilder;
    }

    /**
     * Sets execute output.
     *
     * @param executeBuilder the execute builder
     * @param outputName     the output name
     * @param mimeType       the mime type
     * @param schema         the schema
     */
    public void setExecuteOutput(ExecuteRequestBuilder executeBuilder, String outputName, String mimeType, String schema) {
        executeBuilder.setMimeTypeForOutput(mimeType, outputName);
        executeBuilder.setSchemaForOutput(schema, outputName);
    }

    /**
     * Execute.
     * (IData) analyser.getComplexDataByIndex(0, GTVectorDataBinding.class);
     *
     * @param executeBuilder     the execute builder
     * @param processDescription the process description
     * @return the execute response analyser
     * @throws Exception the exception
     */
    public ExecuteResponseAnalyser execute(ExecuteRequestBuilder executeBuilder, ProcessDescriptionType processDescription) throws Exception {
//        WPSConfig config = WPSConfig.getInstance("classpath:wps_config.xml");
//        System.out.println(config.getPropertiesForServer().length);

        if (!executeBuilder.isExecuteValid()) {
            System.out.println("Created execute request is NOT valid.");
        }

        ExecuteDocument execute = executeBuilder.getExecute();

        log.debug(execute.getExecute().xmlText());

        execute.getExecute().setService("WPS");
        Object responseObject = wpsClient.execute(serviceEndpoint, execute);

        System.out.println("Got response:\n" + responseObject.toString() + "\n");

        if (responseObject instanceof ExecuteResponseDocument) {
            ExecuteResponseDocument responseDoc = (ExecuteResponseDocument) responseObject;
            return new ExecuteResponseAnalyser(execute, responseDoc, processDescription);
        }
        throw new Exception("Exception: " + responseObject.toString());
    }

    public Object executeRequest(ExecuteRequestBuilder executeBuilder, ProcessDescriptionType processDescription) throws WPSClientException {
        if (!executeBuilder.isExecuteValid()) {
            System.out.println("Created execute request is NOT valid.");
        }
        ExecuteDocument execute = executeBuilder.getExecute();

//        log.debug(execute.getExecute().xmlText());

        execute.getExecute().setService("WPS");
        return wpsClient.execute(serviceEndpoint, execute);
    }

    private String getOutputMimeType(ProcessDescriptionType processDescriptionType, boolean isGetDefaultMimeType) {

        String result = "";
        ProcessDescriptionType.ProcessOutputs processOutputs = processDescriptionType.getProcessOutputs();

        OutputDescriptionType outputDescriptionType = processOutputs.getOutputArray(0);
        SupportedComplexDataType complexDataType = outputDescriptionType.getComplexOutput();

        if (isGetDefaultMimeType) {
            ComplexDataCombinationType defaultFormat = complexDataType.getDefault();
            ComplexDataDescriptionType format = defaultFormat.getFormat();
            result = format.getMimeType();
        } else {
            ComplexDataCombinationsType supportedFormats = complexDataType.getSupported();
            ComplexDataDescriptionType format = supportedFormats.getFormatArray(0);
            result = format.getMimeType();
        }

        return result;
    }
}
