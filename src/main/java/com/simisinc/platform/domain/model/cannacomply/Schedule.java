package com.simisinc.platform.domain.model.cannacomply;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Schedule extends Entity {
    private String id;
    private String farmId;
    private String status;
    private String startingDate;
    private String endingDate;
    private String title;
    private String type;
    private String assignedTo;
    private String description;
    private String repeat;
}
