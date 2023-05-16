package com.simisinc.platform.domain.model.carfix;

import com.simisinc.platform.domain.model.Entity;
import lombok.*;

import java.util.ArrayList;

/**
 * Julius Nikitaridis
 * This will represent the service requests that are lodged by members of the platform. There can
 * be 3 types of service requests
 */

@Getter
@Setter
@NoArgsConstructor
public class ServiceRequest extends Entity {

    private String id;
    private String createdDate;
    private String type;
    private String vehicleId;
    private String memberId;
    private String radius;
    private String status;
    private String currentOdoReading;
    private String pictureData;
    private String lastServiceDate;
    private String additionalDescription;
    private ArrayList<ServiceRequestItem> serviceRequestItems;
    private String confirmedServiceProvider;
    private String acceptedQuoteId;
    private String categoryHash;
    private String vehicleBrandId;
    private String preferredDate;
    private String confirmedDate;
    private String customerReference;
    private String technician;
    private String serviceAdvisor;
}




