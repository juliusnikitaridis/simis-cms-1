package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.carfix.ServiceRequest;
import com.simisinc.platform.infrastructure.database.DataResult;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceRequestRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceRequestSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Description
 * Service to return all service records created. if a member id is set, return the service records for that member
 * @author Julius Nikitaridis
 * @created 18/11/22 11:28 AM
 */


public class RFSListService {

    private static Log LOG = LogFactory.getLog(RFSListService.class);

    public ServiceResponse get(ServiceContext context) {


        try {

            ObjectMapper mapper = new ObjectMapper();
            RFSListServiceRequest request = mapper.readValue(context.getJsonRequest(), RFSListServiceRequest.class);
            ServiceRequestSpecification specification = new ServiceRequestSpecification();

            if(request.getMemberId()!= null) {
                specification.setMemberId(request.getMemberId());
            }
            else if(request.getVehicleId()!= null) {
                specification.setVehicleId(request.getVehicleId());
            }
            else if(request.getServiceRequestId() != null) {
                specification.setServiceRequestId(request.getServiceRequestId());
            } else {
                //nothing to set in the specification
            }
            DataResult result = ServiceRequestRepository.query(specification,null);
            List<ServiceRequest> serviceRequestList = (List<ServiceRequest>) result.getRecords();

            ServiceResponse response = new ServiceResponse(200);
            response.setData(serviceRequestList);
            return response;

        } catch (Exception e) {
            LOG.error("Error in CreateRFSService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }

}


@Data
class RFSListServiceRequest {
    String memberId;
    String vehicleId;
    String serviceRequestId;
}
