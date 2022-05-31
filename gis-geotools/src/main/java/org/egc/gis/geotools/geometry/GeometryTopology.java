package org.egc.gis.geotools.geometry;

import org.locationtech.jts.geom.Geometry;

/**
 * description:
 *
 * @author houzhiwei
 * @date 2022/5/30/0030 16:09
 */
public class GeometryTopology {

    public boolean equals(Geometry geom1, Geometry geom2) {
        return geom1.equals(geom2);
    }

}
