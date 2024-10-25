package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Activity extends Entity {
    private String id;
    private String cropId;
    private String type;
    private String activityData;
    private String userId;
    private String farmId;
    private String date;
    private String status;
    private String blockId;
    private String itemType;
    private String itemId;
    private String locationType;
    private String locationId;
    private String lotNumber;
}
