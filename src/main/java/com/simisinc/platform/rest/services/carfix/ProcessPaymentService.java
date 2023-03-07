package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.persistence.carfix.VehicleRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

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
            ProcessPaymentServiceRequest paymentServiceRequest = mapper.readValue(context.getJsonRequest(), ProcessPaymentServiceRequest.class);
            String redirectUrl = processPeachPayment(paymentServiceRequest);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("Payment has been initiated");}};
            responseMessage.add(redirectUrl);
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in ProcessPaymentService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }


    private String processPeachPayment(ProcessPaymentServiceRequest serviceRequest) throws Exception {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(serviceRequest.getAmount());
        paymentRequest.setMerchantTransactionId(serviceRequest.getMerchantTransactionId());
        paymentRequest.setShopperResultUrl(serviceRequest.getShopperResultUrl());

        generateSignatureForRequest(paymentRequest);
        String redirectUrl = invokePeachPaymentsAPI(paymentRequest);
        return redirectUrl;
    }


    private void logTransaction(PaymentRequest paymentRequest, ProcessPaymentServiceRequest serviceRequest) {
        //TODO log this in transaction history table
    }


    public static void generateSignatureForRequest(PaymentRequest request) throws  Exception{
        String key = "674a06bd235711eb93d502d14de18c0c";
        String hmacSHA256Algorithm = "HmacSHA256";
        //generate https://www.baeldung.com/java-hmac
        //tested with https://www.devglan.com/online-tools/hmac-sha256-online
        String concatenatedString = request.getConcetenatedString();
        String hash = new HmacUtils(hmacSHA256Algorithm,key).hmacHex(concatenatedString);
        request.setSignature(hash);
    }



    public static String invokePeachPaymentsAPI(PaymentRequest request) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonS = mapper.writeValueAsString(request);
        //for some reason the json fields needs to be authentication.entityId and not authenticationEntityId
        jsonS = jsonS.replace("authenticationEntityId","authentication.entityId");
        String url = "https://testsecure.peachpayments.com/checkout/initiate";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(jsonS));
            CloseableHttpResponse response = client.execute(httpPost);

            if(response == null) {
                throw new Exception("Response from peach API [https://testsecure.peachpayments.com/checkout/initiate] is null ");
            }

            HttpEntity entity = response.getEntity();
            if (entity == null) {
                throw new Exception("response entity is null");
            }

            // Check for content
            String remoteContent = EntityUtils.toString(entity);
            if (StringUtils.isBlank(remoteContent)) {
               throw new Exception("HttpPost Remote content is empty");
            }
            LOG.debug("REMOTE TEXT: " + remoteContent);

            // Check for errors... HTTP/1.1 405 Method Not Allowed
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() > 299) {
                throw new Exception("HttpPost Error for URL (" + url + "): " + statusLine.getStatusCode() + " " + statusLine.getReasonPhrase()+"Remote content"+remoteContent);
            }
            return remoteContent;
        } catch(Exception e) {
            LOG.error("Exception from peach payments API "+e);
            throw e;
        }
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
class PaymentRequest {
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

@Data
class ProcessPaymentServiceRequest{
    private String shopperResultUrl;
    private String merchantTransactionId;
    private String amount;
    private String memberId;  //for logging
    private String serviceProviderId; //for logging
}
