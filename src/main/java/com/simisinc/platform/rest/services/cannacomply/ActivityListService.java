package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Activity;
import com.simisinc.platform.domain.model.cannacomply.Farm;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ActivityRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ActivitySpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.FarmRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.FarmSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * @author Julius Nikitaridis
 * @created 01/11/23 11:28 AM
 */
public class ActivityListService {

    private static Log LOG = LogFactory.getLog(ActivityListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            String activityId = context.getParameter("activityId");

            ActivitySpecification specification = new ActivitySpecification();
            if(null!= activityId) {
                specification.setId(activityId);
            }
            List<Activity> activityList = (List<Activity>) ActivityRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Activity List", activityList, null);
            response.setData(activityList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in ActivityListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}