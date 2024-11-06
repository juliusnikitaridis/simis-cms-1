package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.Yield;
import com.simisinc.platform.infrastructure.persistence.cannacomply.HarvestRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.YieldRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.YieldSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author Julius Nikitaridis
 * @created 18/07/23 11:28 AM
 */


public class CreateHarvestService {

    private static Log LOG = LogFactory.getLog(CreateHarvestService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if (!ValidateApiAccessHelper.validateAccess(this.getClass().getName(), context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            ObjectMapper mapper = new ObjectMapper();
            Yield harvest = mapper.readValue(context.getJsonRequest(), Yield.class);
            YieldSpecification spec = new YieldSpecification();
            spec.setContainerNumber(harvest.getContainerNumber());
            Yield yieldRecord = (Yield) YieldRepository.query(spec,null).getRecords().get(0);
            yieldRecord.setIsMixed(harvest.getIsMixed());
            calculateAmountsFromYieldRecords(harvest.getContainerNumber(),yieldRecord);

            HarvestRepository.add(yieldRecord);


            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>() {{
                add("harvest has been added");
            }};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e, this.getClass());

        }
    }

    //add up everything from all the records in the harvest table
    public static void calculateAmountsFromYieldRecords(String containerNumber, Yield yieldDto) throws Exception {
        YieldSpecification specification = new YieldSpecification();
        specification.setContainerNumber(containerNumber);
        List<Yield> yieldRecords = (List<Yield>) YieldRepository.query(specification, null).getRecords();
        if (yieldRecords.isEmpty()) {
            throw new Exception("could not find corresponding records from Yield table for container number :" + containerNumber);
        }
        AtomicReference<Double> totalWetWeight = new AtomicReference<>(0.0);
        AtomicReference<Double> totalQuantity = new AtomicReference<>(0.0);
        AtomicReference<Double> totalLoss = new AtomicReference<>(0.0);

        yieldRecords.forEach(yield -> {
            totalWetWeight.updateAndGet(x -> x + Double.valueOf(yield.getWetWeight()));
            totalQuantity.updateAndGet(x -> x + Double.valueOf(yield.getQuantity()));
            totalLoss.updateAndGet(x -> x + Double.valueOf(yield.getLoss()));
        });

        yieldDto.setQuantity(totalQuantity.toString());
        yieldDto.setWetWeight(totalWetWeight.toString());
        yieldDto.setLoss(totalLoss.toString());
    }
}
