package com.simisinc.platform.infrastructure.persistence.cannacomply;

import com.simisinc.platform.domain.model.cannacomply.Customer;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;


public class CustomerRepository {

    private static Log LOG = LogFactory.getLog(CustomerRepository.class);
    private static String TABLE_NAME = "cannacomply.customer";
    private static String[] PRIMARY_KEY = new String[]{"id"};

    public static Customer add(Customer record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("customer_name",record.getCustomerName())
                .add("country",record.getCountry())
                .add("city",record.getCity())
                .add("contact_no",record.getContactNo())
                .add("email",record.getEmail())
                .add("farm_id",record.getFarmId())
                .add("address",record.getAddress());

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


    public static void update(Customer record) throws Exception {
        SqlUtils updateValues = new SqlUtils()
                .add("customer_name",record.getCustomerName())
                .add("country",record.getCountry())
                .add("city",record.getCity())
                .add("contact_no",record.getContactNo())
                .add("email",record.getEmail())
                .add("farm_id",record.getFarmId())
                .add("address",record.getAddress());
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


    public static Customer findById(String id) {

        return (Customer) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("id = ?", id),
                CustomerRepository::buildRecord);
    }


    public static DataResult query(CustomerSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("farm_id = ?",specification.getFarmId())
                    .addIfExists("id = ?",specification.getId());
        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, CustomerRepository::buildRecord);
    }


    public static void delete(String customerId) throws Exception {
        try (Connection connection = DB.getConnection()) {
            DB.deleteFrom(connection, TABLE_NAME, new SqlUtils().add("id = ?", customerId));
            LOG.debug("Customere has been deleted:>>" + customerId);
        } catch (Exception e) {
            throw e;
        }
    }


    private static Customer buildRecord(ResultSet rs) {

         Customer customer = new Customer();
        try {
          customer.setId(rs.getString("id"));
          customer.setCustomerName(rs.getString("customer_name"));
          customer.setCountry(rs.getString("country"));
          customer.setCity(rs.getString("city"));
          customer.setContactNo(rs.getString("contact_no"));
          customer.setEmail(rs.getString("email"));
          customer.setFarmId(rs.getString("farm_id"));
          customer.setAddress(rs.getString("address"));
          return customer;
        } catch (Exception e) {
            LOG.error("exception when building record for Customer" + e.getMessage());
            return null;
        }
    }
}

