package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.carfix.ServiceRequest;
import com.simisinc.platform.domain.model.carfix.ServiceRequestItemOption;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceRequestRepository;
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
 * Will return reference data for the options menu when creating service request in the app
 *
 * @author Julius Nikitaridis
 * @created 01/11/22 11:28 AM
 */
public class ServiceRequestOptionsListService {

    private static Log LOG = LogFactory.getLog(ServiceRequestOptionsListService.class);

    public ServiceResponse get(ServiceContext context) {


        try {
            List<ServiceRequestItemOption> optionsList = (List<ServiceRequestItemOption>) ServiceRequestRepository.findOptions().getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Options List", optionsList, null);
            response.setData(optionsList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in ServiceRequestOptionsListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }

    }

}
