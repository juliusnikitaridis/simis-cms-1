package com.simisinc.platform.infrastructure.persistence.cannacomply;


import com.simisinc.platform.domain.model.cannacomply.Hygiene;
import com.simisinc.platform.domain.model.cannacomply.Strain;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class HygieneRepository {

    private static Log LOG = LogFactory.getLog(HygieneRepository.class);
    private static String TABLE_NAME = "cannacomply.hygiene";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static Hygiene add(Hygiene record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("user_id", record.getUserId())
                .add("location_id", record.getLocationId())
                .add("type",record.getType())
                .add("form",record.getForm());

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


    public static void update(Hygiene record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("id", record.getId())
                .addIfExists("user_id", record.getUserId())
                .addIfExists("location_id", record.getLocationId())
                .addIfExists("type",record.getType())
                .addIfExists("form",record.getForm());

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
                HygieneRepository::buildRecord);
    }



    public static DataResult query(HygieneSpecification specification) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();

        if (specification != null) {
            where
                    .addIfExists("location_id = ?", specification.getLocationId())
                    .addIfExists("user_id = ?",specification.getUserId());

        }

        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, null, HygieneRepository::buildRecord);
    }


    public static void delete(String strainId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", strainId));
            LOG.debug("strain has been deleted:>>" + strainId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static Hygiene buildRecord(ResultSet rs) {

        Hygiene hygiene = new Hygiene();
        try {
            hygiene.setId(rs.getString("id"));
            hygiene.setDate(rs.getString("date"));
            hygiene.setForm(rs.getString("form"));
            hygiene.setLocationId(rs.getString("location_id"));
            hygiene.setType(rs.getString("type"));
            return hygiene;
        } catch (Exception e) {
            LOG.error("exception when building record for Strain" + e.getMessage());
            return null;
        }
    }
}

