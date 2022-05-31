package org.egc.gis.geotools.geometry;

import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 *   Coordinates.Builder builder = new Coordinates.Builder();
 *   Coordinate[] coordinates = builder.xy(1, 2).xy(2, 3).build().getCoordinates();
 *
 * @author houzhiwei
 * @date 2022/5/30/0030 17:51
 */
public class Coordinates {
    Coordinate[] coordinates;

    public Coordinate[] getCoordinates() {
        return this.coordinates;
    }

    private Coordinates(Builder builder) {
        Coordinate[] coordinateArray = new Coordinate[builder.coordinateList.size()];
        this.coordinates = builder.coordinateList.toArray(coordinateArray);
    }

    public static final class Builder {

        List<Coordinate> coordinateList = new ArrayList<>();

        public Builder() {
        }

        public Builder xy(double x, double y) {
            this.coordinateList.add(new Coordinate(x, y));
            return this;
        }

        public Builder xyz(double x, double y, double z) {
            this.coordinateList.add(new Coordinate(x, y, z));
            return this;
        }

        public Coordinates build() {
            return new Coordinates(this);
        }
    }
}
