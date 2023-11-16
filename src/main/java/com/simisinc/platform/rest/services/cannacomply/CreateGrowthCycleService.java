package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.GrowthCycle;
import com.simisinc.platform.domain.model.cannacomply.Reading;
import com.simisinc.platform.infrastructure.persistence.cannacomply.GrowthCycleRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ReadingRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


/**
 * @author Julius Nikitaridis
 * @created 25/04/23 11:28 AM
 */


public class CreateGrowthCycleService {

    private static Log LOG = LogFactory.getLog(CreateGrowthCycleService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            ObjectMapper mapper = new ObjectMapper();
            GrowthCycle cycle = mapper.readValue(context.getJsonRequest(), GrowthCycle.class);
            String cycleId = UUID.randomUUID().toString();
            cycle.setId(cycleId);

            GrowthCycleRepository.add(cycle);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add(cycleId);}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());
        }
    }
}