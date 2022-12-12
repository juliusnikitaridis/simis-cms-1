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
            String memberId = context.getParameter("memberId");
            String vehicleId = context.getParameter("vehicleId");

            if(memberId == null && vehicleId == null) {
                LOG.error("Error in VehicleListService. No parameters set in request");
                ServiceResponse response = new ServiceResponse(400);
                response.getError().put("title", "memberId or vehicleId parameter must be set");
                return response;
            }

            VehicleSpecification specification = new VehicleSpecification();

            //set the memberId
            if(null!= memberId) {
                specification.setMemberId(memberId);
            } else if (null != vehicleId) {
                specification.setVehicleId(vehicleId);
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