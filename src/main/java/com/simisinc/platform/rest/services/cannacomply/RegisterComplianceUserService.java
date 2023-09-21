package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.application.register.SaveUserCommand;
import com.simisinc.platform.domain.events.cms.UserInvitedEvent;
import com.simisinc.platform.domain.events.cms.UserSignedUpEvent;
import com.simisinc.platform.domain.model.Group;
import com.simisinc.platform.domain.model.Role;
import com.simisinc.platform.domain.model.User;
import com.simisinc.platform.domain.model.cannacomply.ComplianceUser;
import com.simisinc.platform.infrastructure.persistence.GroupRepository;
import com.simisinc.platform.infrastructure.persistence.RoleRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ComplianceUserRepository;
import com.simisinc.platform.infrastructure.workflow.WorkflowManager;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class RegisterComplianceUserService {
    private static Log LOG = LogFactory.getLog(RegisterComplianceUserService.class);

    public ServiceResponse post(ServiceContext context) {


        try {

            ObjectMapper mapper = new ObjectMapper();
            ComplianceUser newUser = mapper.readValue(context.getJsonRequest(), ComplianceUser.class);
            String newSysUserId = addUser(newUser, context.getUserId());

            newUser.setUuid(UUID.randomUUID().toString());
            newUser.setSysUniqueUserId(newSysUserId);
            ComplianceUserRepository.add(newUser);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>() {{
                add(newSysUserId);
            }};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in RegisterComplianceUser Service", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }


    //this should add a user - same as the logic in UserFormWidget
    public String addUser(User user, long modifiedById) throws Exception {

        user.setModifiedBy(modifiedById);
        user.setUserType(user.getUserType());

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
            throw new Exception("user could not be saved when calling RegisterComplianceUserService");
        }
        // Trigger events - send the registration verification email.
        WorkflowManager.triggerWorkflowForEvent(new UserInvitedEvent(savedUser,savedUser));
        WorkflowManager.triggerWorkflowForEvent(new UserSignedUpEvent(savedUser));
        return savedUser.getUniqueId();
    }
}
