package com.simisinc.platform.domain.model.carfix;

import com.simisinc.platform.domain.model.Entity;
import lombok.Data;

/**
 * Julius Nikitaridis
 * This will represent the service requests that are lodged by members of the platform. There can
 * be 3 types of service requests
 */
public class ServiceRequest extends Entity {

    private Long id;
    private String date;
    private String type;
    private String vehicleId;
    private String memberId;
    private String radius;
    private String status;
    private String currentOdoReading;
    private String pictureData;
    private String lastServiceDate;
    private ServiceRequestItem[] serviceRequestItems;

    public ServiceRequest() {

    }

    public ServiceRequestItem[] getServiceRequestItems() {
        return serviceRequestItems;
    }

    public void setServiceRequestItems(ServiceRequestItem[] serviceRequestItems) {
        this.serviceRequestItems = serviceRequestItems;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentOdoReading() {
        return currentOdoReading;
    }

    public void setCurrentOdoReading(String currentOdoReading) {
        this.currentOdoReading = currentOdoReading;
    }

    public String getPictureData() {
        return pictureData;
    }

    public void setPictureData(String pictureData) {
        this.pictureData = pictureData;
    }

    public String getLastServiceDate() {
        return lastServiceDate;
    }

    public void setLastServiceDate(String lastServiceDate) {
        this.lastServiceDate = lastServiceDate;
    }
}




