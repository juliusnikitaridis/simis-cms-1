package com.simisinc.platform.infrastructure.persistence.cannacomply;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class YieldSpecification {
    private String cropId = null;
    private String id = null;
    private String farmId = null;
    private String containerNumber = null;
}