package com.simisinc.platform.rest.services.carfix;

import com.simisinc.platform.domain.model.Entity;
import com.simisinc.platform.domain.model.User;
import com.simisinc.platform.domain.model.carfix.ServiceProvider;
import com.simisinc.platform.domain.model.carfix.ServiceRequest;
import com.simisinc.platform.infrastructure.database.DataResult;
import com.simisinc.platform.infrastructure.persistence.UserRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceProviderRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceProviderSpecification;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceRequestRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceRequestSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import lombok.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;


/**
 * Description
 * List all the service providers
 * returns address, name , supported brands , services, about us ,
 * @author Julius Nikitaridis
 * @created 18/11/22 11:28 AM
 */


public class ServiceProviderListService {

    private static Log LOG = LogFactory.getLog(ServiceProviderListService.class);

    public ServiceResponse get(ServiceContext context) {


        try {
            ServiceProviderSpecification specification = new ServiceProviderSpecification();
            String spUniqueId = context.getParameter("serviceProviderUniqueId");
            if(spUniqueId != null) {
                specification.setServiceProviderUniqueId(spUniqueId);
            }

            DataResult result = ServiceProviderRepository.query(specification,null);
            //fet stuff from the user table
            List<ServiceProvider> serviceProviderList = (List<ServiceProvider>) result.getRecords();

            serviceProviderList.forEach(x-> {
                User serviceProviderUser = UserRepository.findByUniqueId(x.getUniqueId());
                x.setLatitude(serviceProviderUser.getLatitude());
                x.setLongitude(serviceProviderUser.getLongitude());
            });

            ServiceResponse response = new ServiceResponse(200);
            response.setData(serviceProviderList);
            return response;

        } catch (Exception e) {
            LOG.error("Error in ServiceProviderListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}



