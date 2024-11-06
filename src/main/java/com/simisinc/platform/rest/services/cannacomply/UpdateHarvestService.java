package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.Yield;
import com.simisinc.platform.infrastructure.persistence.cannacomply.HarvestRepository;
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


public class UpdateHarvestService {

    private static Log LOG = LogFactory.getLog(UpdateHarvestService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            ObjectMapper mapper = new ObjectMapper();
            Yield item = mapper.readValue(context.getJsonRequest(), Yield.class);
            if(item.getContainerNumber() == null) {
                throw new Exception("containerNumber parameter is mandatory when calling update");
            }
            //always re-calculate the totals when calling this update service

            //allow these totals to be updated and not re-calculated - comment out the line below.
            //CreateHarvestService.calculateAmountsFromYieldRecords(item.getContainerNumber(),item);
            HarvestRepository.update(item);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>(){{add("Harvest has been updated");}};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());

        }
    }
}
