package com.simisinc.platform.domain.model.cannacomply;

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
public class Yield extends Entity {
    private String id = null;
    private String quantity = null;
    private String loss = null;
    private String containerNumber = null;
    private String batchNumber = null;
    private String cropId = null;
    private String strain = null;
    private String date = null;
    private String farmId = null;
    private String notes = null;
    private String stage = null;
    private String fromBlockId = null;
    private String wetWeight = null;
    private String userId;
    private String harvestedItem;
    private String lastUpdated;
    private String locationId;
    private String isMixed;
    private String moistureLoss;

}
