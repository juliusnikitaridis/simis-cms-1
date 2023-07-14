package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.events.carfix.ServiceProviderQuoteAcceptedEvent;
import com.simisinc.platform.domain.model.User;
import com.simisinc.platform.domain.model.carfix.Quote;
import com.simisinc.platform.domain.model.carfix.ServiceRequest;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.database.DB;
import com.simisinc.platform.infrastructure.persistence.UserRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.QuoteRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceRequestRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.VehicleRepository;
import com.simisinc.platform.infrastructure.workflow.WorkflowManager;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

        try {
            ObjectMapper mapper = new ObjectMapper();
            AcceptQuoteRequest request = mapper.readValue(context.getJsonRequest(), AcceptQuoteRequest.class);

            //update quote status once it has been accepted by the member
            QuoteRepository.updateQuoteStatus(request.getAcceptedQuoteId(), "ACCEPTED");

            //updaete service request status once a quote has been accepted - also update confirmed date here
            QuoteRepository.updateServiceRequestStatus(request.getServiceRequestId(),"ACCEPTED", request.getConfirmedDate());

            //insert the quoteId of the accepted quote into the service request table
            QuoteRepository.updateServiceRequestAcceptedQuoteId(request.getAcceptedQuoteId(),request.getServiceRequestId());

            ////need to update which service providers quote has been accepted for this service request
            QuoteRepository.updateAcceptedServiceProviderId(request.getConfirmedServiceProviderId(), request.getServiceRequestId());

            //find all other quotes for this SR and set the status to rejected.
            QuoteRepository.declineOtherQuotes(request.getServiceRequestId(),request.getAcceptedQuoteId(),"REJECTED");

            //send mail to the SP attached to the serviceRequestId
            sendQuoteAcceptedMailToSp(request);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("quote has been Accepted");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in CreateQuoteService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }



    public void sendQuoteAcceptedMailToSp(AcceptQuoteRequest request) throws Exception {
        ServiceProviderQuoteAcceptedEvent event = new ServiceProviderQuoteAcceptedEvent();
        ServiceRequest serivceRequest = ServiceRequestRepository.findById(request.getServiceRequestId());
        event.setConfirmedDate(serivceRequest.getConfirmedDate());
        //vehicle info
        Vehicle vehicle = VehicleRepository.findById(serivceRequest.getVehicleId());
        if(vehicle == null) {
            throw new Exception("vehicle could not be found for service request");
        }
        event.setVehicleMakeModel(vehicle.getMake()+" "+vehicle.getModel());
        //sp user info
        User spUser = UserRepository.findByUniqueId(request.getConfirmedServiceProviderId());
        event.setServiceProviderUser(spUser);
        //member info
        User member = UserRepository.findByUniqueId(serivceRequest.getMemberId());
        event.setCustomerFullName(member.getFirstName()+" "+ member.getLastName());
        event.setServiceProviderName(spUser.getFirstName());
        //quote info
        Quote quote = QuoteRepository.findById(request.getAcceptedQuoteId());
        event.setQuoteCreatedDate(quote.getCreatedDate());
        WorkflowManager.triggerWorkflowForEvent(event);
    }
}


@Data
class AcceptQuoteRequest {
    private String serviceRequestId; // the service request that this quote coresponds to
    private String acceptedQuoteId; // the id of the actual quote being accepted
    private String confirmedServiceProviderId; //service provider who gave a quote that was accepted
    private String confirmedDate;
}
