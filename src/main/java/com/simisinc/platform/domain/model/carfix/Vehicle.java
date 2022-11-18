package com.simisinc.platform.domain.model.carfix;

import com.simisinc.platform.domain.model.Entity;

/**
 * Description
 * This will represent a vehicle object
 * @author Julius Nikitaridis
 * @created 17/10/22 9:28 AM
 */

/**
 * CRUD vin number
 * CRUD registration number
 * CRUD make
 * CRUD model
 * CRUD year
 * CRUD fuel type
 * CRUD transmission
 * CRUD ODO reading *optional
 * CRUD CRUD engine code *optional
 */

public class Vehicle extends Entity {
    private Long vehicleId = -1L;
    private String vinNumber = null;
    private String registration = null;
    private String make = null;
    private String model = null;
    private String year = null;
    private String fuelType = null;
    private String transmission = null;
    private String odoReading = null;
    private String engineCode = null;

    public Vehicle() {

    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRegistration() {
        return registration;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getReqistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getOdoReading() {
        return odoReading;
    }

    public void setOdoReading(String odoReading) {
        this.odoReading = odoReading;
    }

    public String getEngineCode() {
        return engineCode;
    }

    public void setEngineCode(String engineCode) {
        this.engineCode = engineCode;
    }
}
