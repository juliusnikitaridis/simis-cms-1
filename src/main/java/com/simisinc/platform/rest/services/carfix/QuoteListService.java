package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.carfix.Quote;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.persistence.carfix.QuoteRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.QuoteSpecification;
import com.simisinc.platform.infrastructure.persistence.carfix.VehicleRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.VehicleSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Description
 * Service that will return all entries in the vehicles table
 *
 * @author Julius Nikitaridis
 * @created 01/11/22 11:28 AM
 */
public class QuoteListService {

    private static Log LOG = LogFactory.getLog(QuoteRepository.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            QuoteListServiceRequest request = mapper.readValue(context.getJsonRequest(), QuoteListServiceRequest.class);
            QuoteSpecification specification = new QuoteSpecification();

            if(null != request.getQuoteId()) {
                specification.setId(request.getQuoteId());
            } else

            if (null != request.getRequestForServiceId()) {
                specification.setRequestForServiceId(request.getRequestForServiceId()); //TODO should change this to UUID
            } else
            if (null != request.getServiceProviderId()) {
                specification.setServiceProviderId(request.getServiceProviderId());
            }

            List<Quote> quoteList = (List<Quote>) QuoteRepository.query(specification, null).getRecords();
            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "QuoteList", quoteList, null);
            response.setData(quoteList);
            return response;

        } catch (Exception e) {
            LOG.error("Error in QuoteListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}

@Data
class QuoteListServiceRequest {
    String serviceProviderId;
    String requestForServiceId;
    String quoteId;
}

