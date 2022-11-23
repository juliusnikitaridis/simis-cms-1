package com.simisinc.platform.infrastructure.persistence.carfix;

import com.simisinc.platform.domain.model.carfix.ServiceRequest;
import com.simisinc.platform.domain.model.carfix.ServiceRequestItem;
import com.simisinc.platform.domain.model.carfix.Vehicle;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Random;

/**
 * Julius Nikitaridis
 * 22/12/2022
 * repo class for ServiceRequest objects
 */

public class ServiceRequestRepository {

    private static Log LOG = LogFactory.getLog(ServiceRequestRepository.class);
    private static String TABLE_NAME = "carfix.Service_request";
    private static String TABLE_NAME_ITEMS = "carfix.Service_request_items";
    private static String[] PRIMARY_KEY = new String[]{"id"};
    private static String[] PRIMARY_KEY_ITEMS = new String[]{"id"};


    public static void addItems(ServiceRequest serviceRequest) throws Exception {
         Connection connection = DB.getConnection();
        for (ServiceRequestItem serviceRequestItem : serviceRequest.getServiceRequestItems()) {
            SqlUtils insertValue = new SqlUtils();
            insertValue
            .add("id", new Random().nextLong()) //TODO should be changed to UUID v4
                    .add("service_request_id",serviceRequest.getId())
                    .add("service_request_option_id",serviceRequestItem.getServiceRequestOptionId());

            AutoStartTransaction a = new AutoStartTransaction(connection);
            AutoRollback transaction = new AutoRollback(connection);
            DB.insertInto(connection, TABLE_NAME_ITEMS, insertValue, PRIMARY_KEY_ITEMS); //TODO is there a way to insert batches - lists ???
            transaction.commit();
        }
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
                .add("last_service_date", record.getLastServiceDate());

        try {
            try (Connection connection = DB.getConnection();
                 AutoStartTransaction a = new AutoStartTransaction(connection);
                 AutoRollback transaction = new AutoRollback(connection)) {
                // In a transaction (use the existing connection)
                DB.insertInto(connection, TABLE_NAME, insertValues, PRIMARY_KEY);
                transaction.commit();
                return record;
            }
        } catch (Exception se) {
            LOG.error("SQLException: " + se.getMessage());
            throw new Exception(se.getMessage());
        }
    }


    public static List<ServiceRequest> findAll(ServiceRequestSpecification specification, DataConstraints constraints) {
        if (constraints == null) {
            constraints = new DataConstraints();
        }
        constraints.setDefaultColumnToSortBy("date");
        DataResult result = query(specification, constraints);
        return (List<ServiceRequest>) result.getRecords();
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
                    .addIfExists("vehicle_id = ?", specification.getVehicleId(), -1)
                    .addIfExists("member_id = ?", specification.getMemberId(), -1)
                    .addIfExists("id = ?", specification.getServiceRequestId(), -1);

        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, ServiceRequestRepository::buildRecord);
    }


    private static ServiceRequest buildRecord(ResultSet rs) {

        ServiceRequest request = new ServiceRequest();
        try {
            request.setId(Long.valueOf(rs.getString("id")));
            request.setDate(rs.getString("date"));
            request.setType(rs.getString("type"));
            request.setVehicleId(rs.getString("vehicle_id"));
            request.setMemberId(rs.getString("member_id"));
            request.setRadius(rs.getString("radius"));
            request.setStatus(rs.getString("status"));
            request.setCurrentOdoReading("current_odo_reading");
            request.setPictureData("picture_data");
            request.setLastServiceDate("last_service_date");
            return request;
        } catch (Exception e) {
            LOG.error("exception when building record for ServiceRequest" + e.getMessage());
            return null;
        }
    }
}

