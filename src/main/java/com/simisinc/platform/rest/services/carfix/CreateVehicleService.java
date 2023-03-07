package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.persistence.carfix.VehicleRepository;
import com.simisinc.platform.rest.controller.ServiceResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.digest.HmacUtils;
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


public class CreateVehicleService {

    private static Log LOG = LogFactory.getLog(CreateVehicleService.class);

    public ServiceResponse post(ServiceContext context) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            Vehicle newVehicle = mapper.readValue(context.getJsonRequest(), Vehicle.class);
            //newVehicle.setVehicleId(UUID.randomUUID().toString()); //todo seems that PK has to be a long here ??
            newVehicle.setVehicleId(UUID.randomUUID().toString());

            VehicleRepository.add(newVehicle);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("vehicle has been created");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in CreateVehicleService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}
