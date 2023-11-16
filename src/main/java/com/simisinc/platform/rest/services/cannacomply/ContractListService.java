package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Contract;
import com.simisinc.platform.domain.model.cannacomply.GrowthCycle;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ContractRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ContractSpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.GrowthCycleRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.GrowthCycleSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Description
 * Service that will return all farms or details for a particular farm
 *
 * @author Julius Nikitaridis
 * @created 01/11/22 11:28 AM
 */
public class ContractListService {

    private static Log LOG = LogFactory.getLog(ContractListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }
            String farmId = context.getParameter("farmId");
            String customerId = context.getParameter("contractId");
            String cycleId = context.getParameter("growthCycleId");

            ContractSpecification specification = new ContractSpecification();

            if(farmId != null){
                specification.setFarmId(farmId);
            }
            if(customerId != null) {
                specification.setCustomerId(customerId);
            }
            if(cycleId != null) {
                specification.setGrowthCycleId(cycleId);
            }
            List<Contract> cycleList = (List<Contract>) ContractRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Contract List", cycleList, null);
            response.setData(cycleList);
            return response;
        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());
        }
    }
}