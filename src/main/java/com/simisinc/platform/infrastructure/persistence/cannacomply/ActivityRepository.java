package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Activity;
import com.simisinc.platform.domain.model.cannacomply.Crop;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class ActivityRepository {

    private static Log LOG = LogFactory.getLog(ActivityRepository.class);
    private static String TABLE_NAME = "cannacomply.activity";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static Activity add(Activity record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("crop_id", record.getCropId())
                .add("type", record.getType())
                .add("activity_data", record.getActivityData())
                .add("user_id",record.getUserId())
                .add("farm_id",record.getFarmId())
                .add("date",record.getDate())
                .add("item_type",record.getItemType())
                .add("block_id",record.getBlockId())
                .add("item_id",record.getItemId())
                .add("location_type",record.getLocationType())
                .add("location_id",record.getLocationId())
                .add("status",record.getStatus());

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


    public static void update(Activity record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("crop_id", record.getCropId())
                .addIfExists("type", record.getType())
                .addIfExists("activity_data", record.getActivityData())
                .addIfExists("user_id",record.getUserId())
                .addIfExists("farm_id",record.getFarmId())
                .addIfExists("date",record.getDate())
                .addIfExists("item_type",record.getItemType())
                .addIfExists("block_id",record.getBlockId())
                .addIfExists("item_id",record.getItemId())
                .addIfExists("location_type",record.getLocationType())
                .addIfExists("location_id",record.getLocationId())
                .addIfExists("status",record.getStatus());

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
                ActivityRepository::buildRecord);
    }


    public static DataResult query(ActivitySpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("id = ?", specification.getId())
                    .addIfExists("block_id = ?",specification.getBlockId())
                    .addIfExists("location_id = ?",specification.getLocationId())
                    .addIfExists("item_id = ?",specification.getItemId())
                    .addIfExists("farm_id = ?",specification.getFarmId());

        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, ActivityRepository::buildRecord);
    }


    public static void delete(String activityId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", activityId));
            LOG.debug("activity has been deleted:>>" + activityId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static Activity buildRecord(ResultSet rs) {

        Activity activity = new Activity();
        try {
              activity.setActivityData(rs.getString("activity_data"));
              activity.setFarmId(rs.getString("farm_id"));
              activity.setId(rs.getString("id"));
              activity.setItemType(rs.getString("item_type"));
              activity.setStatus(rs.getString("status"));
              activity.setType(rs.getString("type"));
              activity.setUserId(rs.getString("user_id"));
              activity.setCropId(rs.getString("crop_id"));
              activity.setItemId(rs.getString("item_id"));
              activity.setBlockId(rs.getString("block_id"));
              activity.setLocationId(rs.getString("location_id"));
              activity.setLocationType(rs.getString("location_type"));
              activity.setDate(rs.getString("date"));

            return activity;
        } catch (Exception e) {
            LOG.error("exception when building record for activity" + e.getMessage());
            return null;
        }
    }
}

