package com.simisinc.platform.domain.model.carfix;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class PaymentRequest {
    private String amount;
    private String authenticationEntityId="8ac7a4c98694e687018696fe5bdd024f";
    private String currency="ZAR";
    private String merchantTransactionId;
    private String nonce="PeachTest";
    private String paymentType="DB";
    private String shopperResultUrl;
    private String signature;

    @JsonIgnore
    public String getConcetenatedString() {
        return "amount"+amount+"authentication.entityId"+authenticationEntityId+"currency"+currency+"merchantTransactionId"+merchantTransactionId+"nonce"+nonce+"paymentType"+paymentType+"shopperResultUrl"+shopperResultUrl;
    }
}
