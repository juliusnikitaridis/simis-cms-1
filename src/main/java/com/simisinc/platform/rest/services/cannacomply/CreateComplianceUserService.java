package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.ComplianceUser;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ComplianceUserRepository;
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


public class CreateComplianceUserService {

    private static Log LOG = LogFactory.getLog(CreateComplianceUserService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }
            ObjectMapper mapper = new ObjectMapper();
            ComplianceUser complianceUser = mapper.readValue(context.getJsonRequest(), ComplianceUser.class);
            String id = UUID.randomUUID().toString();
            if(complianceUser.getStatus() == null) {
                throw new Exception("compliance user status value is null");
            }
            if(complianceUser.getFarmId() == null) {
                throw new Exception("compliance user farm id is null");
            }
            if(complianceUser.getUserRole() == null) {
                throw new Exception("compliance user role is null");
            }
            complianceUser.setUuid(id);
            ComplianceUserRepository.add(complianceUser);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add(id);}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in CreateComplianceUser service", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}

// updateQ