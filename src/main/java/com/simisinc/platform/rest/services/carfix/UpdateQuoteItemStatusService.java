package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.infrastructure.persistence.carfix.QuoteRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;


/**
 * Description
 * update the status of a quote item
 * @author Julius Nikitaridis
 * @created 18/11/22 11:28 AM
 */


public class UpdateQuoteItemStatusService {

    private static Log LOG = LogFactory.getLog(UpdateQuoteItemStatusService.class);

    public ServiceResponse post(ServiceContext context) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            UpdateQuoteItemStatusServiceReq request = mapper.readValue(context.getJsonRequest(), UpdateQuoteItemStatusServiceReq.class);
            //TODO check if the item was actually found
            QuoteRepository.updateQuoteItemStatus(request.getQuoteId(),request.getQuoteItemId(),request.getStatus());

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("Quote item status been updated");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in UpdateQuoteItemStatusService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }

}

@Data
class UpdateQuoteItemStatusServiceReq {
    String quoteId;
    String quoteItemId;
    String status;
}
