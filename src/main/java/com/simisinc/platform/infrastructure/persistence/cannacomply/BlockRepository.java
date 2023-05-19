package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Activity;
import com.simisinc.platform.domain.model.cannacomply.Block;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class BlockRepository {

    private static Log LOG = LogFactory.getLog(BlockRepository.class);
    private static String TABLE_NAME = "cannacomply.block";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static Block add(Block record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("block_location", record.getBlockLocation())
                .add("barcode_data", record.getBarcodeData())
                .add("farm_id", record.getFarmId())
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


    public static void update(Block record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("block_location", record.getBlockLocation())
                .addIfExists("barcode_data", record.getBarcodeData())
                .addIfExists("farm_id", record.getFarmId())
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


    public static Activity findById(String id) {

        return (Activity) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("id = ?", id),
                BlockRepository::buildRecord);
    }


    public static DataResult query(BlockSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("id = ?", specification.getId())
                    .addIfExists("farm_id = ?",specification.getFarmId());

        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, BlockRepository::buildRecord);
    }


    public static void delete(String blockId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", blockId));
            LOG.debug("block has been deleted:>>" + blockId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static Block buildRecord(ResultSet rs) {

        Block block = new Block();
        try {
              block.setBlockLocation(rs.getString("block_location"));
              block.setBarcodeData(rs.getString("barcode_data"));
              block.setFarmId(rs.getString("farm_id"));
              block.setDate(rs.getString("date"));
              block.setId(rs.getString("id"));

            return block;
        } catch (Exception e) {
            LOG.error("exception when building record for block" + e.getMessage());
            return null;
        }
    }
}

