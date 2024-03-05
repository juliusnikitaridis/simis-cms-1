package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Device;
import com.simisinc.platform.domain.model.cannacomply.GrowthCycle;
import com.simisinc.platform.domain.model.cannacomply.Issue;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class GrowthCycleRepository {

    private static Log LOG = LogFactory.getLog(GrowthCycleRepository.class);
    private static String TABLE_NAME = "cannacomply.growth_cycle";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static GrowthCycle add(GrowthCycle record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("plants",record.getPlants())
                .add("start_date",record.getStartDate())
                .add("end_date",record.getEndDate())
                .add("variety",record.getVariety())
                .add("commodity",record.getCommodity())
                .add("yield",record.getYield())
                .add("growth_cycle_name",record.getGrowthCycleName())
                .add("farm_id",record.getFarmId());

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


    public static void update(GrowthCycle record) throws Exception {
        SqlUtils updateValues = new SqlUtils()

                .addIfExists("id", record.getId())
                .addIfExists("plants",record.getPlants())
                .addIfExists("start_date",record.getStartDate())
                .addIfExists("end_date",record.getEndDate())
                .addIfExists("variety",record.getVariety())
                .addIfExists("growth_cycle_name",record.getGrowthCycleName())
                .addIfExists("yield",record.getYield())
                .addIfExists("commodity",record.getCommodity())
                .addIfExists("farm_id",record.getFarmId());
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
                GrowthCycleRepository::buildRecord);
    }


    public static DataResult query(GrowthCycleSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("farm_id = ?",specification.getFarmId());
        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, GrowthCycleRepository::buildRecord);
    }


    public static void delete(String issueId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", issueId));
            LOG.debug("growth cycle has been deleted:>>" + issueId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static GrowthCycle buildRecord(ResultSet rs) {

        GrowthCycle cycle = new GrowthCycle();
        try {
          cycle.setId(rs.getString("id"));
          cycle.setPlants(rs.getString("plants"));
          cycle.setVariety(rs.getString("variety"));
          cycle.setFarmId(rs.getString("farm_id"));
          cycle.setYield(rs.getString("yield"));
          cycle.setCommodity(rs.getString("commodity"));
          cycle.setGrowthCycleName(rs.getString("growth_cycle_name"));
          cycle.setStartDate(rs.getString("start_date"));
          cycle.setEndDate(rs.getString("end_date"));
          return cycle;
        } catch (Exception e) {
            LOG.error("exception when building record for Growth Cycle" + e.getMessage());
            return null;
        }
    }
}

