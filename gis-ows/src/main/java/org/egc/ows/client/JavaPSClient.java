package org.egc.ows.client;

import org.n52.geoprocessing.wps.client.WPSClientException;
import org.n52.geoprocessing.wps.client.WPSClientSession;
import org.n52.geoprocessing.wps.client.model.Process;
import org.n52.geoprocessing.wps.client.model.*;
import org.n52.geoprocessing.wps.client.model.execution.ComplexData;
import org.n52.geoprocessing.wps.client.model.execution.Data;
import org.n52.geoprocessing.wps.client.model.execution.Execute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * https://github.com/52North/wps-client-lib/blob/master/src/test/java/org/n52/geoprocessing/wps/client/WPSClientExample.java
 *
 * @author houzhiwei
 * @date 2020/8/25 19:40
 */
public class JavaPSClient {

    private static final Logger log = LoggerFactory.getLogger(JavaPSClient.class);

    public static final String VERSION_100 = "1.0.0";
    public static final String VERSION_200 = "2.0.0";
    public static final String DESCRIBE_PROCESS = "DescribeProcess";
    public static final String EXECUTE = "Execute";
    private WPSClientSession wpsClient;
    private String version, serviceEndpoint;
    List<Process> processList;

    /**
     * Instantiates a new WPS client.
     *
     * @param serviceEndpoint the service endpoint, e.g., "http://localhost:8080/wps/service";
     * @param version         {@link JavaPSClient#VERSION_100} or {@link JavaPSClient#VERSION_200}
     */
    public JavaPSClient(String serviceEndpoint, String version) throws WPSClientException {
        this.version = version;
        this.serviceEndpoint = serviceEndpoint;
        wpsClient = WPSClientSession.getInstance();
        wpsClient.connect(serviceEndpoint, version);
    }

    public WPSCapabilities requestGetCapabilities() {
        return wpsClient.getWPSCaps(serviceEndpoint);
    }

    public List<String> getProcessIds(WPSCapabilities capabilities) {
        processList = capabilities.getProcesses();
        return processList.stream().map(Process::getId).collect(Collectors.toList());
    }

    /**
     * Gets process inputs、outputs.
     *
     * @param processId the process id
     * @return map with keys: inputs、outputs
     */
    public Map<String, List<String>> getProcessIOs(String processId) {
        Map<String, List<String>> ios = new HashMap<>(2);
        Process processDescription = requestDescribeProcess(processId);
        List<InputDescription> inputList = processDescription.getInputs();
        List<OutputDescription> outputList = processDescription.getOutputs();
        ios.put("inputs", inputList.stream().map(InputDescription::getId).collect(Collectors.toList()));
        ios.put("outputs", outputList.stream().map(OutputDescription::getId).collect(Collectors.toList()));
        return ios;
    }

    public Process requestDescribeProcess(String processId) {
        return wpsClient.getProcessDescription(serviceEndpoint, processId, version);
    }

    public void execute(Execute execute) {
        try {
            Object o = wpsClient.execute(serviceEndpoint, execute, version);
            if (o instanceof Result) {
                printOutputs((Result) o);
            } else if (o instanceof StatusInfo) {
                printOutputs(((StatusInfo) o).getResult());
            }
        } catch (WPSClientException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printOutputs(Result result) {
        List<Data> outputs = result.getOutputs();
        for (Data data : outputs) {
            if (data instanceof ComplexData) {
                ComplexData complexData = (ComplexData) data;
                System.out.println(complexData);
            }
            System.out.println(data);
        }
    }
}
