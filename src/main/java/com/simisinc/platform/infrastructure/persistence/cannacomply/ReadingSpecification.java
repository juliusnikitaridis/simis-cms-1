package com.simisinc.platform.infrastructure.persistence.cannacomply;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ReadingSpecification {
    private String id = null;
    private String deviceId = null;
    private String farmId = null;
    private String locationId = null;
}
