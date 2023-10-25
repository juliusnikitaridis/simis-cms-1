package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Customer extends Entity {
   private String id;
   private String customerName;
   private String country;
   private String city;
   private String contactNo;
   private String email;
   private String farmId;
   private String address;
}
