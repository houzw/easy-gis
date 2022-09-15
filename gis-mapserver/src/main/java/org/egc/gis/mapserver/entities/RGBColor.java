package org.egc.gis.mapserver.entities;

/**
 * @author houzhiwei
 * @date 2020/8/12 11:42
 */
public class RGBColor {
    private int red;
    private int green;
    private int blue;

    public RGBColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public String toString() {
        return "RGBColor{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                '}';
    }

    private boolean check(int value) {
        return value >= 0 && value <= 255;
    }
}
