package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Issue extends Entity {
    private String id;
    private String cropId;
    private String title;
    private String description;
    private String severity;
    private String assignedTo;
    private String comment;
    private String attachments;
    private String farmId;
    private String createdDate;
    private String lastUpdated;
}
