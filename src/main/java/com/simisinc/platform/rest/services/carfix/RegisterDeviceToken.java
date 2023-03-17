package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.User;
import com.simisinc.platform.domain.model.carfix.DeviceToken;
import com.simisinc.platform.infrastructure.persistence.carfix.DeviceTokenRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;

public class RegisterDeviceToken {
    private static Log LOG = LogFactory.getLog(DeviceToken.class);


    public ServiceResponse post(ServiceContext context) {


        try {

            ObjectMapper mapper = new ObjectMapper();
            DeviceToken deviceToken = mapper.readValue(context.getJsonRequest(), DeviceToken.class);
            DeviceTokenRepository.add(deviceToken);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>() {{
                add("Token has been registered");
            }};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in Register Token Service", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }


}
