package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.GrowthCycle;
import com.simisinc.platform.domain.model.cannacomply.Issue;
import com.simisinc.platform.infrastructure.persistence.cannacomply.GrowthCycleRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.GrowthCycleSpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.IssueRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.IssueSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
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
public class GrowthCycleListService {

    private static Log LOG = LogFactory.getLog(GrowthCycleListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }
            String farmId = context.getParameter("farmId");

            GrowthCycleSpecification specification = new GrowthCycleSpecification();

            if(farmId != null){
                specification.setFarmId(farmId);
            }
            List<GrowthCycle> cycleList = (List<GrowthCycle>) GrowthCycleRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Growth Cycle List", cycleList, null);
            response.setData(cycleList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in GrowthCycleListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}