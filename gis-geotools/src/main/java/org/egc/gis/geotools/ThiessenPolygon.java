package org.egc.gis.geotools;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.egc.gis.geotools.utils.SimpleFeatureTypes;
import org.egc.gis.geotools.utils.VectorUtils;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.geom.prep.PreparedGeometryFactory;
import org.locationtech.jts.index.strtree.ItemBoundable;
import org.locationtech.jts.index.strtree.ItemDistance;
import org.locationtech.jts.index.strtree.STRtree;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO test
 * <pre>
 * Thiessen Polygon / Voronoi diagram
 * modified from org.geotools.process.spatialstatistics.operations
 * @author houzhiwei
 * @date 2020/5/17 18:15
 */
@Slf4j
@Data
public class ThiessenPolygon {

    public static final String TYPE_NAME = "ThiessenPolygon";

    static final String FID_FIELD = "FID";

    private double proximalTolerance = 0d;

    private Geometry clipArea = null;

    final GeometryFactory gf = JTSFactoryFinder.getGeometryFactory(null);

    public enum AttributeMode {
        /**
         * Only the FID field from the input features will be transferred to the output feature class. This is the default.
         */
        OnlyFID,
        /**
         * All attributes from the input features will be transferred to the output feature class.
         */
        All
    }

    public SimpleFeatureCollection buildThiessenPolygon(SimpleFeatureCollection pointFeatures, AttributeMode attributeMode) {
        SimpleFeatureType pointSchema = pointFeatures.getSchema();
        CoordinateReferenceSystem crs = pointSchema.getCoordinateReferenceSystem();

        // adjust extent
        List<Coordinate> coordinateList = VectorUtils.getCoordinateList(pointFeatures);

        Geometry clipPolygon = clipArea;
        ReferencedEnvelope clipEnvelope = pointFeatures.getBounds();
        if (clipArea == null) {
            double deltaX = clipEnvelope.getWidth() * 0.2;
            double deltaY = clipEnvelope.getHeight() * 0.2;
            clipEnvelope.expandBy(deltaX, deltaY);
            clipPolygon = gf.toGeometry(clipEnvelope);
        } else {
            clipEnvelope = new ReferencedEnvelope(clipArea.getEnvelopeInternal(), crs);
        }
        // fast test
        PreparedGeometry praparedGeom = PreparedGeometryFactory.prepare(clipPolygon);

        // create voronoi diagram
        VoronoiDiagramBuilder vdBuilder = new VoronoiDiagramBuilder();
        vdBuilder.setClipEnvelope(clipEnvelope);
        vdBuilder.setSites(coordinateList);
        vdBuilder.setTolerance(proximalTolerance);

        Geometry thiessenGeoms = vdBuilder.getDiagram(gf);
        coordinateList.clear();

        STRtree spatialIndex = new STRtree();
        for (int k = 0; k < thiessenGeoms.getNumGeometries(); k++) {
            Geometry geometry = thiessenGeoms.getGeometryN(k);
            spatialIndex.insert(geometry.getEnvelopeInternal(), geometry);
        }

        SimpleFeatureType featureType = null;
        switch (attributeMode) {
            case OnlyFID:
                String shapeFieldName = pointSchema.getGeometryDescriptor().getLocalName();
                featureType = SimpleFeatureTypes.createFeatureType(pointSchema.getTypeName(), crs, Polygon.class, shapeFieldName, null);
                break;
            case All:
                featureType = SimpleFeatureTypes.createFeatureType(pointSchema, pointSchema.getTypeName(), Polygon.class, pointSchema.getCoordinateReferenceSystem());
                break;
        }
        featureType = SimpleFeatureTypes.add(featureType, FID_FIELD, Integer.class);

        SimpleFeatureIterator featureIter = pointFeatures.features();

        int fid = 0;
        List<SimpleFeature> simpleFeatures = new ArrayList<>();
        while (featureIter.hasNext()) {
            SimpleFeature feature = featureIter.next();
            Geometry geometry = (Geometry) feature.getDefaultGeometry();
            Point centroid = geometry.getCentroid();

            // get polygon
            Geometry voronoiPolygon = (Geometry) spatialIndex.nearestNeighbour(
                    geometry.getEnvelopeInternal(), geometry, new ItemDistance() {
                        @Override
                        public double distance(ItemBoundable item1, ItemBoundable item2) {
                            Geometry s1 = (Geometry) item1.getItem();
                            Geometry s2 = (Geometry) item2.getItem();
                            return s1.distance(s2);
                        }
                    });
            if (voronoiPolygon.contains(centroid)) {
                Geometry finalVoronoi = voronoiPolygon;

                if (praparedGeom.disjoint(voronoiPolygon)) {
                    continue;
                } else if (!praparedGeom.contains(voronoiPolygon)) {
                    finalVoronoi = voronoiPolygon.intersection(clipPolygon);
                    if (finalVoronoi == null || finalVoronoi.isEmpty()) {
                        continue;
                    }
                }
                // create feature
                SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
                SimpleFeature newFeature = featureBuilder.buildFeature(null);
                if (attributeMode == AttributeMode.All) {
                    for (int i = 0; i < feature.getAttributeCount(); i++) {
                        newFeature.setAttribute(i, feature.getAttribute(i));
                    }
                }
                newFeature.setAttribute(FID_FIELD, fid++);
                newFeature.setDefaultGeometry(finalVoronoi);
                simpleFeatures.add(newFeature);
            } else {
                log.warn("duplicated point feature!");
            }
        }
        return DataUtilities.collection(simpleFeatures);
    }

    public List<Geometry> buildThiessenPolygon(List<Coordinate> coordinates, String dstFile) throws IOException {
        VoronoiDiagramBuilder voronoiDiagramBuilder = new VoronoiDiagramBuilder();
        List<Coordinate> coords = new ArrayList<Coordinate>();
        Envelope clipEnvelpoe = new Envelope();
        for (Coordinate coord : coordinates) {
            coords.add(coord);
            clipEnvelpoe.expandToInclude(coord);
        }
        voronoiDiagramBuilder.setSites(coords);
        voronoiDiagramBuilder.setClipEnvelope(clipEnvelpoe);
        Geometry geom = voronoiDiagramBuilder.getDiagram(JTSFactoryFinder.getGeometryFactory());
        List<Geometry> geoms = new ArrayList<Geometry>();
        for (int i = 0; i < geom.getNumGeometries(); i++) {
            geoms.add(geom.getGeometryN(i));
        }
        return geoms;
    }
}
