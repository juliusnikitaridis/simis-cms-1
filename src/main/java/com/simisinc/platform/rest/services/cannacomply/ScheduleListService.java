package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Farm;
import com.simisinc.platform.domain.model.cannacomply.Schedule;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ActivityRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ActivitySpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ScheduleRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ScheduleSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * @author Julius Nikitaridis
 * @created 01/11/23 11:28 AM
 */
public class ScheduleListService {

    private static Log LOG = LogFactory.getLog(ScheduleListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }
            String scheduleId = context.getParameter("scheduleId");
            String farmId = context.getParameter("farmId");

            ScheduleSpecification specification = new ScheduleSpecification();
            if(null!= scheduleId) {
                specification.setId(scheduleId);
            }
            if(null != farmId) {
                specification.setFarmId(farmId);

            }
            List<Schedule> farmList = (List<Schedule>) ScheduleRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Schedule List", farmList, null);
            response.setData(farmList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in ScheduleListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}