package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.carfix.Supplier;
import com.simisinc.platform.domain.model.carfix.Yield;
import com.simisinc.platform.infrastructure.persistence.carfix.SupplierRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.YieldRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.UUID;


/**
 * @author Julius Nikitaridis
 * @created 18/07/23 11:28 AM
 */


public class CreateSupplierService {

    private static Log LOG = LogFactory.getLog(CreateSupplierService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(ActivityListService.class.getName(),context)) {
                throw new Exception("User does not have required roles to access API");
            }
            ObjectMapper mapper = new ObjectMapper();
            Supplier item = mapper.readValue(context.getJsonRequest(), Supplier.class);
            item.setId(UUID.randomUUID().toString());

            SupplierRepository.add(item);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("Supplier has been added");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in CreateSupplierService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}
