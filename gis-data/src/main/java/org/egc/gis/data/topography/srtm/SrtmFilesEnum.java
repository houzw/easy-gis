package org.egc.gis.data.topography.srtm;

/**
 * https://dds.cr.usgs.gov/srtm/version2_1/SRTM30/srtm30_documentation.pdf
 *
 * @author houzhiwei
 * @date 2020/5/4 23:25
 */
public enum SrtmFilesEnum {
    DEM("digital elevation model data", "dem"),
    HDR("header file for DEM", "hdr"),
    DMW("world file", "dmw"),
    STX("statistics file", "stx"),
    PRJ("projection information file", "prj"),
    GIF("shaded relief image", "gif"),
    SRC("source map", "src"),
    SCH("header file for source map", "sch"),
    DIF("difference between SRTM30 and GTOPO30", "dif"),
    JPG("color coded shaded relief image", "jpg"),
    NUM("number of valid point included in the 10x10 average", "num"),
    STD("standard deviation of the elevations used in the average", "std");
    private String description;
    private String ext;

    SrtmFilesEnum(String description, String ext) {
        this.description = description;
        this.ext = ext;
    }

    public String getDescription() {
        return description;
    }

    public String getExt() {
        return ext;
    }
}
