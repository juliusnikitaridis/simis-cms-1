package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.Hygiene;
import com.simisinc.platform.infrastructure.persistence.cannacomply.HygieneRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author Julius Nikitaridis
 * @created 01/07/23 11:28 AM
 */
public class CreateSoilManagementService {

    private static Log LOG = LogFactory.getLog(CreateSoilManagementService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }
            ObjectMapper mapper = new ObjectMapper();
            Hygiene hygiene = mapper.readValue(context.getJsonRequest(), Hygiene.class);
            String hygieneId = UUID.randomUUID().toString();
            hygiene.setId(hygieneId);

            HygieneRepository.add(hygiene);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add(hygieneId);}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());
        }
    }
}