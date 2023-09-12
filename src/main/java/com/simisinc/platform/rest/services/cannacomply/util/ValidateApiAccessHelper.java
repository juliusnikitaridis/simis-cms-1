package com.simisinc.platform.rest.services.cannacomply.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.simisinc.platform.domain.model.User;
import com.simisinc.platform.domain.model.cannacomply.ApiAccessRecord;
import com.simisinc.platform.domain.model.cannacomply.ComplianceUser;
import com.simisinc.platform.infrastructure.persistence.cannacomply.APIAccessRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ComplianceUserRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ValidateApiAccessHelper {


    public static boolean validateAccess(String apiName, User user) throws Exception {
        //get list of roles from compliance user table
        //todo - also pass int he farm id to find the correct ONE compliance user and NOT a list
        List<ComplianceUser> complianceUserList = ComplianceUserRepository.findAllByUniqueId(user.getUniqueId());
        ArrayList<String> complianceUserRoleList = new ArrayList<String>();
        complianceUserList.forEach(x->{
            complianceUserRoleList.add(x.getUserRole());
        });

        //get list of allowed roles from api_access table
        ObjectMapper mapper = new ObjectMapper();
        ApiAccessRecord apiAccessRecord = APIAccessRepository.findByApiName(apiName);
        JsonNode jsonNode = mapper.readTree(apiAccessRecord.getAllowedRoles());
        Iterator<JsonNode> elements = jsonNode.elements();
        ArrayList<String> allowedRolesForApiList = new ArrayList<String>();
        while(elements.hasNext()) {
            JsonNode element = elements.next();
            allowedRolesForApiList.add(element.asText());
        }

        //go through the compliance user list and see if we can match at least one role with the api_access list. This is not
        //ideal as we should also pass a farm ID into this method to know which farm/role combo we are dealing with specifically
        for(String role :complianceUserRoleList) {
            if(allowedRolesForApiList.contains(role)){
                return true;
            }
        }
        return false;
    }
}
