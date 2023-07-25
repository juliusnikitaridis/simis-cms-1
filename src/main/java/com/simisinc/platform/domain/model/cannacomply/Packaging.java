package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Julius Nikitaridis
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Packaging extends Entity {

    private String id;
    private String packageTag;
    private String item;
    private String quantity;
    private String farmId;
    private String location;
    private String status;
    private String date;
    private String harvestId;
}




