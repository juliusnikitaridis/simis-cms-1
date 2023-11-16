package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.carfix.Yield;
import com.simisinc.platform.infrastructure.persistence.carfix.YieldRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.YieldSpecification;
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
 * @created 01/07/23 11:28 AM
 */
public class YieldListService {

    private static Log LOG = LogFactory.getLog(YieldListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            String cropId = context.getParameter("cropId");
            String yieldId = context.getParameter("yieldId");
            String farmId = context.getParameter("farmId");

            YieldSpecification specification = new YieldSpecification();

            //set the memberId
            if(null!= yieldId) {
                specification.setId(yieldId);
            } else if (null != cropId) {
                specification.setCropId(cropId);
            } else if (null != farmId) {
                specification.setFarmId(farmId);
            }
            List<Yield> yieldList = (List<Yield>) YieldRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Yield List", yieldList, null);
            response.setData(yieldList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in YieldListService", e);
            ServiceResponse response = new ServiceResponse(500);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}