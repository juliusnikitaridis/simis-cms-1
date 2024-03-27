package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.FeedingRegime;
import com.simisinc.platform.domain.model.cannacomply.Fuel;
import com.simisinc.platform.infrastructure.persistence.cannacomply.FeedingRegimeRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.FuelRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;


/**
 * @author Julius Nikitaridis
 * @created 25/04/23 11:28 AM
 */


public class UpdateFeedingRegimeService {

    private static Log LOG = LogFactory.getLog(UpdateFeedingRegimeService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            ObjectMapper mapper = new ObjectMapper();
            FeedingRegime regime = mapper.readValue(context.getJsonRequest(), FeedingRegime.class);
            FeedingRegimeRepository.update(regime);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("Feeding regime has has been updated.");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());

        }
    }
}