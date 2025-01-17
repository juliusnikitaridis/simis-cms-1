package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.Packaging;
import com.simisinc.platform.domain.model.carfix.Supplier;
import com.simisinc.platform.infrastructure.persistence.cannacomply.PackageRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.SupplierRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;


/**
 * Description
 * @author Julius Nikitaridis
 * @created 04/05/23 11:28 AM
 */


public class UpdateSupplierService {

    private static Log LOG = LogFactory.getLog(UpdateSupplierService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            ObjectMapper mapper = new ObjectMapper();
            Supplier item = mapper.readValue(context.getJsonRequest(), Supplier.class);

            SupplierRepository.update(item);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("Supplier has been updated");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());

        }
    }
}
