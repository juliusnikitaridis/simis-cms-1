package com.simisinc.platform.domain.model.cannacomply;


import lombok.Getter;
import lombok.Setter;
import com.simisinc.platform.domain.model.Entity;


@Getter
@Setter
public class  SoilManagement extends Entity {
    private String id;
    private String texture;
    private String ph;
    private String farmId;
    private String date;
    private String nutrientLevels;
    private String soilType;
    private String locationId;
    private String locationType;
    private String wharehouseItemId;
    private String organicMatter;
}
