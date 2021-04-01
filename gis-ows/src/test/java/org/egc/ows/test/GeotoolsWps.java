package org.egc.ows.test;

import net.opengis.wps10.ProcessDescriptionType;
import net.opengis.wps10.ProcessDescriptionsType;
import net.opengis.wps10.ProcessOfferingsType;
import net.opengis.wps10.WPSCapabilitiesType;
import org.eclipse.emf.common.util.EList;
import org.geotools.data.wps.WebProcessingService;
import org.geotools.data.wps.request.DescribeProcessRequest;
import org.geotools.data.wps.response.DescribeProcessResponse;
import org.geotools.ows.ServiceException;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

/**
 * @author houzhiwei
 * @date 2020/12/25 19:02
 */
public class GeotoolsWps {
    String url = "http://localhost:8080/wps/WebProcessingService";
    String process = "org.example.GeotiffAlgorithm";
    String process2 = "org.example.GenericFileAlgorithm";
    String pitremove = "org.example.PitRemove";

    @Test
    public void testGwps() throws IOException, ServiceException {
//        new WebProcessingService(new URL(url), new SimpleHttpClient(), null);
        WebProcessingService wps = new WebProcessingService(new URL(url));
        WPSCapabilitiesType capabilities = wps.getCapabilities();

// view a list of processes offered by the server
        ProcessOfferingsType processOfferings = capabilities.getProcessOfferings();
        EList processes = processOfferings.getProcess();
        Object o = processes.get(0);

        // create a WebProcessingService as shown above, then do a full describeprocess on my process
        DescribeProcessRequest descRequest = wps.createDescribeProcessRequest();
        descRequest.setIdentifier("DoubleAddition"); // describe the double addition process

// send the request and get the ProcessDescriptionType bean to create a WPSFactory
        DescribeProcessResponse descResponse = wps.issueRequest(descRequest);
        ProcessDescriptionsType processDesc = descResponse.getProcessDesc();
        ProcessDescriptionType pdt = (ProcessDescriptionType) processDesc.getProcessDescription().get(0);
//        WPSFactory wpsfactory = new WPSFactory(pdt, url);

// create a process
//        Process process = wpsfactory.create();
    }
}
