package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ApiAccess extends Entity {
   private String id;
   private String apiName;
   private String allowedRoles;
}
