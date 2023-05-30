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
import java.util.Random;
import java.util.UUID;


/**
 * Description
 * Service that will create vehicles. at this point vehicles can not be updated
 * @author Julius Nikitaridis
 * @created 18/11/22 11:28 AM
 */


public class CreateRFSService {

    private static Log LOG = LogFactory.getLog(CreateRFSService.class);
    private static String REQUEST_FOR_SERVICE="REQUEST_FOR_SERVICE";
    private static String REQUEST_FOR_DIAGNOSTIC ="REQUEST_FOR_DIAGNOSTIC";
    private static String REQUEST_FOR_REPAIR = "REQUEST_FOR_REPAIR";

    public ServiceResponse post(ServiceContext context) {


        try {

            final String serviceRequestId = UUID.randomUUID().toString();
            ObjectMapper mapper = new ObjectMapper();
            ServiceRequest serviceRequest = mapper.readValue(context.getJsonRequest(), ServiceRequest.class);
            if(serviceRequest.getType() == null || !validateServiceRequestType(serviceRequest.getType())) {
                throw new Exception("Service request type not recognized ::"+serviceRequest.getType());
            }
            serviceRequest.setId(serviceRequestId);
            serviceRequest.setCustomerReference(new RandomToken(6).nextString());

            String imageFileUrl = ServiceUtils.uploadImageFile(context.getRequest());
            if(imageFileUrl != null) {
                serviceRequest.setPictureData(imageFileUrl);
            }
            ServiceRequestRepository.add(serviceRequest);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("service request has been created");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in CreateRFSService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }

    public boolean validateServiceRequestType(String type) {
        if(!type.equalsIgnoreCase(REQUEST_FOR_REPAIR) && !type.equalsIgnoreCase(REQUEST_FOR_SERVICE) && !type.equalsIgnoreCase(REQUEST_FOR_DIAGNOSTIC)) {
            return false;
        }
        return true;
    }

}
