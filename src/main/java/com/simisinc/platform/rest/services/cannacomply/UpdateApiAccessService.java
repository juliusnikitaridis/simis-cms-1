package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.ApiAccess;
import com.simisinc.platform.domain.model.cannacomply.GrowthCycle;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ApiAccessRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.GrowthCycleRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;


/**
 * @author Julius Nikitaridis
 * @created 25/04/23 11:28 AM
 */


public class UpdateApiAccessService {

    private static Log LOG = LogFactory.getLog(UpdateApiAccessService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception("User does not have required roles to access API");
            }

            ObjectMapper mapper = new ObjectMapper();
            ApiAccess access = mapper.readValue(context.getJsonRequest(), ApiAccess.class);
            ApiAccessRepository.update(access);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("Api Access has been updated.");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in UpdateApiAccessService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}