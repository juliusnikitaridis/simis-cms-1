package com.simisinc.platform.infrastructure.persistence.cannacomply;


import com.simisinc.platform.domain.model.cannacomply.Packaging;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class PackagingTotalsRepository {

    private static Log LOG = LogFactory.getLog(PackagingTotalsRepository.class);
    private static String TABLE_NAME = "cannacomply.packaging_totals";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static Packaging add(Packaging record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("package_tag", record.getPackageTag())
                .add("item", record.getItem())
                .add("quantity", record.getQuantity())
                .add("farm_id",record.getFarmId())
                .add("location",record.getLocation())
                .add("user_id",record.getUserId())
                .add("last_updated",record.getLastUpdated())
                .add("status",record.getStatus())
                .add("moisture_loss",record.getMoistureLoss())
                .add("date",record.getDate())
                .add("bud_size",record.getBudSize())
                .add("container_type",record.getContainerType())
                .add("stage",record.getStage())
                .add("harvest_id",record.getHarvestId());

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


    public static void update(Packaging record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("package_tag", record.getPackageTag())
                .addIfExists("item", record.getItem())
                .addIfExists("quantity", record.getQuantity())
                .addIfExists("farm_id",record.getFarmId())
                .addIfExists("location",record.getLocation())
                .addIfExists("last_updated",record.getLastUpdated())
                .addIfExists("user_id",record.getUserId())
                .addIfExists("moisture_loss",record.getMoistureLoss())
                .addIfExists("status",record.getStatus())
                .addIfExists("date",record.getDate())
                .addIfExists("bud_size",record.getBudSize())
                .addIfExists("container_type",record.getContainerType())
                .addIfExists("stage",record.getStage())
                .addIfExists("harvest_id",record.getHarvestId());

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




    public static DataResult query(PackagingSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("id = ?", specification.getId())
                    .addIfExists("package_tag = ?",specification.getPackageTag())
                    .addIfExists("farm_id = ?" ,specification.getFarmId());
        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, PackagingTotalsRepository::buildRecord);
    }


    public static void delete(String farmId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", farmId));
            LOG.debug("package has been deleted:>>" + farmId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static Packaging buildRecord(ResultSet rs) {

        Packaging pack = new Packaging();
        try {
            pack.setId(rs.getString("id"));
            pack.setPackageTag(rs.getString("package_tag"));
            pack.setItem(rs.getString("item"));
            pack.setQuantity(rs.getString("quantity"));
            pack.setUserId(rs.getString("user_id"));
            pack.setFarmId(rs.getString("farm_id"));
            pack.setLastUpdated(rs.getString("last_updated"));
            pack.setLocation(rs.getString("location"));
            pack.setStatus(rs.getString("status"));
            pack.setMoistureLoss(rs.getString("moisture_loss"));
            pack.setContainerType(rs.getString(("container_type")));
            pack.setStage(rs.getString("stage"));
            pack.setBudSize(rs.getString("bud_size"));
            pack.setDate(rs.getString("date"));
            pack.setHarvestId(rs.getString("harvest_id"));

            return pack;
        } catch (Exception e) {
            LOG.error("exception when building record for Packaging" + e.getMessage());
            return null;
        }
    }
}

