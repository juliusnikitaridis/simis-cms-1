package com.simisinc.platform.infrastructure.persistence.carfix;

import com.simisinc.platform.domain.model.carfix.Quote;
import com.simisinc.platform.domain.model.carfix.QuoteItem;
import com.simisinc.platform.domain.model.carfix.ServiceRequest;
import com.simisinc.platform.domain.model.carfix.ServiceRequestItem;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Julius Nikitaridis
 * 22/12/2022
 * repo class for Quote items
 */

public class QuoteRepository {

    private static Log LOG = LogFactory.getLog(QuoteRepository.class);
    private static String TABLE_NAME = "carfix.Quote";
    private static String TABLE_NAME_ITEMS = "carfix.quotation_item";
    private static String[] PRIMARY_KEY = new String[]{"id"};
    private static String[] PRIMARY_KEY_ITEMS = new String[]{"id"};


    public static void addItems(Quote quote) throws Exception {
         Connection connection = DB.getConnection();
        for (QuoteItem quoteItem : quote.getQuotationItems()) {
            SqlUtils insertValue = new SqlUtils();
            insertValue
            .add("id", UUID.randomUUID().toString())
                    .add("quote_id",quote.getId())
                    .add("part_number",quoteItem.getPartNumber())
                    .add("part_description",quoteItem.getPartDescription())
                    .add("item_total_price",quoteItem.getItemTotalPrice());

            AutoStartTransaction a = new AutoStartTransaction(connection);
            AutoRollback transaction = new AutoRollback(connection);
            DB.insertIntoWithStringPk(connection, TABLE_NAME_ITEMS, insertValue, PRIMARY_KEY_ITEMS); //TODO is there a way to insert batches - lists ???
            transaction.commit();
        }
    }


    public static Quote add(Quote record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("request_for_service_id", record.getRequestForServiceId())
                .add("service_provider_id", record.getServiceProviderId())
                .add("service_provider_name",record.getServiceProviderName())
                .add("date",record.getDate())
                .add("total",record.getQuotationTotal());
        try {
            try (Connection connection = DB.getConnection();
                 AutoStartTransaction a = new AutoStartTransaction(connection);
                 AutoRollback transaction = new AutoRollback(connection)) {
                // In a transaction (use the existing connection)
                DB.insertIntoWithStringPk(connection, TABLE_NAME, insertValues, PRIMARY_KEY);
                transaction.commit();
                return record;
            }
        } catch (Exception se) {
            LOG.error("SQLException: " + se.getMessage());
            throw new Exception(se.getMessage());
        }
    }




    public static Quote findById(long id) {
        if (id == -1) {
            return null;
        }
        return (Quote) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("id = ?", id),
                QuoteRepository::buildRecord);
    }


    public static DataResult query(QuoteSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();

        if (specification != null) {
            where
                    .addIfExists("request_for_service_id = ?", specification.getRequestForServiceId())
                    .addIfExists("service_provider_id = ?", specification.getServiceProviderId())
                    .addIfExists("id = ?", specification.getId());

        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, QuoteRepository::buildRecord);
    }


    private static Quote buildRecord(ResultSet rs) {

        Quote request = new Quote();
        try {
            request.setId(rs.getString("id"));
            request.setRequestForServiceId(rs.getString("request_for_service_id"));
            request.setServiceProviderId(rs.getString("service_provider_id"));
            request.setServiceProviderName(rs.getString("service_provider_name"));
            request.setDate(rs.getString("date"));
            request.setQuotationTotal(rs.getString("total"));
            return request;
        } catch (Exception e) {
            LOG.error("exception when building record for Quote" + e.getMessage());
            return null;
        }
    }
}

