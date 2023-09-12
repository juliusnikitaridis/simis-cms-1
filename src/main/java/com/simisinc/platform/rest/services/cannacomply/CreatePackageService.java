package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.Farm;
import com.simisinc.platform.domain.model.cannacomply.Packaging;
import com.simisinc.platform.infrastructure.persistence.cannacomply.FarmRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.PackageRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.UUID;


/**
 * @author Julius Nikitaridis
 * @created 18/11/22 11:28 AM
 */


public class CreatePackageService {

    private static Log LOG = LogFactory.getLog(CreatePackageService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(ActivityListService.class.getName(),context)) {
                throw new Exception("User does not have required roles to access API");
            }

            ObjectMapper mapper = new ObjectMapper();
            Packaging packageO = mapper.readValue(context.getJsonRequest(), Packaging.class);
            packageO.setId(UUID.randomUUID().toString());

            PackageRepository.add(packageO);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("Packaging has been created");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in CreatePackageService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}
