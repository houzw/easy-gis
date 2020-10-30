package org.egc.ows.client;

import lombok.extern.slf4j.Slf4j;
import net.opengis.gml.profiles.gml4wcs.v_1_0_0.TimePositionType;
import net.opengis.ows.v_2_0.BoundingBoxType;
import net.opengis.ows.v_2_0.LanguageStringType;
import net.opengis.wcs.v_1_0_0.CoverageDescription;
import net.opengis.wcs.v_1_0_0.CoverageOfferingBriefType;
import net.opengis.wcs.v_1_0_0.LonLatEnvelopeType;
import net.opengis.wcs.v_1_0_0.WCSCapabilitiesType;
import net.opengis.wcs.v_1_1.CoverageDescriptions;
import net.opengis.wcs.v_2_0.CapabilitiesType;
import net.opengis.wcs.v_2_0.CoverageDescriptionsType;
import net.opengis.wcs.v_2_0.CoverageSummaryType;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.exception.BusinessException;
import org.egc.commons.web.GeoMimeTypes;
import org.egc.ows.commons.CoverageInfo;
import org.egc.ows.commons.HttpUtils;
import org.egc.ows.request.wcs.GetCoverageRequest10;
import org.egc.ows.request.wcs.GetCoverageRequest20;
import org.egc.ows.request.wcs.WCSRequestBuilder;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Wcs client.
 *
 * @author houzhiwei
 * @date 2020 /8/25 19:28
 */
@Slf4j
public class WCSClient {

    /*
     * 因为请求参数为 kvp （key-value-pair），因此请求参数方式应当是 Get
     * */

    private String serviceRoot;

    private WCSRequestBuilder requestBuilder;

    public WCSClient(String serviceRoot) {
        this.serviceRoot = serviceRoot;
        this.requestBuilder = new WCSRequestBuilder(serviceRoot);
    }

    public net.opengis.wcs.v_1_0_0.WCSCapabilitiesType getCapabilities10() {
        try {
            URI url = this.requestBuilder.getCapabilities(WCSRequestBuilder.Version.Version_100);
            String s = HttpUtils.doGetString(url);
            assert s != null;
            return JAXB.unmarshal(new StringReader(s), WCSCapabilitiesType.class);
        } catch (URISyntaxException e) {
            throw new BusinessException(e, "URISyntaxException");
        }
    }

    public List<CoverageInfo> getCoveragesInfo10(net.opengis.wcs.v_1_0_0.WCSCapabilitiesType capabilities) {
        List<CoverageOfferingBriefType> cobs = capabilities.getContentMetadata().getCoverageOfferingBrief();
        List<CoverageInfo> coverageInfoList = new ArrayList<>(cobs.size());
        for (CoverageOfferingBriefType cob : cobs) {
            CoverageInfo coverageInfo = new CoverageInfo();
            coverageInfo.setCoverageId(cob.getWcsName());
            coverageInfo.setTitle(cob.getLabel());
            LonLatEnvelopeType lonLatEnvelope = cob.getLonLatEnvelope();
            coverageInfo.setSrsName(lonLatEnvelope.getSrsName());
            List<String> timePositions = lonLatEnvelope.getTimePosition()
                    .stream().map(TimePositionType::getValue).collect(Collectors.toList());
            coverageInfo.setTimePositions(timePositions);
            coverageInfo.setLowerCorner(lonLatEnvelope.getPos().get(0).getValue().get(0), lonLatEnvelope.getPos().get(0).getValue().get(1));
            coverageInfo.setUpperCorner(lonLatEnvelope.getPos().get(1).getValue().get(0), lonLatEnvelope.getPos().get(1).getValue().get(1));
            coverageInfoList.add(coverageInfo);
        }
        return coverageInfoList;
    }

    public net.opengis.wcs.v_1_1.Capabilities getCapabilities11() {
        try {
            URI url = this.requestBuilder.getCapabilities(WCSRequestBuilder.Version.Version_110);
            String s = HttpUtils.doGetString(url);
            assert s != null;
            return JAXB.unmarshal(new StringReader(s), net.opengis.wcs.v_1_1.Capabilities.class);
        } catch (URISyntaxException e) {
            throw new BusinessException(e, "URISyntaxException");
        }
    }

    public List<CoverageInfo> getCoveragesInfo20(CapabilitiesType capabilities) {
        List<CoverageSummaryType> csts = capabilities.getContents().getCoverageSummary();
        List<CoverageInfo> coverageInfoList = new ArrayList<>(csts.size());
        for (CoverageSummaryType cst : csts) {
            CoverageInfo coverageInfo = new CoverageInfo();
            coverageInfo.setCoverageId(cst.getCoverageId());
            coverageInfo.setTitle(cst.getTitle().get(0).getValue());
            List<LanguageStringType> keywords = cst.getKeywords().get(0).getKeyword();
            coverageInfo.setKeyworks(keywords.stream().map(LanguageStringType::getValue).collect(Collectors.toList()));
            BoundingBoxType bbox = (BoundingBoxType) cst.getBoundingBox().get(0).getValue();
            coverageInfo.setSrsName(bbox.getCrs());
            /*List<String> timePositions = lonLatEnvelope.getTimePosition()
                    .stream().map(TimePositionType::getValue).collect(Collectors.toList());
            coverageInfo.setTimePositions(timePositions);*/
            coverageInfo.setWgs84LowerCorner(cst.getWGS84BoundingBox().get(0).getLowerCorner().get(0), cst.getWGS84BoundingBox().get(0).getLowerCorner().get(1));
            coverageInfo.setWgs84UpperCorner(cst.getWGS84BoundingBox().get(0).getUpperCorner().get(0), cst.getWGS84BoundingBox().get(0).getUpperCorner().get(1));
            coverageInfo.setLowerCorner(bbox.getLowerCorner().get(0), bbox.getLowerCorner().get(1));
            coverageInfo.setUpperCorner(bbox.getUpperCorner().get(0), bbox.getUpperCorner().get(1));
            coverageInfoList.add(coverageInfo);
        }
        return coverageInfoList;
    }

    public CapabilitiesType getCapabilities20() {
        try {
            URI url = this.requestBuilder.getCapabilities(WCSRequestBuilder.Version.Version_201);
            String s = HttpUtils.doGetString(url);
            assert s != null;
            return JAXB.unmarshal(new StringReader(s), CapabilitiesType.class);
        } catch (URISyntaxException e) {
            throw new BusinessException(e, "URISyntaxException");
        }
    }


    /**
     * Describe coverage 10 coverage description.
     *
     * @param coverages the coverages, could use Lists.newArrayList(coverage)
     * @return the coverage description
     */
    public CoverageDescription describeCoverage10(List<String> coverages) {
        try {
            URI url = this.requestBuilder.describeCoverage10(coverages);
            String s = HttpUtils.doGetString(url);
            assert s != null;
            return JAXB.unmarshal(new StringReader(s), CoverageDescription.class);
        } catch (URISyntaxException e) {
            throw new BusinessException(e, "URISyntaxException");
        }
    }


    public CoverageDescriptions describeCoverage11(List<String> identifies) {
        try {
            URI url = this.requestBuilder.describeCoverage11(identifies);
            String s = HttpUtils.doPostString(url);
            assert s != null;
            return JAXB.unmarshal(new StringReader(s), CoverageDescriptions.class);
        } catch (URISyntaxException e) {
            throw new BusinessException(e, "URISyntaxException");
        }
    }

    public CoverageDescriptionsType describeCoverage20(List<String> coverageIds) {
        try {
            URI url = this.requestBuilder.describeCoverage20(coverageIds);
            String s = HttpUtils.doPostString(url);
            assert s != null;
            return JAXB.unmarshal(new StringReader(s), CoverageDescriptionsType.class);
        } catch (URISyntaxException e) {
            throw new BusinessException(e, "URISyntaxException");
        }
    }

    public void getCoverage10(GetCoverageRequest10 getCoverage10, String saveFilename) {
        try {
            URI uri = this.requestBuilder.getCoverage10(getCoverage10);
            saveFilename = HttpUtils.doGetFile(uri, saveFilename, StringUtils.isBlank(saveFilename));
            if (saveFilename != null) {
                log.info("Coverage saved to {}", saveFilename);
            } else {
                log.error("GetCoverage failed");
            }
        } catch (URISyntaxException e) {
            throw new BusinessException(e, "URISyntaxException");
        }
    }

    /**
     * Gets coverage 20.
     *
     * @param coverageId     the coverage id
     * @param formatMimeType the format mime type, use {@link GeoMimeTypes}
     * @param saveFilepath   the save filepath
     * @return the coverage 20
     */
    public String getCoverage20(String coverageId, String formatMimeType, String saveFilepath) {
        try {
            GetCoverageRequest20.Builder builder = new GetCoverageRequest20.Builder(coverageId, formatMimeType);
            URI url = this.requestBuilder.getCoverage20(builder.build());
            return HttpUtils.doGetFile(url, saveFilepath, false);
        } catch (URISyntaxException e) {
            throw new BusinessException(e, "URISyntaxException");
        }
    }

    /**
     * Gets coverage 20.
     *
     * @param request      the request
     * @param saveFilepath the save filepath
     * @return the coverage 20
     */
    public String getCoverage20(GetCoverageRequest20 request, String saveFilepath) {
        try {
            URI url = this.requestBuilder.getCoverage20(request);
            System.out.println(url.toString());
            return HttpUtils.doGetFile(url, saveFilepath, false);
        } catch (URISyntaxException e) {
            throw new BusinessException(e, "URISyntaxException");
        }
    }

    public String getServiceRoot() {
        return this.serviceRoot;
    }
}
