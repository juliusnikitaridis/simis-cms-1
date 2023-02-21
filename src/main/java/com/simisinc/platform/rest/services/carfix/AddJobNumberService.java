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

public class AddJobNumberService {

    private static Log LOG = LogFactory.getLog(UpdateServiceRequestStatusService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            AddJobNumberRequest request = mapper.readValue(context.getJsonRequest(), AddJobNumberRequest.class);
            ServiceRequestRepository.addJobNumber(request.getJobNumber(),request.getServiceRequestId(), DB.getConnection());

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("Service Job Number has been updated ");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in AddJobNumberService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}

@Data
class AddJobNumberRequest {
    private String serviceRequestId;
    private String jobNumber;
}

