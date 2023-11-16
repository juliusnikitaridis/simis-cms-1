package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.application.admin.LoadSitePropertyCommand;
import com.simisinc.platform.domain.model.cannacomply.Block;
import com.simisinc.platform.domain.model.cannacomply.UserUpload;
import com.simisinc.platform.infrastructure.persistence.cannacomply.BlockRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.UserUploadRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
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
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            String uploadDir = LoadSitePropertyCommand.loadByName("useruploads.upload.dir");
            if(uploadDir == null) {
                throw new Exception(ErrorMessageStatics.ERR_08);
            }

            Part jsonPart = context.getRequest().getPart("json");
            if(jsonPart == null) {
                throw new Exception(ErrorMessageStatics.ERR_09);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(jsonPart.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            ObjectMapper mapper = new ObjectMapper();
            UserUpload userUploadRequest = mapper.readValue(sb.toString(), UserUpload.class);

            if(userUploadRequest.getFarmId() != null && userUploadRequest.getReferenceId() != null) {
                //user upload should belong to a farm OR and activity/issue - not both.
                //we want to use user uploads for images aswell
                throw new Exception("farm_id and reference_id can not both be set");
            }
            int counter = 0;
            for (Part part : context.getRequest().getParts()) {
                if (part.getName().equalsIgnoreCase("FILE")) {
                    String filePath = uploadDir + "/" + part.getSubmittedFileName();
                    part.write(filePath);
                    UserUpload userUpload = createUserUploadRecord(userUploadRequest, part, filePath);
                    UserUploadRepository.add(userUpload);
                    counter++;
                }
            }
            if(counter == 0) {
                throw new Exception(ErrorMessageStatics.ERR_10);
            }

            ServiceResponse response = new ServiceResponse(200);
            int finalCounter = counter;
            ArrayList<String> responseMessage = new ArrayList<String>() {{
                add("user upload has been created"+ finalCounter +" files uploaded");
            }};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());

        }
    }

    public UserUpload createUserUploadRecord(UserUpload uploadRequest, Part currentPart, String currentFilePath) throws Exception {
        UserUpload userUploadEntry = new UserUpload();
        String createdDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
        userUploadEntry.setCreatedBy(uploadRequest.getCreatedBy());
        userUploadEntry.setName(uploadRequest.getName());
        userUploadEntry.setCreatedDate(createdDate);
        userUploadEntry.setReferenceId(uploadRequest.getReferenceId());
        userUploadEntry.setFileName(currentPart.getSubmittedFileName());
        userUploadEntry.setFilePath(currentFilePath);
        userUploadEntry.setDescription(uploadRequest.getDescription());
        userUploadEntry.setFileSize(String.valueOf(currentPart.getSize()));
        userUploadEntry.setFileType(currentPart.getContentType());
        userUploadEntry.setId(UUID.randomUUID().toString());
        userUploadEntry.setFarmId(uploadRequest.getFarmId());

        return userUploadEntry;
    }
}