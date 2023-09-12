package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.ApiAccessRecord;
import com.simisinc.platform.infrastructure.database.DB;
import com.simisinc.platform.infrastructure.database.SqlUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.ResultSet;

public class APIAccessRepository {
    private static String TABLE_NAME = "cannacomply.api_access";
    private static String[] PRIMARY_KEY = new String[]{"id"};
    private static Log LOG = LogFactory.getLog(APIAccessRepository.class);


    public static ApiAccessRecord findByApiName(String apiName) {

        return (ApiAccessRecord) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("api_name = ?", apiName),
                APIAccessRepository::buildRecord);
    }


    private static ApiAccessRecord buildRecord(ResultSet rs) {

        ApiAccessRecord record = new ApiAccessRecord();
        try {
            record.setUuid(rs.getString("id"));
            record.setApiName(rs.getString("api_name"));
            record.setAllowedRoles(rs.getString("allowed_roles"));

            return record;
        } catch (Exception e) {
            LOG.error("exception when building record for API Record" + e.getMessage());
            return null;
        }
    }
}
