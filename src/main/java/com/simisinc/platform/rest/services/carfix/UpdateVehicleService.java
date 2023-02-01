package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.persistence.carfix.VehicleRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.UUID;


/**
 * Description
 * Service that will create vehicles. at this point vehicles can not be updated
 * @author Julius Nikitaridis
 * @created 18/11/22 11:28 AM
 */


public class UpdateVehicleService {

    private static Log LOG = LogFactory.getLog(UpdateVehicleService.class);

    public ServiceResponse post(ServiceContext context) {


        try {

            ObjectMapper mapper = new ObjectMapper();
            Vehicle newVehicle = mapper.readValue(context.getJsonRequest(), Vehicle.class);

            VehicleRepository.update(newVehicle);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("vehicle has been updated");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in UpdateVehicleService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }

}
