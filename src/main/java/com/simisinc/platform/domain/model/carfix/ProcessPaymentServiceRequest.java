package com.simisinc.platform.domain.model.carfix;


import lombok.Data;

@Data
public class ProcessPaymentServiceRequest{
    private String serviceRequestId; //used to generate merchant transaction id
    private String memberId;  //for logging
    private String serviceProviderId; //for logging
    private String invoiceAmount;
}
