package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.CropData;
import com.simisinc.platform.domain.model.cannacomply.Feedback;
import com.simisinc.platform.infrastructure.persistence.cannacomply.CropDataRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.CropDataSpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.FeedbackRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.FeedbackSpecification;
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
public class FeedbackListService {

    private static Log LOG = LogFactory.getLog(FeedbackListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            String farmId = context.getParameter("farmId");
            String userId = context.getParameter("userId");

            FeedbackSpecification specification = new FeedbackSpecification();
            if(farmId != null) {
                specification.setFarmId(farmId);
            }
            if(userId != null) {
                specification.setUserId(userId);
            }
            List<Feedback> list = (List<Feedback>) FeedbackRepository.query(specification).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Feedback List", list, null);
            response.setData(list);
            return response;
        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());
        }
    }
}