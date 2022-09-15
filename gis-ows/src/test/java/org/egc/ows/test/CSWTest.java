package org.egc.ows.test;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.opengis.csw.v_2_0_2.CapabilitiesType;
import net.opengis.csw.v_2_0_2.GetRecordsResponseType;
import org.egc.ows.client.CSWClient;
import org.egc.ows.client.OWSClient;
import org.egc.ows.commons.Consts;
import org.egc.ows.request.csw.GetRecordsRequest;
import org.junit.Test;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

/**
 * @author houzhiwei
 * @date 2021/7/8 14:20
 */
@Slf4j
public class CSWTest {

    @Test
    public void testGetCaps() {
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
    public void testGetRecords() {
        CSWClient cswClient = OWSClient.createCSWClient("https://sdi.eea.europa.eu/catalogue/srv/eng/csw");
        GetRecordsRequest.Builder builder = new GetRecordsRequest.Builder();
        builder.namespace("xmlns(csw=http://www.opengis.net/cat/csw)").outputSchema(Consts.GMD_SCHEMA_2005)
                .typeNames(CSWClient.DEFAULT_TYPE_NAME).elementSetName("full").maxRecords(20)
                .resultType(Consts.RESULT_TYPE_RESULTS)
                .constraintLanguage(CSWClient.CONSTRAINT_LANGUAGE_CQL_TEXT).constraintLanguageVersion("1.1.0");

        GetRecordsResponseType records = cswClient.getRecords(builder.build());
        String s = JSON.toJSONString(records);
        System.out.println(s);
    }

    @Test
    public void csw2geodcat() {
        String xsltFile = "E:/workspace/iso-19139-to-dcat-ap/iso-19139-to-dcat-ap.xsl";
    }

    public void transformContentByXslt(String xmlStr, String xsltFile) {
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Templates cachedXslt = tf.newTemplates(new StreamSource(xsltFile));
            // 获取转换器对象实例
            Transformer transformer = cachedXslt.newTransformer();
            //transformer.setParameter("baseUrl", "getBaseUrl");
            // 进行转换
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            transformer.transform(new StreamSource(new StringReader(xmlStr)),
                    new StreamResult(outputStream));
            System.out.println(outputStream.toString());
//            transformer.transform(new StreamSource(new StringReader(xmlStr)),
//                    new StreamResult(new FileOutputStream(dstXml)));
        } catch (TransformerException e) {
            log.error("Cannot transform the xml using the given xslt", e);
        }
    }

}
