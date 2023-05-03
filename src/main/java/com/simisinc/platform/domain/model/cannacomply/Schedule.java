package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;

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
    private Long column10;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFarmId() {
        return this.farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartingDate() {
        return this.startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public String getEndingDate() {
        return this.endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAssignedTo() {
        return this.assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getColumn10() {
        return this.column10;
    }

    public void setColumn10(Long column10) {
        this.column10 = column10;
    }
}
