package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.carfix.TreatmentProduct;
import com.simisinc.platform.infrastructure.persistence.cannacomply.TreatmentProductRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.TreatmentProductSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;


public class TreatmentProductListService {

    private static Log LOG = LogFactory.getLog(TreatmentProductListService.class);

    public ServiceResponse get(ServiceContext context) {


        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }
            TreatmentProductSpecification specification = new TreatmentProductSpecification();
            if(context.getParameter("farmId")!= null) {
                specification.setFarmId(context.getParameter("farmId"));
            }
            if(context.getParameter("productId")!= null) {
                specification.setProductId(context.getParameter("productId"));
            }
            List<TreatmentProduct> optionsList = (List<TreatmentProduct>) TreatmentProductRepository.query(specification,null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Treatment Product List", optionsList, null);
            response.setData(optionsList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in TreatmentProductListService", e);
            ServiceResponse response = new ServiceResponse(500);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}
