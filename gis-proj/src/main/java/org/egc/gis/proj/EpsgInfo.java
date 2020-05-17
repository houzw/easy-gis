package org.egc.gis.proj;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EpsgInfo implements Serializable {

    private static final long serialVersionUID = -6826973038098680907L;
    @JSONField(name = "area")
    private String area;

    @JSONField(name = "wkt")
    private String wkt;

    @JSONField(name = "unit")
    private String unit;

    @JSONField(name = "code")
    private String code;

    @JSONField(name = "kind")
    private String kind;

    @JSONField(name = "bbox")
    private List<Double> bbox;

    @JSONField(name = "proj4")
    private String proj4;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "accuracy")
    private double accuracy;

    @JSONField(name = "default_trans")
    private int defaultTrans;

    @JSONField(name = "trans")
    private List<Integer> trans;
}