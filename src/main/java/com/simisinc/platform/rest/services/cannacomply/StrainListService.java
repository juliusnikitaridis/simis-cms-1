package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Packaging;
import com.simisinc.platform.domain.model.cannacomply.Strain;
import com.simisinc.platform.infrastructure.persistence.cannacomply.PackageRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.PackagingSpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.StrainRepository;
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
 * @created 01/07/23 11:28 AM
 */
public class StrainListService {

    private static Log LOG = LogFactory.getLog(StrainListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            List<Strain> strainList = (List<Strain>) StrainRepository.query().getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Strain List", strainList, null);
            response.setData(strainList);
            return response;
        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());

        }
    }
}