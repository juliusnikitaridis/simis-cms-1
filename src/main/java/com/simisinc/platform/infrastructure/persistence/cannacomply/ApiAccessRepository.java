package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.ApiAccess;
import com.simisinc.platform.domain.model.cannacomply.GrowthCycle;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;


public class ApiAccessRepository {

    private static Log LOG = LogFactory.getLog(ApiAccessRepository.class);
    private static String TABLE_NAME = "cannacomply.api_access";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static ApiAccess add(ApiAccess record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("api_name",record.getApiName())
                .add("allowed_roles",record.getAllowedRoles());
        try (Connection connection = DB.getConnection();
             AutoStartTransaction a = new AutoStartTransaction(connection);
             AutoRollback transaction = new AutoRollback(connection)) {
            // In a transaction (use the existing connection)
            DB.insertIntoWithStringPk(connection, TABLE_NAME, insertValues, PRIMARY_KEY);
            transaction.commit();
            return record;

        } catch (Exception se) {
            LOG.error("SQLException: " + se.getMessage());
            throw new Exception(se.getMessage());
        }
    }


    public static void update(ApiAccess record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .add("allowed_roles", record.getAllowedRoles());
            try (Connection connection = DB.getConnection()) {
                // In a transaction (use the existing connection)
                DB.update(connection, TABLE_NAME, updateValues, new SqlUtils().add("api_name = ?", record.getApiName()));

        } catch (Exception se) {
            LOG.error("SQLException: " + se.getMessage());
            throw new Exception(se.getMessage());
        }
    }


    public static DataResult findAll() {

        return  DB.selectAllFrom(
                TABLE_NAME, new SqlUtils(),null,
                ApiAccessRepository::buildRecord);
    }


    public static void delete(String accessId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", accessId));
            LOG.debug("API Access has been deleted:>>" + accessId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static ApiAccess buildRecord(ResultSet rs) {

        ApiAccess access = new ApiAccess();
        try {
          access.setId(rs.getString("id"));
          access.setApiName(rs.getString("api_name"));
          access.setAllowedRoles(rs.getString("allowed_roles"));
          return access;
        } catch (Exception e) {
            LOG.error("exception when building record for API Access" + e.getMessage());
            return null;
        }
    }
}

