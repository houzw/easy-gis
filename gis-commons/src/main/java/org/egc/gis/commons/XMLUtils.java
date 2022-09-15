package org.egc.gis.commons;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * description:
 * @see cn.hutool.core.util.XmlUtil
 * @author houzhiwei
 * @date 2022/9/11/0011 9:46
 */
@Slf4j
public class XMLUtils {

    public static String doc2Str(Document xmlDoc) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            // below code to remove XML declaration
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(xmlDoc), new StreamResult(writer));
            return writer.getBuffer().toString();
        } catch (TransformerException e) {
            e.printStackTrace();
            log.error(e.getLocalizedMessage(), e);
        }

        return null;
    }

    public static Document str2Doc(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlStr)));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    private static Document xmlFile2XmlDoc(String filePath) {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();
            //Parse the content to Document object
            return builder.parse(new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    private static void writeXmlDoc2File(Document xmlDocument, String fileName) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();

            //Uncomment if you do not require XML declaration
            //transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

            FileOutputStream outStream = new FileOutputStream(new File(fileName));
            transformer.transform(new DOMSource(xmlDocument), new StreamResult(outStream));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getLocalizedMessage(), e);
        }
    }
}
