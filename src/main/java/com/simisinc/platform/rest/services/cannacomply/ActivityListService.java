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
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.helper.Validate;

import java.util.List;

/**
 * @author Julius Nikitaridis
 * @created 01/11/23 11:28 AM
 */
public class ActivityListService {

    private static Log LOG = LogFactory.getLog(ActivityListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            String activityId = context.getParameter("activityId");
            String farmId = context.getParameter("farmId");
            String blockId = context.getParameter("blockId");
            String locationId = context.getParameter("locationId");
            String itemId = context.getParameter("cropId");


            ActivitySpecification specification = new ActivitySpecification();
            if(null!= activityId) {
                specification.setId(activityId);
            }
            if(null!= farmId) {
                specification.setFarmId(farmId);
            }
            if(blockId != null) {
                specification.setBlockId(blockId);
            }
            if(itemId != null) {
                specification.setItemId(itemId);
            }
            if(locationId != null) {
                specification.setLocationId(locationId);
            }
            List<Activity> activityList = (List<Activity>) ActivityRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Activity List", activityList, null);
            response.setData(activityList);
            return response;
        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());
        }
    }
}