package com.simisinc.platform.rest.services.carfix;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.granule.json.JSONObject;
import com.simisinc.platform.domain.model.carfix.PaymentRequest;
import com.simisinc.platform.domain.model.carfix.ProcessPaymentServiceRequest;
import com.simisinc.platform.infrastructure.persistence.carfix.PaymentRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import okhttp3.*;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
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
            System.out.println("here");
            invokePeachPaymentsAPI();

        } catch (Exception e) {
            LOG.error("Error in ProcessPaymentService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
        return null;
    }



    private static okhttp3.Response  invokePeachPaymentsAPI()  throws Exception {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("request","<APIPaymentsRequest>\n" +
                        "                        <Header>\n" +
                        "                <PsVer>2.0.1</PsVer>\n" +
                        "<Client>PEA001</Client>\n" +
                        "<Service>Creditors</Service>\n" +
                        "<ServiceType>SDV</ServiceType>\n" +
                        "<DueDate>20200917</DueDate>\n" +
                        "<CallbackUrl>https://www.example.com/</CallbackUrl>\n" +
                        "<Reference>202009170001</Reference>\n" +
                        "</Header>\n" +
                        "<Payments>\n" +
                        "<FileContents>\n" +
                        "<Initial>JS</Initial>\n" +
                        "<FirstName>Smith</FirstName>\n" +
                        "<Surname>Smith</Surname>\n" +
                        "<BranchCode>157852</BranchCode>\n" +
                        "<AccountNumber>1203389809</AccountNumber>\n" +
                        "<FileAmount>1.00</FileAmount>\n" +
                        "<AccountType>0</AccountType>\n" +
                        "<AmountMultiplier>1</AmountMultiplier>\n" +
                        "<Reference>PeachPayments</Reference>\n" +
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
                        "<Amount>1.00</Amount>\n" +
                        "<BranchHash>157852</BranchHash>\n" +
                        "<AccountHash>1203389809</AccountHash>\n" +
                        "</Totals>\n" +
                        "</APIPaymentsRequest>")
                .build();
        Request request = new Request.Builder()
                .url("https://test.peachpay.co.za/API/Payments?key=062199b6-1460-4feb-a3df-699808377e07")
                .method("POST", body)
                .build();
        okhttp3.Response response = client.newCall(request).execute();
        return response;
    }
}

