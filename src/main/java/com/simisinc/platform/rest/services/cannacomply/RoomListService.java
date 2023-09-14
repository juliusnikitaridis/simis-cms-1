package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Crop;
import com.simisinc.platform.domain.model.cannacomply.Room;
import com.simisinc.platform.infrastructure.persistence.cannacomply.CropRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.CropSpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.RoomRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.RoomSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 *
 * @author Julius Nikitaridis
 * @created 25/04/23 11:28 AM
 */
public class RoomListService {

    private static Log LOG = LogFactory.getLog(RoomListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception("User does not have required roles to access API");
            }
            String farmId = context.getParameter("farmId");
            String id = context.getParameter("id");

            RoomSpecification specification = new RoomSpecification();

            if(null!= id) {
                specification.setId(id);
            }
            if(null != farmId) {
                specification.setFarmId(farmId);
            }

            List<Room> roomList = (List<Room>) RoomRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Room List", roomList, null);
            response.setData(roomList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in RoomListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}