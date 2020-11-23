package org.egc.ows.test;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import net.opengis.gml.v_3_2_1.EnvelopeType;
import net.opengis.ows.v_2_0.BoundingBoxType;
import net.opengis.wcs.v_1_0_0.CoverageDescription;
import net.opengis.wcs.v_1_0_0.LonLatEnvelopeType;
import net.opengis.wcs.v_1_0_0.WCSCapabilitiesType;
import net.opengis.wcs.v_2_0.CapabilitiesType;
import net.opengis.wcs.v_2_0.CoverageDescriptionType;
import net.opengis.wcs.v_2_0.CoverageDescriptionsType;
import net.opengis.wcs.v_2_0.CoverageSummaryType;
import net.opengis.wcs20.GetCapabilitiesType;
import org.egc.ows.client.OWSClient;
import org.egc.ows.client.WCSClient;
import org.egc.ows.request.wcs.GetCoverageRequest20;
import org.egc.ows.commons.WCSUtils;
import org.geotools.wcs.WCSConfiguration;
import org.geotools.xsd.Parser;
import org.junit.Test;

import javax.xml.bind.JAXBElement;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author houzhiwei
 * @date 2020/9/6 15:30
 */
public class WCSTest {
    public static final String TEST_URL = "https://elevation.nationalmap.gov/arcgis/services/3DEPElevation/ImageServer/WCSServer";
    //    public static final String TEST_URL = "https://demo.mapserver.org/cgi-bin/wcs";
//    public static final String TEST_URL = "https://demo.mapserver.org/cgi-bin/wcs?SERVICE=WCS&VERSION=1.0.0&REQUEST=GetCapabilities";
    String url = "https://demo.mapserver.org/cgi-bin/wcs?SERVICE=WCS&VERSION=1.0.0&REQUEST=GetCapabilities";
    String base_url = "https://demo.mapserver.org/cgi-bin/wcs";
    String base_url2 = "http://194.66.252.155/geoserver/OneGDev/wcs";

    @Test
    public void testGetCap100() {
        WCSClient client = OWSClient.createWCSClient(base_url);
        WCSCapabilitiesType capabilities10 = client.getCapabilities10();
        System.out.println(capabilities10.getService().getName());
        //layer名称应该使用wcsName
        System.out.println(capabilities10.getContentMetadata().getCoverageOfferingBrief().get(0).getLabel());//[]
        LonLatEnvelopeType lonLatEnvelope = capabilities10.getContentMetadata().getCoverageOfferingBrief().get(0)
                .getLonLatEnvelope();
        lonLatEnvelope.getSrsName();
        System.out.println(lonLatEnvelope.getPos().get(0).getValue());//[]
        System.out.println(JSON.toJSONString(capabilities10.getContentMetadata().getCoverageOfferingBrief(), true));
    }

    @Test
    public void testGetCap20() {
        WCSClient client = OWSClient.createWCSClient(base_url2);
        CapabilitiesType capabilities20 = client.getCapabilities20();
        capabilities20.getServiceMetadata().getFormatSupported();
        String endpoint = capabilities20.getOperationsMetadata().getOperation()
                .get(0).getDCP().get(0).getHTTP().getGetOrPost().get(0).getValue().getHref();
        CoverageSummaryType coverageSummaryType = capabilities20.getContents().getCoverageSummary().get(0);
        coverageSummaryType.getCoverageId();
        coverageSummaryType.getKeywords().get(0).getKeyword().get(0).getValue();
        coverageSummaryType.getTitle();
        coverageSummaryType.getWGS84BoundingBox().get(0).getLowerCorner();
        coverageSummaryType.getWGS84BoundingBox().get(0).getUpperCorner();
        coverageSummaryType.getBoundingBox().get(0).getName().getLocalPart();
        BoundingBoxType value = (BoundingBoxType) coverageSummaryType.getBoundingBox().get(0).getValue();
        value.getCrs();
        value.getLowerCorner();
        value.getUpperCorner();
        System.out.println(JSON.toJSONString(capabilities20, true));
    }

    @Test
    public void testDescribe() {
        String name = "modis-001";
        WCSClient client = OWSClient.createWCSClient(base_url);
        CoverageDescription coverageDescription = client.describeCoverage10(Lists.newArrayList(name));
        System.out.println(JSON.toJSONString(coverageDescription.getCoverageOffering(), true));
    }
    @Test
    public void testDescribe20() {
        String name = "OneGDev__GreaterNorthSea-MCol";
        WCSClient client = OWSClient.createWCSClient(base_url2);
        CoverageDescriptionsType coverageDescription = client.describeCoverage20(Lists.newArrayList(name));
        CoverageDescriptionType coverageDescriptionType = coverageDescription.getCoverageDescription().get(0);
        System.out.println(coverageDescriptionType.getCoverageId());
        System.out.println(coverageDescriptionType.getDescription().getValue());
        System.out.println(coverageDescriptionType.getName().get(0).getValue());
        JAXBElement<? extends EnvelopeType> envelope = coverageDescriptionType.getBoundedBy().getEnvelope();
        envelope.getValue().getLowerCorner().getValue();
        envelope.getValue().getUpperCorner().getValue();
        envelope.getValue().getSrsName();
    }

    @Test
    public void testGetCoverage20() {
        String name = "OneGDev__GreaterNorthSea-MCol";
        WCSClient client = OWSClient.createWCSClient(base_url2);
        GetCoverageRequest20 getCoverageRequest20 = new GetCoverageRequest20.Builder(name)
                .format("image/png")
                .subsetLatLong(49.70253, 8.99089, 54.44863, 13.56120).build();
        client.getCoverage20(getCoverageRequest20,"I:/download/test.png");
    }

    @Test
    public void parseCap() {
        String capRequestPath = "/wcsCapabilities20.xml";
        CapabilitiesType capabilities = WCSUtils.parse201Capabilities(getClass().getResourceAsStream(capRequestPath));
        System.out.println(capabilities.getServiceIdentification().getTitle().get(0).getValue());
        capabilities.getOperationsMetadata().getOperation().get(0).getName();
        capabilities.getServiceMetadata().getFormatSupported();
        CoverageSummaryType coverageSummary = capabilities.getContents().getCoverageSummary().get(0);
        coverageSummary.getCoverageId();
        coverageSummary.getWGS84BoundingBox().get(0).getCrs();
        coverageSummary.getWGS84BoundingBox().get(0).getLowerCorner();
    }

    Parser parser = new Parser(new WCSConfiguration());

    @Test
    public void testParseCapabilitiesRequest() throws Exception {
        String capRequestPath = "/wcsCapabilities20.xml";
//        String capRequestPath = "requestGetCapabilities.xml";
        GetCapabilitiesType caps = (GetCapabilitiesType) parser.parse(getClass()
                .getResourceAsStream(capRequestPath));
        assertEquals("WCS", caps.getService());

        List versions = caps.getAcceptVersions().getVersion();
        assertEquals("2.0.1", versions.get(0));
        assertEquals("2.0.0", versions.get(1));
        assertEquals("1.1.0", versions.get(2));

        List sections = caps.getSections().getSection();
        assertEquals(1, sections.size());
        assertEquals("OperationsMetadata", sections.get(0));

        List formats = caps.getAcceptFormats().getOutputFormat();
        assertEquals(1, formats.size());
        assertEquals("application/xml", formats.get(0));
    }

}
