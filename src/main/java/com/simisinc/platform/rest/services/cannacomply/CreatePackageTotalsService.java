package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.Packaging;
import com.simisinc.platform.infrastructure.persistence.cannacomply.PackageRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.PackagingSpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.PackagingTotalsRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author Julius Nikitaridis
 * @created 18/11/22 11:28 AM
 */


public class CreatePackageTotalsService {

    private static Log LOG = LogFactory.getLog(CreatePackageTotalsService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if (!ValidateApiAccessHelper.validateAccess(this.getClass().getName(), context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            ObjectMapper mapper = new ObjectMapper();
            Packaging request = mapper.readValue(context.getJsonRequest(), Packaging.class);
            if (request.getPackageTag() == null || request.getFarmId() == null) {
                throw new Exception("Package tag parameter and farm ID parameter mandatory");
            }
            if(request.getMoistureLoss() == null) {
                request.setMoistureLoss("0");
            }
            //get all related records in packaging table
            PackagingSpecification spec = new PackagingSpecification();
            spec.setPackageTag(request.getPackageTag());
            spec.setFarmId(request.getFarmId());
            List<Packaging> packagingRecords = (List<Packaging>) PackageRepository.query(spec, null).getRecords();
           // AtomicReference<Double> totalMoistureLoss = new AtomicReference<>(0.0);
            AtomicReference<Double> totalQuantity = new AtomicReference<Double>(0.0);
            Set<String> budSizes = new HashSet<>();

            Packaging templateRecord = packagingRecords.stream().findFirst().orElseThrow(() -> new Exception("related packaging records not found"));
            packagingRecords.forEach(packaging -> {
                if(packaging.getMoistureLoss() == null) {
                    throw new RuntimeException("related packaging record has null value for moisture loss");
                }
                if(packaging.getQuantity() == null) {
                    throw new RuntimeException("related packaging record has null value for quantity");
                }
              //  totalMoistureLoss.updateAndGet(x -> x + Double.valueOf(packaging.getMoistureLoss()));
                totalQuantity.updateAndGet(x -> x + Double.valueOf(packaging.getQuantity()));
                budSizes.add(packaging.getBudSize());
            });
          //  templateRecord.setMoistureLoss(String.valueOf(totalMoistureLoss));
            templateRecord.setMoistureLoss(request.getMoistureLoss());
            templateRecord.setQuantity(String.valueOf(totalQuantity));
            templateRecord.setBudSize(budSizes.toString());
            PackagingTotalsRepository.add(templateRecord);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>() {{
                add("Packaging totals record has been created");
            }};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e, this.getClass());
        }
    }
}
