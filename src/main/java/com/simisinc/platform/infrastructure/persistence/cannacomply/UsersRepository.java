package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.User;
import com.simisinc.platform.domain.model.cannacomply.Farm;
import com.simisinc.platform.domain.model.cannacomply.Users;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class UsersRepository {

    private static Log LOG = LogFactory.getLog(UsersRepository.class);
    private static String TABLE_NAME = "cannacomply.users";
    private static String[] PRIMARY_KEY = new String[]{"sys_unique_user_id"};

    public static Users add(Users record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("sys_unique_user_id", record.getSysUniqueUserId())
                .add("farm_id", record.getFarmId());

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


    public static void update(Users record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("farm_id", record.getFarmId());

            try (Connection connection = DB.getConnection();
                 AutoStartTransaction a = new AutoStartTransaction(connection);
                 AutoRollback transaction = new AutoRollback(connection)) {
                // In a transaction (use the existing connection)
                DB.update(connection, TABLE_NAME, updateValues, new SqlUtils().add("sys_unique_user_id = ?", record.getSysUniqueUserId()));
                transaction.commit();

        } catch (Exception se) {
            LOG.error("SQLException: " + se.getMessage());
            throw new Exception(se.getMessage());
        }
    }


    public static Users findById(String id) {

        return (Users) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("sys_unique_user_id = ?", id),
                UsersRepository::buildRecord);
    }


    public static DataResult query(UsersSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("sys_unique_user_id = ?",specification.getSysUniqueUserId());

        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, UsersRepository::buildRecord);
    }


    public static void delete(String farmId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("sys_unique_user_id = ?", farmId));
            LOG.debug("users object has been deleted:>>" + farmId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static Users buildRecord(ResultSet rs) {

        Users user = new Users();
        try {
          user.setFarmId(rs.getString("farm_id"));
          user.setSysUniqueUserId(rs.getString("sys_unique_user_id"));
            return user;
        } catch (Exception e) {
            LOG.error("exception when building record for farm" + e.getMessage());
            return null;
        }
    }
}

