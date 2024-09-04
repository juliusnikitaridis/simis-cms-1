package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Issue;
import com.simisinc.platform.domain.model.cannacomply.Reading;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class ReadingRepository {

    private static Log LOG = LogFactory.getLog(ReadingRepository.class);
    private static String TABLE_NAME = "cannacomply.reading";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static Reading add(Reading record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("date", record.getDate())
                .add("location_id",record.getLocationId())
                .add("device_id", record.getDeviceId())
                .add("reading_type", record.getReadingType())
                .add("reading_value", record.getReadingValue())
                .add("location_type",record.getLocationType())
                .add("status", record.getStatus())
                .add("farm_id", record.getFarmId())
                .add("action_taken", record.getActionTaken())
                .add("user_id", record.getUserId());

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


    public static void update(Reading record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("date", record.getDate())
                .addIfExists("device_id", record.getDeviceId())
                .addIfExists("reading_type", record.getReadingType())
                .addIfExists("reading_value", record.getReadingValue())
                .addIfExists("status", record.getStatus())
                .addIfExists("location_id",record.getLocationId())
                .addIfExists("location_type",record.getLocationType())
                .addIfExists("farm_id", record.getFarmId())
                .addIfExists("action_taken", record.getActionTaken())
                .addIfExists("user_id", record.getUserId());
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
                ReadingRepository::buildRecord);
    }


    public static DataResult query(ReadingSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("id = ?", specification.getId())
                    .addIfExists("farm_id = ?", specification.getFarmId())
                    .addIfExists("location_id = ?",specification.getLocationId())
                    .addIfExists("device_id = ?", specification.getDeviceId());

        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, ReadingRepository::buildRecord);
    }

    public static List<String> getAllDevicesForLocation(String farmId, String deviceType, String locationId) throws Exception {
        List<String> deviceList = new ArrayList<>();
        String sql = "select id from cannacomply.device" +
                " where location_id = ?" +
                " and farm_id = ?" +
                " and status = 'on' and device_type= ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, locationId);
            pstmt.setString(2, farmId);
            pstmt.setString(3, deviceType);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                deviceList.add(rs.getString("id"));
            }
            return deviceList;
        } catch (Exception e) {
            throw e;
        }
    }


    public static double getLatestReadingForDevice(String deviceId, String readingType) throws Exception {
        String sql = "select * from cannacomply.reading where device_id = ? and reading_type = ? order by date desc limit 1";
        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, deviceId);
            pstmt.setString(2, readingType);
            ResultSet rs = pstmt.executeQuery();
            double reading = 0.0;
            while (rs.next()) {
                reading = Double.valueOf(rs.getString("reading_value"));
            }
            return reading;

        } catch (Exception e) {
            throw e;
        }
    }


    public static void delete(String issueId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", issueId));
            LOG.debug("Reading has been deleted:>>" + issueId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static Reading buildRecord(ResultSet rs) {

        Reading reading = new Reading();
        try {
            reading.setId(rs.getString("id"));
            reading.setDate(rs.getString("date"));
            reading.setDeviceId(rs.getString("device_id"));
            reading.setReadingType(rs.getString("reading_type"));
            reading.setReadingValue(rs.getString("reading_value"));
            reading.setStatus(rs.getString("status"));
            reading.setFarmId(rs.getString("farm_id"));
            reading.setLocationId(rs.getString("location_id"));
            reading.setLocationType(rs.getString("location_type"));
            reading.setActionTaken(rs.getString("action_taken"));
            reading.setUserId(rs.getString("user_id"));
            return reading;
        } catch (Exception e) {
            LOG.error("exception when building record for Reading" + e.getMessage());
            return null;
        }
    }
}

