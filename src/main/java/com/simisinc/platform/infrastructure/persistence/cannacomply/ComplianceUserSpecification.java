package com.simisinc.platform.infrastructure.persistence.cannacomply;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ComplianceUserSpecification {
    private String farmId;
    private String id;
    private String uniqueSysUserId;
}
