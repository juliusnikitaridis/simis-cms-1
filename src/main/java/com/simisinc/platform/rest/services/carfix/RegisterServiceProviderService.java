package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.application.register.SaveUserCommand;
import com.simisinc.platform.domain.events.cms.UserInvitedEvent;
import com.simisinc.platform.domain.events.cms.UserSignedUpEvent;
import com.simisinc.platform.domain.model.Group;
import com.simisinc.platform.domain.model.Role;
import com.simisinc.platform.domain.model.User;
import com.simisinc.platform.domain.model.carfix.ServiceProvider;
import com.simisinc.platform.infrastructure.persistence.GroupRepository;
import com.simisinc.platform.infrastructure.persistence.RoleRepository;
import com.simisinc.platform.infrastructure.persistence.UserRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceProviderRepository;
import com.simisinc.platform.infrastructure.workflow.WorkflowManager;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RegisterServiceProviderService {
    private static Log LOG = LogFactory.getLog(RegisterServiceProviderService.class);

    public ServiceResponse post(ServiceContext context) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            ServiceProvider newServiceProvider = mapper.readValue(context.getJsonRequest(), ServiceProvider.class);
            newServiceProvider.setServiceProviderId(UUID.randomUUID().toString());

            User userProviderUser = addUser(newServiceProvider);
                newServiceProvider.setUniqueId(userProviderUser.getUniqueId());
            ServiceProviderRepository.add(newServiceProvider);

            // Trigger events - send the registration verification email.
            WorkflowManager.triggerWorkflowForEvent(new UserSignedUpEvent(userProviderUser));
            WorkflowManager.triggerWorkflowForEvent(new UserInvitedEvent(userProviderUser,userProviderUser));

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("Service Provider has been registered");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in RegisterServiceProvider Service", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }


    public User addUser(ServiceProvider user) throws Exception {
        ////////////////////TODO this should be removed for prod!!!!!!!
        //user.setValidated(new Timestamp(System.currentTimeMillis()));
        //user.setPassword("$argon2i$v=19$m=65536,t=2,p=1$hH+MUSJwqG6XiF1B4QIjjg$jAiprPhfvTTwL4/imiKUmZis2//YGYcfxNzdm/z5zZw"); //this is dcd673bb-6f82-43c5-979c-df30da062562

        user.setModifiedBy(1); //this should be the id of the sysadmin user
        user.setUserType("SERVICE_PROVIDER");

        // Populate the roles
        if (user.getRoleId() != null) {
            List<Role> roleList = RoleRepository.findAll();
            if (roleList != null) {
                List<Role> userRoleList = new ArrayList<>();
                for (Role role : roleList) {
                    if (user.getRoleId().equals(String.valueOf(role.getId()))) {
                        LOG.debug("Adding user to role: " + role.getCode());
                        userRoleList.add(role);
                    }
                }
                user.setRoleList(userRoleList);
            }
        }

        // Populate the groups
        if (user.getGroupId() != null) {
            List<Group> groupList = GroupRepository.findAll();
            if (groupList != null) {
                List<Group> userGroupList = new ArrayList<>();
                for (Group group : groupList) {
                    if (user.getGroupId().equals(String.valueOf(group.getId()))) {
                        userGroupList.add(group);
                    }
                }
                user.setGroupList(userGroupList);
            }
        }

        // Save the user
        User savedUser = null;
        savedUser = SaveUserCommand.saveUser(user);
        if (savedUser == null) {
            throw new Exception("user could not be saved when calling RegisterServiceProviderService");
        }
        return savedUser;
    }
}
