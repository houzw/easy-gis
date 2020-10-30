package org.egc.ows.request.wcs;

import net.opengis.wcs.v_1_0_0.GetCapabilities;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.egc.ows.commons.FormatConsts;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.egc.ows.commons.Consts.*;

/**
 * The type Wcs request builder.
 *
 * @author houzhiwei
 * @date 2020 /10/2 19:35
 */
public class WCSRequestBuilder {

    public static final String DESCRIBE_COVERAGE = "DescribeCoverage";
    public static final String GET_COVERAGE = "GetCoverage";
    public static final String WCS = "WCS";
    public static final String IDENTIFIERS = "IDENTIFIERS";
    public static final String IDENTIFIER = "IDENTIFIER";
    public static final String COVERAGE_ID = "COVERAGEID";
    public static final String COVERAGE = "COVERAGE";
    public static final String RES_X = "RESX";
    public static final String RES_Y = "RESY";
    public static final String RESPONSE_CRS = "response_crs";
    public static final String SUBSETTING_CRS = "SUBSETTINGCRS";
    public static final String OUTPUT_CRS = "OUTPUTCRS";
    public static final String SUBSET = "SUBSET";
    public static final String SCALE_FACTOR = "SCALEFACTOR";
    public static final String INTERPOLATION = "INTERPOLATION";
    public static final String GRID_ORIGIN = "GridOrigin";
    public static final String GRID_OFFSETS = "GridOffsets";
    public static final String GRID_TYPE = "GridType";
    public static final String GRID_BASE_CRS = "GridBaseCRS";
    public static final String GRID_CS = "GridCS";
    public static final String RANGE_SUBSET = "RangeSubset";


    private String serviceRoot;

    public WCSRequestBuilder(String serviceRoot) {
        this.serviceRoot = serviceRoot;
    }

    public enum Version {
        Version_100("1.0.0"),
        Version_110("1.1.0"),
        Version_111("1.1.1"),
        Version_112("1.1.2"),
        Version_201("2.0.1");

        public String getVersion() {
            return version;
        }

        private String version;

        Version(String version) {
            this.version = version;
        }
    }

    /*
     * e.g., http://modwebsrv.modaps.eosdis.nasa.gov/wcs/5/MOD09/
     */

    public URI getCapabilities(WCSRequestBuilder.Version version) throws URISyntaxException {
        return getCapabilities(version, null);
    }

    public URI getCapabilities(WCSRequestBuilder.Version version, String section) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(this.serviceRoot);
        uriBuilder.addParameter(SERVICE, WCS);
        uriBuilder.addParameter(VERSION, version.getVersion());
        uriBuilder.addParameter(REQUEST, GET_CAPABILITIES);
        if (StringUtils.isNotBlank(section)) {
            uriBuilder.addParameter("Section", section);
        }
        return uriBuilder.build();
    }


    /**
     * Create a 100 getCapabilities request.
     * xml 请求形式
     *
     * @return the getCapabilities xml string
     */
    public String GetCapabilities10() throws JAXBException {
        net.opengis.wcs.v_1_0_0.ObjectFactory of = new net.opengis.wcs.v_1_0_0.ObjectFactory();
        GetCapabilities getCapabilities = of.createGetCapabilities();
        // serialise to xml
        StringWriter writer = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(net.opengis.wcs.v_1_0_0.GetCapabilities.class);
        Marshaller m = context.createMarshaller();
        QName qName = new QName("http://www.opengis.net/wcs/1.0.0", "GetCapabilities");
        JAXBElement<GetCapabilities> root = new JAXBElement<>(qName, GetCapabilities.class, getCapabilities);
        m.marshal(root, writer);
        return writer.toString();
    }

    /*
     * WCS 1.0.0: http://gisserver.domain.com:6080/arcgis/services/World/Temperature/ImageServer/WCSServer?
     * SERVICE=WCS
     * &VERSION=1.0.0
     * &REQUEST=DescribeCoverage
     * &COVERAGE=1
     *
     */

    public URI describeCoverage10(List<String> coverage) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(this.serviceRoot);
        uriBuilder.addParameter(SERVICE, WCS);
        uriBuilder.addParameter(VERSION, WCSRequestBuilder.Version.Version_100.getVersion());
        uriBuilder.addParameter(REQUEST, DESCRIBE_COVERAGE);
        uriBuilder.addParameter(COVERAGE, String.join(",", coverage));
        return uriBuilder.build();
    }

    public URI describeCoverage11(List<String> identifies) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(this.serviceRoot);
        uriBuilder.addParameter(SERVICE, WCS);
        uriBuilder.addParameter(VERSION, WCSRequestBuilder.Version.Version_110.getVersion());
        uriBuilder.addParameter(REQUEST, DESCRIBE_COVERAGE);
        uriBuilder.addParameter(IDENTIFIERS, String.join(",", identifies));
        return uriBuilder.build();
    }

    public URI describeCoverage20(List<String> coverageIds) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(this.serviceRoot);
        uriBuilder.addParameter(SERVICE, WCS);
        uriBuilder.addParameter(VERSION, WCSRequestBuilder.Version.Version_201.getVersion());
        uriBuilder.addParameter(REQUEST, DESCRIBE_COVERAGE);
        uriBuilder.addParameter(COVERAGE_ID, String.join(",", coverageIds));
        return uriBuilder.build();
    }

    public URI getCoverage10(GetCoverageRequest10 request) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(this.serviceRoot);
        uriBuilder.addParameter(SERVICE, WCS);
        uriBuilder.addParameter(VERSION, WCSRequestBuilder.Version.Version_100.getVersion());
        uriBuilder.addParameter(REQUEST, GET_COVERAGE);
        uriBuilder.addParameter(COVERAGE, request.getCoverage());
        uriBuilder.addParameter(CRS, request.getCrs());
        uriBuilder.addParameter(BBOX, request.getBbox());
        if (StringUtils.isNotBlank(request.getTime())) {
            uriBuilder.addParameter(TIME, request.getTime());
        }
        if (StringUtils.isNotBlank(request.getResponseCrs())) {
            uriBuilder.addParameter(RESPONSE_CRS, request.getResponseCrs());
        }
        if (StringUtils.isNotBlank(request.getFormat())) {
            uriBuilder.addParameter(FORMAT, request.getFormat());
        } else {
            uriBuilder.addParameter(FORMAT, "GTiff");
        }
        if (request.getHeight() > 0) {
            uriBuilder.addParameter(HEIGHT, request.getHeight() + "");
        }
        if (request.getWidth() > 0) {
            uriBuilder.addParameter(WIDTH, request.getWidth() + "");
        }
        if (request.getResx() > 0) {
            uriBuilder.addParameter(RES_X, request.getResx() + "");
        }
        if (request.getResy() > 0) {
            uriBuilder.addParameter(RES_Y, request.getResy() + "");
        }
        return uriBuilder.build();
    }

    //http://194.66.252.155/geoserver/OneGDev/wcs?
    // service=WCS
    // &version=2.0.1
    // &CoverageId=OneGDev__GreaterNorthSea-MCol
    // &request=GetCoverage
    // &format=image/png
    // &subset=Lat(50.14199,54.36074),Long(7.14519,13.03386)&=

    //http://ows.eox.at/cite/mapserver?
    // service=wcs
    // &version=2.0.1
    // &request=getcoverage
    // &coverageid=MER_FRS_1PNUPA20090701_124435_000005122080_00224_38354_6861_RGB

    public URI getCoverage20(GetCoverageRequest20 request) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(this.serviceRoot);
        uriBuilder.addParameter(SERVICE, WCS);
        uriBuilder.addParameter(VERSION, WCSRequestBuilder.Version.Version_201.getVersion());
        uriBuilder.addParameter(REQUEST, GET_COVERAGE);
        uriBuilder.addParameter(COVERAGE_ID, request.getCoverageId());
        if (StringUtils.isNotBlank(request.getFormat())) {
            uriBuilder.addParameter(FORMAT, request.getFormat());
        } else {
            uriBuilder.addParameter(FORMAT, FormatConsts.MediaType_GeoTIFF);
        }
        List<NameValuePair> kvps = new ArrayList<>();
        kvps.add(new BasicNameValuePair(MAP, request.getMapserverMap()));
        kvps.add(new BasicNameValuePair(SUBSETTING_CRS, request.getSubSettingCrs()));
        kvps.add(new BasicNameValuePair(MEDIATYPE, request.getMediaType()));
        kvps.add(new BasicNameValuePair(SUBSET, request.getSubsetX()));
        kvps.add(new BasicNameValuePair(SUBSET, request.getSubsetY()));
        kvps.add(new BasicNameValuePair(SUBSET, request.getSubsetDateTime()));
        kvps.add(new BasicNameValuePair(OUTPUT_CRS, request.getOutputCrs()));
        kvps.add(new BasicNameValuePair(INTERPOLATION, request.getInterpolation()));
        List<NameValuePair> notBlankKvps = kvps.stream().filter(kvp -> StringUtils.isNotBlank(kvp.getValue()))
                .collect(Collectors.toList());
        uriBuilder.addParameters(notBlankKvps);
        return uriBuilder.build();
    }

   /*
   public GetCoverageType getCoverageType20(GetCoverageRequest20 request) {
        ObjectFactory factory = new ObjectFactory();
        GetCoverageType getCoverageType = factory.createGetCoverageType();
        getCoverageType.setCoverageId(request.getCoverageId());
        getCoverageType.setFormat(request.getFormat());
        return getCoverageType;
    }
    */
}
