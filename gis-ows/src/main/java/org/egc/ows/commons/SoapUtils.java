package org.egc.ows.commons;

import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlImporter;
import lombok.extern.slf4j.Slf4j;

import javax.wsdl.*;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import java.util.Iterator;
import java.util.List;

/**
 * https://www.javatips.net/api/com.eviware.soapui.impl.wsdl.wsdlrequest
 * https://stackoverflow.com/questions/26046052/updating-a-wsdlrequest-value-via-soapui-pro
 * 另可参考 https://github.com/hiwepy/soapui-template
 *
 * @author houzhiwei
 * @date 2020/10/9 15:25
 */
@Slf4j
@Deprecated
public class SoapUtils {

    public static String buildRequest(Definition wsdl, Operation operation) {
        String tnsUrl = wsdl.getTargetNamespace();
        String tns = wsdl.getPrefix(tnsUrl);
        StringBuffer soapRequest = new StringBuffer();
        soapRequest.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" ");
        soapRequest.append("xmlns:").append(tns).append("=\"").append(tnsUrl).append("\">");
        soapRequest.append("<soapenv:Header/>");
        soapRequest.append("<soapenv:Body>");
        soapRequest.append("<").append(tns).append(":").append(operation.getName()).append(">");
        //.....
        soapRequest.append("</").append(tns).append(":").append(operation.getName()).append(">");
        soapRequest.append("</soapenv:Body>");
        soapRequest.append("</soapenv:Envelope>");
        return soapRequest.toString();
    }
/*

<!--带参数：d8Pointer 为方法名称，filledDem 为参数名称-->
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
xmlns:whitebox="http://whitebox.gis.ws.egc.org">
   <soapenv:Header/>
   <soapenv:Body>
      <whitebox:d8Pointer>
         <filledDem>?</filledDem>
         <!--Optional:-->
         <esriPntr>?</esriPntr>
         <!--Optional:-->
         <flowDirectionDem>?</flowDirectionDem>
      </whitebox:d8Pointer>
   </soapenv:Body>
</soapenv:Envelope>

<!--无参数：test 为方法名称-->
<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
xmlns:seim="http://seims.hydro.ws.egc.org">
   <soap:Header/>
   <soap:Body>
      <seim:test/>
   </soap:Body>
</soap:Envelope>
<!--或者-->
<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" >
   <soap:Header/>
   <soap:Body>
      <test xmlns="http://seims.hydro.ws.egc.org"/>
   </soap:Body>
</soap:Envelope>

*/


    /**
     * Wsdl.
     * https://www.soapui.org/developers-corner/integrating-with-soapui/
     *
     * @param wsdlUrl the wsdl url or filepath
     * @throws Exception the exception
     */
    public static void parseWsdl(String wsdlUrl) throws Exception {
        WsdlProject project = new WsdlProject();
        WsdlInterface[] wsdls = WsdlImporter.importWsdl(project, wsdlUrl);
        WsdlInterface wsdl = wsdls[0];
        String[] endPoints = wsdl.getEndpoints();
        for (String endPoint : endPoints) {
            System.out.println("End Point URL " + endPoint);
        }
        wsdl.getOperations();
        for (com.eviware.soapui.model.iface.Operation operation : wsdl.getOperationList()) {
            WsdlOperation wsdlOperation = (WsdlOperation) operation;
            String request = wsdlOperation.createRequest(true);
            System.out.println("Request: \n" + request);
            wsdlOperation.release();
        }
        wsdl.release();
        project.release();
    }

    public static Definition readWsdl(String wsdlUrl) throws WSDLException {
        WSDLFactory factory = WSDLFactory.newInstance();
        WSDLReader reader = factory.newWSDLReader();
        reader.setFeature("javax.wsdl.verbose", true);
        reader.setFeature("javax.wsdl.importDocuments", true);
        return reader.readWSDL(wsdlUrl);
    }

    public static Service getService(Definition wsdlDef, String serviceName) {
        String tns = wsdlDef.getTargetNamespace();
        Service service = wsdlDef.getService(new QName(tns, serviceName));
        log.debug(service.getQName().getLocalPart());
        return service;
    }


    public static void getOperations(Service service, String portTypeName) {
        Port port = service.getPort(portTypeName);
        Binding binding = port.getBinding();
        PortType portType = binding.getPortType();
        List operations = portType.getOperations();
        Iterator operIter = operations.iterator();
        while (operIter.hasNext()) {
            Operation operation = (Operation) operIter.next();
            if (!operation.isUndefined()) {
                System.out.println(operation.getName());
                operation.getInput().getName();
                operation.getOutput().getName();
            }
        }
    }

    public static void getPortType(Definition wsdlDef, String portTypeName) {
        wsdlDef.getPortType(new QName(portTypeName));
    }
}
