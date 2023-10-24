package com.simisinc.platform.infrastructure.persistence.cannacomply;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ContractSpecification {
    private String farmId = null;
    private String customerId = null;
    private String growthCycleId = null;
}
