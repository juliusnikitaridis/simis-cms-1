package com.simisinc.platform.infrastructure.persistence.cannacomply;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ActivitySpecification {
    private String id;
    private String farmId;
    private String blockId;
    private String cropId;
}
