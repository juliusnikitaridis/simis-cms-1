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



        } catch (Exception e) {
            LOG.error("Error in ProcessPaymentService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
        return null;
    }

    public static void main (String[] srgs) {
        invokePeachPaymentsAPI();
    }




    private static String invokePeachPaymentsAPI()  {

        String authenticationEntityId="062199b6-1460-4feb-a3df-699808377e07";
        String xmlRequest = "<APIPaymentsRequest>\n" +
                " <Header>\n" +
                "  <PsVer>2.0.1</PsVer>\n" +
                "  <Client>ZER001</Client>\n" +
                "  <DueDate>20200625</DueDate>\n" +
                "  <Service>Creditors</Service>\n" +
                "  <ServiceType>1day</ServiceType>\n" +
                "  <Reference>Example Batch</Reference>\n" +
                "  <CallBackUrl>https://example.com/Callback</CallBackUrl>\n" +
                " </Header>\n" +
                " <Payments>\n" +
                "  <FileContents>\n" +
                "   <Initials>EX</Initials>\n" +
                "   <FirstNames>Example</FirstNames>\n" +
                "   <Surname>Recipient</Surname>\n" +
                "   <BranchCode>632009</BranchCode>\n" +
                "   <AccountNumber>123456789</AccountNumber>\n" +
                "   <FileAmount>549.01</FileAmount>\n" +
                "   <AccountType>0</AccountType>\n" +
                "   <AmountMultiplier>1</AmountMultiplier>\n" +
                "   <CustomerCode>EXA9292</CustomerCode>\n" +
                "   <Reference>Example Reference</Reference>\n" +
                "  </FileContents>\n" +
                " </Payments>\n" +
                " <Totals>\n" +
                "  <Records>1</Records>\n" +
                "  <Amount>549.01</Amount>\n" +
                "  <BranchHash>632009</BranchHash>\n" +
                "  <AccountHash>123456789</AccountHash>\n" +
                " </Totals>\n" +
                "</APIPaymentsRequest>";

        String url = "https://test.peachpay.co.za/API/Payments?key="+authenticationEntityId;

        String remoteContent = "NO VALUE";
        StatusLine statusLine = null;

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "text/xml");
            httpPost.setEntity(new StringEntity(xmlRequest));

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
            return remoteContent;
        } catch (Throwable e) {
            LOG.error("Exception from peach payments API " + e+" "+remoteContent);
            return null;
        }
    }
}

