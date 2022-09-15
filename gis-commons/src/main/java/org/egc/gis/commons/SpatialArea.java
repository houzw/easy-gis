package org.egc.gis.commons;

/**
 * @author houzhiwei
 * @date 2020/9/6 11:53
 */
public class SpatialArea {
    public double getArea() {
        return area;
    }


    /**
     * Gets area in km^2. <br/>
     * <b>Works only when original area are in square Meters/Metres</b>
     * @return the area in km^2
     */
    public double getAreaInKmSquare() {
        if ("Meter".equalsIgnoreCase(this.getUnit()) || "Metre".equalsIgnoreCase(this.getUnit())) {
            return area/(1000*1000);
        }
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

    public SpatialArea(double area, String unit) {
        this.area = area;
        this.unit = unit;
    }

    @Override
    public String toString() {
        return area + ", linear unit is " + unit;
    }
}
