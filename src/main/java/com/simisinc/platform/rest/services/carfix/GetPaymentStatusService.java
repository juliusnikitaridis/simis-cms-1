package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.granule.json.JSONObject;
import com.simisinc.platform.infrastructure.persistence.carfix.PaymentHistoryRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import lombok.Data;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Description
 * get the status of a particular transaction.
 *
 * @author Julius Nikitaridis
 * @created 16/03/23 11:28 AM
 */
public class GetPaymentStatusService {

    private static Log LOG = LogFactory.getLog(GetPaymentStatusService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            String merchantTransactionNo = context.getParameter("merchantTransactionNo");
            if(merchantTransactionNo == null) {
                throw new Exception("merchantTransactionNo is not set");
            }
            //todo check if there is already a status in the DB
            String status = getTransactionStatus(merchantTransactionNo);
            ServiceResponse response = new ServiceResponse(200);
            response.setData(status);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in GetPaymentStatusService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }

    }


    private String getTransactionStatus(String merchantTransactionNo) throws Exception {
        PaymentStatus request = new PaymentStatus();
        request.setMerchantTransactionId(merchantTransactionNo);
        generateSignatureForRequest(request);
        return invokePaymentStatusAPI(request);
    }


    private static void generateSignatureForRequest(PaymentStatus request) throws Exception {
        String key = "674a06bd235711eb93d502d14de18c0c";
        String hmacSHA256Algorithm = "HmacSHA256";
        //generate https://www.baeldung.com/java-hmac
        //tested with https://www.devglan.com/online-tools/hmac-sha256-online
        String concatenatedString = request.getConcatenatedString();
        String hash = new HmacUtils(hmacSHA256Algorithm, key).hmacHex(concatenatedString);
        request.setSignature(hash);
    }


    private static String invokePaymentStatusAPI(PaymentStatus request) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonS = mapper.writeValueAsString(request);
        //for some reason the json fields needs to be authentication.entityId and not authenticationEntityId
        //jsonS = jsonS.replace("authenticationEntityId", "authentication.entityId");

        String url = "https://testsecure.peachpayments.com/status?merchantTransactionId="+request.getMerchantTransactionId()+"&signature="+request.getSignature()+"&authentication.entityId="+request.getAuthenticationEntityId();

        String remoteContent = "NO VALUE";
        StatusLine statusLine = null;

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpPost = new HttpGet(url);
            //httpPost.setHeader("Content-Type", "application/json");

            CloseableHttpResponse response = client.execute(httpPost);

            if (response == null) {
                throw new Exception("Response from peach API [\"https://testsecure.peachpayments.com/status\"] is null ");
            }

            HttpEntity entity = response.getEntity();
            if (entity == null) {
                throw new Exception("response entity is null");
            }

            // Check for content
            remoteContent = EntityUtils.toString(entity);
            if (StringUtils.isBlank(remoteContent)) {
                throw new Exception("HttpPost Remote content is empty");
            }
            LOG.debug("REMOTE TEXT: " + remoteContent);

            // Check for errors... HTTP/1.1 405 Method Not Allowed
            statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() > 299) {
                throw new Exception("HttpPost Error for URL (" + url + "): " + statusLine.getStatusCode() + " " + statusLine.getReasonPhrase() + "Remote content" + remoteContent);
            }
            //update status in payments history table
            PaymentHistoryRepository.updatePaymentHistory(request.getMerchantTransactionId(), remoteContent);

            JSONObject jsonObject = new JSONObject(remoteContent);
            String transStatus = jsonObject.getString("result.description");
            Response res = new Response();
            if(transStatus == null) {
                throw new Exception("result.description is not present in response from API");
            }
            if(transStatus.contains("Request successfully processed")) {
                res.setStatus("SUCCESS");
            } else {
                res.setStatus("FAILURE");
            }
            return new ObjectMapper().writeValueAsString(res);
        } catch (Throwable e) {
            LOG.error("Exception from API when checking payment status" + e);
            try {
                PaymentHistoryRepository.updatePaymentHistory(request.getMerchantTransactionId(), remoteContent);
            } catch (Throwable ee) {
                //horrific error , cant write to db
                LOG.error("error when writing to table(" + url + "): " + statusLine.getStatusCode() + " " + statusLine.getReasonPhrase() + "Remote content" + remoteContent + "::" + ee.getMessage());
            }
            throw e;
        }
    }
}


@Data
class PaymentStatus {
    String merchantTransactionId;
    String signature;
    String authenticationEntityId = "8ac7a4c98694e687018696fe5bdd024f";


    @JsonIgnore
    public String getConcatenatedString() {
        return "authentication.entityId" + authenticationEntityId + "merchantTransactionId" + merchantTransactionId;
    }
}

@Data
class Response {
    String status;
}

/**  whas is returned from invokePaymentStatusAPI
 * "data": {
 *     "amount": "10.69",
 *     "card.bin": "411111",
 *     "card.expiryMonth": "12",
 *     "card.expiryYear": "2026",
 *     "card.holder": "Test",
 *     "card.last4Digits": "1111",
 *     "checkoutId": "602633d7a8074bb1b574fd8898a04f5d",
 *     "currency": "ZAR",
 *     "id": "8ac7a4a186ea561c0186ea825c826766",
 *     "merchant.name": "Connect Mobiles24 Sandbox",
 *     "merchantTransactionId": "SR4857774677AA",
 *     "paymentBrand": "VISA",
 *     "paymentType": "DB",
 *     "result.code": "000.100.110",
 *     "result.description": "Request successfully processed in 'Merchant in Integrator Test Mode'",
 *     "resultDetails.AcquirerResponse": "0",
 *     "signature": "9b0f49c6e33a55ceba08d620ed2ecc6d1b4b052e1e82ae4914e360610084e3fc",
 *     "timestamp": "2023-03-16T13:01:10Z"
 *   }
 */


