package org.egc.gis.geotools.test;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.egc.gis.geotools.IOFactory;
import org.egc.gis.geotools.VectorIO;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.Before;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author houzhiwei
 * @date 2020/5/18 20:16
 */
@Slf4j
public class Thiessen extends TestCase {

    @Before
    public void init() {
        Configurator.setAllLevels("", Level.DEBUG);
    }

    public void testThiessenPolygon() throws IOException {
        VoronoiDiagramBuilder voronoiDiagramBuilder = new VoronoiDiagramBuilder();
        List<Coordinate> coords = new ArrayList<Coordinate>();
        Envelope clipEnvelpoe = new Envelope();
        int xmin = 0, xmax = 180;
        int ymin = 0, ymax = 90;
        Random random = new Random();
        List<Geometry> geomsPoints = new ArrayList<Geometry>();
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(xmax) % (xmax - xmin + 1) + xmin,
                    y = random.nextInt(ymax) % (ymax - ymin + 1) + ymin;
            Coordinate coord = new Coordinate(x, y, i);
            coords.add(coord);
            clipEnvelpoe.expandToInclude(coord);
            geomsPoints.add(new GeometryFactory().createPoint(coord));
        }
        String pointpath = "H:/gisdemo/out/tspoint.shp";
        VectorIO vectorIO = IOFactory.createVectorIO();
        vectorIO.writeGeometry2Shp(geomsPoints, new File(pointpath), Point.class);

        voronoiDiagramBuilder.setSites(coords);
        voronoiDiagramBuilder.setClipEnvelope(clipEnvelpoe);
        Geometry geom = voronoiDiagramBuilder.getDiagram(JTSFactoryFinder.getGeometryFactory());
        List<Geometry> geoms = new ArrayList<Geometry>();
        for (int i = 0; i < geom.getNumGeometries(); i++) {
            geoms.add(geom.getGeometryN(i));
        }
        String polygonpath = "H:/gisdemo/out/thiessen.shp";
        vectorIO.writeGeometry2Shp(geoms, new File(polygonpath), Polygon.class);
    }
}
