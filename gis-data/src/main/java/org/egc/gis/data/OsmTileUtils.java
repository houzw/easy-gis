package org.egc.gis.data;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * https://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
 *
 * @author houzhiwei
 * @date 2020/5/23 22:54
 */
@Slf4j
public class OsmTileUtils {
    @Data
    public static class BoundingBox {
        double north;
        double south;
        double east;
        double west;
    }

    public static BoundingBox tile2boundingBox(int x, int y, int zoom) {
        BoundingBox bb = new BoundingBox();
        bb.setNorth(tile2lat(y, zoom));
        bb.setSouth(tile2lat(y + 1, zoom));
        bb.setWest(tile2lon(x, zoom));
        bb.setEast(tile2lon(x + 1, zoom));
        return bb;
    }

    public static double tile2lon(int x, int z) {
        return x / Math.pow(2.0, z) * 360.0 - 180;
    }

    public static double tile2lat(int y, int z) {
        double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
        return Math.toDegrees(Math.atan(Math.sinh(n)));
    }

    /**
     * Gets tile number.
     *
     * @param lat  the lat
     * @param lon  the lon
     * @param zoom the zoom
     * @return the tile number： [zoom , xtile , ytile]
     */
    public static int[] getTileNumber(double lat, double lon, int zoom) {
        int xtile = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
        int ytile = (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1 << zoom));
        if (xtile < 0) {
            xtile = 0;
        }
        if (xtile >= (1 << zoom)) {
            xtile = ((1 << zoom) - 1);
        }
        if (ytile < 0) {
            ytile = 0;
        }
        if (ytile >= (1 << zoom)) {
            ytile = ((1 << zoom) - 1);
        }
        return new int[]{zoom, xtile, ytile};
    }
}
