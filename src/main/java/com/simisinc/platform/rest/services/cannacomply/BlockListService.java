package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Block;
import com.simisinc.platform.domain.model.cannacomply.Crop;
import com.simisinc.platform.infrastructure.persistence.cannacomply.BlockRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.BlockSpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.CropRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.CropSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 *
 * @author Julius Nikitaridis
 * @created 25/04/23 11:28 AM
 */
public class BlockListService {

    private static Log LOG = LogFactory.getLog(BlockListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            String farmId = context.getParameter("farmId");
            String blockId = context.getParameter("blockId");

            BlockSpecification specification = new BlockSpecification();

            if(null != farmId) {
                specification.setFarmId(farmId);
            }
            if(blockId != null) {
                specification.setId(blockId);
            }
            List<Block> blockList = (List<Block>) BlockRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Block List", blockList, null);
            response.setData(blockList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in BlockListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}