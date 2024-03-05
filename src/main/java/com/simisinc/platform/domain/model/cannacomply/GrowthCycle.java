package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GrowthCycle extends Entity {
   private String id;
   private String plants;
   private String startDate;
   private String endDate;
   private String variety;
   private String farmId;
   private String yield;
   private String growthCycleName;
   private String commodity;
}
