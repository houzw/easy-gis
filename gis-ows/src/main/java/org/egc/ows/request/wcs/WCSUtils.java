package org.egc.ows.request.wcs;

import net.opengis.wcs.v_1_0_0.WCSCapabilitiesType;
import net.opengis.wcs.v_2_0.CapabilitiesType;

import javax.xml.bind.JAXB;
import java.io.InputStream;

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
}
