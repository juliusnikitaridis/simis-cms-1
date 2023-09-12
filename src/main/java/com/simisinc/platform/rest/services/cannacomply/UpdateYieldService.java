package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.Block;
import com.simisinc.platform.domain.model.carfix.Yield;
import com.simisinc.platform.infrastructure.persistence.cannacomply.BlockRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.YieldRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;


/**
 * Description
 * @author Julius Nikitaridis
 * @created 04/05/23 11:28 AM
 */


public class UpdateYieldService {

    private static Log LOG = LogFactory.getLog(UpdateYieldService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(ActivityListService.class.getName(),context)) {
                throw new Exception("User does not have required roles to access API");
            }

            ObjectMapper mapper = new ObjectMapper();
            Yield item = mapper.readValue(context.getJsonRequest(), Yield.class);

            YieldRepository.update(item);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("Yield has been updated");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in UpdateYieldService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}
