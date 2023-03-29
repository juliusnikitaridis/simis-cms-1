package com.simisinc.platform.infrastructure.persistence.carfix;

import com.simisinc.platform.domain.model.carfix.DeviceToken;
import com.simisinc.platform.domain.model.carfix.Quote;
import com.simisinc.platform.domain.model.carfix.QuoteItem;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

public class DeviceTokenRepository {
    private static Log LOG = LogFactory.getLog(DeviceToken.class);
    private static String TABLE_NAME = "carfix.device_token";
    private static String[] PRIMARY_KEY = new String[]{"unique_id"};

    public static DeviceToken add(DeviceToken deviceToken) throws Exception {
            SqlUtils insertValue = new SqlUtils();

            insertValue
                    .add("unique_id", deviceToken.getUniqueId())
                    .add("token", deviceToken.getToken());



        try (Connection connection = DB.getConnection();
             AutoStartTransaction a = new AutoStartTransaction(connection);
             AutoRollback transaction = new AutoRollback(connection)) {
            // In a transaction (use the existing connection)
            DB.insertIntoWithStringPk(connection, TABLE_NAME, insertValue, PRIMARY_KEY); //TODO is there a way to insert batches - lists ???
            transaction.commit();
            return deviceToken;

        } catch (Exception se) {
            LOG.error("SQLException: " + se.getMessage());
            throw new Exception(se.getMessage());
        }
    }

    public static DataResult query(String UniqueId,DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        where.addIfExists("unique_id =?",UniqueId);


        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, DeviceTokenRepository::buildRecord);
    }

    private static DeviceToken buildRecord(ResultSet rs) {

        DeviceToken deviceToken= new DeviceToken();
        try {
            deviceToken.setToken(rs.getString("token"));
            //deviceToken.setUniqueId(rs.getString("unique_id"));
            //get the line items for this record

            return deviceToken;
        } catch (Exception throwables) {
            LOG.error("exception when building device token item record");
            throwables.printStackTrace();
            return null;
        }
    }
}
