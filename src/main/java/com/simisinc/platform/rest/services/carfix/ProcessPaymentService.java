package com.simisinc.platform.rest.services.carfix;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.granule.json.JSONObject;
import com.simisinc.platform.domain.model.carfix.PaymentRequest;
import com.simisinc.platform.domain.model.carfix.ProcessPaymentServiceRequest;
import com.simisinc.platform.infrastructure.persistence.carfix.PaymentRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
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
 *
 * @author Julius Nikitaridis
 * @created 23/03/10 11:28 AM
 */


public class ProcessPaymentService {

    private static Log LOG = LogFactory.getLog(ProcessPaymentService.class);

    public ServiceResponse post(ServiceContext context) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            ProcessPaymentServiceRequest paymentServiceRequest = mapper.readValue(context.getJsonRequest(), ProcessPaymentServiceRequest.class);
            String redirectUrl = processPeachPayment(paymentServiceRequest);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>() {{
                add("Payment has been initiated");
            }};
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
        String uniqueTransactionNo = serviceRequest.getServiceRequestId()+"/"+System.currentTimeMillis();

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(serviceRequest.getAmount());
        paymentRequest.setMerchantTransactionId(uniqueTransactionNo);
        paymentRequest.setShopperResultUrl("https://member.carfixsa.com/home/service/"+"/"+uniqueTransactionNo);

        generateSignatureForRequest(paymentRequest);
        String redirectUrl = invokePeachPaymentsAPI(paymentRequest, serviceRequest);
        return redirectUrl;
    }



    private static void generateSignatureForRequest(PaymentRequest request) throws Exception {
        String key = "674a06bd235711eb93d502d14de18c0c";
        String hmacSHA256Algorithm = "HmacSHA256";
        //generate https://www.baeldung.com/java-hmac
        //tested with https://www.devglan.com/online-tools/hmac-sha256-online
        String concatenatedString = request.getConcetenatedString();
        String hash = new HmacUtils(hmacSHA256Algorithm, key).hmacHex(concatenatedString);
        request.setSignature(hash);
    }


    private static String invokePeachPaymentsAPI(PaymentRequest request, ProcessPaymentServiceRequest serviceRequest) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonS = mapper.writeValueAsString(request);
        //for some reason the json fields needs to be authentication.entityId and not authenticationEntityId
        jsonS = jsonS.replace("authenticationEntityId", "authentication.entityId");
        String url = "https://testsecure.peachpayments.com/checkout/initiate";

        String remoteContent = "NO VALUE";
        StatusLine statusLine = null;

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(jsonS));
            CloseableHttpResponse response = client.execute(httpPost);

            if (response == null) {
                throw new Exception("Response from peach API [https://testsecure.peachpayments.com/checkout/initiate] is null ");
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
            //everything ok to this point, record the payment in DB
            PaymentRepository.add(serviceRequest, request, "NO ERROR-OK");
            remoteContent = new JSONObject(remoteContent).append("transactionId",request.getMerchantTransactionId()).toString();
            return remoteContent;
        } catch (Throwable e) {
            LOG.error("Exception from peach payments API " + e);
            try {
                PaymentRepository.add(serviceRequest, request, "HttpPost Error for URL (" + url + "): " + statusLine.getStatusCode() + " " + statusLine.getReasonPhrase() + "Remote content" + remoteContent + "::" + e.getMessage());
            } catch (Throwable ee) {
                //horrific error , cant write to db
                LOG.error("cent even write error to payments table->>HttpPost Error for URL (" + url + "): " + statusLine.getStatusCode() + " " + statusLine.getReasonPhrase() + "Remote content" + remoteContent + "::" + ee.getMessage());
            }
            throw e;
        }
    }
}

