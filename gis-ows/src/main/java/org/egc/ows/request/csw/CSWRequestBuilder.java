package org.egc.ows.request.csw;

import net.opengis.cat.csw20.Csw20Factory;
import net.opengis.cat.csw20.GetCapabilitiesType;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;

import static org.egc.ows.commons.Consts.*;

/**
 * The type Wcs request builder.
 * http://docs.opengeospatial.org/is/12-176r7/12-176r7.html
 *
 * @author houzhiwei
 * @date 2020 /10/2 19:35
 */
public class CSWRequestBuilder {

    public static final String GMD_NS = "http://www.isotc211.org/2005/gmd";
    public static final String GML_NS = "http://www.opengis.net/gml";
    public static final String CSW_202_NS = "http://www.opengis.net/cat/csw/2.0.2";
    public static final String OGC_NS = "http://www.opengis.net/ogc";
    public static final String RIM_NS = "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0";
    public static final String FORMAT_XML = "application/xml";

    public final static String GET_CAPABILITIES = "GetCapabilities";
    public final static String DESCRIBE_RECORD = "DescribeRecord";
    public final static String GET_RECORD_BY_ID = "GetRecordById";
    public final static String GET_RECORDS = "GetRecords";
    public final static String GET_DOMAIN = "GetDomain";
    public final static String HARVEST = "Harvest";

    public final static String PROPERTY_NAME = "propertyname";
    public final static String PARAMETER_NAME = "parametername";
    public final static String TYPE_NAMES = "typenames";
    public final static String ELEMENT_SET_NAME = "elementsetname";

    public final static String NAMESPACE = "NAMESPACE";
    public final static String REQUEST_ID = "requestId";
    public final static String OUTPUT_FORMAT = "outputFormat";
    public final static String OUTPUT_SCHEMA = "outputSchema";
    public final static String START_POSITION = "startPosition";
    public final static String MAX_RECORDS = "maxRecords";
    public final static String ELEMENT_SET_NAME_TYPENAME = "ElementSetName_TypeName";
    public final static String ELEMENT_NAME = "ElementName";
    public final static String SORT_BY = "SortBy";
    public final static String DISTRIBUTED_SEARCH = "DistributedSearch";
    public final static String HOP_COUNT = "HopCount";
    public final static String CLIENT_ID = "ClientId";
    public final static String DISTRIBUTED_SEARCH_ID_TIMOUT = "DistributedSearchIdTimout";
    public final static String DISTRIBUTED_SEARCH_ID = "DistributedSearchId";
    public final static String FEDERATED_CATALOGUES = "FederatedCatalogues";
    public final static String RESPONSE_HANDLER = "ResponseHandler";

    public static final String CSW = "CSW";


    private String serviceRoot;

    public CSWRequestBuilder(String serviceRoot) {
        this.serviceRoot = serviceRoot;
    }

    public enum Version {
        Version_202("2.0.2"),
        Version_300("3.0.0");

        public String getVersion() {
            return version;
        }

        private String version;

        Version(String version) {
            this.version = version;
        }
    }

    public String GetCapabilities() throws JAXBException {
        Csw20Factory csw20Factory = Csw20Factory.eINSTANCE;
        GetCapabilitiesType getCapabilitiesType = csw20Factory.createGetCapabilitiesType();
        // serialise to xml
        StringWriter writer = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(net.opengis.csw.v_2_0_2.GetCapabilitiesType.class);
        Marshaller m = context.createMarshaller();
        QName qName = new QName("http://www.opengis.net/csw/2.0.2", "GetCapabilities");
        JAXBElement<GetCapabilitiesType> root = new JAXBElement<>(qName, GetCapabilitiesType.class, getCapabilitiesType);
        m.marshal(root, writer);
        return writer.toString();
    }


    public URI getCapabilities(CSWRequestBuilder.Version version) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(this.serviceRoot);
        uriBuilder.addParameter(SERVICE, CSW);
        uriBuilder.addParameter(VERSION, version.getVersion());
        uriBuilder.addParameter(REQUEST, GET_CAPABILITIES);
        return uriBuilder.build();
    }


    /**
     * The DescribeRecord operation provides CSW clients with elements of supported information models of the CSW service.
     *
     * @return describeRecord URI
     * @throws URISyntaxException the uri syntax exception
     */
    public URI describeRecord() throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(this.serviceRoot);
        uriBuilder.addParameter(SERVICE, CSW);
        uriBuilder.addParameter(VERSION, Version.Version_202.getVersion());
        uriBuilder.addParameter(REQUEST, DESCRIBE_RECORD);
        return uriBuilder.build();
    }

    /**
     * Gets domain.
     * The GetDomain operation provides an interface to return all possible values for a given metadata property/queryable or parameter.
     *
     * @param propertyname  the given metadata property
     * @param parametername the given metadata parameter, e.g., GetRecords.outputSchema
     * @return the domain uri
     * @throws URISyntaxException the uri syntax exception
     */
    public URI getDomain(String propertyname, String parametername) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(this.serviceRoot);
        uriBuilder.addParameter(SERVICE, CSW);
        uriBuilder.addParameter(VERSION, Version.Version_202.getVersion());
        uriBuilder.addParameter(REQUEST, GET_DOMAIN);
        if (StringUtils.isNotBlank(propertyname)) {
            uriBuilder.addParameter(PROPERTY_NAME, propertyname);
        }
        if (StringUtils.isNotBlank(parametername)) {
            uriBuilder.addParameter(PARAMETER_NAME, parametername);
        }
        return uriBuilder.build();
    }

    private void addBuilderParameter(URIBuilder uriBuilder, String paramName, String paramVal) {
        if (StringUtils.isNotBlank((String) paramVal)) {
            uriBuilder.addParameter(paramName, (String) paramVal);
        }
    }

    private void addBuilderIntParameter(URIBuilder uriBuilder, String paramName, int paramVal) {
        if (paramVal > 0) {
            uriBuilder.addParameter(paramName, String.valueOf(paramVal));
        }
    }

    public URI getRecords(GetRecordsRequest request) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(this.serviceRoot);
        uriBuilder.addParameter(SERVICE, CSW);
        uriBuilder.addParameter(VERSION, Version.Version_202.getVersion());
        uriBuilder.addParameter(REQUEST, GET_RECORDS);
        uriBuilder.addParameter(TYPE_NAMES, request.getTypeNames());
        addBuilderParameter(uriBuilder, NAMESPACE, request.getNamespace());
        addBuilderParameter(uriBuilder, OUTPUT_FORMAT, request.getOutputFormat());
        addBuilderParameter(uriBuilder, OUTPUT_SCHEMA, request.getOutputSchema());
        addBuilderParameter(uriBuilder, ELEMENT_NAME, request.getElementName());
        addBuilderParameter(uriBuilder, ELEMENT_SET_NAME, request.getElementSetName());
        addBuilderParameter(uriBuilder, ELEMENT_SET_NAME_TYPENAME, request.getElementSetNameTypeName());
        addBuilderParameter(uriBuilder, SORT_BY, request.getSortBy());
        addBuilderParameter(uriBuilder, RESPONSE_HANDLER, request.getResponseHandler());

        //mandatory in the case of a distributed search.
        addBuilderParameter(uriBuilder, REQUEST_ID, request.getRequestId());
        addBuilderParameter(uriBuilder, CLIENT_ID, request.getClientId());
        addBuilderParameter(uriBuilder, DISTRIBUTED_SEARCH_ID, request.getDistributedSearchId());

        addBuilderIntParameter(uriBuilder, MAX_RECORDS, request.getMaxRecords());
        addBuilderIntParameter(uriBuilder, HOP_COUNT, request.getHopCount());
        addBuilderIntParameter(uriBuilder, START_POSITION, request.getStartPosition());
        if (request.getDistributedSearch()) {
            uriBuilder.addParameter(DISTRIBUTED_SEARCH, request.getDistributedSearch().toString());
            addBuilderParameter(uriBuilder, FEDERATED_CATALOGUES, request.getFederatedCatalogues());

        }
        return uriBuilder.build();
    }

    /**
     * Gets record by id.
     * The GetRecordById operation returns defailed information for specific metadata records.
     *
     * @param id             the record id
     * @param elementSetName the element set name: “brief”, “summary” or “full”
     * @param outputFormat   the output format, a MIME type, application/xml (and “application/soap+xml” for SOAP).
     * @param outputSchema   the output schema, uri
     * @return the record by id
     * @throws URISyntaxException the uri syntax exception
     */
    public URI getRecordById(String id, String elementSetName, String outputFormat, String outputSchema) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(this.serviceRoot);
        uriBuilder.addParameter(SERVICE, CSW);
        uriBuilder.addParameter(VERSION, Version.Version_202.getVersion());
        uriBuilder.addParameter(REQUEST, GET_RECORD_BY_ID);
        uriBuilder.addParameter("Id", id);
        if (StringUtils.isNotBlank(elementSetName)) {
            uriBuilder.addParameter(ELEMENT_SET_NAME, elementSetName);
        }
        if (StringUtils.isNotBlank(outputFormat)) {
            uriBuilder.addParameter(OUTPUT_FORMAT, outputFormat);
        }
        if (StringUtils.isNotBlank(outputSchema)) {
            uriBuilder.addParameter(OUTPUT_SCHEMA, outputSchema);
        }
        return uriBuilder.build();
    }


}
