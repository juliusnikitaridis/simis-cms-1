package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;

public class Activity extends Entity {
    private String id;
    private String cropId;
    private String type;
    private String activityData;
    private String userId;
    private String farmId;
    private String date;
    private String status;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCropId() {
        return this.cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActivityData() {
        return this.activityData;
    }

    public void setActivityData(String activutyData) {
        this.activityData = activutyData;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFarmId() {
        return this.farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
