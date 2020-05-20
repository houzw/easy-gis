package org.egc.gis.geotools.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * reference org.geotools.process.spatialstatistics.core.FeatureTypes
 *
 * @author houzhiwei
 * @date 2020/5/19 19:53
 */
@Slf4j
public class SimpleFeatureTypes {

    /**
     * <pre>
     * 定义图形信息和属性信息
     * Create default simple feature type.
     * the field name is the_geom, CRS is WGS84
     * @param geomType the geom clazz
     * @param typeName the type name, set as "shapefile" if null
     * @return the simple feature type
     */
    public static SimpleFeatureType createDefaultType(@NotNull Class<?> geomType, String typeName) {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setCRS(DefaultGeographicCRS.WGS84);
        typeName = Optional.ofNullable(typeName).orElse("shapefile");
        tb.setName(typeName);
        if (geomType.isAssignableFrom(Polygon.class)) {
            geomType = MultiPolygon.class;
        } else if (geomType.isAssignableFrom(LineString.class)) {
            geomType = MultiLineString.class;
        }
        tb.add("the_geom", geomType);
        return tb.buildFeatureType();
    }

    /**
     * @param typeName e.g., "Location",
     * @param typeSpec "geom:Point,name:String,age:Integer,description:String" or
     *                 "the_geom:Point:srid=4326,name:String,number:Integer"
     * @throws SchemaException
     */
    public static SimpleFeatureType createFeatureType(String typeName, Map<String, String> typeSpec) throws SchemaException {
        String typeSpecStr = typeSpecStr(typeSpec);
        return DataUtilities.createType(typeName, typeSpecStr);
    }

    public static SimpleFeatureType add(SimpleFeatureType schema, AttributeDescriptor descriptor) {
        if (existProeprty(schema, descriptor.getLocalName())) {
            return schema;
        }

        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.init(schema);
        typeBuilder.add(descriptor);
        return typeBuilder.buildFeatureType();
    }

    public static SimpleFeatureType add(SimpleFeatureType schema, String propertyName, Class<?> attributeBinding) {
        return add(schema, propertyName, attributeBinding, 0);
    }

    public static boolean existProeprty(SimpleFeatureType schema, String propertyName) {
        propertyName = validateProperty(schema, propertyName);
        return schema.indexOf(propertyName) != -1;
    }

    public static String validateProperty(SimpleFeatureType schema, String propertyName) {
        if (StringUtils.isBlank(propertyName)) {
            return null;
        }

        for (AttributeDescriptor descriptor : schema.getAttributeDescriptors()) {
            if (descriptor.getLocalName().equalsIgnoreCase(propertyName)) {
                return descriptor.getLocalName();
            }
        }
        return propertyName;
    }

    public static SimpleFeatureType add(SimpleFeatureType schema, String propertyName, Class<?> attributeBinding, int length) {
        if (existProeprty(schema, propertyName)) {
            return schema;
        }
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.init(schema);
        if (attributeBinding.isAssignableFrom(String.class)) {
            if (length == 0) {
                length = 255; // string default length
            }
            typeBuilder.length(length).add(propertyName, attributeBinding);
        } else {
            typeBuilder.add(propertyName, attributeBinding);
        }
        return typeBuilder.buildFeatureType();
    }

    /**
     * Create simple feature type.
     *
     * @param typeName e.g., "Location",
     * @param crs      the crs, set to WGS84 if null
     * @param geomType the the_geom type, e.g, org.locationtech.jts.geom.Point.class
     * @param typeSpec e.g., name:String.class  age:Integer.class
     * @return the simple feature type
     */
    public static SimpleFeatureType createFeatureType(String typeName, CoordinateReferenceSystem crs, Class<?> geomType, Map<String, Class<?>> typeSpec) {
        return createFeatureType(typeName, crs, geomType, null, typeSpec);
    }

    public static SimpleFeatureType createFeatureType(SimpleFeatureType type, String typeName, Class<?> geomType, CoordinateReferenceSystem crs) {
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        String namespaceURI = type.getName().getNamespaceURI();
        if (namespaceURI != null) {
            typeBuilder.setNamespaceURI(namespaceURI);
        }
        typeBuilder.setName(typeName);
        typeBuilder.setCRS(crs);
        for (AttributeDescriptor descriptor : type.getAttributeDescriptors()) {
            if (descriptor instanceof GeometryDescriptor) {
                GeometryDescriptor geomDesc = (GeometryDescriptor) descriptor;
                if (geomType.isAssignableFrom(Polygon.class)) {
                    geomType = MultiPolygon.class;
                } else if (geomType.isAssignableFrom(LineString.class)) {
                    geomType = MultiLineString.class;
                }
                typeBuilder.add(geomDesc.getLocalName(), geomType, crs);
            } else {
                typeBuilder.add(descriptor);
            }
        }
        return typeBuilder.buildFeatureType();
    }

    public static SimpleFeatureType createFeatureType(String typeName, CoordinateReferenceSystem crs, Class<?> geomType, String fieldName, Map<String, Class<?>> typeSpec) {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setCRS(Optional.ofNullable(crs).orElse(DefaultGeographicCRS.WGS84));
        tb.setName(typeName);
        if (geomType.isAssignableFrom(Polygon.class)) {
            geomType = MultiPolygon.class;
        } else if (geomType.isAssignableFrom(LineString.class)) {
            geomType = MultiLineString.class;
        }
        tb.add(Optional.ofNullable(fieldName).orElse("the_geom"), geomType);
        if (typeSpec != null) {
            for (Map.Entry<String, Class<?>> next : typeSpec.entrySet()) {
                tb.add(next.getKey(), next.getValue());
            }
        }
        return tb.buildFeatureType();
    }

    private static String typeSpecStr(Map<String, String> typeSpec) {
        Iterator<Map.Entry<String, String>> iterator = typeSpec.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            sb.append(next.getKey()).append(":").append(next.getValue()).append(",");
        }
        return StringUtils.removeEnd(sb.toString(), ",");
    }

}
