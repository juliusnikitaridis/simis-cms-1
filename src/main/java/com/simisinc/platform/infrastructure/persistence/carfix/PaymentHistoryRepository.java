package com.simisinc.platform.infrastructure.persistence.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.Entity;
import com.simisinc.platform.domain.model.carfix.PaymentRequest;
import com.simisinc.platform.domain.model.carfix.ProcessPaymentServiceRequest;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class PaymentHistoryRepository {

    private static Log LOG = LogFactory.getLog(PaymentHistoryRepository.class);
    private static String TABLE_NAME = "carfix.payment_history";
    private static String[] PRIMARY_KEY = new String[]{"id"};


    public static void add(ProcessPaymentServiceRequest paymentServiceRequest, PaymentRequest paymentRequest, String errorStack) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("transaction_amount", paymentRequest.getAmount())
                .add("date", paymentRequest.getDate())
                .add("sysdate", paymentRequest.getTimeStamp())
                .add("invoice_amount", paymentRequest.getInvoiceAmount())
                .add("commission_amount", paymentRequest.getCommissionAmount())
                .add("vat_amount", paymentRequest.getVatAmount())
                .add("merchant_transaction_no", paymentRequest.getMerchantTransactionId())
                .add("member_id", paymentServiceRequest.getMemberId())
                .add("service_provider_id", paymentServiceRequest.getServiceProviderId())
                .add("id", UUID.randomUUID().toString())
                .add("raw_request", new ObjectMapper().writeValueAsString(paymentRequest))
                .add("status", errorStack);

        try (Connection connection = DB.getConnection();
             AutoStartTransaction a = new AutoStartTransaction(connection);
             AutoRollback transaction = new AutoRollback(connection)) {
            // In a transaction (use the existing connection)
            DB.insertIntoWithStringPk(connection, TABLE_NAME, insertValues, PRIMARY_KEY);
            transaction.commit();
        } catch (Exception se) {
            LOG.error("SQLException: " + se.getMessage());
            throw new Exception(se.getMessage());
        }
    }

    public static DataResult query(PaymentHistorySpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();

        if (specification != null) {
            where
                    .addIfExists("batch_payment_status = ?", specification.getBatchPaymentStatus());

        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, PaymentHistoryRepository::buildRecord);
    }


    private static Entity buildRecord(ResultSet resultSet) {
        try {
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setId(resultSet.getString("id"));
            paymentRequest.setCommissionAmount(resultSet.getString("commission_amount"));
            paymentRequest.setAmount(resultSet.getString("transaction_amount"));
            paymentRequest.setTimeStamp(resultSet.getString("sysdate"));
            paymentRequest.setVatAmount(resultSet.getString("vat_amount"));
            paymentRequest.setInvoiceAmount(resultSet.getString("invoice_amount"));
            paymentRequest.setServiceProviderId(resultSet.getString("service_provider_id"));
            paymentRequest.setDate(resultSet.getString("date"));
            return paymentRequest;
        } catch (Exception e) {
            LOG.error("error when building record for service provider " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public static void updatePaymentHistory(String merchantTransactionId, String status) throws Exception {

        String sql = "update carfix.payment_history set status = ? where merchant_transaction_no = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, merchantTransactionId);
            pstmt.execute();
        } catch (Exception e) {
            throw e;
        }
    }
}


