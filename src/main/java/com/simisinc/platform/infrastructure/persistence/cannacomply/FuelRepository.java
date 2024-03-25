package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Fuel;
import com.simisinc.platform.domain.model.cannacomply.GrowthCycle;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class FuelRepository {

    private static Log LOG = LogFactory.getLog(FuelRepository.class);
    private static String TABLE_NAME = "cannacomply.fuel";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static Fuel add(Fuel record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("machinery_type",record.getMachineryType())
                .add("start_time",record.getStartTime())
                .add("end_time",record.getEndTime())
                .add("fuel_consumption",record.getFuelConsumption())
                .add("farm_id",record.getFarmId())
                .add("capacity",record.getCapacity())
                .add("fuel_used",record.getFuelUsed())
                .add("mileage",record.getMileage())
                .add("reason_for_usage",record.getReasonForUsage())
                .add("warehouse_item_id",record.getWarehouseItemId())
                .add("user_id",record.getUserId())
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


    public static void update(Fuel record) throws Exception {
        SqlUtils updateValues = new SqlUtils()

                .addIfExists("machinery_type",record.getMachineryType())
                .addIfExists("start_time",record.getStartTime())
                .addIfExists("end_time",record.getEndTime())
                .addIfExists("fuel_consumption",record.getFuelConsumption())
                .addIfExists("farm_id",record.getFarmId())
                .addIfExists("mileage",record.getMileage())
                .addIfExists("fuel_used",record.getFuelUsed())
                .addIfExists("capacity",record.getCapacity())
                .addIfExists("reason_for_usage",record.getReasonForUsage())
                .addIfExists("warehouse_item_id",record.getWarehouseItemId())
                .addIfExists("user_id",record.getUserId())
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

    public static GrowthCycle findById(String id) {

        return (GrowthCycle) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("id = ?", id),
                FuelRepository::buildRecord);
    }


    public static DataResult query(FuelSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("farm_id = ?",specification.getFarmId())
                    .addIfExists("id = ?",specification.getId());
        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, FuelRepository::buildRecord);
    }


    public static void delete(String issueId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", issueId));
            LOG.debug("fuel record has been deleted:>>" + issueId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static Fuel buildRecord(ResultSet rs) {
        Fuel fuel = new Fuel();
        try {
          fuel.setId(rs.getString("id"));
          fuel.setMachineryType(rs.getString("machinery_type"));
          fuel.setStartTime(rs.getString("start_time"));
          fuel.setEndTime(rs.getString("end_time"));
          fuel.setFuelConsumption(rs.getString("fuel_consumption"));
          fuel.setFarmId(rs.getString("farm_id"));
          fuel.setMileage(rs.getString("mileage"));
          fuel.setFuelUsed(rs.getString("fuel_used"));
          fuel.setReasonForUsage(rs.getString("reason_for_usage"));
          fuel.setCapacity(rs.getString("capacity"));
          fuel.setWarehouseItemId(rs.getString("warehouse_item_id"));
          fuel.setUserId(rs.getString("user_id"));
          fuel.setDate(rs.getString("date"));
          return fuel;
        } catch (Exception e) {
            LOG.error("exception when building record for Fuel" + e.getMessage());
            return null;
        }
    }
}

