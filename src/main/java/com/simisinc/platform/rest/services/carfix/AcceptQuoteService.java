package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.carfix.Quote;
import com.simisinc.platform.infrastructure.database.DB;
import com.simisinc.platform.infrastructure.persistence.carfix.QuoteRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.UUID;


/**
 * Description
 * service that will alow a user to accept a quote from a service provider
 * @author Julius Nikitaridis
 * @created 18/11/22 11:28 AM
 */


public class AcceptQuoteService {

    private static Log LOG = LogFactory.getLog(AcceptQuoteService.class);

    public ServiceResponse post(ServiceContext context) {

        try (Connection conn = DB.getConnection()){
            ObjectMapper mapper = new ObjectMapper();
            AcceptQuoteRequest request = mapper.readValue(context.getJsonRequest(), AcceptQuoteRequest.class);

            //update quote status once it has been accepted by the member
            QuoteRepository.updateQuoteStatus(request.getAcceptedQuoteId(), "ACCEPTED",conn);

            //updaete service request status once a quote has been accepted - also update confirmed date here
            QuoteRepository.updateServiceRequestStatus(request.getServiceRequestId(),conn,"ACCEPTED");

            //insert the quoteId of the accepted quote into the service request table
            QuoteRepository.updateServiceRequestAcceptedQuoteId(request.getAcceptedQuoteId(),request.getServiceRequestId(),conn);

            ////need to update which service providers quote has been accepted for this service request
            QuoteRepository.updateAcceptedServiceProviderId(request.getConfirmedServiceProviderId(), request.getServiceRequestId(),conn);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("quote has been created");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in CreateQuoteService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}


@Data
class AcceptQuoteRequest {
    private String serviceRequestId; // the service request that this quote coresponds to
    private String acceptedQuoteId; // the id of the actual quote being accepted
    private String confirmedServiceProviderId; //service provider who gave a quote that was accepted
    private String confirmedDate;
}
