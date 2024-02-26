package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.CropData;
import com.simisinc.platform.domain.model.cannacomply.Customer;
import com.simisinc.platform.infrastructure.persistence.cannacomply.CropDataRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.CropDataSpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.CustomerRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julius Nikitaridis
 * @created 21/02/24 11:28 AM
 */
public class UpdateCropDataService {

    private static Log LOG = LogFactory.getLog(UpdateCropDataService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            ObjectMapper mapper = new ObjectMapper();
            CropData data = mapper.readValue(context.getJsonRequest(), CropData.class);
            CropDataRepository.update(data);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("Crop Data has been updated.");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());
        }
    }
}