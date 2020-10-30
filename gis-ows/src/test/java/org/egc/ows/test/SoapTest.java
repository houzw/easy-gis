package org.egc.ows.test;

import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.*;
import com.eviware.soapui.model.iface.Request;
import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.support.SoapUIException;
import org.apache.xmlbeans.XmlException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author houzhiwei
 * @date 2020/10/16 14:15
 */
public class SoapTest {

    @Test
    public void test1() throws XmlException, IOException, SoapUIException {
        WsdlProject project = new WsdlProject();
        WsdlInterface iface = WsdlInterfaceFactory.importWsdl(project,
                "http://www.mycorp.com/somewsdl.wsdl", true)[0];

    }

    @Test
    public void test() throws XmlException, IOException, SoapUIException, Request.SubmitException {
        // create new project
        WsdlProject project = new WsdlProject();

// import amazon wsdl
        WsdlInterface iface = WsdlInterfaceFactory.importWsdl(project, "http://www.mycorp.com/somewsdl.wsdl", true)[0];

// get desired operation
        WsdlOperation operation =
                (WsdlOperation) iface.getOperationByName("MyOperation");

// create a new empty request for that operation
        WsdlRequest request = operation.addNewRequest("My request");

// generate the request content from the schema
        request.setRequestContent(operation.createRequest(true));
        WsdlSubmitContext context = new WsdlSubmitContext(request);
// submit the request
        WsdlSubmit submit = (WsdlSubmit) request.submit(context, false);

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
