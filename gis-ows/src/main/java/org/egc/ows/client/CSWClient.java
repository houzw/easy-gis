package org.egc.ows.client;

import lombok.extern.slf4j.Slf4j;
import net.opengis.csw.v_2_0_2.*;
import org.egc.commons.exception.BusinessException;
import org.egc.ows.commons.HttpUtils;
import org.egc.ows.request.csw.CSWRequestBuilder;
import org.egc.ows.request.csw.GetRecordsRequest;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author houzhiwei
 * @date 2021/7/7 14:38
 */
@Slf4j
public class CSWClient {

    static net.opengis.csw.v_2_0_2.ObjectFactory cswof = new net.opengis.csw.v_2_0_2.ObjectFactory();
    static net.opengis.filter.v_1_1_0.ObjectFactory of = new net.opengis.filter.v_1_1_0.ObjectFactory();
    static net.opengis.gml.v_3_1_1.ObjectFactory gmlof = new net.opengis.gml.v_3_1_1.ObjectFactory();
    static Marshaller csw_jaxbmarshaller;
    static Unmarshaller csw_jaxbUnmarshaller;

    static {
        JAXBContext cswJaxbContext = null;
        try {
            cswJaxbContext = JAXBContext.newInstance(net.opengis.csw.v_2_0_2.ObjectFactory.class, net.opengis.filter.v_1_1_0.ObjectFactory.class, net.opengis.gml.v_3_1_1.ObjectFactory.class);
            csw_jaxbUnmarshaller = cswJaxbContext.createUnmarshaller();
            csw_jaxbmarshaller = cswJaxbContext.createMarshaller();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public String getServiceEndpoint() {
        return serviceEndpoint;
    }

    public void setServiceEndpoint(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }

    private String serviceEndpoint;
    private CSWRequestBuilder requestBuilder;

    public CSWClient(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
        this.requestBuilder = new CSWRequestBuilder(this.serviceEndpoint);
    }

    public CapabilitiesType getCapabilities() {
        try {
            URI url = this.requestBuilder.getCapabilities(CSWRequestBuilder.Version.Version_202);
            String s = HttpUtils.doGetString(url);
            assert s != null;
            return JAXB.unmarshal(new StringReader(s), net.opengis.csw.v_2_0_2.CapabilitiesType.class);
        } catch (URISyntaxException e) {
            throw new BusinessException(e, "URISyntaxException");
        }
    }

    public JAXBElement<GetRecordsType> getRecordsRequest(GetRecordsRequest request) {

        DistributedSearchType distributedSearchType = cswof.createDistributedSearchType();
        distributedSearchType.setHopCount(BigInteger.valueOf(request.getHopCount()));

        QueryType query = cswof.createQueryType();
        QueryConstraintType constraint = cswof.createQueryConstraintType();
        constraint.setVersion("1.1.0");
        query.setConstraint(constraint);
        query.getTypeNames().add(new QName(CSWRequestBuilder.CSW_202_NS,"Record"));
        query.getTypeNames().add(new QName("http://www.isotc211.org/2005/gmd",
                "MD_Metadata"));

        GetRecordsType getRecordsType = cswof.createGetRecordsType();
        getRecordsType
                .withResultType(request.getResultType())
                .withDistributedSearch(distributedSearchType)
                .withStartPosition(BigInteger.valueOf(request.getStartPosition()))
                .withRequestId(request.getRequestId())
                .withResponseHandler(request.getResponseHandler())
                .withMaxRecords(BigInteger.valueOf(request.getMaxRecords()))
                .withOutputSchema(request.getOutputSchema())
                .withOutputFormat(request.getOutputFormat());
        return cswof.createGetRecords(getRecordsType);
    }

    public GetRecordsResponseType getRecords(GetRecordsRequest request) {
        try {
            URI url = this.requestBuilder.getRecords(request);
            System.out.println(url);
            String s = HttpUtils.doGetString(url);
            assert s != null;
            return JAXB.unmarshal(new StringReader(s), net.opengis.csw.v_2_0_2.GetRecordsResponseType.class);
        } catch (URISyntaxException e) {
            throw new BusinessException(e, "URISyntaxException");
        }
    }

    public void getRecordsFile(GetRecordsRequest request, String saveFilename) {
        try {
            URI url = this.requestBuilder.getRecords(request);
            saveFilename = HttpUtils.doGetFile(url, saveFilename, true);
            if (saveFilename != null) {
                log.info("File of catalog records saved to {}", saveFilename);
            } else {
                log.error("GetRecords failed");
            }
        } catch (URISyntaxException e) {
            throw new BusinessException(e, "URISyntaxException");
        }
    }
}
