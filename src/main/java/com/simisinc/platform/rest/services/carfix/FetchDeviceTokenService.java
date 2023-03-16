package com.simisinc.platform.rest.services.carfix;

import com.simisinc.platform.domain.model.carfix.DeviceToken;
import com.simisinc.platform.domain.model.carfix.Quote;
import com.simisinc.platform.infrastructure.persistence.carfix.DeviceTokenRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.QuoteRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.QuoteSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class FetchDeviceTokenService {
    private static Log LOG = LogFactory.getLog(DeviceToken.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            String uniqueId = context.getParameter("uniqueId");

            List<DeviceToken> tokenList = (List<DeviceToken>) DeviceTokenRepository.query(uniqueId, null).getRecords();
            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "TokenList", tokenList, null);
            response.setData(tokenList);
            return response;

        } catch (Exception e) {
            LOG.error("Error in Device Token Service", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}
