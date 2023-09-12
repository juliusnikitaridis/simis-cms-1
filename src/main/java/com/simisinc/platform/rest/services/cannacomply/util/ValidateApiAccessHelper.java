package com.simisinc.platform.rest.services.cannacomply.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.simisinc.platform.domain.model.cannacomply.ApiAccessRecord;
import com.simisinc.platform.domain.model.cannacomply.ComplianceUser;
import com.simisinc.platform.infrastructure.persistence.cannacomply.APIAccessRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ComplianceUserRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;


public class ValidateApiAccessHelper {


    public static boolean validateAccess(String apiName, ServiceContext context) throws Exception {
        //get the role from compliance user table based on unique_id and the current farm persona
        String farmId = context.getRequest().getHeader("Farm-Id");
        if (farmId == null) {
            throw new Exception("Farm-Id header must be set for all API requests");
        }
        ComplianceUser complianceUser = ComplianceUserRepository.findByUniqueIdAndFarmId(context.getUser().getUniqueId(), farmId);
        if(complianceUser == null) {
            throw new Exception("Compliance user not found !!");
        }
        //get list of allowed roles from api_access table
        ObjectMapper mapper = new ObjectMapper();
        ApiAccessRecord apiAccessRecord = APIAccessRepository.findByApiName(apiName.substring(apiName.lastIndexOf(".")+1));
        if (apiAccessRecord == null) {
            throw new Exception("API permissions not defined in api_access table");
        }
        JsonNode jsonNode = mapper.readTree(apiAccessRecord.getAllowedRoles());
        Iterator<JsonNode> elements = jsonNode.elements();
        ArrayList<String> allowedRolesForApiList = new ArrayList<String>();
        while (elements.hasNext()) {
            JsonNode element = elements.next();
            allowedRolesForApiList.add(element.asText().toUpperCase(Locale.ROOT));
        }

        if (allowedRolesForApiList.contains(complianceUser.getUserRole().toUpperCase())) {
            return true;
        } else {
            return false;
        }
    }
}
