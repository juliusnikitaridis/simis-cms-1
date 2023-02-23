package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.carfix.Quote;
import com.simisinc.platform.infrastructure.database.DB;
import com.simisinc.platform.infrastructure.persistence.carfix.QuoteRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceRequestRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.UUID;


/**
 * Description
 * Service that will update the status col of a service request
 * @author Julius Nikitaridis
 * @created 18/11/22 11:28 AM
 */


public class UpdateServiceRequestStatusService {

    private static Log LOG = LogFactory.getLog(UpdateServiceRequestStatusService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            UpdateServiceRequestStatusRequest request = mapper.readValue(context.getJsonRequest(), UpdateServiceRequestStatusRequest.class);
            ServiceRequestRepository.updateStatus(request.getStatus(),request.getServiceRequestId());

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("Service request status has been updated ");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in UpdateServiceRequestStatusService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}


@Data
class UpdateServiceRequestStatusRequest {
    private String serviceRequestId;
    private String status;
}