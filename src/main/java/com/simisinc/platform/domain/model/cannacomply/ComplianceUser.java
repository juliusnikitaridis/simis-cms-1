package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;
import com.simisinc.platform.domain.model.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ComplianceUser extends User {
    private String uuid;
    private String sysUniqueUserId;
    private String farmId;
    private String userRole;
    private String status;
}
