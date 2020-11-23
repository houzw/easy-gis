package org.egc.ows.test;

import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.*;
import com.eviware.soapui.model.iface.Request;
import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.support.SoapUIException;
import com.eviware.soapui.support.XmlHolder;
import org.apache.xmlbeans.XmlException;
import org.egc.ows.client.SoapClient;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * https://www.javatips.net/api/com.eviware.soapui.impl.wsdl.wsdlrequest
 * https://stackoverflow.com/questions/26046052/updating-a-wsdlrequest-value-via-soapui-pro
 * 另可参考 https://github.com/hiwepy/soapui-template
 *
 * @author houzhiwei
 * @date 2020/10/16 14:15
 */
public class SoapTest {

    String taudemWsdl = "http://localhost:8008/ws/services/TauDEMAnalysisService?wsdl";
    String taudemWsdl_local = "/taudem.wsdl";

    @Test
    public void test1() throws XmlException, IOException, SoapUIException {
        WsdlProject project = new WsdlProject();
        WsdlInterface iface = WsdlInterfaceFactory.importWsdl(project,
                "http://www.mycorp.com/somewsdl.wsdl", true)[0];
    }

    @Test
    public void testSoapClient() throws XmlException, IOException, SoapUIException {
        String file = getClass().getResource(taudemWsdl_local).getPath();
        SoapClient client = new SoapClient(file);
        System.out.println(client.getEndpoint());
        System.out.println(client.getOperations());
        System.out.println(client.getDefaultRequestContent("pitRemove", true));
        System.out.println(client.getOperationParameters("pitRemove", true));
        Map<String, Object> p = new HashMap<>();
        p.put("elevation", "J:/demos/raster/dem1.tif");
        System.out.println(client.generateRequestXml("pitRemove", p));
    }

    @Test
    public void testOperation() throws Exception {
        WsdlProject project = new WsdlProject();
        String file = getClass().getResource(taudemWsdl_local).getPath();
        WsdlInterface iface = WsdlInterfaceFactory.importWsdl(project, file, true)[0];
        iface.getOperationList();

        WsdlOperation pitRemove = iface.getOperationByName("pitRemove");

        WsdlRequest request = pitRemove.addNewRequest("Request");
        String defaultRequest = pitRemove.createRequest(true);
//        String requiredRequest = pitRemove.createRequest(false);

        pitRemove.getAction();
        //pitRemove
        System.out.println(pitRemove.getRequestBodyElementQName().getLocalPart());
//        String defaultRequest = pitRemove.getRequestAt(0).getRequestContent();
        System.out.println(defaultRequest);
//        System.out.println(requiredRequest);

        XmlHolder xmlHolder = new XmlHolder(defaultRequest);
        Node body = xmlHolder.getDomNode("//*:Body");
        NodeList childNodes = body.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            //#text
            //taud:pitRemove
            //#text
            // nodetype=3 为文本节点
            if (item.getNodeType() != 3) {
                System.out.println(item.getNodeName());//taud:pitRemove
                System.out.println(item.getNodeValue());//null
            }
        }
        NodeList parameterNodes = body.getChildNodes().item(1).getChildNodes();
        //nodetype = 8 为注释
        for (int i = 0; i < parameterNodes.getLength(); i++) {
            Node node = parameterNodes.item(i);
            System.out.println(node.getNodeType());
            System.out.println(node.getNodeName());
            if (node.getNodeType() != Node.TEXT_NODE) {
                //getTextContent 不能用
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    System.out.println(node.getFirstChild().getNodeValue());
                }
                System.out.println(node.getNodeValue()); //获取节点及子节点的文本
            }
        }

       /* for (MessagePart part : pitRemove.getDefaultRequestParts()) {
            System.out.println(part.getName());
            System.out.println(part.getDescription());
            System.out.println(part.getPartType());
        }*/
//        System.out.println(pitRemove.getInputName());
//        pitRemove.getDefaultResponseParts();

    }

    @Test
    public void testRequest() throws XmlException, IOException, SoapUIException, Request.SubmitException {
        WsdlProject project = new WsdlProject();
        WsdlInterface iface = WsdlInterfaceFactory.importWsdl(project, taudemWsdl, true)[0];
        WsdlOperation pitRemove = iface.getOperationByName("PitRemove");
        WsdlRequest request = pitRemove.addNewRequest("My request");
        String defaultRequest = pitRemove.createRequest(true);

        XmlHolder holder = new XmlHolder(defaultRequest);
        holder.setNodeValue("", "");
        request.setRequestContent(holder.getPrettyXml());

        WsdlSubmitContext context = new WsdlSubmitContext(request);
        WsdlSubmit submit = request.submit(context, false);
        Response response = submit.getResponse();
        String content = response.getContentAsString();
    }


    @Test
    public void test() throws XmlException, IOException, SoapUIException, Request.SubmitException {
        // create new project
        WsdlProject project = new WsdlProject();

// import amazon wsdl
        WsdlInterface iface = WsdlInterfaceFactory.importWsdl(project, "http://www.mycorp.com/somewsdl.wsdl", true)[0];

// get desired operation
        WsdlOperation operation = iface.getOperationByName("MyOperation");

// create a new empty request for that operation
        WsdlRequest request = operation.addNewRequest("My request");

// generate the request content from the schema
        request.setRequestContent(operation.createRequest(true));
        WsdlSubmitContext context = new WsdlSubmitContext(request);
// submit the request
        WsdlSubmit submit = request.submit(context, false);

// wait for the response
        Response response = submit.getResponse();
        System.out.println(submit.getError());

//	print the response
        String content = response.getContentAsString();
        System.out.println(content);
        assertNotNull(content);
        assertTrue(content.indexOf("404 Not Found") > 0);
    }
}
