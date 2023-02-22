package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.carfix.Quote;
import com.simisinc.platform.domain.model.carfix.QuoteItem;
import com.simisinc.platform.infrastructure.database.DB;
import com.simisinc.platform.infrastructure.persistence.carfix.QuoteRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceRequestRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;


/**
 *  * @author Julius Nikitaridis
 *  * @created 18/11/22 11:28 AM
 *
 * Description
 * Service that will add new quotation items to an existing quote.
 *
 * Some statusus could be
 * ADDITIONAL_WORK_ACCEPTED
 * ADDITIONAL_WORK_DECLINED - always keep these record - items - for history auddit
 *
 * some types
 * STANDARD_ITEM
 * ADDITIONAL_WORK_ITEM
 *
 */


public class AddQuotationItemService {

    private static Log LOG = LogFactory.getLog(AddQuotationItemService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            AddQuotationItemServicesRequest request = mapper.readValue(context.getJsonRequest(), AddQuotationItemServicesRequest.class);
            //TODO should validate that the quote exists!!
            QuoteRepository.addItemsToQuote(request.getQuotationItems(),request.getQuoteId());

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("Item has been added to quote");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in AddQuotationItemService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}


@Data
class AddQuotationItemServicesRequest {
    private String quoteId;
    private QuoteItem[] quotationItems;
}