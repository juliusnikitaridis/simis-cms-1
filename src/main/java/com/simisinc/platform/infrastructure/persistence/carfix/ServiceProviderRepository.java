package com.simisinc.platform.infrastructure.persistence.carfix;

import com.simisinc.platform.domain.model.carfix.ServiceProvider;
import com.simisinc.platform.infrastructure.database.AutoRollback;
import com.simisinc.platform.infrastructure.database.AutoStartTransaction;
import com.simisinc.platform.infrastructure.database.DB;
import com.simisinc.platform.infrastructure.database.SqlUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.Connection;

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
}
