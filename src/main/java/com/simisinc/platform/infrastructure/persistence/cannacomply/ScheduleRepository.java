package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Activity;
import com.simisinc.platform.domain.model.cannacomply.Schedule;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class ScheduleRepository {

    private static Log LOG = LogFactory.getLog(ScheduleRepository.class);
    private static String TABLE_NAME = "cannacomply.schedule";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static Schedule add(Schedule record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("farm_id", record.getFarmId())
                .add("status", record.getStatus())
                .add("starting_date", record.getStartingDate())
                .add("ending_date",record.getEndingDate())
                .add("title",record.getTitle())
                .add("type",record.getType())
                .add("assigned_to",record.getAssignedTo())
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


    public static void update(Schedule record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("farm_id", record.getFarmId())
                .addIfExists("status", record.getStatus())
                .addIfExists("starting_date", record.getStartingDate())
                .addIfExists("ending_date",record.getEndingDate())
                .addIfExists("title",record.getTitle())
                .addIfExists("type",record.getType())
                .addIfExists("assigned_to",record.getAssignedTo())
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


    public static Schedule findById(String id) {

        return (Schedule) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("id = ?", id),
                ScheduleRepository::buildRecord);
    }


    public static DataResult query(ScheduleSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("id = ?", specification.getId());

        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, ScheduleRepository::buildRecord);
    }


    public static void delete(String activityId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", activityId));
            LOG.debug("schedule has been deleted:>>" + activityId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static Schedule buildRecord(ResultSet rs) {

        Schedule schedule = new Schedule();
        try {
            schedule.setId(rs.getString("id"));
            schedule.setFarmId(rs.getString("farm_id"));
            schedule.setStatus(rs.getString("status"));
            schedule.setStartingDate(rs.getString("starting_date"));
            schedule.setEndingDate(rs.getString("ending_date"));
            schedule.setTitle(rs.getString("title"));
            schedule.setType(rs.getString("type"));
            schedule.setAssignedTo(rs.getString("assigned_to"));
            schedule.setDescription(rs.getString("description"));

            return schedule;
        } catch (Exception e) {
            LOG.error("exception when building record for schedule" + e.getMessage());
            return null;
        }
    }
}

