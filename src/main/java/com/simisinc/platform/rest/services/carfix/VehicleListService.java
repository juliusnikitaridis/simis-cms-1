package com.simisinc.platform.rest.services.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.persistence.carfix.VehicleRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.VehicleSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Description
 * Service that will return entries from the vehicles table
 * 1. get all vehicles for a memberId
 * 2. get vehicle for a particular id
 *
 * @author Julius Nikitaridis
 * @created 01/11/22 11:28 AM
 */
public class VehicleListService {

    private static Log LOG = LogFactory.getLog(VehicleListService.class);

    public ServiceResponse get(ServiceContext context) {


        try {
            VehicleListServiceRequest request = new ObjectMapper().readValue(context.getJsonRequest(), VehicleListServiceRequest.class);
            VehicleSpecification specification = new VehicleSpecification();

            //set the memberId
            if(null!= request.getMemberId()) {
                specification.setMemberId(request.getMemberId());
            } else if (null != request.getVehicleId()) {
                specification.setVehicleId(Long.valueOf(request.getVehicleId()));
            }
            List<Vehicle> vehiclesList = (List<Vehicle>) VehicleRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Vehicles List", vehiclesList, null);
            response.setData(vehiclesList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in VehicleListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }

    }

}

@Data
class VehicleListServiceRequest {
    private String memberId;
    private String vehicleId;
}