package com.simisinc.platform.rest.services.carfix;

import com.simisinc.platform.domain.model.carfix.Brand;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceRequestRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Description
 * reference data for btands that are supported by a service provider.
 *
 * @author Julius Nikitaridis
 * @created 01/11/22 11:28 AM
 */
public class BrandListService {

    private static Log LOG = LogFactory.getLog(BrandListService.class);

    public ServiceResponse get(ServiceContext context) {


        try {
            List<Brand> brandList = (List<Brand>) ServiceRequestRepository.findBrands().getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Category List", brandList, null);
            response.setData(brandList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in BrandListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }

    }

}

