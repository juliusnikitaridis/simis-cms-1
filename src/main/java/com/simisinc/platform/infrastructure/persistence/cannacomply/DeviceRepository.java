package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Device;
import com.simisinc.platform.domain.model.cannacomply.Issue;
import com.simisinc.platform.domain.model.cannacomply.Reading;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class DeviceRepository {

    private static Log LOG = LogFactory.getLog(DeviceRepository.class);
    private static String TABLE_NAME = "cannacomply.device";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static Device add(Device record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("device_type",record.getDeviceType())
                .add("location_type",record.getLocationType())
                .add("location_id",record.getLocationId())
                .add("date",record.getDate())
                .add("warehouse_item_id",record.getWareHouseItemId())
                .add("device_name",record.getDeviceName())
                .add("barcode",record.getBarcode())
                .add("comments",record.getComments())
                .add("status",record.getStatus())
                .add("farm_id",record.getFarmId())
                .add("usage",record.getUsage())
                .add("last_updated",record.getLastUpdated());

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


    public static void update(Device record) throws Exception {
        SqlUtils updateValues = new SqlUtils()

                .addIfExists("device_type",record.getDeviceType())
                .addIfExists("location_type",record.getLocationType())
                .addIfExists("location_id",record.getLocationId())
                .addIfExists("date",record.getDate())
                .addIfExists("comments",record.getComments())
                .addIfExists("status",record.getStatus())
                .addIfExists("warehouse_item_id",record.getWareHouseItemId())
                .addIfExists("device_name",record.getDeviceName())
                .addIfExists("barcode",record.getBarcode())
                .addIfExists("farm_id",record.getFarmId())
                .addIfExists("usage",record.getUsage())
                .addIfExists("last_updated",record.getLastUpdated());
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
                DeviceRepository::buildRecord);
    }


    public static DataResult query(DeviceSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("id = ?", specification.getId())
                    .addIfExists("farm_id = ?",specification.getFarmId())
                    .addIfExists("location_id = ?",specification.getLocationId());


        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, DeviceRepository::buildRecord);
    }


    public static void delete(String issueId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", issueId));
            LOG.debug("Device has been deleted:>>" + issueId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static Device buildRecord(ResultSet rs) {

        Device device = new Device();
        try {
            device.setId(rs.getString("id"));
            device.setDeviceType(rs.getString("device_type"));
            device.setLocationType(rs.getString("location_type"));
            device.setLocationId(rs.getString("location_id"));
            device.setDate(rs.getString("date"));
            device.setStatus(rs.getString("status"));
            device.setDeviceName(rs.getString("device_name"));
            device.setWareHouseItemId(rs.getString("warehouse_item_id"));
            device.setBarcode(rs.getString("barcode"));
            device.setFarmId(rs.getString("farm_id"));
            device.setUsage(rs.getString("usage"));
            device.setComments(rs.getString("comments"));
            device.setLastUpdated(rs.getString("last_updated"));
            return device;
        } catch (Exception e) {
            LOG.error("exception when building record for Device" + e.getMessage());
            return null;
        }
    }
}

