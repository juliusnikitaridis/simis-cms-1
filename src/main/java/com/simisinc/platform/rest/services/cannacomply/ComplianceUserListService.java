package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.ComplianceUser;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ComplianceUserRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ComplianceUserSpecification;
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
 * @created 25/04/23 11:28 AM
 */
public class ComplianceUserListService {

    private static Log LOG = LogFactory.getLog(ComplianceUserListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
//            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
//                throw new Exception(ErrorMessageStatics.ERR_01);
//            }

            String sysUniqueUserId = context.getParameter("sysUniqueUserId");
            String farmId = context.getParameter("farmId");
            String id = context.getParameter("id");
            ComplianceUserSpecification specification = new ComplianceUserSpecification();

            if(null!= sysUniqueUserId) {
                specification.setUniqueSysUserId(sysUniqueUserId);
            }
            if(null != farmId) {
                specification.setFarmId(farmId);
            }
            if(id != null) {
                specification.setId(id);
            }

            List<ComplianceUser> list = (List<ComplianceUser>) ComplianceUserRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Compliance user List", list, null);
            response.setData(list);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in ComplianceUserListService", e);
            ServiceResponse response = new ServiceResponse(500);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}