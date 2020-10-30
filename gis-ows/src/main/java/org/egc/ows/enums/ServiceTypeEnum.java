package org.egc.ows.enums;

/**
 * @author houzhiwei
 * @date 2020/8/25 19:57
 */
public enum ServiceTypeEnum {
    WPS("WPS"),
    WCS("WCS"),
    WFS("WFS"),
    WMS("WMS"),
    WMTS("WMTS");

    public String getName() {
        return name;
    }

    private String name;

    ServiceTypeEnum(String name) {
        this.name = name;
    }
}
