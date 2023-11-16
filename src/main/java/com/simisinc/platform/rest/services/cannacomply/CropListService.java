package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Crop;
import com.simisinc.platform.domain.model.cannacomply.Farm;
import com.simisinc.platform.infrastructure.persistence.cannacomply.CropRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.CropSpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.FarmRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.FarmSpecification;
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
public class CropListService {

    private static Log LOG = LogFactory.getLog(CropListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }
            String cropId = context.getParameter("cropId");
            String farmId = context.getParameter("farmId");
            String blockId = context.getParameter("blockId");
            String roomId = context.getParameter("roomId");

            CropSpecification specification = new CropSpecification();

            if(null!= cropId) {
                specification.setId(cropId);
            }
            if(null != farmId) {
                specification.setFarmId(farmId);
            }
            if(blockId != null) {
                specification.setBlockId(blockId);
            }
            if(roomId != null) {
                specification.setRoomId(roomId);
            }
            List<Crop> cropList = (List<Crop>) CropRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Crop List", cropList, null);
            response.setData(cropList);
            return response;
        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());
        }
    }
}