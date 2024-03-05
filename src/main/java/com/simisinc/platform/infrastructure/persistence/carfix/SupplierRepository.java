package com.simisinc.platform.infrastructure.persistence.carfix;

import com.simisinc.platform.domain.model.carfix.Supplier;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.domain.model.carfix.Yield;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class SupplierRepository {

    private static Log LOG = LogFactory.getLog(SupplierRepository.class);
    private static String TABLE_NAME = "cannacomply.supplier";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static Supplier add(Supplier record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("farm_id", record.getFarmId())
                .add("total_quantity",record.getTotalQuantity())
                .add("contact_no",record.getContactNo())
                .add("contact_title",record.getContactTitle())
                .add("location_type",record.getLocationType())
                .add("location_id",record.getLocationId())
                .add("email",record.getEmail())
                .add("site",record.getSite())
                .add("supplier_name", record.getSupplierName())
                .add("product_name", record.getProductName())
                .add("quantity",record.getQuantity())
                .add("expiry_date", record.getExpiryDate())
                .add("receipt", record.getReceipt())
                .add("type", record.getType())
                .add("lot_number",record.getLotNumber())
                .add("container",record.getContainer())
                .add("mass",record.getMass());

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


    public static void update(Supplier record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("farm_id", record.getFarmId())
                .addIfExists("supplier_name", record.getSupplierName())
                .addIfExists("product_name", record.getProductName())
                .addIfExists("quantity",record.getQuantity())
                .addIfExists("contact_no",record.getContactNo())
                .addIfExists("contact_title",record.getContactTitle())
                .addIfExists("email",record.getEmail())
                .addIfExists("site",record.getSite())
                .addIfExists("total_quantity",record.getTotalQuantity())
                .addIfExists("expiry_date", record.getExpiryDate())
                .addIfExists("receipt", record.getReceipt())
                .addIfExists("type", record.getType())
                .addIfExists("location_type",record.getLocationType())
                .addIfExists("location_id",record.getLocationId())
                .addIfExists("lot_number",record.getLotNumber())
                .addIfExists("container",record.getContainer())
                .addIfExists("mass",record.getMass());


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
                SupplierRepository::buildRecord);
    }


    public static DataResult query(SupplierSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("id = ?", specification.getId())
                    .addIfExists("farm_id = ?",specification.getFarmId());


        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, SupplierRepository::buildRecord);
    }


    public static void delete(String id) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", id));
            LOG.debug("Supplier has  has been deleted:>>" + id);
        } catch (Exception e) {
            throw e;
        }
    }


    private static Supplier buildRecord(ResultSet rs) {

        Supplier record = new Supplier();
        try {
            record.setId(rs.getString("id"));
            record.setFarmId(rs.getString("farm_id"));
            record.setSupplierName(rs.getString("supplier_name"));
            record.setProductName(rs.getString("product_name"));
            record.setQuantity(rs.getString("quantity"));
            record.setExpiryDate(rs.getString("expiry_date"));
            record.setReceipt(rs.getString("receipt"));
            record.setContactNo(rs.getString("contact_no"));
            record.setContactTitle(rs.getString("contact_title"));
            record.setEmail(rs.getString("email"));
            record.setSite(rs.getString("site"));
            record.setTotalQuantity(rs.getString("total_quantity"));
            record.setType(rs.getString("type"));
            record.setLocationId(rs.getString("location_id"));
            record.setLocationType("location_type");
            record.setLotNumber(rs.getString("lot_number"));
            record.setContainer(rs.getString("container"));
            record.setMass(rs.getString("mass"));
            return record;
        } catch (Exception e) {
            LOG.error("exception when building record for Supplier" + e.getMessage());
            return null;
        }
    }
}

