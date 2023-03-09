package com.simisinc.platform.infrastructure.persistence.carfix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.carfix.PaymentRequest;
import com.simisinc.platform.domain.model.carfix.ProcessPaymentServiceRequest;
import com.simisinc.platform.infrastructure.database.AutoRollback;
import com.simisinc.platform.infrastructure.database.AutoStartTransaction;
import com.simisinc.platform.infrastructure.database.DB;
import com.simisinc.platform.infrastructure.database.SqlUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.util.UUID;

public class PaymentRepository {

    private static Log LOG = LogFactory.getLog(PaymentRepository.class);
    private static String TABLE_NAME = "carfix.payment_history";
    private static String[] PRIMARY_KEY = new String[]{"id"};


    public static void add(ProcessPaymentServiceRequest paymentServiceRequest, PaymentRequest paymentRequest, String errorStack) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("amount", paymentServiceRequest.getAmount())
                .add("merchant_transaction_no", paymentServiceRequest.getMerchantTransactionId())
                .add("member_id", paymentServiceRequest.getMemberId())
                .add("service_provider_id", paymentServiceRequest.getServiceProviderId())
                .add("id", UUID.randomUUID().toString())
                .add("raw_request",new ObjectMapper().writeValueAsString(paymentRequest))
                .add("status",errorStack);

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
}

