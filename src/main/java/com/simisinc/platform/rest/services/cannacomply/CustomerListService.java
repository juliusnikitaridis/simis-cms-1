package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Customer;
import com.simisinc.platform.domain.model.cannacomply.GrowthCycle;
import com.simisinc.platform.infrastructure.persistence.cannacomply.CustomerRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.CustomerSpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.GrowthCycleRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.GrowthCycleSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
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
public class CustomerListService {

    private static Log LOG = LogFactory.getLog(CustomerListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception("User does not have required roles to access API");
            }
            String farmId = context.getParameter("farmId");
            String customerId = context.getParameter("customerId");

            CustomerSpecification specification = new CustomerSpecification();

            if(farmId != null){
                specification.setFarmId(farmId);
            }
            if(customerId != null) {
                specification.setId(customerId);
            }
            List<Customer> cycleList = (List<Customer>) CustomerRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "CustomerList", cycleList, null);
            response.setData(cycleList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in CustomerListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}