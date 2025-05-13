package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Contract extends Entity {
   private String id;
   private String customerId;
   private String farmId;
   private String deliveryDate;
   private String type;
   private String price;
   private String quantity;
   private String contractDate;
   private String growthCycleId;
   private String variety;
}
