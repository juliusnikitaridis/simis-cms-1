package com.simisinc.platform.domain.model.carfix;

import com.simisinc.platform.domain.model.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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



@Getter
@Setter
@NoArgsConstructor
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
    private String memberId = null;
}
