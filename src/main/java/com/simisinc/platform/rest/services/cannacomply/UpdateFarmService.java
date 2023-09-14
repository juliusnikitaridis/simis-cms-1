package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.Crop;
import com.simisinc.platform.domain.model.cannacomply.Farm;
import com.simisinc.platform.infrastructure.persistence.cannacomply.CropRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.FarmRepository;
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


public class UpdateFarmService {

    private static Log LOG = LogFactory.getLog(UpdateFarmService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception("User does not have required roles to access API");
            }

            ObjectMapper mapper = new ObjectMapper();
            Farm farm = mapper.readValue(context.getJsonRequest(), Farm.class);

            FarmRepository.update(farm);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("farm has been updated");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in UpdateFarmService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }

}
