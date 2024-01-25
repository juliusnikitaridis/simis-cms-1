package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Hygiene;
import com.simisinc.platform.domain.model.cannacomply.SoilManagement;
import com.simisinc.platform.infrastructure.persistence.cannacomply.HygieneRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.HygieneSpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.SoilManagementRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.SoilManagementSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * Description
 * Service that will return all farms or details for a particular farm
 *
 * @author Julius Nikitaridis
 * @created 01/11/22 11:28 AM
 */
public class SoilManagementListService {

    private static Log LOG = LogFactory.getLog(SoilManagementListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            String farmId = context.getParameter("farmId");

            SoilManagementSpecification specification = new SoilManagementSpecification();

            if(null!= farmId) {
                specification.setFarmId(farmId);
            }
            List<SoilManagement> smList = (List<SoilManagement>) SoilManagementRepository.query(specification).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Soil Management List", smList, null);
            response.setData(smList);
            return response;
        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());

        }
    }
}