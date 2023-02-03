package com.simisinc.platform.infrastructure.persistence.carfix;

import com.simisinc.platform.domain.model.Entity;
import com.simisinc.platform.domain.model.carfix.ServiceRequest;
import com.simisinc.platform.domain.model.carfix.ServiceRequestItem;
import com.simisinc.platform.domain.model.carfix.ServiceRequestItemOption;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Julius Nikitaridis
 * 22/12/2022
 * repo class for ServiceRequest objects
 */

public class ServiceRequestRepository {

    private static Log LOG = LogFactory.getLog(ServiceRequestRepository.class);
    private static String TABLE_NAME = "carfix.Service_request";
    private static String TABLE_NAME_ITEMS = "carfix.Service_request_items";
    private static String TABLE_NAME_ITEM_OPTIONS = "carfix.service_request_items_options";
    private static String[] PRIMARY_KEY = new String[]{"id"};
    private static String[] PRIMARY_KEY_ITEMS = new String[]{"id"};


    public static void addItems(ServiceRequest serviceRequest,Connection conn) throws Exception {
        for (ServiceRequestItem serviceRequestItem : serviceRequest.getServiceRequestItems()) {
            SqlUtils insertValue = new SqlUtils();
            insertValue
                    .add("id", UUID.randomUUID().toString())
                    .add("service_request_id", serviceRequest.getId())
                    .add("service_request_option_id", serviceRequestItem.getServiceRequestOptionId());

            DB.insertIntoWithStringPk(conn, TABLE_NAME_ITEMS, insertValue, PRIMARY_KEY_ITEMS); //TODO is there a way to insert batches - lists ???
        }
    }


    //return reference data for creating service requests
    public static DataResult findOptions() throws Exception {
        return DB.selectAllFrom(TABLE_NAME_ITEM_OPTIONS, null, null, ServiceRequestRepository::buildRecordItemsOption);
    }


    public static ServiceRequest add(ServiceRequest record) throws Exception {
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("date", record.getDate())
                .add("type", record.getType())
                .add("vehicle_id", record.getVehicleId())
                .add("member_id", record.getMemberId())
                .add("radius", record.getRadius())
                .add("status", record.getStatus())
                .add("current_odo_reading", record.getCurrentOdoReading())
                .add("picture_data", record.getPictureData())
                .add("additional_description", record.getAdditionalDescription())
                .add("last_service_date", record.getLastServiceDate());

            try (Connection connection = DB.getConnection();
                 AutoStartTransaction a = new AutoStartTransaction(connection);
                 AutoRollback transaction = new AutoRollback(connection)) {
                // In a transaction (use the existing connection)
                DB.insertIntoWithStringPk(connection, TABLE_NAME, insertValues, PRIMARY_KEY);
                addItems(record,connection);
                transaction.commit();
                return record;

        } catch (Exception se) {
            LOG.error("SQLException: " + se.getMessage());
            throw new Exception(se.getMessage());
        }
    }




    public static ServiceRequest findById(long id) {
        if (id == -1) {
            return null;
        }
        return (ServiceRequest) DB.selectRecordFrom(
                TABLE_NAME, new SqlUtils().add("id = ?", id),
                ServiceRequestRepository::buildRecord);
    }


    public static DataResult query(ServiceRequestSpecification specification, DataConstraints constraints) {
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();
        if (specification != null) {
            where
                    .addIfExists("vehicle_id = ?", specification.getVehicleId())
                    .addIfExists("member_id = ?", specification.getMemberId())
                    .addIfExists("id = ?", specification.getServiceRequestId());

        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, ServiceRequestRepository::buildRecord);
    }


    private static ServiceRequest buildRecord(ResultSet rs) {

        ServiceRequest request = new ServiceRequest();
        try {
            request.setId(rs.getString("id"));
            request.setDate(rs.getString("date"));
            request.setType(rs.getString("type"));
            request.setVehicleId(rs.getString("vehicle_id"));
            request.setMemberId(rs.getString("member_id"));
            request.setRadius(rs.getString("radius"));
            request.setStatus(rs.getString("status"));
            request.setCurrentOdoReading(rs.getString("current_odo_reading"));
            request.setPictureData(rs.getString("picture_data"));
            request.setAdditionalDescription(rs.getString("additional_description"));
            request.setLastServiceDate(rs.getString("last_service_date"));
            request.setConfirmedServiceProvider(rs.getString("confirmed_service_provider_id"));
            request.setAcceptedQuoteId(rs.getString("accepted_quote_id"));

            //now also need to get all the service request items
            ArrayList<ServiceRequestItem> serviceRequestItems = (ArrayList<ServiceRequestItem>) DB.selectAllFrom(TABLE_NAME_ITEMS,new SqlUtils(),new SqlUtils().add("service_request_id = ?",rs.getString("id")),null,null,ServiceRequestRepository::buildRecordServiceRequestItems).getRecords();
            request.setServiceRequestItems(serviceRequestItems);
            return request;
        } catch (Exception e) {
            LOG.error("exception when building record for ServiceRequest" + e.getMessage());
            return null;
        }
    }



    private static ServiceRequestItem buildRecordServiceRequestItems(ResultSet resultSet) {
        ServiceRequestItem serviceRequestItem = new ServiceRequestItem();
        try {
            serviceRequestItem.setId(resultSet.getString("id"));
            serviceRequestItem.setServiceRequestOptionId(resultSet.getString("service_request_option_id"));
            ///get the info from the service_request_items_options table to avoid the additional calls
            ServiceRequestItemOption option = (ServiceRequestItemOption) DB.selectRecordFrom("carfix.service_request_items_options",new SqlUtils().add("id = ?", resultSet.getString("service_request_option_id")),ServiceRequestRepository::buildRecordItemsOption);
            serviceRequestItem.setServiceRequestItemOptionCategory(option.getCategory());
            serviceRequestItem.setServiceRequestItemOptionDescription(option.getDescription());
            return serviceRequestItem;
        } catch (SQLException throwables) {
            LOG.error("exception when building record for buildRecordServiceRequestItems");
            return null;
        }
    }


    private static ServiceRequestItemOption buildRecordItemsOption(ResultSet rs) {
        ServiceRequestItemOption option = new ServiceRequestItemOption();
        try {
            option.setCategory(rs.getString("category"));
            option.setDescription(rs.getString("description"));
            option.setId(rs.getString("id"));
            return option;
        } catch (Exception e) {
            LOG.error("exception when building item option");
            return null;
        }
    }


    //method to update the status
    public static void updateStatus(String status, String serviceRequestId, Connection conn) {
        String sql = "update carfix.service_request set status = ? where id = ?";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,status);
            pstmt.setString(2,serviceRequestId);
            pstmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            LOG.error("error when updating service request status");
        }
    }
}

