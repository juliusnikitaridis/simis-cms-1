package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Packaging;
import com.simisinc.platform.infrastructure.persistence.cannacomply.PackageRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.PackagingSpecification;
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
public class PackagingListService {

    private static Log LOG = LogFactory.getLog(PackagingListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }
            String id = context.getParameter("id");
            String farmId = context.getParameter("farmId");
            PackagingSpecification specification = new PackagingSpecification();

            if (null != id) {
                specification.setId(id);
            }
            if(null != farmId) {
                specification.setFarmId(farmId);
            }
            List<Packaging> packagingList = (List<Packaging>) PackageRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Packaging List", packagingList, null);
            response.setData(packagingList);
            return response;
        } catch (Throwable e) {
            LOG.error("Error in PackageListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }
}