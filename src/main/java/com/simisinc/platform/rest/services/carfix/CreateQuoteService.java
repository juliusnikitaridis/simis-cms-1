package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.carfix.Quote;
import com.simisinc.platform.domain.model.carfix.ServiceRequest;
import com.simisinc.platform.infrastructure.persistence.carfix.QuoteRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceRequestRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;


/**
 * Description
 * Service that will create Quotes from service providers that are responding to service requests.
 * @author Julius Nikitaridis
 * @created 18/11/22 11:28 AM
 */


public class CreateQuoteService {

    private static Log LOG = LogFactory.getLog(CreateQuoteService.class);

    public ServiceResponse post(ServiceContext context) {


        try {

            final String quoteId = UUID.randomUUID().toString();
            ObjectMapper mapper = new ObjectMapper();
            Quote quote = mapper.readValue(context.getJsonRequest(), Quote.class);
            quote.setId(quoteId);

            QuoteRepository.add(quote);
            QuoteRepository.addItems(quote);

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