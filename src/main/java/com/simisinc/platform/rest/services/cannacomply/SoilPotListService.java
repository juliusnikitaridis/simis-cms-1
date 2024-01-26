package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.SoilManagement;
import com.simisinc.platform.domain.model.cannacomply.SoilPot;
import com.simisinc.platform.infrastructure.persistence.cannacomply.SoilManagementRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.SoilManagementSpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.SoilPotRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.SoilPotSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 *
 * @author Julius Nikitaridis
 * @created 26/01/24 11:28 AM
 */
public class SoilPotListService {

    private static Log LOG = LogFactory.getLog(SoilPotListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            String farmId = context.getParameter("farmId");
            String soilId = context.getParameter("soilId");

            SoilPotSpecification specification = new SoilPotSpecification();

            if(null!= farmId) {
                specification.setFarmId(farmId);
            }
            if(soilId != null) {
                specification.setSoilId(soilId);
            }
            List<SoilPot> smList = (List<SoilPot>) SoilPotRepository.query(specification).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Soil Pot List", smList, null);
            response.setData(smList);
            return response;
        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());

        }
    }
}