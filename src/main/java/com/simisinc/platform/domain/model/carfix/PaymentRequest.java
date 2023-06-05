package com.simisinc.platform.domain.model.carfix;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.simisinc.platform.domain.model.Entity;
import lombok.Data;

/**
 *     amount
 *     authentication.entityId
 *     currency
 *     merchantTransactionId (must be equal to or less than 16 alphanumeric characters)
 *     nonce
 *     paymentType
 *     shopperResultUrl
 *     amount10.00 authentication.entityId8ac7a4ca7802ed8e0178176ca52222dc currency ZAR merchantTransactionIdPeachTest noncePeachTest paymentTypeDB shopperResultUrlhttps://httpbin.org/post
 */
@Data
public class PaymentRequest extends Entity {
    private String id;
    private String amount;
    @JsonIgnore
    private String invoiceAmount;

    @JsonIgnore
    private String commissionAmount;

    private String authenticationEntityId="8ac7a4c98694e687018696fe5bdd024f";
    private String currency="ZAR";
    private String merchantTransactionId;
    private String nonce="PeachTest";
    private String paymentType="DB";
    private String shopperResultUrl;
    private String signature;
    private String serviceProviderId;
    @JsonIgnore
    private String timeStamp;

    @JsonIgnore
    private String date;

    @JsonIgnore
    private String vatAmount;

    @JsonIgnore
    public String getConcetenatedString() {
        return "amount"+amount+"authentication.entityId"+authenticationEntityId+"currency"+currency+"merchantTransactionId"+merchantTransactionId+"nonce"+nonce+"paymentType"+paymentType+"shopperResultUrl"+shopperResultUrl;
    }
}
