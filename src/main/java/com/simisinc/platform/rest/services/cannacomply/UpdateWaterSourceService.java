package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.WaterManagement;
import com.simisinc.platform.domain.model.cannacomply.WaterSource;
import com.simisinc.platform.infrastructure.persistence.cannacomply.WaterManagementRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.WaterSourceRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;


/**
 * @author Julius Nikitaridis
 * @created 25/04/23 11:28 AM
 */


public class UpdateWaterSourceService {

    private static Log LOG = LogFactory.getLog(UpdateWaterSourceService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            ObjectMapper mapper = new ObjectMapper();
            WaterSource ws = mapper.readValue(context.getJsonRequest(), WaterSource.class);
            WaterSourceRepository.update(ws);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("Water Source has been updated.");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());

        }
    }
}