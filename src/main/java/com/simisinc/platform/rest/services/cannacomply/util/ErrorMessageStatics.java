package com.simisinc.platform.rest.services.cannacomply.util;

import com.simisinc.platform.rest.controller.ServiceResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ErrorMessageStatics {

    private static Log LOG = LogFactory.getLog(ErrorMessageStatics.class);

    public static String ERR_01 = "User does not have required roles to access API";
    //CalculateAverageForReadingService
    public static String ERR_02 = "reading type not recognized. values are [temperature,humidity]";
    //CreateBlockService
    public static String ERR_03(String blockId, String farmId){
        return "Block with location_id"+blockId+" already exists for farm"+farmId;
    }

    //createComplianceUserService
    public static String ERR_04 = "compliance user status value is null";
    public static String ERR_05 = "compliance user farm id is null";
    public static String ERR_06 = "compliance user role is null";

    //CreateLocationService
    public static final String ERR_07 = "Location with name already exists";

    //createUserUploadService
    public static final String ERR_08 = "useruploads.upload.dir has not been configure in site properties table";
    public static final String ERR_09 ="json part has not been set in request";
    public static final String ERR_10 = "no files detected in multipart request";
    //DeleteUserUploadService
    public static String ERR_11 = "could not find user upload to delete";
    public static String ERR_12 = "user upload file could not be deleted";

    //RegisterComplianceUserSErvice
    public static String ERR_13 ="user could not be saved when calling RegisterComplianceUserService";
    public static String ERR_14 = "Block already exists for farm";
    public static String ERR_15 = "Compliance user has already been assigned to this farm with a role";


    public static ServiceResponse handleException(Exception e, Class clazz) {
        LOG.error("Error in "+clazz.getName(), e);
        ServiceResponse response = new ServiceResponse(500);
        response.getError().put("title", e.getMessage());
        response.stackStrace = ExceptionUtils.getStackTrace(e);
        return response;
    }
}
