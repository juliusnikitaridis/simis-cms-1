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
public class Yield extends Entity {
    private String id = null;
    private String quantity = null;
    private String loss = null;
    private String harvestBatchId = null;
    private String location = null;
    private String cropId = null;
    private String strain = null;
    private String date = null;
    private String farmId = null;
    private String notes = null;
    private String stage = null;
    private String fromBlockId = null;
    private String wetWeight = null;
    private String userId;
}
