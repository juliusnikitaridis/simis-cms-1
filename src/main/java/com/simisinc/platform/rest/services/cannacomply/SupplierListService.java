package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.carfix.Supplier;
import com.simisinc.platform.infrastructure.persistence.carfix.SupplierRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.SupplierSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * @author Julius Nikitaridis
 * @created 01/11/23 11:28 AM
 */
public class SupplierListService {

    private static Log LOG = LogFactory.getLog(SupplierListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            String id = context.getParameter("id");
            String farmId = context.getParameter("farmId");

            SupplierSpecification specification = new SupplierSpecification();

            if(null!= id) {
                specification.setId(id);
            }
            if(farmId != null){
                specification.setFarmId(farmId);
            }
            List<Supplier> issueList = (List<Supplier>) SupplierRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Supplier List", issueList, null);
            response.setData(issueList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in SupplierListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}