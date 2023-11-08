package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.GrowthCycle;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ApiAccessRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.GrowthCycleRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.GrowthCycleSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 *
 * @author Julius Nikitaridis
 * @created 01/11/23 11:28 AM
 */
public class ApiAccessListService {

    private static Log LOG = LogFactory.getLog(ApiAccessListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception("User does not have required roles to access API");
            }

            List<GrowthCycle> accessList = (List<GrowthCycle>) ApiAccessRepository.findAll().getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "API access List", accessList, null);
            response.setData(accessList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in ApiAccessListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}