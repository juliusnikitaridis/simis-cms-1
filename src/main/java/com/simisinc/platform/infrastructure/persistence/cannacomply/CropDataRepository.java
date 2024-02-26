package com.simisinc.platform.infrastructure.persistence.cannacomply;


import com.simisinc.platform.domain.model.cannacomply.CropData;
import com.simisinc.platform.domain.model.cannacomply.WaterSource;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class CropDataRepository {

    private static Log LOG = LogFactory.getLog(CropDataRepository.class);
    private static String TABLE_NAME = "cannacomply.crop_data";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static WaterSource add(WaterSource record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("item_code", record.getName())
                .add("item_name", record.getOptimalReadingsJson())
                .add("country",record.getFarmId())
                .add("seasonality",record.getDate())
                .add("crop_type", record.getType())
                .add("variety",record.getVolume())
                .add("commodity",record.getUsage());

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


    public static void update(CropData record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("item_name", record.getItemName())
                .addIfExists("country",record.getCountry())
                .addIfExists("seasonality",record.getSeasonality())
                .addIfExists("crop_type", record.getCropType())
                .addIfExists("variety",record.getVariety())
                .addIfExists("commodity",record.getCommodity());

            try (Connection connection = DB.getConnection();
                 AutoStartTransaction a = new AutoStartTransaction(connection);
                 AutoRollback transaction = new AutoRollback(connection)) {
                // In a transaction (use the existing connection)
                DB.update(connection, TABLE_NAME, updateValues, new SqlUtils().add("item_code = ?", record.getItemCode()));
                transaction.commit();

        } catch (Exception se) {
            LOG.error("SQLException: " + se.getMessage());
            throw new Exception(se.getMessage());
        }
    }


    public static CropData findById(String id) {

        return (CropData) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("id = ?", id),
                CropDataRepository::buildRecord);
    }



    public static DataResult query(CropDataSpecification specification) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();

        if (specification != null) {
            where.addIfExists("commodity = ?" ,specification.getCommodity());
        }

        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, null, CropDataRepository::buildRecord);
    }


    public static void delete(String cropDataId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", cropDataId));
            LOG.debug("crop data has been deleted:>>" + cropDataId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static CropData buildRecord(ResultSet rs) {

        CropData ws = new CropData();
        try {
            ws.setId(rs.getString("id"));
            ws.setItemCode(rs.getString("item_code"));
            ws.setItemName(rs.getString("item_name"));
            ws.setCountry(rs.getString("country"));
            ws.setSeasonality(rs.getString("seasonality"));
            ws.setCropType(rs.getString("crop_type"));
            ws.setVariety(rs.getString("variety"));
            ws.setCommodity(rs.getString("commodity"));
            return ws;
        } catch (Exception e) {
            LOG.error("exception when building record for crop data" + e.getMessage());
            return null;
        }
    }
}

