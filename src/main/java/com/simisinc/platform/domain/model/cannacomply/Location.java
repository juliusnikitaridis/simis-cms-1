package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Julius Nikitaridis
 *class that will represent a room object
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location extends Entity {

    private String id;
    private String farmId;
    private String locationColour;
    private String locationName;
    private String locationDescription;
    private String locationData;
    private String purpose;
    private String optimalReadings;
    private String type;
    private String dimensions;
}




