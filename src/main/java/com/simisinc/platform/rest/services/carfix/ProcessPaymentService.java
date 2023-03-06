package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.persistence.carfix.VehicleRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;


/**
 * Description
 * Service that will process peach payment into the merchant account
 * https://www.postman.com/peachpayments/workspace/peach-payments-public-workspace/example/13324425-6946f219-4f07-4c83-acdb-bd11a59e7dc4
 * @author Julius Nikitaridis
 * @created 23/02/23 11:28 AM
 */


public class ProcessPaymentService {

    private static Log LOG = LogFactory.getLog(ProcessPaymentService.class);

    public ServiceResponse post(ServiceContext context) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            PaymentRequest paymentRequest = mapper.readValue(context.getJsonRequest(), PaymentRequest.class);
            processPeachPayment(paymentRequest);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("Payment has been processed");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in ProcessPaymentService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }

    private void processPeachPayment(PaymentRequest paymentRequest) {
    }


    public static void main (String [] args) throws Exception {
        generateSignatireForRequest();
    }



    public static void generateSignatireForRequest() throws  Exception{
        String key = "674a06bd235711eb93d502d14de18c0c";
        String hmacSHA256Algorithm = "HmacSHA256";
        PaymentRequest paymentRequest = new PaymentRequest();
        ObjectMapper mapper = new ObjectMapper();
        String jsonS = mapper.writeValueAsString(paymentRequest);
        System.out.println(jsonS);
        System.out.println(paymentRequest.getConcetenatedString());

        //generate https://www.baeldung.com/java-hmac
        //tested with https://www.devglan.com/online-tools/hmac-sha256-online
        String concatenatedString = paymentRequest.getConcetenatedString();
        String hmsc = new HmacUtils(hmacSHA256Algorithm,key).hmacHex(concatenatedString);
        System.out.println(hmsc);

    }
}


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
@Getter
@Setter
class PaymentRequest {
    String amount = "10";
    String authenticationEntityId="8ac7a4c98694e687018696fe5bdd024f";
    String currency="ZAR";
    String merchantTransactionId="SR485777463";
    String nonce="PeachTest";
    String paymentType="DB";
    String shopperResultUrl="https://carfix.connectmobiles24.com";

    public String getConcetenatedString() {
        return "amount"+amount+"authentication.entityId"+authenticationEntityId+"currency"+currency+"merchantTransactionId"+merchantTransactionId+"nonce"+nonce+"paymentType"+paymentType+"shopperResultUrl"+shopperResultUrl;
    }
}
