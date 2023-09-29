package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Activity;
import com.simisinc.platform.domain.model.cannacomply.Block;
import com.simisinc.platform.domain.model.cannacomply.UserUpload;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class UserUploadRepository {

    private static Log LOG = LogFactory.getLog(UserUploadRepository.class);
    private static String TABLE_NAME = "cannacomply.user_upload";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static UserUpload add(UserUpload record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("name", record.getName())
                .add("file_name", record.getFileName())
                .add("file_path",record.getFilePath())
                .add("file_type",record.getFileType())
                .add("created_by", record.getCreatedBy())
                .add("created_date",record.getCreatedDate())
                .add("file_size",record.getFileSize())
                .add("description",record.getDescription());

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


    public static void update(UserUpload record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("name", record.getName())
                .addIfExists("file_name", record.getFileName())
                .addIfExists("file_path",record.getFilePath())
                .addIfExists("file_type",record.getFileType())
                .addIfExists("created_by", record.getCreatedBy())
                .addIfExists("created_date",record.getCreatedDate())
                .addIfExists("file_size",record.getFileSize())
                .addIfExists("description",record.getDescription());
            try (Connection connection = DB.getConnection();
                 AutoStartTransaction a = new AutoStartTransaction(connection);
                 AutoRollback transaction = new AutoRollback(connection)) {
                // In a transaction (use the existing connection)
                DB.update(connection, TABLE_NAME, updateValues, new SqlUtils().add("id = ?", record.getId()));
                transaction.commit();

        } catch (Exception se) {
            LOG.error("SQLException: " + se.getMessage());
            throw new Exception(se.getMessage());
        }
    }


    public static Activity findById(String id) {

        return (Activity) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("id = ?", id),
                UserUploadRepository::buildRecord);
    }


    public static DataResult query(UserUploadSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("id = ?", specification.getId())
                    .addIfExists("name = ?",specification.getName()) //this really is a type for the upload
                    .addIfExists("farm_id = ?",specification.getFarmId())
                    .addIfExists("created_by = ?",specification.getUserId());

        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, UserUploadRepository::buildRecord);
    }


    public static void delete(String blockId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", blockId));
            LOG.debug("User upload has been deleted:>>" + blockId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static UserUpload buildRecord(ResultSet rs) {

        UserUpload userUpload = new UserUpload();
        try {
              userUpload.setId(rs.getString("id"));
              userUpload.setName(rs.getString("name"));
              userUpload.setFileName(rs.getString("file_name"));
              userUpload.setFilePath(rs.getString("file_path"));
              userUpload.setFileType(rs.getString("file_type"));
              userUpload.setCreatedBy(rs.getString("created_by"));
              userUpload.setCreatedDate(rs.getString("created_date"));
              userUpload.setFileSize(rs.getString("file_size"));
              userUpload.setDescription(rs.getString("description"));

            return userUpload;
        } catch (Exception e) {
            LOG.error("exception when building record for User Upload" + e.getMessage());
            return null;
        }
    }
}

