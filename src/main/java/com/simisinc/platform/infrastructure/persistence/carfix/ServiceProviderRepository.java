package com.simisinc.platform.infrastructure.persistence.carfix;

import com.simisinc.platform.domain.model.carfix.ServiceProvider;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceProviderRepository {

    private static String TABLE_NAME = "carfix.service_provider";
    private static String[] PRIMARY_KEY = new String[]{"id"};
    private static Log LOG = LogFactory.getLog(ServiceProviderRepository.class);

    public static ServiceProvider add(ServiceProvider serviceProvider) throws Exception {
            SqlUtils insertValues = new SqlUtils()
                    .add("id", serviceProvider.getId())
                    .add("supported_brands", serviceProvider.getSupportedBrands())
                    .add("name", serviceProvider.getName())
                    .add("services", serviceProvider.getServices())
                    .add("certifications", serviceProvider.getCertifications());
            try {
                try (Connection connection = DB.getConnection();
                     AutoStartTransaction a = new AutoStartTransaction(connection);
                     AutoRollback transaction = new AutoRollback(connection)) {
                    // In a transaction (use the existing connection)
                    DB.insertIntoWithStringPk(connection, TABLE_NAME, insertValues, PRIMARY_KEY);
                    transaction.commit();
                    return serviceProvider;
                }
            } catch (Exception se) {
                LOG.error("SQLException: " + se.getMessage());
                throw new Exception(se.getMessage());
            }
    }

    //get all the service providers
    public static DataResult query(ServiceRequestSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();

        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, ServiceProviderRepository::buildRecord);
    }


    //returns address, name , supported brands , services, about us ,
    public static ServiceProvider buildRecord(ResultSet rs) {
        try {
            ServiceProvider serviceProvider = new ServiceProvider();
            serviceProvider.setName(rs.getString("name"));
            serviceProvider.setSupportedBrands(rs.getString("supported_brands"));
            serviceProvider.setServices(rs.getString("services"));
            serviceProvider.setAboutUs(rs.getString("about_us"));
            serviceProvider.setAddress(rs.getString("address"));
            return serviceProvider;
        } catch (SQLException throwables) {
            LOG.error("error when building record for service provider "+throwables.getMessage());
            throwables.printStackTrace();
            return null;
        }
    }
}
