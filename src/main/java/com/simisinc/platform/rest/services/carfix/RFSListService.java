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
            final String memberId = context.getParameter("memberId");
            final String vehicleId = context.getParameter("vehicleId");
            final String serviceRequestId = context.getParameter("serviceRequestId");
            //todo - return applicable requests based on SP categories and brands = param = serviceProviderId

            ServiceRequestSpecification specification = new ServiceRequestSpecification();

            if(memberId!= null) {
                specification.setMemberId(memberId);
            }
            else if(vehicleId != null) {
                specification.setVehicleId(vehicleId);
            }
            else if(serviceRequestId != null) {
                specification.setServiceRequestId(serviceRequestId);
            } else {
                //nothing to set in the specification
            }
            DataResult result = ServiceRequestRepository.query(specification,null);
            List<ServiceRequest> serviceRequestList = (List<ServiceRequest>) result.getRecords();

            ServiceResponse response = new ServiceResponse(200);
            response.setData(serviceRequestList);
            return response;

        } catch (Exception e) {
            LOG.error("Error in RFSListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}

