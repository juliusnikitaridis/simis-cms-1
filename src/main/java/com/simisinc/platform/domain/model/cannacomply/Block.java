package com.simisinc.platform.domain.model.cannacomply;

import com.simisinc.platform.domain.model.Entity;

public class Block extends Entity {
    private String id;
    private String blockLocation;
    private String barcodeData;
    private String farmId;
    private String date;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBlockLocation() {
        return this.blockLocation;
    }

    public void setBlockLocation(String blockLocation) {
        this.blockLocation = blockLocation;
    }

    public String getBarcodeData() {
        return this.barcodeData;
    }

    public void setBarcodeData(String barcodeData) {
        this.barcodeData = barcodeData;
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
}
