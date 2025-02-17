package com.simisinc.platform.rest.services.cannacomply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.cannacomply.Block;
import com.simisinc.platform.domain.model.cannacomply.Crop;
import com.simisinc.platform.infrastructure.database.DB;
import com.simisinc.platform.infrastructure.persistence.cannacomply.BlockRepository;
import com.simisinc.platform.infrastructure.persistence.cannacomply.CropRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.services.cannacomply.util.ErrorMessageStatics;
import com.simisinc.platform.rest.services.cannacomply.util.ValidateApiAccessHelper;
import lombok.SneakyThrows;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Description
 *
 * @author Julius Nikitaridis
 * @created 04/05/23 11:28 AM
 */


public class UpdateBlockService {

    private static Log LOG = LogFactory.getLog(UpdateBlockService.class);

    public ServiceResponse post(ServiceContext context) {

        try {
            if (!ValidateApiAccessHelper.validateAccess(this.getClass().getName(), context)) {
                throw new Exception(ErrorMessageStatics.ERR_01);
            }

            ObjectMapper mapper = new ObjectMapper();
            Block block = mapper.readValue(context.getJsonRequest(), Block.class);

            Block existingBLock = null;
            if (block.getBlockLocation() != null && block.getFarmId() != null) {
                //validate that there cant be multiple
                existingBLock = BlockRepository.findByLocationAndFarmId(block.getBlockLocation(), block.getFarmId());
                if (existingBLock != null) {
                    throw new Exception(ErrorMessageStatics.ERR_14);
                }
            }
            existingBLock = BlockRepository.findById(block.getId());
            if(existingBLock == null) {
                throw new Exception("could not find existing block to update");
            }
            int cropRecordCount = getRelatedCropsRecords(existingBLock.getLocationId());
            block.setNumberOfCrops(String.valueOf(cropRecordCount));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String updatedDate = LocalDateTime.now().format(formatter);
            block.setDate(updatedDate);

            BlockRepository.update(block);

            ServiceResponse response = new ServiceResponse(200);
            ArrayList<String> responseMessage = new ArrayList<String>() {{
                add("block has been updated");
            }};
            response.setData(responseMessage);
            return response;

        } catch (Exception e) {
            return ErrorMessageStatics.handleException(e, this.getClass());

        }
    }

    @SneakyThrows
    private int getRelatedCropsRecords(String locationId) {
        Connection conn = null;
        try {
            conn = DB.getConnection();
            String sql = "select count(1) as ans from cannacomply.crop" +
                    "         where status = 'growth_stage'" +
                    "         and location_type = 'block' and location_id = '"+locationId+"'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int count = 0;
            while (rs.next()) {
                count = rs.getInt("ans");
            }
            return count;
        } catch (Exception e) {
            throw e;
        } finally {
            if (conn!= null) {
                conn.close();
            }
        }
    }
}
