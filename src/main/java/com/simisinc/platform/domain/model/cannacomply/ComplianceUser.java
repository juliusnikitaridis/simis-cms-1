package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ComplianceUser extends Entity {
    private String id;
    private String sysUniqueUserId;
    private String farmId;
    private String userRole;
}
