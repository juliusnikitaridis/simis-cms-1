package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Farm;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.database.*;
import com.simisinc.platform.infrastructure.persistence.carfix.VehicleSpecification;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class FarmRepository {

    private static Log LOG = LogFactory.getLog(FarmRepository.class);
    private static String TABLE_NAME = "cannacomply.farm";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static Farm add(Farm record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("name", record.getName())
                .add("commodities",record.getCommodities())
                .add("latitude", record.getLatitude())
                .add("longitude", record.getLongitude())
                .add("type",record.getType())
                .add("location_data",record.getLocationData())
                .add("logo_data",record.getLogoData())
                .add("address",record.getAddress())
                .add("user_id",record.getUserId());

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


    public static void update(Farm record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("name", record.getName())
                .addIfExists("latitude", record.getLatitude())
                .addIfExists("longitude", record.getLongitude())
                .addIfExists("type",record.getType())
                .addIfExists("commodities",record.getCommodities())
                .addIfExists("logo_data",record.getLogoData())
                .addIfExists("location_data",record.getLocationData())
                .addIfExists("user_id",record.getUserId())
                .addIfExists("address",record.getAddress());

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


    public static Farm findById(String id) {

        return (Farm) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("id = ?", id),
                FarmRepository::buildRecord);
    }


    public static DataResult query(FarmSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("id = ?", specification.getId())
                    .addIfExists("user_id = ?",specification.getUserId());

        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, FarmRepository::buildRecord);
    }


    public static void delete(String farmId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", farmId));
            LOG.debug("farm has been deleted:>>" + farmId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static Farm buildRecord(ResultSet rs) {

        Farm farm = new Farm();
        try {
            farm.setId(rs.getString("id"));
            farm.setLatitude(rs.getString("latitude"));
            farm.setLongitude(rs.getString("longitude"));
            farm.setLogoData(rs.getString("logo_data"));
            farm.setName(rs.getString("name"));
            farm.setCommodities(rs.getString("commodities"));
            farm.setType(rs.getString("type"));
            farm.setAddress(rs.getString("address"));
            farm.setUserId(rs.getString("user_id"));
            farm.setLocationData(rs.getString("location_data"));
            return farm;
        } catch (Exception e) {
            LOG.error("exception when building record for farm" + e.getMessage());
            return null;
        }
    }
}

