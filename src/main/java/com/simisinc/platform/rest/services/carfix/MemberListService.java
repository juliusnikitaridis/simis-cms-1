package com.simisinc.platform.rest.services.carfix;

import com.simisinc.platform.domain.model.User;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.persistence.UserRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.VehicleRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.VehicleSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * @author Julius Nikitaridis
 * @created 01/11/23 11:28 AM
 */
public class MemberListService {

    private static Log LOG = LogFactory.getLog(MemberListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            String memberId = context.getParameter("memberId");
            if(memberId == null) {
                LOG.error("Error in MemberListService. No parameters set in request");
                ServiceResponse response = new ServiceResponse(400);
                response.getError().put("title", "member unique id has not been set");
                return response;
            }
            User user = UserRepository.findByUniqueIdForAPi(memberId);

            ServiceResponse response = new ServiceResponse(200);
            List<User> userList = new ArrayList<>();
            userList.add(user);
            ServiceResponseCommand.addMeta(response, "Member List", userList, null);
            response.setData(user);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in MemberListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}