package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.Block;
import com.simisinc.platform.domain.model.cannacomply.Room;
import com.simisinc.platform.infrastructure.persistence.cannacomply.BlockRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.RoomRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.UUID;


/**
 * @author Julius Nikitaridis
 * @created 25/04/23 11:28 AM
 */


public class CreateRoomService {

    private static Log LOG = LogFactory.getLog(CreateRoomService.class);

    public ServiceResponse post(ServiceContext context) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            Room room = mapper.readValue(context.getJsonRequest(), Room.class);
            String blockId = UUID.randomUUID().toString();
            room.setId(blockId);

            RoomRepository.add(room);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add(blockId);}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in CreateRoomService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}