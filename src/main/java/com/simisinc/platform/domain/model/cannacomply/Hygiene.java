package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Hygiene extends Entity {
   private String id;
   private String userId;
   private String locationId;
   private String farmId;
   private String type;
   private String date;
   private String form;
}
