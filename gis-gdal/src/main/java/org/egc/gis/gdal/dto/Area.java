package org.egc.gis.gdal.dto;

/**
 * @author houzhiwei
 * @date 2020/9/6 11:53
 */
public class Area {
    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    private double area;
    private String unit;

    public Area(double area, String unit) {
        this.area = area;
        this.unit = unit;
    }

    @Override
    public String toString() {
        return area + ", linear unit is " + unit;
    }
}
