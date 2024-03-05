package com.simisinc.platform.infrastructure.persistence.cannacomply;


import com.rometools.rome.feed.atom.Feed;
import com.simisinc.platform.domain.model.cannacomply.CropData;
import com.simisinc.platform.domain.model.cannacomply.Feedback;
import com.simisinc.platform.domain.model.cannacomply.WaterSource;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class FeedbackRepository {

    private static Log LOG = LogFactory.getLog(FeedbackRepository.class);
    private static String TABLE_NAME = "cannacomply.feedback";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static Feedback add(Feedback record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("farm_id", record.getFarmId())
                .add("user_id", record.getUserId())
                .add("comment",record.getComment())
                .add("date",record.getDate());

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


    public static void update(Feedback record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("farm_id", record.getFarmId())
                .addIfExists("user_id", record.getUserId())
                .addIfExists("comment",record.getComment())
                .addIfExists("date",record.getDate());

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


    public static Feedback findById(String id) {
        return (Feedback) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("id = ?", id),
                FeedbackRepository::buildRecord);
    }


    public static DataResult query(FeedbackSpecification specification) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();

        if (specification != null) {
            where.addIfExists("user_id = ?" ,specification.getUserId());
            where.addIfExists("farm_id = ?",specification.getFarmId());
        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, null, FeedbackRepository::buildRecord);
    }


    private static Feedback buildRecord(ResultSet rs) {

        Feedback ws = new Feedback();
        try {
            ws.setId(rs.getString("id"));
            ws.setFarmId(rs.getString("farm_id"));
            ws.setUserId(rs.getString("user_id"));
            ws.setComment(rs.getString("comment"));
            ws.setDate(rs.getString("date"));
            return ws;
        } catch (Exception e) {
            LOG.error("exception when building record for Feedback" + e.getMessage());
            return null;
        }
    }
}

