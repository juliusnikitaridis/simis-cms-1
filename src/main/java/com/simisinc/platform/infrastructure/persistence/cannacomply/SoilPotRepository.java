package com.simisinc.platform.infrastructure.persistence.cannacomply;


import com.simisinc.platform.domain.model.cannacomply.SoilPot;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.Connection;
import java.sql.ResultSet;


public class SoilPotRepository {

    private static Log LOG = LogFactory.getLog(SoilPotRepository.class);
    private static String TABLE_NAME = "cannacomply.soil_pot";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static SoilPot add(SoilPot record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("soil_id", record.getSoilId())
                .add("quantity", record.getQuantity())
                .add("farm_id",record.getFarmId())
                .add("date",record.getDate())
                .add("measurements", record.getMeasurements())
                .add("status",record.getStatus())
                .add("location_id",record.getLocationId())
                .add("location_type",record.getLocationType())
                .add("container_type",record.getContainerType());

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


    public static void update(SoilPot record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("soil_id", record.getSoilId())
                .addIfExists("quantity", record.getQuantity())
                .addIfExists("farm_id",record.getFarmId())
                .addIfExists("date",record.getDate())
                .addIfExists("measurements", record.getMeasurements())
                .addIfExists("status",record.getStatus())
                .addIfExists("location_id",record.getLocationId())
                .addIfExists("location_type",record.getLocationType())
                .addIfExists("container_type",record.getContainerType());

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


    public static SoilPot findById(String id) {

        return (SoilPot) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("id = ?", id),
                SoilPotRepository::buildRecord);
    }


    public static DataResult query(SoilPotSpecification specification) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();

        if (specification != null) {
            where.addIfExists("farm_id = ?" ,specification.getFarmId());
            where.addIfExists("soil_id = ?",specification.getSoilId());
        }

        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, null, SoilPotRepository::buildRecord);
    }


    public static void delete(String soilPotId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", soilPotId));
            LOG.debug("SoilPot has been deleted:>>" + soilPotId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static SoilPot buildRecord(ResultSet rs) {

        SoilPot soilPot = new SoilPot();
        try {
            soilPot.setId(rs.getString("id"));
            soilPot.setSoilId(rs.getString("soil_id"));
            soilPot.setQuantity(rs.getString("quantity"));
            soilPot.setFarmId(rs.getString("farm_id"));
            soilPot.setDate(rs.getString("date"));
            soilPot.setMeasurements(rs.getString("measurements"));
            soilPot.setStatus(rs.getString("status"));
            soilPot.setLocationType(rs.getString("location_type"));
            soilPot.setLocationId(rs.getString("location_id"));
            soilPot.setContainerType(rs.getString("container_type"));

            return soilPot;
        } catch (Exception e) {
            LOG.error("exception when building record for SoilPot record" + e.getMessage());
            return null;
        }
    }
}

