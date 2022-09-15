package org.egc.gis.wps;

import net.opengis.wps.x100.ExecuteDocument;
import net.opengis.wps.x100.ExecuteResponseDocument;
import net.opengis.wps.x100.ProcessDescriptionType;
import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;
import org.junit.Test;
import org.n52.wps.client.ExecuteRequestBuilder;
import org.n52.wps.client.WPSClientException;
import org.n52.wps.client.WPSClientSession;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Random;

/**
 * description:
 *
 * @author houzhiwei
 * @date 2022/4/29 22:26
 */
public class N52Tests {

    private String url = "http://192.168.60.109:8080/wps/WebProcessingService";

    private String processID = "org.n52.wps.server.algorithm.test.EchoProcess";

    @Test
    public void processTest() throws WPSClientException, IOException, TransformerException {
        // connect session
        WPSClientSession wpsClient = WPSClientSession.getInstance();
        boolean connected = wpsClient.connect(url);

        // take a look at the process description
        ProcessDescriptionType processDescription = wpsClient.getProcessDescription(url, processID);
        System.out.println("Echo process description:\n" + processDescription.xmlText() + "\n");

        Random rand = new Random();

        // create the request, add literal input
        ExecuteRequestBuilder executeBuilder = new ExecuteRequestBuilder(processDescription);
        String input = "Hello lucky number " + rand.nextInt(42) + "!";
        String parameterIn = "literalInput";
        executeBuilder.addLiteralData(parameterIn, input);
        String parameterOut = "literalOutput";
        executeBuilder.setResponseDocument(parameterOut, null, null, null);

        if (!executeBuilder.isExecuteValid()) {
            System.out.println("Created execute request is NOT valid.");
        }


        // build and send the request document
        ExecuteDocument executeRequest = executeBuilder.getExecute();
        System.out.println("Sending execute request:\n" + executeRequest.xmlText() + "\n");
        Object response = wpsClient.execute(url, executeRequest);
        System.out.println("Got response:\n" + response.toString() + "\n");

        // compare input and output
        if (response instanceof ExecuteResponseDocument) {
            ExecuteResponseDocument responseDoc = (ExecuteResponseDocument) response;
            XObject data = XPathAPI.eval(responseDoc.getDomNode(), "//wps:LiteralData");
            String output = data.toString();
            if (output.equals(input)) {
                System.out.println("Echo received!");
            }
        }

    }

}
