package org.egc.gis.data.topography;

import org.egc.gis.data.EnumUtils;

import java.util.function.Function;

/**
 * @author houzhiwei
 * @date 2020/5/5 17:18
 */
public enum DemTypesEnum {
    /**
     * SRTM GL3 (90m)
     */
    SRTMGL3("srtmgl3"),
    /**
     * SRTM GL1 (30m)
     */
    SRTMGL1("srtmgl1"),
    /**
     * SRTM GL1 (Ellipsoidal)
     */
    SRTMGL1_E("srtmgl1_e"),
    /**
     * ALOS World 3D 30m
     */
    AW3D30("aw3d30"),
    /**
     * ALOS World 3D (Ellipsoidal)
     */
    AW3D30_E("aw3d30_e");
    private String demType;

    DemTypesEnum(String demType) {
        this.demType = demType;
    }

    public String getDemType() {
        return this.demType;
    }

    private static final Function<String, DemTypesEnum> func =
            EnumUtils.lookupMap(DemTypesEnum.class, DemTypesEnum::getDemType);

    public static DemTypesEnum lookupByDemType(String type) {
        return func.apply(type);
    }
}
