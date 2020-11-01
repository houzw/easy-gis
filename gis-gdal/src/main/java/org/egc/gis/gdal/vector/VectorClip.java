package org.egc.gis.gdal.vector;

import org.egc.gis.gdal.Clip;
import org.egc.gis.gdal.dto.BoundingBox;

/**
 * TODO
 * @author houzhiwei
 * @date 2020/5/3 21:46
 */
public class VectorClip implements Clip {
    //
    //Clip vectors by bounding box

    //ogr2ogr -f "ESRI Shapefile" output.shp input.shp -clipsrc <x_min> <y_min> <x_max> <y_max>

   // Clip one vector by another

    //ogr2ogr -clipsrc clipping_polygon.shp output.shp input.shp

    @Override
    public boolean clipByBoundingBox(String src, String dst, BoundingBox boundingBox, double expand) {
        return false;
    }

    @Override
    public boolean clipByBoundingBox(String src, String dst, BoundingBox boundingBox) {
        return false;
    }

    @Override
    public boolean clipByVectorBoundingBox(String src, String dst, String shp) {
        return false;
    }

    @Override
    public boolean clipByPolygon(String src, String dst, String polygonShp) {
        return false;
    }
}
