package com.simisinc.platform.infrastructure.persistence.cannacomply;


import com.simisinc.platform.domain.model.cannacomply.SoilPot;
import com.simisinc.platform.domain.model.cannacomply.WaterSource;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class WaterSourceRepository {

    private static Log LOG = LogFactory.getLog(WaterSourceRepository.class);
    private static String TABLE_NAME = "cannacomply.water_source";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static WaterSource add(WaterSource record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("name", record.getName())
                .add("optimal_readings_json", record.getOptimalReadingsJson())
                .add("farm_id",record.getFarmId())
                .add("date",record.getDate())
                .add("type", record.getType())
                .add("volume",record.getVolume())
                .add("usage",record.getUsage())
                .add("geo_data",record.getGeoData())
                .add("colour",record.getColour())
                .add("units",record.getUnits())
                .add("colour",record.getColour());

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


    public static void update(WaterSource record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .add("name", record.getName())
                .add("optimal_readings_json", record.getOptimalReadingsJson())
                .add("farm_id",record.getFarmId())
                .add("date",record.getDate())
                .add("type", record.getType())
                .add("volume",record.getVolume())
                .add("usage",record.getUsage())
                .add("geo_data",record.getGeoData())
                .add("colour",record.getColour())
                .add("units",record.getUnits())
                .add("colour",record.getColour());

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


    public static WaterSource findById(String id) {

        return (WaterSource) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("id = ?", id),
                WaterSourceRepository::buildRecord);
    }


    public static DataResult query(WaterSourceSpecification specification) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();

        if (specification != null) {
            where.addIfExists("farm_id = ?" ,specification.getFarmId());
        }

        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, null, WaterSourceRepository::buildRecord);
    }


    public static void delete(String soilPotId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", soilPotId));
            LOG.debug("water source has been deleted:>>" + soilPotId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static WaterSource buildRecord(ResultSet rs) {

        WaterSource ws = new WaterSource();
        try {
            ws.setId(rs.getString("id"));
            ws.setName(rs.getString("name"));
            ws.setOptimalReadingsJson(rs.getString("optimal_readings_json"));
            ws.setFarmId(rs.getString("farm_id"));
            ws.setDate(rs.getString("date"));
            ws.setType(rs.getString("type"));
            ws.setVolume(rs.getString("volume"));
            ws.setUsage(rs.getString("usage"));
            ws.setGeoData(rs.getString("geo_data"));
            ws.setColour(rs.getString("colour"));
            ws.setUnits(rs.getString("units"));

            return ws;
        } catch (Exception e) {
            LOG.error("exception when building record for Water source record" + e.getMessage());
            return null;
        }
    }
}

