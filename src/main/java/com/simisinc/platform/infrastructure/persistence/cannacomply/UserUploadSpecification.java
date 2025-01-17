package com.simisinc.platform.infrastructure.persistence.cannacomply;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserUploadSpecification {
    private String id = null;
    private String userId;
    private String farmId;
    private String name; // type ?
    private String referenceId;

}
