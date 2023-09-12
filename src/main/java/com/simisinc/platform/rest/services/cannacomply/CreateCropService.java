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
import java.util.UUID;


/**
 * @author Julius Nikitaridis
 * @created 25/04/23 11:28 AM
 */


public class CreateCropService {

    private static Log LOG = LogFactory.getLog(CreateCropService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(ActivityListService.class.getName(),context)) {
                throw new Exception("User does not have required roles to access API");
            }

            ObjectMapper mapper = new ObjectMapper();
            Crop crop = mapper.readValue(context.getJsonRequest(), Crop.class);
            String cropId = UUID.randomUUID().toString();
            crop.setId(cropId);

            CropRepository.add(crop);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add(cropId);}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in CreateCropService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}

// updateQuoteTotalPrice