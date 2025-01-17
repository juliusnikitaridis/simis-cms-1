package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.User;
import com.simisinc.platform.domain.model.carfix.Quote;
import com.simisinc.platform.domain.model.carfix.QuoteItem;
import com.simisinc.platform.domain.model.carfix.ServiceProvider;
import com.simisinc.platform.domain.model.carfix.ServiceRequest;
import com.simisinc.platform.infrastructure.persistence.UserRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.QuoteRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceProviderRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceProviderSpecification;
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
            //calculate values server side
            double vat =0;
            double subTotal = 0;
            double total = 0;

            for(QuoteItem quoteItem : quote.getQuotationItems()) {
                subTotal+=Double.valueOf(quoteItem.getItemTotalPrice());
            }

            vat = subTotal*0.15;
            total += (subTotal + vat); //TODO this vat rate should be configured somewhere

            quote.setQuotationTotal(String.valueOf(total));
            quote.setSubtotal(String.valueOf(subTotal));
            quote.setVat(String.valueOf(vat));
            quote.setDistanceFromSp(String.valueOf(calculateDistanceFromSp(quote)));
            QuoteRepository.add(quote);

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



    //set distance between the SP of the quote and the member of the quote
    public static double calculateDistanceFromSp(Quote quote) throws Exception {
        //get the SP
        User sp = UserRepository.findByUniqueId(quote.getServiceProviderId());

        //get the related SR -
        ServiceRequest sr = ServiceRequestRepository.findById(quote.getRequestForServiceId());
        if(sr.getLatitude() == null || sr.getLongitude() == null) {
            throw new Exception("lat or Long is not set for serviceRequest");
        }
        if(sp.getLatitude() == 0.0 || sp.getLongitude() == 0.0) {
            throw new Exception("lat or long is not set for serviceProvicer");
        }

        double distance = GeoUtils.distanceInKilometers(sp.getLatitude(),sp.getLongitude(),Double.valueOf(sr.getLatitude()),Double.valueOf(sr.getLongitude()));
        LOG.debug("distance has been calcualted for Quote "+distance);
        return distance;
    }
}

// updateQuoteTotalPrice