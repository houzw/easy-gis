package org.egc.ows.client;

import com.alibaba.fastjson.JSON;
import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.*;
import com.eviware.soapui.impl.wsdl.support.RequestFileAttachment;
import com.eviware.soapui.model.iface.Attachment;
import com.eviware.soapui.model.iface.Operation;
import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.support.SoapUIException;
import com.eviware.soapui.support.XmlHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.XmlException;
import org.egc.ows.commons.OperationParameter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Soap client.
 *
 * @author houzhiwei
 * @date 2020 /11/2 16:21
 */
@Slf4j
public class SoapClient {
    public String getWsdlLocation() {
        return wsdlLocation;
    }

    private String wsdlLocation;
    private WsdlInterface iface;
    private WsdlProject project;

    public String getEndpoint() {
        return iface.getEndpoints()[0];
    }

    private String endpoint;
    private boolean remoteEndpoint = false;
    private int timeout = 3000;

    public SoapClient(String wsdlLocation) throws XmlException, IOException, SoapUIException {
        this.wsdlLocation = wsdlLocation;
        // create new project
        WsdlProject project = new WsdlProject();
        this.project = project;
        // import wsdl
        this.iface = WsdlInterfaceFactory.importWsdl(project, this.wsdlLocation, true)[0];
    }

    public List<String> getOperations() {
        List<String> operations = new ArrayList<>();
        for (Operation suop : this.iface.getAllOperations()) {
            String op = suop.getName();
            operations.add(op);
        }
        return operations;
    }

    /**
     * Gets default request content.
     *
     * @param operationName the operation name
     * @return the default request content xml
     */
    public String getDefaultRequestContent(String operationName, boolean buildOptional) {
        WsdlOperation operation = iface.getOperationByName(operationName);
        return operation.createRequest(buildOptional);
    }

    /**
     * Gets operation parameters.
     *
     * @param operationName   the operation name
     * @param includeOptional the include optional
     * @return operation parameters
     * @throws XmlException the xml exception
     */
    public List<OperationParameter> getOperationParameters(String operationName, boolean includeOptional) throws XmlException {
        WsdlOperation operation = iface.getOperationByName(operationName);
        String requestXml = operation.createRequest(includeOptional);
        log.debug("Default request xml is \n {}", requestXml);
        /*
        <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:taud="http://taudem.hydro.ws.egc.org">
           <soap:Header/>
           <soap:Body>
              <taud:pitRemove>
                 <elevation>?</elevation>
                 <!--Optional:-->
                 <depressionMask>?</depressionMask>
                 <!--Optional:-->
                 <fillConsideringOnly4WayNeighbors>?</fillConsideringOnly4WayNeighbors>
                 <!--Optional:-->
                 <pitFilledElevation>?</pitFilledElevation>
              </taud:pitRemove>
           </soap:Body>
        </soap:Envelope>
        **/
        Node body = new XmlHolder(requestXml).getDomNode("//*:Body");
        List<OperationParameter> params = new ArrayList<>();
        //ignore text child nodes
        NodeList parameterNodes = body.getChildNodes().item(1).getChildNodes();

        for (int i = 0; i < parameterNodes.getLength(); i++) {
            Node item = parameterNodes.item(i);
            if (item.getNodeType() == Node.TEXT_NODE) {
                continue;
            }

            if (item.getNodeType() != Node.COMMENT_NODE) {
                OperationParameter parameter = new OperationParameter(item.getNodeName());
                params.add(parameter);
            } else {
                //<!--Optional:-->
                //the first next sibling is a text node
                Node optionalNode = item.getNextSibling().getNextSibling();
                OperationParameter parameter = new OperationParameter(optionalNode.getNodeName());
                parameter.setOptional(true);
                params.add(parameter);
                i += 2;
            }
        }
        return params;
    }


    /**
     * Generate request xml string.<br/>
     * https://stackoverflow.com/questions/26046052/updating-a-wsdlrequest-value-via-soapui-pro
     *
     * @param operationName     the operation name
     * @param requestParameters the request parameter key-value paris
     * @param includeOptional   the include optional
     * @return the string
     * @throws XmlException the xml exception
     */
    public String generateRequestXml(String operationName, Map<String, Object> requestParameters, boolean includeOptional) throws XmlException {
        WsdlOperation operation = iface.getOperationByName(operationName);
        String requestXml = operation.createRequest(includeOptional);
        XmlHolder xmlHolder = new XmlHolder(requestXml);
        for (Map.Entry<String, Object> entry : requestParameters.entrySet()) {
            xmlHolder.setNodeValue("//*:" + entry.getKey(), entry.getValue());
        }
        return xmlHolder.getPrettyXml();
    }

    public String generateRequestXml(String operationName, Map<String, Object> requestParameters) throws XmlException {
        WsdlOperation operation = iface.getOperationByName(operationName);
        String requestXml = operation.createRequest(false);
        XmlHolder xmlHolder = new XmlHolder(requestXml);
        for (Map.Entry<String, Object> entry : requestParameters.entrySet()) {
            xmlHolder.setNodeValue("//*:" + entry.getKey(), entry.getValue());
        }
        return xmlHolder.getPrettyXml();
    }

    public String invoke(WsdlOperation operation, String requestXml) throws Exception {
        return invoke(operation, requestXml, false);
    }

    public String invoke(WsdlOperation operation, String requestXml, boolean async) throws Exception {
        WsdlRequest request = operation.addNewRequest("request");
        request.setRequestContent(requestXml);
        WsdlSubmitContext context = new WsdlSubmitContext(request);
        WsdlSubmit<WsdlRequest> submit = request.submit(context, async);
        Response response = submit.getResponse();
        if (submit.getError() != null) {
            throw submit.getError();
        }
        String content = response.getContentAsString();
        log.info(content);
        return content;
    }
//-----------------------------------------------------------

    /**
     * TODO NOT IMPLEMENTED !
     * Invoke with attachments string.
     * https://stackoverflow.com/questions/9066049/attaching-file-in-soapui-with-groovy
     *
     * @param operation  the operation
     * @param requestXml the request xml
     * @param async      the async
     * @return the string
     * @throws Exception the exception
     */
    public String invokeWithAttachments(WsdlOperation operation, String requestXml, boolean async) throws Exception {
        WsdlRequest request = operation.addNewRequest("request");
        request.setRequestContent(requestXml);
        request.setMtomEnabled(true);
        request.getAttachmentCount();

        Attachment attachment = new RequestFileAttachment(new File(""), true, request);
        attachment.setContentType("application/octet-stream");
        attachment.setPart("filename");
        WsdlSubmitContext context = new WsdlSubmitContext(request);
        WsdlSubmit<WsdlRequest> submit = request.submit(context, async);
        Response response = submit.getResponse();
        if (submit.getError() != null) {
            throw submit.getError();
        }
        String content = response.getContentAsString();
        log.info(content);
        return content;
    }

    /**
     * TODO NOT IMPLEMENTED !
     * Generate request xml with attachments string.
     *
     * @param operationName     the operation name
     * @param requestParameters the request parameters
     * @param includeOptional   the include optional
     * @return the string
     * @throws XmlException the xml exception
     */
    public String generateRequestXmlWithAttachments(String operationName, Map<String, Object> requestParameters, boolean includeOptional) throws XmlException {
        WsdlOperation operation = iface.getOperationByName(operationName);
        String requestXml = operation.createRequest(includeOptional);
        XmlHolder xmlHolder = new XmlHolder(requestXml);

        for (Map.Entry<String, Object> entry : requestParameters.entrySet()) {
            xmlHolder.setNodeValue("//*:" + entry.getKey(), entry.getValue());
        }
        return xmlHolder.getPrettyXml();
    }

//----------------------------

    public void release() {
        this.iface.release();
        this.project.release();
    }

    public WsdlOperation getOperation(String operationName) {
        return iface.getOperationByName(operationName);
    }

    final static String charset = "UTF-8";

    private static String xmlToJson(String responseXml) {
        if (StringUtils.isNotBlank(responseXml)) {
            int beginIndex = responseXml.indexOf("<return>");
            int endIndex = responseXml.indexOf("</return>");
            responseXml = responseXml.substring(beginIndex, endIndex + 9);
        }
        return StringUtils.isNotBlank(responseXml) ? JSON.toJSONString(responseXml, true) : null;
    }
}
