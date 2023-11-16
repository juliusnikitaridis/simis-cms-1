package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Location;
import com.simisinc.platform.infrastructure.persistence.cannacomply.LocationRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.LocationSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 *
 * @author Julius Nikitaridis
 * @created 25/04/23 11:28 AM
 */
public class LocationListService {

    private static Log LOG = LogFactory.getLog(LocationListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }
            String farmId = context.getParameter("farmId");
            String id = context.getParameter("id");

            LocationSpecification specification = new LocationSpecification();

            if(null!= id) {
                specification.setId(id);
            }
            if(null != farmId) {
                specification.setFarmId(farmId);
            }

            List<Location> locationList = (List<Location>) LocationRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Location List", locationList, null);
            response.setData(locationList);
            return response;
        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());

        }
    }
}