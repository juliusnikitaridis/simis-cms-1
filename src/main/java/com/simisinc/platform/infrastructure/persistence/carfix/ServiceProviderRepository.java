package com.simisinc.platform.infrastructure.persistence.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.carfix.Brand;
import com.simisinc.platform.domain.model.carfix.Category;
import com.simisinc.platform.domain.model.carfix.ServiceProvider;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceProviderRepository {

    private static String TABLE_NAME = "carfix.service_provider";
    private static String[] PRIMARY_KEY = new String[]{"id"};
    private static Log LOG = LogFactory.getLog(ServiceProviderRepository.class);

    public static ServiceProvider add(ServiceProvider serviceProvider) throws Exception {
            SqlUtils insertValues = new SqlUtils()
                    .add("id", serviceProvider.getServiceProviderId()) //PK on SP table
                    .add("supported_brands", serviceProvider.getSupportedBrandsAsJSONString())
                    .add("supported_categories",serviceProvider.getSupportedCategoriesAsString())
                    .add("name", serviceProvider.getName())
                    .add("services", serviceProvider.getServices())
                    .add ("address",serviceProvider.getAddress())
                    .add("logo_data",serviceProvider.getLogoData())
                    .add("about_us",serviceProvider.getAboutUs())
                    .add("certifications", serviceProvider.getCertifications())
                    .add("accreditations",serviceProvider.getAccreditations())
                    .add("user_id",serviceProvider.getUniqueId())
                    .add("rating",0)
                    .add("count",0)
                    .add("operating_year",serviceProvider.getOperatingYear())
                    .add("operating_hours",serviceProvider.getOperatingHours())
                    .add("drop_off",serviceProvider.getDropOff())
                    .add("account_firstname",serviceProvider.getAccountFirstName())
                    .add("account_lastname",serviceProvider.getAccountLastName())
                    .add("account_no",serviceProvider.getAccountNo())
                    .add("account_branch",serviceProvider.getAccountBranch())
                    .add("account_bank",serviceProvider.getAccountBank())
                    .add("rmi",serviceProvider.getRMI());

                try (Connection connection = DB.getConnection();
                     AutoStartTransaction a = new AutoStartTransaction(connection);
                     AutoRollback transaction = new AutoRollback(connection)) {
                    // In a transaction (use the existing connection)
                    DB.insertIntoWithStringPk(connection, TABLE_NAME, insertValues, PRIMARY_KEY);
                    transaction.commit();
                    return serviceProvider;

            } catch (Exception se) {
                LOG.error("SQLException: " + se.getMessage());
                throw new Exception(se.getMessage());
            }
    }

    //get all the service providers
    public static DataResult query(ServiceProviderSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();

        where.addIfExists("id = ?",specification.getServiceProviderId())
        .addIfExists("user_id = ?",specification.getServiceProviderUniqueId());

        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, ServiceProviderRepository::buildRecord);
    }


    //returns address, name , supported brands , services, about us ,
    public static ServiceProvider buildRecord(ResultSet rs) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ServiceProvider serviceProvider = new ServiceProvider();
            serviceProvider.setName(rs.getString("name"));
            serviceProvider.setSupportedBrands(mapper.readValue(rs.getString("supported_brands"), Brand[].class));
            serviceProvider.setSupportedCategories(mapper.readValue(rs.getString("supported_categories"), Category[].class));
            serviceProvider.setServices(rs.getString("services"));
            serviceProvider.setAboutUs(rs.getString("about_us"));
            serviceProvider.setAddress(rs.getString("address"));
            serviceProvider.setServiceProviderId(rs.getString("id"));
            serviceProvider.setLogoData(rs.getString("logo_data"));
            serviceProvider.setAccreditations(rs.getString("accreditations"));
            serviceProvider.setCount(rs.getString("count"));
            serviceProvider.setRating(rs.getString("rating"));
            serviceProvider.setDropOff(rs.getString("drop_off"));
            serviceProvider.setRMI(rs.getString("rmi"));
            serviceProvider.setOperatingYear(rs.getString("operating_year"));
            serviceProvider.setOperatingHours(rs.getString("operating_hours"));
            serviceProvider.setAccountBank(rs.getString("account_bank"));
            serviceProvider.setAccountNo(rs.getString("account_no"));
            serviceProvider.setAccountBranch(rs.getString("account_branch"));
            serviceProvider.setAccountFirstName(rs.getString("account_firstname"));
            serviceProvider.setAccountLastName(rs.getString("account_lastname"));
            return serviceProvider;
        } catch (Throwable throwables) {
            LOG.error("error when building record for service provider "+throwables.getMessage());
            throwables.printStackTrace();
            return null;
        }
    }

    public static void rate(int rating, String serviceProviderId) throws Exception {
        String sql = "update carfix.service_provider set rating = rating+?, count=count+1 where user_id = ?";
        try(Connection conn = DB.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,rating);
            pstmt.setString(2,serviceProviderId);
            pstmt.execute();
        } catch(Exception e) {
            throw e;
        }
    }


    public static void update(ServiceProvider record) throws Exception {
        if(record.getServiceProviderId() == null) {
            throw new Exception("serviceProviderId can not be null");
        }
        SqlUtils updateValues = new SqlUtils()
                .addIfExists("id", record.getServiceProviderId())
                .addIfExists("supported_brands", record.getSupportedBrandsAsJSONString())
                .addIfExists("name", record.getName())
                .addIfExists("services", record.getServices())
                .addIfExists("certifications", record.getCertifications())
                .addIfExists("address", record.getAddress())
                .addIfExists("about_us", record.getAboutUs())
                .addIfExists("operating_year",record.getOperatingYear())
                .addIfExists("logo_data", record.getLogoData())
                .addIfExists("supported_categories", record.getSupportedCategoriesAsString())
                .addIfExists("accreditations", record.getAccreditations())
                .addIfExists("operating_hours",record.getOperatingHours())
                .addIfExists("account_firstname",record.getAccountFirstName())
                .addIfExists("account_lastname",record.getAccountLastName())
                .addIfExists("account_no",record.getAccountNo())
                .addIfExists("account_branch",record.getAccountBranch())
                .addIfExists("account_bank",record.getAccountBank());

            try (Connection connection = DB.getConnection();
                 AutoStartTransaction a = new AutoStartTransaction(connection);
                 AutoRollback transaction = new AutoRollback(connection)) {
                // In a transaction (use the existing connection)
                DB.update(connection, TABLE_NAME, updateValues, new SqlUtils().add("id = ?", record.getServiceProviderId()));
                transaction.commit();

        } catch (Exception se) {
            LOG.error("SQLException: " + se.getMessage());
            throw new Exception(se.getMessage());
        }
    }

}
