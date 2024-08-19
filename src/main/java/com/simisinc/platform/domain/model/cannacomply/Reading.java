package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Reading extends Entity {
   private String id;
   private String date;
   private String deviceId;
   private String readingType;
   private String readingValue;
   private String status;
   private String farmId;
   private String actionTaken;
   private String userId;
   private String locationType;
}
