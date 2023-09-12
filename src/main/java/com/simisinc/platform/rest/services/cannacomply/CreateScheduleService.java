package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.Schedule;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ScheduleRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.UUID;


/**
 * @author Julius Nikitaridis
 * @created 25/05/23 11:28 AM
 */


public class CreateScheduleService {

    private static Log LOG = LogFactory.getLog(CreateScheduleService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(ActivityListService.class.getName(),context)) {
                throw new Exception("User does not have required roles to access API");
            }

            ObjectMapper mapper = new ObjectMapper();
            Schedule schedule = mapper.readValue(context.getJsonRequest(), Schedule.class);
            String scheduleId = UUID.randomUUID().toString();
            schedule.setId(scheduleId);

            ScheduleRepository.add(schedule);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add(scheduleId);}};
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