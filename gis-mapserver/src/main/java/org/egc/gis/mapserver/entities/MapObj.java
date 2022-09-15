package org.egc.gis.mapserver.entities;

import java.util.List;

/**
 * mapserver map file MAP
 *
 * @author houzhiwei
 * @date 2020/8/12 11:21
 */
public class MapObj {

    private String imageType;
    /**
     * background color in RGB
     */
    private RGBColor imageColor;
    private String size;
    private int sizeX = 400;
    private int sizeY = 300;
    private String shapepath;
    /**
     * 左下X 左下Y 右上X 右上Y <br/>
     * minx, miny, maxx, maxy in output projection
     */
    private String extent;
    private String debug;
    private String fontset;
    private List<MapConfig> configs;


    private WebObj webObj;
    private List<LayerObj> layers;


    public MapObj(String filename) {

    }

    public void save() {

    }
}
