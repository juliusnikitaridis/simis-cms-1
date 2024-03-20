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
public class Fuel extends Entity {

    private String id;
    private String machineryType;
    private String startTime;
    private String endTime;
    private String fuelConsumption;
    private String farmId;
    private String mileage;
    private String reasonForUsage;
    private String capacity;
    private String warehouseItemId;
    private String userId;
    private String date;
}




