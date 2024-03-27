package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.FeedingRegime;
import com.simisinc.platform.domain.model.cannacomply.Fuel;
import com.simisinc.platform.domain.model.cannacomply.GrowthCycle;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class FeedingRegimeRepository {

    private static Log LOG = LogFactory.getLog(FeedingRegimeRepository.class);
    private static String TABLE_NAME = "cannacomply.fuel";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static FeedingRegime add(FeedingRegime record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("growth_stage",record.getGrowthStage())
                .add("weeks",record.getWeeks())
                .add("nutrient_id",record.getNutrientId())
                .add("volume",record.getVolume())
                .add("frequency",record.getFrequency())
                .add("ph",record.getPh())
                .add("watering_schedule",record.getWateringSchedule())
                .add("flush_schedule",record.getFlushSchedule())
                .add("notes",record.getNotes())
                .add("nutrient_conc",record.getNutrientConc())
                .add("farm_id",record.getFarmId())
                .add("name",record.getName());

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


    public static void update(FeedingRegime record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("growth_stage",record.getGrowthStage())
                .addIfExists("weeks",record.getWeeks())
                .addIfExists("nutrient_id",record.getNutrientId())
                .addIfExists("volume",record.getVolume())
                .addIfExists("frequency",record.getFrequency())
                .addIfExists("ph",record.getPh())
                .addIfExists("watering_schedule",record.getWateringSchedule())
                .addIfExists("flush_schedule",record.getFlushSchedule())
                .addIfExists("notes",record.getNotes())
                .addIfExists("nutrient_conc",record.getNutrientConc())
                .addIfExists("farm_id",record.getFarmId())
                .addIfExists("name",record.getName());
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
                FeedingRegimeRepository::buildRecord);
    }


    public static DataResult query(FeedingRegimeSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("farm_id = ?",specification.getFarmId())
                    .addIfExists("id = ?",specification.getId());
        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, FeedingRegimeRepository::buildRecord);
    }


    public static void delete(String issueId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", issueId));
            LOG.debug("fuel record has been deleted:>>" + issueId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static FeedingRegime buildRecord(ResultSet rs) {
        FeedingRegime regime = new FeedingRegime();
        try {
          regime.setId(rs.getString("id"));
          regime.setGrowthStage(rs.getString("growth_stage"));
          regime.setWeeks(rs.getString("weeks"));
          regime.setNutrientId(rs.getString("nutrient_id"));
          regime.setVolume(rs.getString("volume"));
          regime.setFrequency(rs.getString("frequency"));
          regime.setPh(rs.getString("ph"));
          regime.setWateringSchedule(rs.getString("watering_schedule"));
          regime.setFlushSchedule(rs.getString("flush_schedule"));
          regime.setNotes(rs.getString("notes"));
          regime.setNutrientConc(rs.getString("nutrient_conc"));
          regime.setFarmId(rs.getString("farm_id"));
          regime.setName(rs.getString("name"));
          return regime;
        } catch (Exception e) {
            LOG.error("exception when building record for FeedingRegime" + e.getMessage());
            return null;
        }
    }
}

