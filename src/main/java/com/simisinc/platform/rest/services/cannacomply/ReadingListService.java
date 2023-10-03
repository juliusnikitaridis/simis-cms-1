package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Reading;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ReadingRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ReadingSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 *
 * @author Julius Nikitaridis
 * @created 25/04/23 11:28 AM
 */
public class ReadingListService {

    private static Log LOG = LogFactory.getLog(ReadingListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception("User does not have required roles to access API");
            }
            String readingId = context.getParameter("readingId");
            String deviceId = context.getParameter("deviceId");
            String farmId = context.getParameter("farmId");

            ReadingSpecification specification = new ReadingSpecification();

            if(null!= deviceId) {
                specification.setDeviceId(deviceId);
            }
            if(null != readingId) {
                specification.setId(readingId);
            }
            if(farmId != null) {
                specification.setFarmId(farmId);
            }

            List<Reading> readingsList = (List<Reading>) ReadingRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Reading List", readingsList, null);
            response.setData(readingsList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in ReadingListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}