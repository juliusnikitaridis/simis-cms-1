package com.simisinc.platform.domain.model.cannacomply;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WaterManagement extends Entity {

    private String id;

    private String userId;

    private String locationId;

    private String locationType;

    private String waterUse;

    private String farmId;

    private String waterSource;

    private String startTime;

    private String endTime;

    private String methodUsed;

    private String flowRate;

    private String waterQuantity;

    private String waterUnitsUsed;

    private String waterTemp;

    private String waterPh;

    private String rateUnit;
    private String date;
    private String ppmLevels;
    private String deviceId;

}
