package com.simisinc.platform.rest.services.carfix;


import com.simisinc.platform.domain.model.carfix.PaymentRequest;
import com.simisinc.platform.domain.model.carfix.ServiceProvider;
import com.simisinc.platform.infrastructure.persistence.carfix.PaymentHistoryRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.PaymentHistorySpecification;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceProviderRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceProviderSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import okhttp3.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Julius Nikitaridis
 * @created 23/03/10 11:28 AM
 */


public class ProcessServiceProvidersPayments {

    private static Log LOG = LogFactory.getLog(ProcessServiceProvidersPayments.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            PaymentHistorySpecification specification = new PaymentHistorySpecification();
            specification.setBatchPaymentStatus("NOT_PROCESSED");
            List<PaymentRequest> pendingPaymentRequests = (List<PaymentRequest>) PaymentHistoryRepository.query(specification, null).getRecords();

            ArrayList<String> responseMessages = new ArrayList<String>();
            ServiceResponse response = new ServiceResponse(200);

            for (PaymentRequest pendingPaymentRequest : pendingPaymentRequests) {
                //get the account details.
                ServiceProviderSpecification serviceProviderSpecification = new ServiceProviderSpecification();
                serviceProviderSpecification.setServiceProviderUniqueId(pendingPaymentRequest.getServiceProviderId());
                ServiceProvider serviceProvider = (ServiceProvider) ServiceProviderRepository.query(serviceProviderSpecification, null).getRecords().get(0);

                String apiResponse = invokePeachPaymentsAPI(serviceProvider.getAccountNo(), pendingPaymentRequest);
                System.out.println("Batch transaction has been processed " + pendingPaymentRequest.getId());
                PaymentHistoryRepository.updatePaymentHistoryBatchStatus(pendingPaymentRequest.getId(), apiResponse);

                responseMessages.add("Batch payment has been processed for PaymentHistoryRecord"+pendingPaymentRequest.getId());

            }
            response.setData(responseMessages);
            return response;

        } catch (Exception e) {
            LOG.error("Error in ProcessPaymentService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }


    private static String invokePeachPaymentsAPI(String accountNo, PaymentRequest batchPayment) throws Exception {

        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("request", "<APIPaymentsRequest>\n" +
                            "                        <Header>\n" +
                            "                <PsVer>2.0.1</PsVer>\n" +
                            "<Client>PEA001</Client>\n" +
                            "<Service>Creditors</Service>\n" +
                            "<ServiceType>SDV</ServiceType>\n" +
                            "<DueDate>20200917</DueDate>\n" + //todo check this
                            "<CallbackUrl>https://www.example.com/</CallbackUrl>\n" +
                            "<Reference>202009170001</Reference>\n" + //todo check this
                            "</Header>\n" +
                            "<Payments>\n" +
                            "<FileContents>\n" +
                            "<Initial>JS</Initial>\n" + //todo check this
                            "<FirstName>Smith</FirstName>\n" + //todo check this
                            "<Surname>Smith</Surname>\n" + //todo check this
                            "<BranchCode>157852</BranchCode>\n" + //todo check this
                            "<AccountNumber>" + accountNo + "</AccountNumber>\n" +
                            "<FileAmount>" + batchPayment.getCommissionAmount() + "</FileAmount>\n" +
                            "<AccountType>0</AccountType>\n" +
                            "<AmountMultiplier>1</AmountMultiplier>\n" +
                            "<Reference>SP COMM PAYMENT - " + batchPayment.getId() + "</Reference>\n" +
                            "<ExternalLinks>\n" +
                            "<ExternalLink>\n" +
                            "<Label>Invoice</Label>\n" +
                            "<URL>https://www.google.com/</URL>\n" +
                            "</ExternalLink>\n" +
                            "</ExternalLinks>\n" +
                            "</FileContents>\n" +
                            "</Payments>\n" +
                            "<Totals>\n" +
                            "<Records>1</Records>\n" +
                            "<Amount>" + batchPayment.getCommissionAmount() + "</Amount>\n" +
                            "<BranchHash>157852</BranchHash>\n" +
                            "<AccountHash>" + accountNo + "</AccountHash>\n" +
                            "</Totals>\n" +
                            "</APIPaymentsRequest>")
                    .build();
            Request request = new Request.Builder()
                    .url("https://test.peachpay.co.za/API/Payments?key=062199b6-1460-4feb-a3df-699808377e07")
                    .method("POST", body)
                    .build();

            okhttp3.Response response = client.newCall(request).execute();
            return response.body().string();
        } catch(Exception e) {
            //system exceptions not generated from API
            LOG.error("exception when processing batch payment "+e.getMessage());
            PaymentHistoryRepository.updatePaymentHistoryBatchStatus(batchPayment.getId(),e.getMessage());
            throw e;
        }
    }
}

