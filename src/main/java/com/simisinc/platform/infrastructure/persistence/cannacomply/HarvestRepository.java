package com.simisinc.platform.infrastructure.persistence.cannacomply;


import com.simisinc.platform.domain.model.cannacomply.Yield;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.Connection;
import java.sql.ResultSet;


public class HarvestRepository {

    private static Log LOG = LogFactory.getLog(HarvestRepository.class);
    private static String TABLE_NAME = "cannacomply.harvest";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static Yield add(Yield record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("quantity", record.getQuantity())
                .add("batch_number",record.getBatchNumber())
                .add("loss", record.getLoss())
                .add("notes", record.getNotes())
                .add("farm_id",record.getFarmId())
                .add("container_number", record.getContainerNumber())
                .add("location_id", record.getLocationId())
                .add("crop_id", record.getCropId())
                .add("stage",record.getStage())
                .add("last_updated",record.getLastUpdated())
                .add("harvested_item",record.getHarvestedItem())
                .add("strain", record.getStrain())
                .add("from_block_id",record.getFromBlockId())
                .add("wet_weight",record.getWetWeight())
                .add("user_id",record.getUserId())
                .add("is_mixed",record.getIsMixed())
                .add("date", record.getDate());

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


    public static void update(Yield record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("quantity", record.getQuantity())
                .addIfExists("loss", record.getLoss())
                .addIfExists("notes", record.getNotes())
                .addIfExists("farm_id",record.getFarmId())
                .addIfExists("container_number", record.getContainerNumber())
                .addIfExists("location_id", record.getLocationId())
                .addIfExists("batch_number",record.getBatchNumber())
                .addIfExists("crop_id", record.getCropId())
                .addIfExists("strain", record.getStrain())
                .addIfExists("last_updated",record.getLastUpdated())
                .addIfExists("harvested_item",record.getHarvestedItem())
                .addIfExists("wet_weight",record.getWetWeight())
                .addIfExists("user_id",record.getUserId())
                .addIfExists("stage",record.getStage())
                .addIfExists("from_block_id",record.getFromBlockId())
                .addIfExists("is_mixed",record.getIsMixed())
                .addIfExists("date", record.getDate());


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


    public static Yield findById(String id) {

        return (Yield) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("id = ?", id),
                HarvestRepository::buildRecord);
    }


    public static DataResult query(HarvestSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("id = ?", specification.getId())
                    .addIfExists("farm_id = ?",specification.getFarmId())
                    .addIfExists("batch_number = ?",specification.getBatchNumber())
                    .addIfExists("container_number = ?",specification.getContainerNumber());

        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, HarvestRepository::buildRecord);
    }


    public static void delete(String id) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", id));
            LOG.debug("yield has  has been deleted:>>" + id);
        } catch (Exception e) {
            throw e;
        }
    }


    private static Yield buildRecord(ResultSet rs) {

        Yield record = new Yield();
        try {
            record.setId(rs.getString("id"));
            record.setQuantity(rs.getString("quantity"));
            record.setLoss(rs.getString("loss"));
            record.setStage(rs.getString("stage"));
            record.setNotes(rs.getString("notes"));
            record.setWetWeight(rs.getString("wet_weight"));
            record.setUserId(rs.getString("user_id"));
            record.setContainerNumber(rs.getString("container_number"));
            record.setLocationId(rs.getString("location_id"));
            record.setBatchNumber(rs.getString("batch_number"));
            record.setCropId(rs.getString("crop_id"));
            record.setDate(rs.getString("date"));
            record.setLastUpdated(rs.getString("last_updated"));
            record.setHarvestedItem(rs.getString("harvested_item"));
            record.setFarmId(rs.getString("farm_id"));
            record.setFromBlockId(rs.getString("from_block_id"));
            record.setStrain(rs.getString("strain"));
            record.setIsMixed(rs.getString("is_mixed"));
            return record;
        } catch (Exception e) {
            LOG.error("exception when building record for harvest" + e.getMessage());
            return null;
        }
    }
}
