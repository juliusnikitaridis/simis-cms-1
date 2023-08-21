package com.simisinc.platform.domain.model.carfix;

import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Description
 * This will represent a yield
 * @author Julius Nikitaridis
 * @created 17/07/23 9:28 AM
 */


@Getter
@Setter
@NoArgsConstructor
public class Supplier extends Entity {
    private String id = null;
    private String farmId;
    private String supplierName;
    private String productName;
    private String quantity;
    private String expiryDate;
    private String receipt;
    private String type;
    private String lotNumber;
    private String container;
    private String mass;
}
