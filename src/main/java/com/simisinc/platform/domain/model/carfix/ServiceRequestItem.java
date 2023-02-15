package com.simisinc.platform.domain.model.carfix;

import com.simisinc.platform.domain.model.Entity;
import lombok.Data;

@Data
public class ServiceRequestItem extends Entity {
    private String id;
    private String serviceRequestId;
    private String ItemDescription;
    private String ItemCategoryId;
}
