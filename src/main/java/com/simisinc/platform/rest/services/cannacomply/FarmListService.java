package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Farm;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.persistence.cannacomply.FarmRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.FarmSpecification;
import com.simisinc.platform.infrastructure.persistence.carfix.VehicleRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.VehicleSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Description
 * Service that will return all farms or details for a particular farm
 *
 * @author Julius Nikitaridis
 * @created 01/11/22 11:28 AM
 */
public class FarmListService {

    private static Log LOG = LogFactory.getLog(FarmListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
//            if(!ValidateApiAccessHelper.validateAccess(ActivityListService.class.getName(),context)) {
//                throw new Exception("User does not have required roles to access API");
//            }
            String farmId = context.getParameter("farmId");
            String userId = context.getParameter("userId");

            FarmSpecification specification = new FarmSpecification();

            if(null!= farmId) {
                specification.setId(farmId);
            }
            if(null!= userId) {
                specification.setUserId(userId);
            }
            List<Farm> farmList = (List<Farm>) FarmRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Farm List", farmList, null);
            response.setData(farmList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in FarmListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}