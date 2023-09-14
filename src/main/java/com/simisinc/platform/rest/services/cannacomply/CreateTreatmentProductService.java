package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.carfix.TreatmentProduct;
import com.simisinc.platform.infrastructure.persistence.cannacomply.TreatmentProductRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.UUID;


public class CreateTreatmentProductService {

    private static Log LOG = LogFactory.getLog(CreateTreatmentProductService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception("User does not have required roles to access API");
            }
            ObjectMapper mapper = new ObjectMapper();
            TreatmentProduct product = mapper.readValue(context.getJsonRequest(), TreatmentProduct.class);
            product.setId(UUID.randomUUID().toString());

            TreatmentProductRepository.add(product);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("Treatment Product has been created");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            LOG.error("Error in CreateTreatmentProduct", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}
