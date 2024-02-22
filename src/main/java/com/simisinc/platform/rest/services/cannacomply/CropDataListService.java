package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.CropData;
import com.simisinc.platform.domain.model.cannacomply.WaterSource;
import com.simisinc.platform.infrastructure.persistence.cannacomply.CropDataRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.CropDataSpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.WaterSourceRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.WaterSourceSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * @author Julius Nikitaridis
 * @created 21/02/24 11:28 AM
 */
public class CropDataListService {

    private static Log LOG = LogFactory.getLog(CropDataListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            String commodity = context.getParameter("commodity");

            CropDataSpecification specification = new CropDataSpecification();
            if(commodity != null) {
                specification.setCommodity(commodity);
            }

            List<CropData> cropList = (List<CropData>) CropDataRepository.query(specification).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Crop Data List", cropList, null);
            response.setData(cropList);
            return response;
        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());
        }
    }
}