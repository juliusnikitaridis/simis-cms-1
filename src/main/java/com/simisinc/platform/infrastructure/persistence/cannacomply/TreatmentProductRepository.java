package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Issue;
import com.simisinc.platform.domain.model.carfix.TreatmentProduct;
import com.simisinc.platform.infrastructure.database.*;
import com.simisinc.platform.infrastructure.persistence.carfix.TreatmentProductSpecification;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class TreatmentProductRepository {

    private static Log LOG = LogFactory.getLog(TreatmentProductRepository.class);
    private static String TABLE_NAME = "cannacomply.treatment_product";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static TreatmentProduct add(TreatmentProduct record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("product_name", record.getProductName())
                .add("units",record.getUnits())
                .add("container", record.getContainer())
                .add("mass", record.getMass())
                .add("quantity",record.getQuantity())
                .add("farm_id",record.getFarmId())
                .add("product_id",record.getProductId())
                .add("active_ingredients",record.getActiveIngredients())
                .add("expiry_date",record.getExpiryDate())
                .add("instructions",record.getInstructions())
                .add("location_type",record.getLocationType())
                .add("location_id",record.getLocationId())
                .add("purpose",record.getPurpose());

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


    public static void update(TreatmentProduct record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("product_name", record.getProductName())
                .addIfExists("container", record.getContainer())
                .addIfExists("mass", record.getMass())
                .addIfExists("quantity",record.getQuantity())
                .addIfExists("farm_id",record.getFarmId())
                .addIfExists("product_id",record.getProductId())
                .addIfExists("active_ingredients",record.getActiveIngredients())
                .addIfExists("expiry_date",record.getExpiryDate())
                .addIfExists("units",record.getUnits())
                .addIfExists("location_type",record.getLocationType())
                .addIfExists("location_id",record.getLocationId())
                .addIfExists("instructions",record.getInstructions())
                .addIfExists("purpose",record.getPurpose());

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


    public static Issue findById(String id) {

        return (Issue) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("id = ?", id),
                TreatmentProductRepository::buildRecord);
    }


    public static DataResult query(TreatmentProductSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("product_id = ?", specification.getProductId())
                    .addIfExists("farm_id = ?",specification.getFarmId());

        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, TreatmentProductRepository::buildRecord);
    }


    public static void delete(String issueId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", issueId));
            LOG.debug("issue has been deleted:>>" + issueId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static TreatmentProduct buildRecord(ResultSet rs) {

        TreatmentProduct product = new TreatmentProduct();
        try {
            product.setProductId(rs.getString("id"));
            product.setProductName(rs.getString("product_name"));
            product.setContainer(rs.getString("container"));
            product.setMass(rs.getString("mass"));
            product.setUnits(rs.getString("units"));
            product.setQuantity(rs.getString("quantity"));
            product.setFarmId(rs.getString("farm_id"));
            product.setLocationId("location_id");
            product.setLocationType("location_type");
            product.setImageData(rs.getString("image_data"));
            product.setProductId(rs.getString("product_id"));
            product.setActiveIngredients(rs.getString("active_ingredients"));
            product.setExpiryDate(rs.getString("expiry_date"));
            product.setInstructions(rs.getString("instructions"));
            product.setPurpose(rs.getString("purpose"));

            return product;
        } catch (Exception e) {
            LOG.error("exception when building record for TreatmentProduct" + e.getMessage());
            return null;
        }
    }
}

