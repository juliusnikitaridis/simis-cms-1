package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Schedule;
import com.simisinc.platform.domain.model.cannacomply.UserUpload;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ScheduleRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ScheduleSpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.UserUploadRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.UserUploadSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * @author Julius Nikitaridis
 * @created 01/11/23 11:28 AM
 */
public class UserUploadListService {

    private static Log LOG = LogFactory.getLog(UserUploadListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception("User does not have required roles to access API");
            }
            String id = context.getParameter("id");
            String userId = context.getParameter("userId");
            String farmId = context.getParameter("farmId");
            String referenceId = context.getParameter("referenceId");

            UserUploadSpecification specification = new UserUploadSpecification();
            if(null!= farmId) {
                specification.setId(id);
            }
            if(null != farmId) {
                specification.setFarmId(farmId);
            }
            if(null != userId) {
                specification.setUserId(userId);
            }
            if(null != referenceId) {
                specification.setReferenceId(referenceId);
            }
            List<UserUpload> farmList = (List<UserUpload>) UserUploadRepository.query(specification, null).getRecords();

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