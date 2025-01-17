package com.simisinc.platform.rest.services.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Device;
import com.simisinc.platform.domain.model.cannacomply.Reading;
import com.simisinc.platform.infrastructure.persistence.cannacomply.DeviceRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.DeviceSpecification;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ReadingRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.ReadingSpecification;
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
 * @created 25/04/23 11:28 AM
 */
public class DeviceListService {

    private static Log LOG = LogFactory.getLog(DeviceListService.class);

    public ServiceResponse get(ServiceContext context) {

        try {
            if(!ValidateApiAccessHelper.validateAccess(this.getClass().getName(),context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }
            String farmId = context.getParameter("farmId");
            String deviceId = context.getParameter("deviceId");
            String locationId = context.getParameter("locationId");

            DeviceSpecification specification = new DeviceSpecification();

            if(null!= farmId) {
                specification.setFarmId(farmId);
            }
            if(null != deviceId) {
                specification.setId(deviceId);
            }
            if(locationId != null) {
                specification.setLocationId(locationId);
            }

            List<Device> deviceList = (List<Device>) DeviceRepository.query(specification, null).getRecords();

            ServiceResponse response = new ServiceResponse(200);
            ServiceResponseCommand.addMeta(response, "Device List", deviceList, null);
            response.setData(deviceList);
            return response;
        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e,this.getClass());

        }
    }
}