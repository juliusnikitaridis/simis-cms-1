package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Strain extends Entity {
   private String id;
   private String strainName;
   private String breeder;
   private String flowering;
   private String userRating;
   private String yield;
}
