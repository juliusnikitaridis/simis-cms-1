package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Julius Nikitaridis
 *class that will represent a crop object
 */

@Getter
@Setter



@AllArgsConstructor
@NoArgsConstructor
public class Crop extends Entity {

    private String id;
    private String cropType;
    private String blockLocation;
    private String growthStage;
    private String status;
    private String varietyName;
    private String seedCompany;
    private String farmId;
    private String userId;
    private String cropLabel;
    private String startingPlantData;
    private String createdDate;
    private String barcodeData;
    private String blockId;
    private String locationType;
    private String locationId;
    private String lastUpdated;
    private String contractId;
}




