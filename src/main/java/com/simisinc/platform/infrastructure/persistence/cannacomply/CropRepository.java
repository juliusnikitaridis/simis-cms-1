package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Crop;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.Connection;
import java.sql.ResultSet;


public class CropRepository {

    private static Log LOG = LogFactory.getLog(CropRepository.class);
    private static String TABLE_NAME = "cannacomply.crop";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static Crop add(Crop record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("crop_type", record.getCropType())
                .add("block_location",record.getBlockLocation())
                .add("growth_stage", record.getGrowthStage())
                .add("block_id",record.getBlockId())
                .add("pot_id",record.getPotId())
                .add("status",record.getStatus())
                .add("feeding_regime_id",record.getFeedingRegimeId())
                .add("strain_name",record.getStrainName())
                .add("seed_company",record.getSeedCompany())
                .add("farm_id",record.getFarmId())
                .add("growth_cycle_id",record.getGrowthCycleId())
                .add("user_id",record.getUserId())
                .add("barcode_data",record.getBarcodeData())
                .add("crop_label",record.getCropLabel())
                .add("created_date",record.getCreatedDate())
                .add("location_id",record.getLocationId())
                .add("location_type",record.getLocationType())
                .add("last_updated",record.getLastUpdated())
                .add("lot_number",record.getLotNumber())
                .add("starting_plant_data",record.getStartingPlantData());

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


    public static void update(Crop record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("crop_type", record.getCropType())
                .addIfExists("growth_stage", record.getGrowthStage())
                .addIfExists("block_id",record.getBlockId())
                .addIfExists("status",record.getStatus())
                .addIfExists("strain_name",record.getStrainName())
                .addIfExists("seed_company",record.getSeedCompany())
                .addIfExists("farm_id",record.getFarmId())
                .addIfExists("pot_id",record.getPotId())
                .addIfExists("barcode_data",record.getBarcodeData())
                .addIfExists("user_id",record.getUserId())
                .addIfExists("feeding_regime_id",record.getFeedingRegimeId())
                .addIfExists("crop_label",record.getCropLabel())
                .addIfExists("location_id",record.getLocationId())
                .addIfExists("location_type",record.getLocationType())
                .addIfExists("growth_cycle_id",record.getGrowthCycleId())
                .addIfExists("last_updated",record.getLastUpdated())
                .addIfExists("lot_number",record.getLotNumber())
                .addIfExists("starting_plant_data",record.getStartingPlantData());

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




    public static DataResult query(CropSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("id = ?", specification.getId())
                    .addIfExists("farm_id = ?", specification.getFarmId())
                    .addIfExists("room_id",specification.getRoomId())
                    .addIfExists("location_id = ?",specification.getLocationId())
                    .addIfExists("block_id = ?",specification.getBlockId());

        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, CropRepository::buildRecord);
    }


    public static void delete(String farmId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", farmId));
            LOG.debug("crop has been deleted:>>" + farmId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static Crop buildRecord(ResultSet rs) {

        Crop crop = new Crop();
        try {
            crop.setId(rs.getString("id"));
            crop.setCropType(rs.getString("crop_type"));
            crop.setGrowthStage(rs.getString("growth_stage"));
            crop.setStatus(rs.getString("status"));
            crop.setStrainName(rs.getString("strain_name"));
            crop.setSeedCompany(rs.getString("seed_company"));
            crop.setFarmId(rs.getString("farm_id"));
            crop.setUserId(rs.getString("user_id"));
            crop.setBlockLocation(rs.getString("block_location"));
            crop.setLocationId(rs.getString("location_id"));
            crop.setLocationType(rs.getString("location_type"));
            crop.setPotId(rs.getString("pot_id"));
            crop.setFeedingRegimeId(rs.getString("feeding_regime_id"));
            crop.setGrowthCycleId(rs.getString("growth_cycle_id"));
            crop.setCropLabel(rs.getString("crop_label"));
            crop.setBarcodeData(rs.getString("barcode_data"));
            crop.setCreatedDate(rs.getString("created_date"));
            crop.setBlockId(rs.getString("block_id"));
            crop.setLastUpdated(rs.getString("last_updated"));
            crop.setLotNumber(rs.getString("lot_number"));
            crop.setStartingPlantData(rs.getString("starting_plant_data"));
            return crop;
        } catch (Exception e) {
            LOG.error("exception when building record for crop" + e.getMessage());
            return null;
        }
    }
}

