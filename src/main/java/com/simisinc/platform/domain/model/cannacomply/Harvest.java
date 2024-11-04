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
public class Harvest extends Entity {
    private String id ;
    private String quantity ;
    private String loss ;
    private String containerNumber ;
    private String batchNumber;
    private String cropId ;
    private String strain ;
    private String date ;
    private String farmId ;
    private String notes ;
    private String stage ;
    private String fromBlockId ;
    private String wetWeight;
    private String userId;
    private String harvestedItem;
    private String lastUpdated;
    private String locationId;
    private String isMixed;
}
