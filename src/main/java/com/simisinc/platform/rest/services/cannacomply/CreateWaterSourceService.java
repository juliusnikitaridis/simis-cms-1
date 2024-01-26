package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.SoilManagement;
import com.simisinc.platform.domain.model.cannacomply.WaterSource;
import com.simisinc.platform.infrastructure.persistence.cannacomply.SoilManagementRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.WaterSourceRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author Julius Nikitaridis
 * @created 25/01/24 11:28 AM
 */
public class CreateWaterSourceService {

    private static Log LOG = LogFactory.getLog(CreateWaterSourceService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }
            ObjectMapper mapper = new ObjectMapper();
            WaterSource waterSource = mapper.readValue(context.getJsonRequest(), WaterSource.class);
            String id = UUID.randomUUID().toString();
            waterSource.setId(id);

            WaterSourceRepository.add(waterSource);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add(id);}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());
        }
    }
}