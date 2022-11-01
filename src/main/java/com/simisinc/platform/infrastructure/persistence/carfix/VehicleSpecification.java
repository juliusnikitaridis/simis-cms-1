package com.simisinc.platform.infrastructure.persistence.carfix;

public class VehicleSpecification {

    private long vehicleId = -1L;
    private long cutomerId = -1L;


    public long getCutomerId() {
        return cutomerId;
    }

    public void setCutomer_id(long cutomerId) {
        this.cutomerId = cutomerId;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

}
