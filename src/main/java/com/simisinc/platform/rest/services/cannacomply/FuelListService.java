package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Fuel;
import com.simisinc.platform.domain.model.carfix.Supplier;
import com.simisinc.platform.infrastructure.persistence.cannacomply.FuelRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.FuelSpecification;
import com.simisinc.platform.infrastructure.persistence.carfix.SupplierRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.SupplierSpecification;
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
 * @created 01/11/23 11:28 AM
 */
public class FuelListService {

    private static Log LOG = LogFactory.getLog(FuelListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }
            String id = context.getParameter("id");
            String farmId = context.getParameter("farmId");

            FuelSpecification specification = new FuelSpecification();

            if(null!= id) {
                specification.setId(id);
            }
            if(farmId != null){
                specification.setFarmId(farmId);
            }
            List<Fuel> issueList = (List<Fuel>) FuelRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Supplier List", issueList, null);
            response.setData(issueList);
            return response;
        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());

        }
    }
}