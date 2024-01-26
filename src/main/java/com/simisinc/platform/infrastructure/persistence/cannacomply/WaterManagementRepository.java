package com.simisinc.platform.infrastructure.persistence.cannacomply;


import com.simisinc.platform.domain.model.cannacomply.Hygiene;
import com.simisinc.platform.domain.model.cannacomply.WaterManagement;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class WaterManagementRepository {

    private static Log LOG = LogFactory.getLog(WaterManagementRepository.class);
    private static String TABLE_NAME = "cannacomply.water_management";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static WaterManagement add(WaterManagement record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("user_id", record.getUserId())
                .add("location_id", record.getLocationId())
                .add("location_type",record.getLocationType())
                .add("water_use",record.getWaterUse())
                .add("farm_id",record.getFarmId())
                .add("water_source_id",record.getWaterSourceId())
                .add("start_time",record.getStartTime())
                .add("end_time",record.getEndTime())
                .add("method_used",record.getMethodUsed())
                .add("flow_rate",record.getFlowRate())
                .add("ppm_levels",record.getPpmLevels())
                .add("device_id",record.getDeviceId())
                .add("water_quantity",record.getWaterQuantity())
                .add("water_units_used",record.getWaterUnitsUsed())
                .add("water_temp",record.getWaterTemp())
                .add("water_ph",record.getWaterPh())
                .add("rate_unit",record.getRateUnit())
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


    public static void update(WaterManagement record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .add("user_id", record.getUserId())
                .add("location_id", record.getLocationId())
                .add("location_type",record.getLocationType())
                .add("water_use",record.getWaterUse())
                .add("farm_id",record.getFarmId())
                .add("water_source_id",record.getWaterSourceId())
                .add("start_time",record.getStartTime())
                .add("end_time",record.getEndTime())
                .add("ppm_levels",record.getPpmLevels())
                .add("device_id",record.getDeviceId())
                .add("method_used",record.getMethodUsed())
                .add("flow_rate",record.getFlowRate())
                .add("water_quantity",record.getWaterQuantity())
                .add("water_units_used",record.getWaterUnitsUsed())
                .add("water_temp",record.getWaterTemp())
                .add("water_ph",record.getWaterPh())
                .add("rate_unit",record.getRateUnit())
                .add("date",record.getDate());

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


    public static WaterManagement findById(String id) {

        return (WaterManagement) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("id = ?", id),
                WaterManagementRepository::buildRecord);
    }



    public static DataResult query(WaterManagementSpecification specification) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();

        if (specification != null) {
            where
                    .addIfExists("location_id = ?", specification.getLocationId())
                    .addIfExists("farm_id = ?",specification.getFarmId());

        }

        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, null, WaterManagementRepository::buildRecord);
    }


    public static void delete(String strainId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", strainId));
            LOG.debug("strain has been deleted:>>" + strainId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static WaterManagement buildRecord(ResultSet rs) {

        WaterManagement waterManagement = new WaterManagement();
        try {
            waterManagement.setId(rs.getString("id"));
            waterManagement.setUserId(rs.getString("user_id"));
            waterManagement.setLocationId(rs.getString("location_id"));
            waterManagement.setLocationType(rs.getString("location_type"));
            waterManagement.setWaterUse(rs.getString("water_use"));
            waterManagement.setFarmId(rs.getString("farm_id"));
            waterManagement.setWaterSourceId(rs.getString("water_source_id"));
            waterManagement.setStartTime(rs.getString("start_time"));
            waterManagement.setEndTime(rs.getString("end_time"));
            waterManagement.setMethodUsed(rs.getString("method_used"));
            waterManagement.setFlowRate(rs.getString("flow_rate"));
            waterManagement.setPpmLevels(rs.getString("ppm_levels"));
            waterManagement.setDeviceId(rs.getString("device_id"));
            waterManagement.setWaterTemp(rs.getString("water_temp"));
            waterManagement.setWaterQuantity(rs.getString("water_quantity"));
            waterManagement.setWaterUnitsUsed(rs.getString("water_units_used"));
            waterManagement.setWaterPh(rs.getString("water_ph"));
            waterManagement.setRateUnit(rs.getString("rate_unit"));
            waterManagement.setDate(rs.getString("date"));
            return waterManagement;
        } catch (Exception e) {
            LOG.error("exception when building record for Strain" + e.getMessage());
            return null;
        }
    }
}

