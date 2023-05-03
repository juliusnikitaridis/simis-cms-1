package com.simisinc.platform.domain.model.cannacomply;

public class TreatmentProductDTO {
    private String id;
    private String productName;
    private String container;
    private String mass;
    private String quantity;
    private String farmId;
    private String imageData;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getContainer() {
        return this.container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getMass() {
        return this.mass;
    }

    public void setMass(String mass) {
        this.mass = mass;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getFarmId() {
        return this.farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getImageData() {
        return this.imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}
