package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Activity;
import com.simisinc.platform.domain.model.cannacomply.Issue;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class IssueRepository {

    private static Log LOG = LogFactory.getLog(IssueRepository.class);
    private static String TABLE_NAME = "cannacomply.issue";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static Issue add(Issue record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("crop_id", record.getCropId())
                .add("title", record.getTitle())
                .add("description", record.getDescription())
                .add("severity",record.getSeverity())
                .add("assigned_to",record.getAssignedTo())
                .add("comment",record.getComment())
                .add("attachments",record.getAttachments())
                .add("farm_id",record.getFarmId());

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


    public static void update(Issue record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("crop_id", record.getCropId())
                .addIfExists("title", record.getTitle())
                .addIfExists("description", record.getDescription())
                .addIfExists("severity",record.getSeverity())
                .addIfExists("assigned_to",record.getAssignedTo())
                .addIfExists("comment",record.getComment())
                .addIfExists("attachments",record.getAttachments())
                .addIfExists("farm_id",record.getFarmId());

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


    public static Issue findById(String id) {

        return (Issue) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("id = ?", id),
                IssueRepository::buildRecord);
    }


    public static DataResult query(IssueSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("id = ?", specification.getId());

        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, IssueRepository::buildRecord);
    }


    public static void delete(String issueId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", issueId));
            LOG.debug("issue has been deleted:>>" + issueId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static Issue buildRecord(ResultSet rs) {

        Issue issue = new Issue();
        try {
            issue.setId(rs.getString("id"));
            issue.setCropId(rs.getString("crop_id"));
            issue.setTitle(rs.getString("title"));
            issue.setDescription(rs.getString("description"));
            issue.setSeverity(rs.getString("severity"));
            issue.setAssignedTo(rs.getString("assigned_to"));
            issue.setComment(rs.getString("comment"));
            issue.setAttachments(rs.getString("attachments"));
            issue.setFarmId(rs.getString("farm_id"));

            return issue;
        } catch (Exception e) {
            LOG.error("exception when building record for issue" + e.getMessage());
            return null;
        }
    }
}
