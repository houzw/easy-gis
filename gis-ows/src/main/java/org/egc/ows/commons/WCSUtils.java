package org.egc.ows.commons;

import net.opengis.gml.profiles.gml4wcs.v_1_0_0.TimePositionType;
import net.opengis.ows.v_2_0.BoundingBoxType;
import net.opengis.ows.v_2_0.LanguageStringType;
import net.opengis.wcs.v_1_0_0.CoverageOfferingBriefType;
import net.opengis.wcs.v_1_0_0.LonLatEnvelopeType;
import net.opengis.wcs.v_1_0_0.WCSCapabilitiesType;
import net.opengis.wcs.v_2_0.CapabilitiesType;
import net.opengis.wcs.v_2_0.CoverageSummaryType;

import javax.xml.bind.JAXB;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author houzhiwei
 * @date 2020/10/15 15:14
 */
public class WCSUtils {

    public static CapabilitiesType parse201Capabilities(InputStream xmlFile) {
        CapabilitiesType ca = null;
        try {
            ca = JAXB.unmarshal(xmlFile, CapabilitiesType.class);
        } catch (Exception e) {
            throw new RuntimeException("Fail to parse the capabilities document. " + e.getLocalizedMessage());
        }
        return ca;
    }

    public static WCSCapabilitiesType parse100Capabilities(InputStream xmlFile) {
        WCSCapabilitiesType ca = null;
        try {
            ca = JAXB.unmarshal(xmlFile, WCSCapabilitiesType.class);
        } catch (Exception e) {
            throw new RuntimeException("Fail to parse the capabilities document. " + e.getLocalizedMessage());
        }
        return ca;
    }

    public static List<CoverageInfo> getCoveragesInfo20(CapabilitiesType capabilities) {
        List<CoverageSummaryType> csts = capabilities.getContents().getCoverageSummary();
        List<CoverageInfo> coverageInfoList = new ArrayList<>(csts.size());
        for (CoverageSummaryType cst : csts) {
            CoverageInfo coverageInfo = new CoverageInfo();
            coverageInfo.setCoverageId(cst.getCoverageId());
            coverageInfo.setTitle(cst.getTitle().size() == 0 ? "" : cst.getTitle().get(0).getValue());
            List<LanguageStringType> keywords = cst.getKeywords().size() > 0 ? cst.getKeywords().get(0).getKeyword() : null;
            if (keywords != null) {
                coverageInfo.setKeywords(keywords.stream().map(LanguageStringType::getValue).collect(Collectors.toList()));
            }
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

    public static List<CoverageInfo> getCoveragesInfo10(net.opengis.wcs.v_1_0_0.WCSCapabilitiesType capabilities) {
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
}
