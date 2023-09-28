package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.Block;
import com.simisinc.platform.domain.model.cannacomply.UserUpload;
import com.simisinc.platform.infrastructure.persistence.cannacomply.BlockRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * @author Julius Nikitaridis
 * @created 25/04/23 11:28 AM
 */


public class CreateUserUploadService {

    private static Log LOG = LogFactory.getLog(CreateUserUploadService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if (!ValidateApiAccessHelper.validateAccess(this.getClass().getName(), context)) {
                throw new Exception("User does not have required roles to access API");
            }
            String uploadDir = "/home/julius/ccUploads";

            Part jsonPart = context.getRequest().getPart("json");
            if(jsonPart == null) {
                throw new Exception("json part has not been set in request");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(jsonPart.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            ObjectMapper mapper = new ObjectMapper();
            UserUpload userUploadRequest = mapper.readValue(sb.toString(), UserUpload.class);

            int counter = 0;
            for (Part part : context.getRequest().getParts()) {
                if (part.getName().equalsIgnoreCase("FILE")) {
                    String filePath = uploadDir + "/" + part.getSubmittedFileName();
                    part.write(filePath);
                    UserUpload userUpload = createUserUploadRecord(userUploadRequest, part, filePath);
                    counter++;
                }
            }
            if(counter == 0) {
                throw new Exception("no files detected in multipart request");
            }

            ServiceResponse response = new ServiceResponse(200);
            int finalCounter = counter;
            ArrayList<String> responseMessage = new ArrayList<String>() {{
                add("user upload has been created"+ finalCounter +" files uploaded");
            }};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in CreateUserUploadService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }

    public UserUpload createUserUploadRecord(UserUpload uploadRequest, Part currentPart, String currentFilePath) throws Exception {
        UserUpload userUploadEntry = new UserUpload();
        String createdDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
        userUploadEntry.setCreatedBy(uploadRequest.getCreatedBy());
        userUploadEntry.setName(uploadRequest.getName());
        userUploadEntry.setCreatedDate(createdDate);
        userUploadEntry.setFileName(currentPart.getSubmittedFileName());
        userUploadEntry.setFilePath(currentFilePath);
        userUploadEntry.setDescription(uploadRequest.getDescription());
        userUploadEntry.setFileSize(String.valueOf(currentPart.getSize()));
        userUploadEntry.setFileType(uploadRequest.getFileType());
        userUploadEntry.setId(UUID.randomUUID().toString());

        return userUploadEntry;
    }
}