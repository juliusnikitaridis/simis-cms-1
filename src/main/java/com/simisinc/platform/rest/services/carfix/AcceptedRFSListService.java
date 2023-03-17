package com.simisinc.platform.rest.services.carfix;

import com.simisinc.platform.domain.model.carfix.ServiceRequest;
import com.simisinc.platform.infrastructure.database.DataResult;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceRequestRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceRequestSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class AcceptedRFSListService {
    private static Log LOG = LogFactory.getLog(RFSListService.class);

    public ServiceResponse get(ServiceContext context) {

        try{
            final String serviceProviderUniqueId = context.getParameter("serviceProviderUniqueId");
            ServiceRequestSpecification specification = new ServiceRequestSpecification();
            specification.setServiceProviderId(serviceProviderUniqueId);
            DataResult result = ServiceRequestRepository.AcceptedSPQuotes(serviceProviderUniqueId,null);
            List<ServiceRequest> serviceRequestList = (List<ServiceRequest>) result.getRecords();

            ServiceResponse response = new ServiceResponse(200);
            response.setData(serviceRequestList);
            return response;

        }catch (Exception e) {
            LOG.error("Error in AcceptedRFSListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }

    }
