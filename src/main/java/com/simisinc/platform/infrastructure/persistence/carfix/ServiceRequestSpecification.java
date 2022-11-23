package com.simisinc.platform.infrastructure.persistence.carfix;

public class ServiceRequestSpecification {

    private long vehicleId = -1L;
    private long memberId = -1L;
    private long serviceRequestId = -1L;


    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public long getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(long serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }
}
