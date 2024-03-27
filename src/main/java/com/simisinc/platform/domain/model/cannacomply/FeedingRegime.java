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
public class FeedingRegime extends Entity {

    private String id;
    private String growthStage;
    private String weeks;
    private String nutrientId;
    private String volume;
    private String frequency;
    private String ph;
    private String wateringSchedule;
    private String flushSchedule;
    private String notes;
    private String nutrientConc;
    private String farmId;
    private String name;
}




