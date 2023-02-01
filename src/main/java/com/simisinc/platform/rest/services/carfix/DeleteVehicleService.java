package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.database.DB;
import com.simisinc.platform.infrastructure.persistence.carfix.VehicleRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.UUID;


/**
 * Description
 * Service that will delete vehicles.
 * @author Julius Nikitaridis
 * @created 18/11/22 11:28 AM
 */


public class DeleteVehicleService {

    private static Log LOG = LogFactory.getLog(DeleteVehicleService.class);

    public ServiceResponse post(ServiceContext context) {


        try {
            ObjectMapper mapper = new ObjectMapper();
            DeleteVehicleRequest request = mapper.readValue(context.getJsonRequest(),DeleteVehicleRequest.class);
            String vehicleId = request.getVehicleId();

            VehicleRepository.delete(vehicleId);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("vehicle has been deleted");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in DeleteVehicleService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }

}


@Data
class DeleteVehicleRequest {
    String vehicleId;
}
