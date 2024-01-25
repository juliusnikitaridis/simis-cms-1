package com.simisinc.platform.domain.model.cannacomply;
import com.simisinc.platform.domain.model.Entity;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SoilPot extends Entity {
    private String id;
    private String soilId;
    private String quantity;
    private String farmId;
    private String date;
    private String measurements;
    private String status;
    private String locationId;
    private String locationType;
    private String containerType;
}
