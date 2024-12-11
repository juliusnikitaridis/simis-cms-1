package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Packaging;
import com.simisinc.platform.infrastructure.persistence.cannacomply.PackageRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.PackagingSpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.PackagingTotalsRepository;
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
public class PackagingTotalsListService {

    private static Log LOG = LogFactory.getLog(PackagingTotalsListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }
            String id = context.getParameter("id");
            String farmId = context.getParameter("farmId");
            String packageTag = context.getParameter("packageTag");
            PackagingSpecification specification = new PackagingSpecification();

            if (null != id) {
                specification.setId(id);
            }
            if(null != farmId) {
                specification.setFarmId(farmId);
            }
            if(packageTag != null) {
                specification.setPackageTag(packageTag);
            }
            List<Packaging> packagingTotalsList = (List<Packaging>) PackagingTotalsRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Packaging totals List", packagingTotalsList, null);
            response.setData(packagingTotalsList);
            return response;
        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());
        }
    }
}