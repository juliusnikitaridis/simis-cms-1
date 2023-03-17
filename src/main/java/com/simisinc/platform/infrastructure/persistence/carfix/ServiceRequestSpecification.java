package com.simisinc.platform.infrastructure.persistence.carfix;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ServiceRequestSpecification {

    private String vehicleId ;
    private String memberId;
    private String serviceRequestId;
    private String status;
    private String vehicleBrandId;
    private String serviceProviderId;
}
