package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.carfix.ServiceRequest;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceRequestRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.VehicleRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;


/**
 *
 * @author Julius Nikitaridis
 * @created 18/11/23 11:28 AM
 */


public class UpdateServiceRequestService {

    private static Log LOG = LogFactory.getLog(UpdateServiceRequestService.class);

    public ServiceResponse post(ServiceContext context) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            ServiceRequest serviceRequest = mapper.readValue(context.getJsonRequest(), ServiceRequest.class);

            ServiceRequestRepository.update(serviceRequest);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("Service Request has been updated");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in UpdateServiceRequest", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }

}
