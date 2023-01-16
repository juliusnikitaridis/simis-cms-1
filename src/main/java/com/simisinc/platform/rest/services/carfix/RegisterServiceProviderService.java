package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.User;
import com.simisinc.platform.infrastructure.persistence.UserRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceProviderRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Random;

public class RegisterServiceProviderService {
    private static Log LOG = LogFactory.getLog(RegisterServiceProviderService.class);

    public ServiceResponse post(ServiceContext context) {


        try {

            ObjectMapper mapper = new ObjectMapper();
            User newUser = mapper.readValue(context.getJsonRequest(), User.class);
            newUser.setId(new Random().nextLong());

            UserRepository.add(newUser);
           //ServiceProviderRepository.add();
            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("User has been registered");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in Register Member Service", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}
