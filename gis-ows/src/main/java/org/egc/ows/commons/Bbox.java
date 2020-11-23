package org.egc.ows.commons;

/**
 * BoundingBox
 *
 * @author houzhiwei
 * @date 2020/8/25 22:22
 */
public class Bbox {
    public float getMinx() {
        return minx;
    }

    public void setMinx(float minx) {
        this.minx = minx;
    }

    public float getMiny() {
        return miny;
    }

    public void setMiny(float miny) {
        this.miny = miny;
    }

    public float getMaxx() {
        return maxx;
    }

    public void setMaxx(float maxx) {
        this.maxx = maxx;
    }

    public float getMaxy() {
        return maxy;
    }

    public void setMaxy(float maxy) {
        this.maxy = maxy;
    }

    private float minx;
    private float miny;
    private float maxx;
    private float maxy;

    @Override
    public String toString() {
        return "Bbox{" +
                "minx=" + minx +
                ", miny=" + miny +
                ", maxx=" + maxx +
                ", maxy=" + maxy +
                '}';
    }
}
