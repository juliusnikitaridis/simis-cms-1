package com.simisinc.platform.infrastructure.persistence.cannacomply;


import com.simisinc.platform.domain.model.cannacomply.SoilManagement;
import com.simisinc.platform.domain.model.cannacomply.Strain;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class SoilManagementRepository {

    private static Log LOG = LogFactory.getLog(SoilManagementRepository.class);
    private static String TABLE_NAME = "cannacomply.soil_management";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static SoilManagement add(SoilManagement record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("texture", record.getTexture())
                .add("ph", record.getPh())
                .add("farm_id",record.getFarmId())
                .add("date",record.getDate())
                .add("nutrient_levels", record.getNutrientLevels())
                .add("soil_type",record.getSoilType())
                .add("warehouse_item_id",record.getWarehouseItemId())
                .add("organic_matter",record.getOrganicMatter())
                .add("location_type",record.getLocationType())
                .add("location_id",record.getLocationId());

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


    public static void update(SoilManagement record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .add("texture", record.getTexture())
                .add("ph", record.getPh())
                .add("farm_id",record.getFarmId())
                .add("date",record.getDate())
                .add("nutrient_levels", record.getNutrientLevels())
                .add("soil_type",record.getSoilType())
                .add("warehouse_item_id",record.getWarehouseItemId())
                .add("organic_matter",record.getOrganicMatter())
                .add("location_type",record.getLocationType())
                .add("location_id",record.getLocationId());

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


    public static Vehicle findById(String id) {

        return (Vehicle) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("id = ?", id),
                SoilManagementRepository::buildRecord);
    }


    public static DataResult query(SoilManagementSpecification specification) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();

        if (specification != null) {
            where.addIfExists("farm_id = ?" ,specification.getFarmId());
        }

        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, null, SoilManagementRepository::buildRecord);
    }


    public static void delete(String strainId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", strainId));
            LOG.debug("soilManagement has been deleted:>>" + strainId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static SoilManagement buildRecord(ResultSet rs) {

        SoilManagement soilManagement = new SoilManagement();
        try {
            soilManagement.setId(rs.getString("id"));
            soilManagement.setTexture(rs.getString("texture"));
            soilManagement.setPh(rs.getString("ph"));
            soilManagement.setFarmId(rs.getString("farm_id"));
            soilManagement.setDate(rs.getString("date"));
            soilManagement.setNutrientLevels(rs.getString("nutrient_levels"));
            soilManagement.setSoilType(rs.getString("soil_type"));
            soilManagement.setLocationType(rs.getString("location_type"));
            soilManagement.setWarehouseItemId(rs.getString("warehouse_item_id"));
            soilManagement.setOrganicMatter(rs.getString("organic_matter"));

            return soilManagement;
        } catch (Exception e) {
            LOG.error("exception when building record for SoilManagement" + e.getMessage());
            return null;
        }
    }
}

