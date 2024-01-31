package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.WaterManagement;
import com.simisinc.platform.domain.model.cannacomply.WaterSource;
import com.simisinc.platform.infrastructure.persistence.cannacomply.WaterManagementRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.WaterManagementSpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.WaterSourceRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.WaterSourceSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * @author Julius Nikitaridis
 * @created 01/11/22 11:28 AM
 */
public class WaterSourceListService {

    private static Log LOG = LogFactory.getLog(WaterSourceListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            String id = context.getParameter("id");
            String farmId = context.getParameter("farmId");

            WaterSourceSpecification specification = new WaterSourceSpecification();
            if(farmId != null) {
                specification.setFarmId(farmId);
            }

            if(id != null) {
                specification.setId(id);
            }

            List<WaterSource> watersourceList = (List<WaterSource>) WaterSourceRepository.query(specification).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Water Source List", watersourceList, null);
            response.setData(watersourceList);
            return response;
        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());
        }
    }
}