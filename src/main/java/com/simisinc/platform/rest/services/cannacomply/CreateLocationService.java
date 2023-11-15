package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.Location;
import com.simisinc.platform.infrastructure.database.DataResult;
import com.simisinc.platform.infrastructure.persistence.cannacomply.LocationRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.UUID;


/**
 * @author Julius Nikitaridis
 * @created 25/04/23 11:28 AM
 */


public class CreateLocationService {

    private static Log LOG = LogFactory.getLog(CreateLocationService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            ObjectMapper mapper = new ObjectMapper();
            Location location = mapper.readValue(context.getJsonRequest(), Location.class);

            DataResult result = LocationRepository.findByLocationNameAndFarm(location.getLocationName(),location.getFarmId());
            if(result.getTotalRecordCount() > 0) {
                throw new Exception("Location with name already exists");
            }
            String blockId = UUID.randomUUID().toString();
            location.setId(blockId);

            LocationRepository.add(location);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add(blockId);}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in CreateLocationService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}