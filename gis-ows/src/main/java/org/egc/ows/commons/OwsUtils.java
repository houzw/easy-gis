package org.egc.ows.commons;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.io.StringWriter;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author houzhiwei
 * @date 2020/9/26 14:23
 */
public class OwsUtils {

    private static final Logger log = LoggerFactory.getLogger(OwsUtils.class);

    /**
     * CRS in URN format.
     *
     * @param epsgCode the epsg code (without "EPSG")
     * @return the string
     */
    public static String urnCrs(int epsgCode) {
        return "urn:ogc:def:crs:EPSG::" + epsgCode;
    }

    /**
     * CRS in URN format.
     *
     * @param ogcCrs CRS defined by OGC (essentially WGS84), e.g., "CRS84"
     * @return the string
     */
    public static String urnCrs(String ogcCrs) {
        return "urn:ogc:def:crs:OGC::" + ogcCrs;
    }

    public static String utcDatetimeStr(Date date) {
        return DateTimeFormatter.ofPattern(DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.getPattern())
                .withZone(ZoneOffset.UTC)
                .format(date.toInstant());
    }

    /**
     * Java POJO 2 xml string.
     *
     * @param <T>   the type parameter, for {@code JAXBElement<T>}
     * @param clazz the clazz
     * @param value the value
     * @param qName the qname, e.g., {@code new QName("http://www.opengis.net/wcs/2.0", "GetCoverage");}
     * @return the string
     * @throws JAXBException the jaxb exception
     */
    public static <T> String java2XmlStr(Class<T> clazz, T value, QName qName) throws JAXBException {
        StringWriter writer = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(clazz);
        Marshaller m = context.createMarshaller();
        JAXBElement<T> root = new JAXBElement<>(qName, clazz, value);
        m.marshal(root, writer);
        return writer.toString();
    }

}
