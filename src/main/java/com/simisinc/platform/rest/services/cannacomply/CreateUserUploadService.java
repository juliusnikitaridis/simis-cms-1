package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.Block;
import com.simisinc.platform.infrastructure.persistence.cannacomply.BlockRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.Part;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;


/**
 * @author Julius Nikitaridis
 * @created 25/04/23 11:28 AM
 */


public class CreateUserUploadService {

    private static Log LOG = LogFactory.getLog(CreateUserUploadService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception("User does not have required roles to access API");
            }
            File uploadDir = new File("/home/julius");

            for(Part part : context.getRequest().getParts()) {
                String fileName = part.getName();
                System.out.println(fileName);
            }

//            ObjectMapper mapper = new ObjectMapper();
//            Block block = mapper.readValue(context.getJsonRequest(), Block.class);
//            String blockId = UUID.randomUUID().toString();
//            block.setId(blockId);
//
//            BlockRepository.add(block);
//
//            ServiceResponse response = new ServiceResponse(200);
//            ArrayList<String> responseMessage = new ArrayList<String>(){{add(blockId);}};
//            response.setData(responseMessage);
//            return response;
              return null;

        } catch (Exception e) {
            LOG.error("Error in CreateUserUploadService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}