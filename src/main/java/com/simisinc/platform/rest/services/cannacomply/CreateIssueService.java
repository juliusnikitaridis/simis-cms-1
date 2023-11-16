package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.Crop;
import com.simisinc.platform.domain.model.cannacomply.Issue;
import com.simisinc.platform.infrastructure.persistence.cannacomply.CropRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.IssueRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.UUID;


/**
 * @author Julius Nikitaridis
 * @created 25/04/23 11:28 AM
 */


public class CreateIssueService {

    private static Log LOG = LogFactory.getLog(CreateIssueService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }
            ObjectMapper mapper = new ObjectMapper();
            Issue issue = mapper.readValue(context.getJsonRequest(), Issue.class);
            String issueId = UUID.randomUUID().toString();
            issue.setId(issueId);

            IssueRepository.add(issue);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add(issueId);}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());
        }
    }
}

// updateQuoteTotalPrice