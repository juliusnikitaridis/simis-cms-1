package com.simisinc.platform.domain.model.carfix;


import lombok.Data;

@Data
public class ProcessPaymentServiceRequest{
    private String shopperResultUrl;
    private String merchantTransactionId;
    private String amount;
    private String memberId;  //for logging
    private String serviceProviderId; //for logging
}
