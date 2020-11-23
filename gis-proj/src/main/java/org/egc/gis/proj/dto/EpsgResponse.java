package org.egc.gis.proj.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EpsgResponse implements Serializable {

    private static final long serialVersionUID = -1181992897001643655L;
    @JSONField(name = "number_result")
    private int numberResult;

    @JSONField(name = "results")
    private List<EpsgInfo> results;

    @JSONField(name = "status")
    private String status;

}