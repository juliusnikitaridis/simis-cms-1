package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.infrastructure.persistence.cannacomply.ReadingRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Julius Nikitaridis
 * @created 25/04/23 11:28 AM
 */
public class CalculateAverageForReadingsService {

    private static Log LOG = LogFactory.getLog(CalculateAverageForReadingsService.class);

    public ServiceResponse get(ServiceContext context) throws Exception {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception("User does not have required roles to access API");
            }
            String farmId = context.getParameter("farmId");
            String locationId = context.getParameter("locationId");
            String readingType = context.getParameter("readingType");

            if(!readingType.equalsIgnoreCase("temperature") && !readingType.equalsIgnoreCase("humidity")) {
                throw new Exception("reading type not recognized. values are [temperature,humidity]");
            }
            String deviceType = null;
            if(readingType.equalsIgnoreCase("temperature")) {
                deviceType = "thermometer";
            } else if(readingType.equalsIgnoreCase("humidity")) {
                deviceType = "humidity meter";
            }
            //get all devices for this location
            List<String> deviceIdList =  ReadingRepository.getAllDevicesForLocation(farmId,deviceType,locationId);
            double latestReadingTotal = 0.0;
            int totalDevices = 0;

            for(String deviceId : deviceIdList) {
                latestReadingTotal += ReadingRepository.getLatestReadingForDevice(deviceId,readingType);
                totalDevices++;
            }
            double average = latestReadingTotal/totalDevices;
            List<Double> result = new ArrayList<>();
            result.add(average);

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Average",result, null);
            response.setData(result);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in CalculateAverageForReadingsService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}