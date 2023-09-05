package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.User;
import com.simisinc.platform.domain.model.cannacomply.Activity;
import com.simisinc.platform.domain.model.cannacomply.ComplianceUser;
import com.simisinc.platform.infrastructure.database.*;
import com.simisinc.platform.infrastructure.persistence.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;


public class ComplianceUserRepository {

    private static Log LOG = LogFactory.getLog(ComplianceUserRepository.class);
    private static String TABLE_NAME = "cannacomply.compliance_user";
    private static String[] PRIMARY_KEY = new String[]{"uuid"};

    public static ComplianceUser add(ComplianceUser record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("uuid", record.getUuid())
                .add("unique_sys_user_id",record.getSysUniqueUserId())
                .add("farm_id",record.getFarmId())
                .add("user_role",record.getUserRole());

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


    public static void update(ComplianceUser record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("unique_sys_user_id",record.getSysUniqueUserId())
                .addIfExists("farm_id",record.getFarmId())
                .addIfExists("user_role",record.getUserRole());


        try (Connection connection = DB.getConnection();
                 AutoStartTransaction a = new AutoStartTransaction(connection);
                 AutoRollback transaction = new AutoRollback(connection)) {
                // In a transaction (use the existing connection)
                DB.update(connection, TABLE_NAME, updateValues, new SqlUtils().add("uuid = ?", record.getUuid()));
                transaction.commit();

        } catch (Exception se) {
            LOG.error("SQLException: " + se.getMessage());
            throw new Exception(se.getMessage());
        }
    }


    public static ComplianceUser findById(String id) {

        return (ComplianceUser) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("uuid = ?", id),
                ComplianceUserRepository::buildRecord);
    }

    public static ComplianceUser findByUniqueId(String uniqueId) {

        return (ComplianceUser) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("unique_sys_user_id = ?", uniqueId),
                ComplianceUserRepository::buildRecord);
    }

    public static List<ComplianceUser> findAllByFarmId(String farmId) {
        return (List<ComplianceUser>) DB.selectAllFrom(TABLE_NAME,new SqlUtils().add("farm_id = ?",farmId),null,ComplianceUserRepository::buildRecord).getRecords();
    }


    public static DataResult query(ComplianceUserSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("farm_id = ?",specification.getFarmId())
                    .addIfExists("uuid = ?",specification.getId())
                    .addIfExists("unique_sys_user_id = ?",specification.getUniqueSysUserId());


        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, ComplianceUserRepository::buildRecord);
    }


    public static void delete(String activityId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("uuid = ?", activityId));
            LOG.debug("activity has been deleted:>>" + activityId);
        } catch (Exception e) {
            throw e;
        }
    }





    private static ComplianceUser buildRecord(ResultSet rs) {

        try {

        //get base user info
        User baseUser = (User) DB.selectRecordFrom(
                "Users", new SqlUtils().add("unique_id = ?", rs.getString("unique_sys_user_id")),
                UserRepository::buildRecordMinimal);

        ComplianceUser complianceUser = new ComplianceUser();
        complianceUser.setSysUniqueUserId(baseUser.getUniqueId());
        complianceUser.setUserType(baseUser.getUserType());
        complianceUser.setFirstName(baseUser.getFirstName());
        complianceUser.setLastName(baseUser.getLastName());
        complianceUser.setContactNo(baseUser.getContactNo());
        complianceUser.setOrganization(baseUser.getOrganization());
        complianceUser.setNickname(baseUser.getNickname());
        complianceUser.setEmail(baseUser.getEmail());
        complianceUser.setIdnum(baseUser.getIdnum());
        complianceUser.setUsername(baseUser.getUsername());
        // record.setPassword(rs.getString("password"));
        // complianceUser.setEnabled(rs.getBoolean("enabled"));
        //  record.setCreated(rs.getTimestamp("created"));
        //  record.setModified(rs.getTimestamp("modified"));
        //  record.setAccountToken(rs.getString("account_token"));
        //   record.setValidated(rs.getTimestamp("validated"));
        //  record.setCreatedBy(rs.getLong("created_by"));
        //   record.setModifiedBy(rs.getLong("modified_by"));
        //   complianceUser.setTitle(rs.getString("title"));
        complianceUser.setDepartment(baseUser.getDepartment());
        complianceUser.setTimeZone(baseUser.getTimeZone());
        complianceUser.setCity(baseUser.getCity());
        complianceUser.setState(baseUser.getState());
        complianceUser.setCountry(baseUser.getCountry());
        complianceUser.setPostalCode(baseUser.getPostalCode());
        complianceUser.setLatitude(baseUser.getLatitude());
        complianceUser.setLongitude(baseUser.getLongitude());
        complianceUser.setUserRole(rs.getString("user_role"));
        complianceUser.setUuid(rs.getString("uuid"));
        complianceUser.setFarmId(rs.getString("farm_id"));
        complianceUser.setSysUniqueUserId(rs.getString("unique_sys_user_id"));

            return complianceUser;
        } catch (Exception e) {
            LOG.error("exception when building record for ComplianceUser" + e.getMessage());
            return null;
        }
    }
}

