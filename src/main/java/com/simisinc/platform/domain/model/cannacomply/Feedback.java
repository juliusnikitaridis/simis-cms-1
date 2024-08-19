package com.simisinc.platform.domain.model.cannacomply;


import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Feedback extends Entity {
    private String id;
    private String farmId;
    private String userId;
    private String comments;
    private String date;
}
