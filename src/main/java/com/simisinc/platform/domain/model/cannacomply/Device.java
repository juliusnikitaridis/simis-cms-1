package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Device extends Entity {
   private String id;
   private String deviceType;
   private String locationType;
   private String locationId;
   private String date;
   private String status;
   private String farmId;
   private String usage;
   private String lastUpdated;
}
