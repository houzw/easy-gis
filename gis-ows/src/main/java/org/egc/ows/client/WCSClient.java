package org.egc.ows.client;

import lombok.extern.slf4j.Slf4j;
import net.opengis.wcs.v_1_0_0.CoverageDescription;
import net.opengis.wcs.v_1_0_0.WCSCapabilitiesType;
import net.opengis.wcs.v_1_1.CoverageDescriptions;
import net.opengis.wcs.v_2_0.CapabilitiesType;
import net.opengis.wcs.v_2_0.CoverageDescriptionsType;
import net.opengis.wcs.v_2_0.CoverageSummaryType;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.exception.BusinessException;
import org.egc.commons.web.GeoMimeTypes;
import org.egc.ows.commons.HttpUtils;
import org.egc.ows.request.wcs.GetCoverageRequest10;
import org.egc.ows.request.wcs.GetCoverageRequest20;
import org.egc.ows.request.wcs.WCSRequestBuilder;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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

    private String serviceEndpoint;

    private WCSRequestBuilder requestBuilder;

    public WCSClient(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
        this.requestBuilder = new WCSRequestBuilder(serviceEndpoint);
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

    public List<String> availableConverageIds20(CapabilitiesType capabilities) {
        List<CoverageSummaryType> csts = capabilities.getContents().getCoverageSummary();
        List<String> coverageIds = new ArrayList<>(csts.size());
        for (CoverageSummaryType cst : csts) {
            coverageIds.add(cst.getCoverageId());
        }
        return coverageIds;
    }

    /**
     * Gets coverage 20.
     *
     * @param request      the request
     * @param saveFilePath the save filepath
     * @return the coverage 20
     */
    public String getCoverage20(GetCoverageRequest20 request, String saveFilePath) {
        File saveFile = new File(saveFilePath);
        try {
            URI url = this.requestBuilder.getCoverage20(request);
            System.out.println(url.toString());
            if (saveFile.isDirectory()) {
                return HttpUtils.doGetFile(url, saveFilePath, true);
            } else {
                return HttpUtils.doGetFile(url, saveFilePath, false);
            }
        } catch (URISyntaxException e) {
            throw new BusinessException(e, "URISyntaxException");
        }
    }

    public String getServiceEndpoint() {
        return this.serviceEndpoint;
    }
}
