package com.simisinc.platform.infrastructure.persistence.cannacomply;


import com.simisinc.platform.domain.model.cannacomply.Packaging;
import com.simisinc.platform.domain.model.cannacomply.Strain;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class StrainRepository {

    private static Log LOG = LogFactory.getLog(StrainRepository.class);
    private static String TABLE_NAME = "cannacomply.strain_data";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static Strain add(Strain record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("strain_name", record.getStrainName())
                .add("breeder", record.getBreeder())
                .add("type",record.getType())
                .add("grow_location",record.getGrowLocation())
                .add("flowering", record.getFlowering())
                .add("potency",record.getPotency())
                .add("yield",record.getYield());

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


    public static void update(Strain record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .add("strain_name", record.getStrainName())
                .add("breeder", record.getBreeder())
                .add("type",record.getType())
                .add("grow_location",record.getGrowLocation())
                .add("flowering", record.getFlowering())
                .add("potency",record.getPotency())
                .add("yield",record.getYield());

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
                StrainRepository::buildRecord);
    }


    public static DataResult query() {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();

        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, null, StrainRepository::buildRecord);
    }


    public static void delete(String strainId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", strainId));
            LOG.debug("strain has been deleted:>>" + strainId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static Strain buildRecord(ResultSet rs) {

        Strain pack = new Strain();
        try {
            pack.setId(rs.getString("id"));
            pack.setStrainName(rs.getString("strain_name"));
            pack.setBreeder(rs.getString("breeder"));
            pack.setFlowering(rs.getString("flowering"));
            pack.setType(rs.getString("type"));
            pack.setYield(rs.getString("yield"));
            pack.setGrowLocation(rs.getString("grow_location"));
            pack.setPotency(rs.getString("potency"));

            return pack;
        } catch (Exception e) {
            LOG.error("exception when building record for Strain" + e.getMessage());
            return null;
        }
    }
}

