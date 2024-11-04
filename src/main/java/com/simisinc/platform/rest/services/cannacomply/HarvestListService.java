package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Harvest;
import com.simisinc.platform.domain.model.cannacomply.Yield;
import com.simisinc.platform.infrastructure.persistence.cannacomply.HarvestRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.HarvestSpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.YieldRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.YieldSpecification;
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
public class HarvestListService {

    private static Log LOG = LogFactory.getLog(HarvestListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            String farmId = context.getParameter("farmId");
            String batchNumber = context.getParameter("batchNumber");
            String containerNumber = context.getParameter("containerNumber");

            HarvestSpecification specification = new HarvestSpecification();

            if(null!= containerNumber) {
                specification.setContainerNumber(containerNumber);
            } else if (null != farmId) {
                specification.setFarmId(farmId);
            } else if (null != batchNumber) {
                specification.setBatchNumber(batchNumber);
            }
            List<Harvest> yieldList = (List<Harvest>) HarvestRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Harvest List", yieldList, null);
            response.setData(yieldList);
            return response;
        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());

        }
    }
}