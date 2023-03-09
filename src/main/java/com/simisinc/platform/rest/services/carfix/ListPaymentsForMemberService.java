package com.simisinc.platform.rest.services.carfix;

import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Description
 * reference data for btands that are supported by a service provider.
 *
 * @author Julius Nikitaridis
 * @created 01/11/22 11:28 AM
 */
public class ListPaymentsForMemberService {

    private static Log LOG = LogFactory.getLog(ListPaymentsForMemberService.class);

    public ServiceResponse get(ServiceContext context) {

        try {

            ServiceResponse response = new ServiceResponse(200);
            //ServiceResponseCommand.addMeta(response, "Category List", brandList, null);
            //response.setData(brandList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in BrandListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }

    }


    private void getTransactionStatus() {

    }

}


