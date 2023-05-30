package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.application.DataException;
import com.simisinc.platform.application.register.SaveUserCommand;
import com.simisinc.platform.domain.events.cms.UserInvitedEvent;
import com.simisinc.platform.domain.events.cms.UserSignedUpEvent;
import com.simisinc.platform.domain.model.Group;
import com.simisinc.platform.domain.model.Role;
import com.simisinc.platform.domain.model.User;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.persistence.GroupRepository;
import com.simisinc.platform.infrastructure.persistence.RoleRepository;
import com.simisinc.platform.infrastructure.persistence.UserRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.VehicleRepository;
import com.simisinc.platform.infrastructure.workflow.WorkflowManager;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RegisterMemberService {
    private static Log LOG = LogFactory.getLog(RegisterMemberService.class);

    public ServiceResponse post(ServiceContext context) {


        try {

            ObjectMapper mapper = new ObjectMapper();
            User newUser = mapper.readValue(context.getJsonRequest(), User.class);
            addUser(newUser, context.getUserId());

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>() {{
                add("User has been registered");
            }};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in Register Member Service", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }


    //this should add a user - same as the logic in UserFormWidget
    public void addUser(User user, long modifiedById) throws Exception {
        
        ////////////////////TODO this should be removed for prod!!!!!!!
        //user.setValidated(new Timestamp(System.currentTimeMillis()));
        //user.setPassword("$argon2i$v=19$m=65536,t=2,p=1$hH+MUSJwqG6XiF1B4QIjjg$jAiprPhfvTTwL4/imiKUmZis2//YGYcfxNzdm/z5zZw"); //this is dcd673bb-6f82-43c5-979c-df30da062562

        user.setModifiedBy(modifiedById);
        user.setUserType("MEMBER");

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
            throw new Exception("user could not be saved when calling RegisterMemberService");
        }
        // Trigger events - send the registration verification email.
       // WorkflowManager.triggerWorkflowForEvent(new UserInvitedEvent(savedUser,savedUser));
        WorkflowManager.triggerWorkflowForEvent(new UserSignedUpEvent(savedUser));
    }
}
