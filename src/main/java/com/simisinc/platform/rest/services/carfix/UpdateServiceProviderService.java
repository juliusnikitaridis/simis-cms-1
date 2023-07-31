package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.User;
import com.simisinc.platform.domain.model.carfix.ServiceProvider;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.persistence.UserRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceProviderRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.VehicleRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;


/**
 * Description
 * update anything on a service provider record
 * @author Julius Nikitaridis
 * @created 18/11/22 11:28 AM
 */


public class UpdateServiceProviderService {

    private static Log LOG = LogFactory.getLog(UpdateServiceProviderService.class);

    public ServiceResponse post(ServiceContext context) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            ServiceProvider newServiceProvider = mapper.readValue(context.getJsonRequest(), ServiceProvider.class);
            //update stuff in user table
            ServiceProvider existingServiceProvider = ServiceProviderRepository.findById(newServiceProvider.getServiceProviderId());
            if(newServiceProvider.getLongitude()!= 0.0 && newServiceProvider.getLatitude() != 0.0) {
                UserRepository.updateLatAndLong(existingServiceProvider.getUniqueId(),newServiceProvider.getLatitude(),newServiceProvider.getLongitude());
            }
            //TODO should check if the SP exists - check if the record was actually updated
            ServiceProviderRepository.update(newServiceProvider);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("Service Provider has been updated");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in UpdateServiceProviderService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }

}
