package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.Location;
import com.simisinc.platform.domain.model.cannacomply.UserUpload;
import com.simisinc.platform.infrastructure.persistence.cannacomply.LocationRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.UserUploadRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;


/**
 * Description
 * @author Julius Nikitaridis
 * @created 04/05/23 11:28 AM
 */


public class DeleteUserUploadService {

    @Data
    static class ServiceRequest {
        String uploadId;
    }

    private static Log LOG = LogFactory.getLog(DeleteUserUploadService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            ObjectMapper mapper = new ObjectMapper();
            ServiceRequest request = mapper.readValue(context.getJsonRequest(), ServiceRequest.class);

            //get the path to delete the file
            UserUpload upload = UserUploadRepository.findById(request.getUploadId());

            if(upload == null) {
                throw new Exception("could not find user upload to delete");
            }

            if(!UserUploadRepository.delete(request.getUploadId(), upload.getFilePath())) {
                throw new Exception("user upload file could not be deleted");
            }

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("User Upload has been deleted");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in DeleteUserUploadService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}
