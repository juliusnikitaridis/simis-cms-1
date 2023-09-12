package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.User;
import com.simisinc.platform.domain.model.cannacomply.ComplianceUser;
import com.simisinc.platform.domain.model.carfix.Yield;
import com.simisinc.platform.infrastructure.persistence.UserRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ComplianceUserRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.YieldRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.YieldSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julius Nikitaridis
 * @created 01/07/23 11:28 AM
 * This is used to list all the base users for canna comple. this will not return compliance user info
 * the CompplianceUserListService Should be called here
 */
public class UserListService {

    private static Log LOG = LogFactory.getLog(UserListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(ActivityListService.class.getName(),context)) {
                throw new Exception("User does not have required roles to access API");
            }
            List<User> userList = new ArrayList<>();
            String uniqueId = context.getParameter("uniqueId");
            String farmId = context.getParameter("farmId");

            if(uniqueId != null) {
                //todo check this - is now a list of users
                List<ComplianceUser> users = ComplianceUserRepository.findAllByUniqueId(uniqueId);
                userList.addAll(users);
            } else if (farmId != null) {
                userList.addAll(ComplianceUserRepository.findAllByFarmId(farmId));
            }


            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "User List", userList, null);
            response.setData(userList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in UserListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}