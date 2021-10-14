package org.egc.ows.test;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.opengis.csw.v_2_0_2.CapabilitiesType;
import net.opengis.csw.v_2_0_2.GetRecordsResponseType;
import org.egc.ows.client.CSWClient;
import org.egc.ows.client.OWSClient;
import org.egc.ows.request.csw.GetRecordsRequest;
import org.junit.Test;

/**
 * @author houzhiwei
 * @date 2021/7/8 14:20
 */
@Slf4j
public class CSWTest {

    @Test
    public void testGetCaps(){
        CSWClient cswClient = OWSClient.createCSWClient("https://sdi.eea.europa.eu/catalogue/srv/eng/csw");
        CapabilitiesType capabilities = cswClient.getCapabilities();
        String s = JSON.toJSONString(capabilities);
        System.out.println(s);
    }

//    http://catalog.data.gov/csw?service=CSW&version=2.0.2&request=DescribeRecord
//    http://catalog.data.gov/csw?service=CSW&version=2.0.2&request=GetDomain&propertyname=dc:type

//    http://catalog.data.gov/csw?service=CSW&version=2.0.2&request=GetDomain&parametername=GetRecords.outputSchema
//    http://catalog.data.gov/csw?service=CSW&version=2.0.2&request=GetRecordById&id=16bbf4f8-8e88-45c6-a76b-6af51b2b3555&elementsetname=full

//    HTTP GET (ISO 19139): http://catalog.data.gov/csw?service=CSW&version=2.0.2&request=GetRecordById&id=16bbf4f8-8e88-45c6-a76b-6af51b2b3555&elementsetname=full&outputSchema=http://www.isotc211.org/2005/gmd

//    HTTP GET (ISO 19139 brief): http://catalog.data.gov/csw?service=CSW&version=2.0.2&request=GetRecordById&id=16bbf4f8-8e88-45c6-a76b-6af51b2b3555&elementsetname=brief&outputSchema=http://www.isotc211.org/2005/gmd
    @Test
    public void testGetRecords(){
        CSWClient cswClient = OWSClient.createCSWClient("https://sdi.eea.europa.eu/catalogue/srv/eng/csw");
        GetRecordsRequest.Builder builder = new GetRecordsRequest.Builder();
        builder.namespace("xmlns(csw=http://www.opengis.net/cat/csw)").outputSchema("http://www.isotc211.org/2005/gmd")
                .typeNames("csw:Record").elementSetName("full").maxRecords(20)
                .resultType("results")
        .constraintLanguage("CQL_TEXT").constraintLanguageVersion("1.1.0");
        GetRecordsResponseType records = cswClient.getRecords(builder.build());
        String s = JSON.toJSONString(records);
        System.out.println(s);
    }
}
