package com.simisinc.platform.rest.services.carfix;

import com.simisinc.platform.domain.model.carfix.Category;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceRequestRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Description
 * Will return reference data for the options menu when creating service request in the app
 *
 * @author Julius Nikitaridis
 * @created 01/11/22 11:28 AM
 */
public class CategoryListService {

    private static Log LOG = LogFactory.getLog(CategoryListService.class);

    public ServiceResponse get(ServiceContext context) {


        try {
            List<Category> optionsList = (List<Category>) ServiceRequestRepository.findCategories().getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Category List", optionsList, null);
            response.setData(optionsList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in CategoryListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }

    }

}
