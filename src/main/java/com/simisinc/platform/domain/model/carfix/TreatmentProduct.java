package com.simisinc.platform.domain.model.carfix;

import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class TreatmentProduct extends Entity {
   private String id = null;
   private String productId = null;
   private String productName = null;
   private String container = null;
   private String mass = null;
   private String quantity = null;
   private String farmId = null;
   private String imageData = null;
   private String activeIngredients = null;
   private String expiryDate = null;
   private String instructions = null;
   private String purpose = null;
   private String units;
   private String createdDate;

}
