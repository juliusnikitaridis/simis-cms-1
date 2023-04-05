package com.simisinc.platform.infrastructure.persistence.carfix;

import com.simisinc.platform.domain.model.carfix.Quote;
import com.simisinc.platform.domain.model.carfix.QuoteItem;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

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


    private static void addItems(Quote quote,Connection conn) throws Exception {
        for (QuoteItem quoteItem : quote.getQuotationItems()) {
            SqlUtils insertValue = new SqlUtils();
            double labourTotal = 0;
            double partsTotal = 0; //unit price for part
            double quantity =1;
            if(quoteItem.getLabourTotal() != null) {
                labourTotal = Double.valueOf(quoteItem.getLabourTotal());
            }
            if(quoteItem.getPartsTotal() != null) {
                partsTotal = Double.valueOf(quoteItem.getPartsTotal());
            }
            if(quoteItem.getQuantity() != null) {
                quantity = Double.valueOf(quoteItem.getQuantity());
            }
            double total = labourTotal+(quantity*partsTotal);

            insertValue
                    .add("id", UUID.randomUUID().toString())
                    .add("quote_id", quote.getId())
                    .addIfExists("part_number", quoteItem.getPartNumber())
                    .addIfExists("part_description", quoteItem.getPartDescription())
                    .addIfExists("quantity",quoteItem.getQuantity())
                    .add("item_total_price", total)
                    .addIfExists("parts_total",quoteItem.getPartsTotal())
                    .addIfExists("labour_total",quoteItem.getLabourTotal())
                    .addIfExists("item_status",quoteItem.getStatus())
                    .addIfExists("replacement_reason",quoteItem.getReplacementReason());

            DB.insertIntoWithStringPk(conn, TABLE_NAME_ITEMS, insertValue, PRIMARY_KEY_ITEMS); //TODO is there a way to insert batches - lists ???
        }
    }


    public static Quote add(Quote record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("request_for_service_id", record.getRequestForServiceId())
                .add("service_provider_id", record.getServiceProviderId())
                .add("service_provider_name", record.getServiceProviderName())
                .add("created_date", String.valueOf(System.currentTimeMillis()))
                .add("booking_date",record.getBookingDate())
                .add("status", "CREATED")
                .add("total", record.getQuotationTotal());

        try (Connection connection = DB.getConnection();
             AutoStartTransaction a = new AutoStartTransaction(connection);
             AutoRollback transaction = new AutoRollback(connection)) {
            // In a transaction (use the existing connection)
            DB.insertIntoWithStringPk(connection, TABLE_NAME, insertValues, PRIMARY_KEY);
            addItems(record, connection);
            //now insert the items
            transaction.commit();
            return record;
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

        Quote quote = new Quote();
        try {
            quote.setId(rs.getString("id"));
            quote.setRequestForServiceId(rs.getString("request_for_service_id"));
            quote.setServiceProviderId(rs.getString("service_provider_id"));
            quote.setServiceProviderName(rs.getString("service_provider_name"));
            quote.setQuotationTotal(rs.getString("total"));
            quote.setBookingDate(rs.getString("booking_date"));
            quote.setCreatedDate(rs.getString("created_date"));

            //get the line items for this record
            SqlUtils select = new SqlUtils();
            SqlUtils where = new SqlUtils();

            where.add("quote_id = ?", rs.getString("id"));
            ArrayList<QuoteItem> quoteItems = (ArrayList<QuoteItem>) (DB.selectAllFrom(TABLE_NAME_ITEMS, select, where, null, null, QuoteRepository::buildRecordQuoteItem)).getRecords();
            quote.setQuotationItems(quoteItems);
            return quote;
        } catch (Exception throwables) {
            LOG.error("exception when building quote item record");
            throwables.printStackTrace();
            return null;
        }
    }


    private static QuoteItem buildRecordQuoteItem(ResultSet rs){
        QuoteItem item = new QuoteItem();
        try {
            item.setId(rs.getString("id"));
            item.setItemTotalPrice(rs.getString("item_total_price"));
            item.setPartDescription(rs.getString("part_description"));
            item.setQuoteId(rs.getString("quote_id"));
            item.setItemStatus(rs.getString("item_status"));
            item.setItemType(rs.getString("item_type"));
            item.setLabourTotal(rs.getString("labour_total"));
            item.setPartsTotal(rs.getString("parts_total"));
            item.setReplacementReason(rs.getString("replacement_reason"));
            return item;
        } catch (Exception throwables) {
            LOG.error("Error when building quotetation item ", throwables);
            throwables.printStackTrace();
            return null;
        }
    }

    //update quote status once it has been accepted by the membere
    public static void updateQuoteStatus(String quoteId, String status) throws Exception {

        String sql = "update carfix.quote set status = ? where id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, quoteId);
            pstmt.execute();
        } catch (Exception e) {
            throw e;
        }
    }

    /*
     Iterate through all the quote items that have an accepted status and
     add up all the totals.
     */
    public static void updateQuoteTotal(String quoteId) throws Exception {

        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();

        where.add("quote_id = ?", quoteId);
        where.add("item_status like ?","%ACCEPTED%"); //TODO check this
        ArrayList<QuoteItem> quoteItems = (ArrayList<QuoteItem>) (DB.selectAllFrom(TABLE_NAME_ITEMS, select, where, null, null, QuoteRepository::buildRecordQuoteItem)).getRecords();

        //add up all the items that have a valid accepted status
        System.out.print(quoteItems);
        double newTotal = 0;
        for(QuoteItem item : quoteItems) {

            newTotal+=Double.parseDouble(item.getItemTotalPrice());
        }

        String sql = "update carfix.quote set total = ? where id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, Double.toString(newTotal));
            pstmt.setString(2, quoteId);
            pstmt.execute();
        } catch (Exception e) {
            throw e;
        }
    }


    //updaete service request status once a quote has been accepted
    public static void updateServiceRequestStatus(String serviceRequestId, String status) throws Exception {
        String sql = "update carfix.service_request set status = ?,confirmed_date = ? where id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2,String.valueOf(System.currentTimeMillis()));
            pstmt.setString(3, serviceRequestId);
            pstmt.execute();
        } catch (Exception e) {
            throw e;
        }
    }


    //insert the quoteId of the accepted quote into the service request table
    public static void updateServiceRequestAcceptedQuoteId(String acceptedQuoteId, String serviceRequestId) throws Exception {
        String sql = "update carfix.service_request set accepted_quote_id = ? where id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, acceptedQuoteId);
            pstmt.setString(2, serviceRequestId);
            pstmt.execute();
        } catch (Exception e) {
            throw e;
        }
    }

    public static void updateQuoteItemStatus(String quoteId,String itemId,String status) throws Exception {
        String sql = "update carfix.quotation_item set item_status = ? where id = ? and quote_id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, itemId);
            pstmt.setString(3,quoteId);
            pstmt.execute();
        } catch (Exception e) {
            throw e;
        }
    }


    //need to update which service providers quote has been accepted for this service request
    public static void updateAcceptedServiceProviderId(String acceptedServiceProviderId, String serviceRequestId) throws Exception {
        String sql = "update carfix.service_request set confirmed_service_provider_id = ? where id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, acceptedServiceProviderId);
            pstmt.setString(2, serviceRequestId);
            pstmt.execute();
        } catch (Exception e) {
            throw e;
        }
    }


    // add additional work items to the quote
    public static void addItemsToQuote(QuoteItem[] quoteList,String quoteId) throws Exception {

        try (Connection connection = DB.getConnection();
             AutoStartTransaction a = new AutoStartTransaction(connection);
             AutoRollback transaction = new AutoRollback(connection)) {
            // In a transaction (use the existing connection)
            for (QuoteItem quoteItem : Arrays.asList(quoteList)) {
                double labourTotal = 0;
                double partsTotal = 0; //unit price for part
                double quantity =1;
                if(quoteItem.getLabourTotal() != null) {
                    labourTotal = Double.valueOf(quoteItem.getLabourTotal());
                }
                if(quoteItem.getPartsTotal() != null) {
                    partsTotal = Double.valueOf(quoteItem.getPartsTotal());
                }
                if(quoteItem.getQuantity() != null) {
                    quantity = Double.valueOf(quoteItem.getQuantity());
                }
                double total = labourTotal+(quantity*partsTotal);
                SqlUtils insertValue = new SqlUtils();
                insertValue
                        .add("id", UUID.randomUUID().toString())
                        .add("quote_id", quoteId)
                        .addIfExists("part_number", quoteItem.getPartNumber())
                        .addIfExists("part_description", quoteItem.getPartDescription())
                        .add("item_type",quoteItem.getItemType())
                        .addIfExists("item_status",quoteItem.getItemStatus())
                        .addIfExists("quantity",quoteItem.getQuantity())
                        .add("item_total_price", total)
                        .addIfExists("labour_total",quoteItem.getLabourTotal())
                        .addIfExists("parts_total",quoteItem.getPartsTotal())
                        .addIfExists("replacement_reason",quoteItem.getReplacementReason())
                        .addIfExists("parts_picture",quoteItem.getPartsPicture());


                DB.insertIntoWithStringPk(connection, TABLE_NAME_ITEMS, insertValue, PRIMARY_KEY_ITEMS);
            }
            //now insert the items
            transaction.commit();
        } catch (Exception se) {
            LOG.error("Exception when adding item to quote!!: " + se.getMessage());
            throw new Exception(se.getMessage());
        }
    }
}

