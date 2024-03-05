package com.simisinc.platform.infrastructure.persistence.carfix;

import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.domain.model.carfix.Yield;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class YieldRepository {

    private static Log LOG = LogFactory.getLog(YieldRepository.class);
    private static String TABLE_NAME = "cannacomply.yield";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static Yield add(Yield record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("quantity", record.getQuantity())
                .add("notes", record.getNotes())
                .add("farm_id",record.getFarmId())
                .add("harvest_batch_id", record.getHarvestBatchId())
                .add("location_id", record.getLocationId())
                .add("crop_id", record.getCropId())
                .add("stage",record.getStage())
                .add("variety", record.getVariety())
                .add("last_updated",record.getLastUpdated())
                .add("harvested_item",record.getHarvestedItem())
                .add("from_block_id",record.getFromBlockId())
                .add("color",record.getColor())
                .add("texture",record.getTexture())
                .add("user_id",record.getUserId())
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
                .addIfExists("notes", record.getNotes())
                .addIfExists("farm_id",record.getFarmId())
                .addIfExists("harvest_batch_id", record.getHarvestBatchId())
                .addIfExists("location_id", record.getLocationId())
                .addIfExists("crop_id", record.getCropId())
                .addIfExists("variety", record.getVariety())
                .addIfExists("last_updated",record.getLastUpdated())
                .addIfExists("harvested_item",record.getHarvestedItem())
                .addIfExists("user_id",record.getUserId())
                .addIfExists("stage",record.getStage())
                .addIfExists("color",record.getColor())
                .addIfExists("texture",record.getTexture())
                .addIfExists("from_block_id",record.getFromBlockId())
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


    public static Vehicle findById(String id) {

        return (Vehicle) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("id = ?", id),
                YieldRepository::buildRecord);
    }


    public static DataResult query(YieldSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("id = ?", specification.getId())
                    .addIfExists("farm_id = ?",specification.getFarmId())
                    .addIfExists("crop_id = ?",specification.getCropId());

        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, YieldRepository::buildRecord);
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
            record.setStage(rs.getString("stage"));
            record.setNotes(rs.getString("notes"));
            record.setUserId(rs.getString("user_id"));
            record.setHarvestBatchId(rs.getString("harvest_batch_id"));
            record.setLocationId(rs.getString("location_id"));
            record.setCropId(rs.getString("crop_id"));
            record.setDate(rs.getString("date"));
            record.setLastUpdated(rs.getString("last_updated"));
            record.setHarvestedItem(rs.getString("harvested_item"));
            record.setFarmId(rs.getString("farm_id"));
            record.setFromBlockId(rs.getString("from_block_id"));
            record.setVariety(rs.getString("variety"));
            record.setColor("color");
            record.setTexture("texture");
            return record;
        } catch (Exception e) {
            LOG.error("exception when building record for yield" + e.getMessage());
            return null;
        }
    }
}

