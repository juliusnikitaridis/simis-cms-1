package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Activity;
import com.simisinc.platform.domain.model.cannacomply.Location;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class LocationRepository {

    private static Log LOG = LogFactory.getLog(LocationRepository.class);
    private static String TABLE_NAME = "cannacomply.location";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static Location add(Location record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("location_name", record.getLocationName())
                .add("location_description", record.getLocationDescription())
                .add("location_color", record.getLocationColour())
                .add("farm_id",record.getFarmId())
                .add("dimensions",record.getDimensions())
                .add("purpose",record.getPurpose())
                .add("optimal_readings",record.getOptimalReadings())
                .add("type",record.getType())
                .add("location_data",record.getLocationData());

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


    public static void update(Location record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("location_name", record.getLocationName())
                .addIfExists("location_description", record.getLocationDescription())
                .addIfExists("location_color", record.getLocationColour())
                .addIfExists("farm_id",record.getFarmId())
                .addIfExists("optimal_readings",record.getOptimalReadings())
                .addIfExists("purpose",record.getPurpose())
                .addIfExists("dimensions",record.getDimensions())
                .addIfExists("type",record.getType())
                .addIfExists("location_data",record.getLocationData());

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
                LocationRepository::buildRecord);
    }


    public static DataResult query(LocationSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("id = ?", specification.getId())
                    .addIfExists("farm_id = ?",specification.getFarmId());

        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, LocationRepository::buildRecord);
    }


    public static void delete(String blockId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", blockId));
            LOG.debug("Location has been deleted:>>" + blockId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static Location buildRecord(ResultSet rs) {

        Location location = new Location();
        try {
              location.setId(rs.getString("id"));
              location.setPurpose(rs.getString("purpose"));
              location.setLocationName(rs.getString("location_name"));
              location.setFarmId(rs.getString("farm_id"));
              location.setDimensions(rs.getString("dimensions"));
              location.setLocationDescription(rs.getString("location_description"));
              location.setLocationColour(rs.getString("location_color"));
              location.setType(rs.getString("type"));
              location.setOptimalReadings(rs.getString("optimal_readings"));
              location.setLocationData(rs.getString("location_data"));

            return location;
        } catch (Exception e) {
            LOG.error("exception when building record for Location" + e.getMessage());
            return null;
        }
    }
}

