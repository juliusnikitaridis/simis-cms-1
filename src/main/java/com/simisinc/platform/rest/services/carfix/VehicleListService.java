package com.simisinc.platform.rest.services.carfix;

import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.persistence.carfix.VehicleRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.VehicleSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;

import java.util.List;

/**
 * Description
 * Service that will return all entries in the vehicles table
 * @author Julius Nikitaridis
 * @created 01/11/22 11:28 AM
 */
public class VehicleListService {

    public ServiceResponse get(ServiceContext context) {

        VehicleSpecification specification = new VehicleSpecification();
        List<Vehicle> vehiclesList = VehicleRepository.findAll(specification,null);

        ServiceResponse response = new ServiceResponse(200);
        ServiceResponseCommand.addMeta(response, "Vehicles List", vehiclesList, null);
        response.setData(vehiclesList);
        return response;

    }

}
