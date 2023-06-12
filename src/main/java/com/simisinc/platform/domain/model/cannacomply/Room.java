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
public class Room extends Entity {

    private String id;
    private String farmId;
    private String roomColour;
    private String roomName;
    private String roomDescription;
    private String locationData;
}




