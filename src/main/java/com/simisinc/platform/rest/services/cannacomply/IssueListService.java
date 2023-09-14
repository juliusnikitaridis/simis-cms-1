package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Farm;
import com.simisinc.platform.domain.model.cannacomply.Issue;
import com.simisinc.platform.infrastructure.persistence.cannacomply.FarmRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.FarmSpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.IssueRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.IssueSpecification;
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
public class IssueListService {

    private static Log LOG = LogFactory.getLog(IssueListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception("User does not have required roles to access API");
            }
            String id = context.getParameter("issueId");
            String farmId = context.getParameter("farmId");

            IssueSpecification specification = new IssueSpecification();

            if(null!= id) {
                specification.setId(id);
            }
            if(farmId != null){
                specification.setFarmId(farmId);
            }
            List<Issue> issueList = (List<Issue>) IssueRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Issue List", issueList, null);
            response.setData(issueList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in IssueListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}