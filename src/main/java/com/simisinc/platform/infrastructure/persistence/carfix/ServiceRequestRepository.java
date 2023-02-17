package com.simisinc.platform.infrastructure.persistence.carfix;

import com.simisinc.platform.domain.model.carfix.Brand;
import com.simisinc.platform.domain.model.carfix.ServiceRequest;
import com.simisinc.platform.domain.model.carfix.ServiceRequestItem;
import com.simisinc.platform.domain.model.carfix.Category;
import com.simisinc.platform.infrastructure.database.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Julius Nikitaridis
 * 22/12/2022
 * repo class for ServiceRequest objects
 */

public class ServiceRequestRepository {

    private static Log LOG = LogFactory.getLog(ServiceRequestRepository.class);
    private static String TABLE_NAME = "carfix.Service_request";
    private static String TABLE_NAME_ITEMS = "carfix.Service_request_items";
    private static String TABLE_NAME_CATEGORYS = "carfix.categories";
    private static String TABLE_NAME_BRANDS = "carfix.brands";
    private static String[] PRIMARY_KEY = new String[]{"id"};
    private static String[] PRIMARY_KEY_ITEMS = new String[]{"id"};


    public static void addItems(ServiceRequest serviceRequest,Connection conn) throws Exception {
        for (ServiceRequestItem serviceRequestItem : serviceRequest.getServiceRequestItems()) {
            SqlUtils insertValue = new SqlUtils();
            insertValue
                    .add("id", UUID.randomUUID().toString())
                    .add("service_request_id", serviceRequest.getId())
                    .add("category_id", serviceRequestItem.getItemCategoryId())
                    .add("item_description",serviceRequestItem.getItemDescription());

            DB.insertIntoWithStringPk(conn, TABLE_NAME_ITEMS, insertValue, PRIMARY_KEY_ITEMS); //TODO is there a way to insert batches - lists ???
        }
    }


    //return reference data for creating service requests
    public static DataResult findCategories() throws Exception {
        return DB.selectAllFrom(TABLE_NAME_CATEGORYS, null, null, ServiceRequestRepository::buildRecordCategories);
    }

    public static DataResult findBrands() throws Exception {
        return DB.selectAllFrom(TABLE_NAME_BRANDS, null, null, ServiceRequestRepository::buildRecordBrands);
    }


    public static ServiceRequest add(ServiceRequest record) throws Exception {
        //build up the category hasn for lookups - this should be refined at some point

        StringBuilder requestItemsCategoryHash = new StringBuilder("");
        for(ServiceRequestItem serviceRequestItem : record.getServiceRequestItems()) {
            requestItemsCategoryHash.append(serviceRequestItem.getItemCategoryId()+"|");
        }
        SqlUtils insertValues = new SqlUtils()
                .add("id", record.getId())
                .add("created_date", String.valueOf(System.currentTimeMillis()))
                .add("type", record.getType())
                .add("vehicle_id", record.getVehicleId())
                .add("member_id", record.getMemberId())
                .add("radius", record.getRadius())
                .add("status", record.getStatus())
                .add("current_odo_reading", record.getCurrentOdoReading())
                .add("picture_data", record.getPictureData())
                .add("additional_description", record.getAdditionalDescription())
                .add("last_service_date", record.getLastServiceDate())
                .add("vehicle_brand_id",record.getVehicleBrandId())
                .add("category_hash",requestItemsCategoryHash.toString())
                .add("preferred_date",record.getPreferredDate());

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
                    .addIfExists("id = ?", specification.getServiceRequestId())
                    .addIfExists("status = ?",specification.getStatus());

        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, ServiceRequestRepository::buildRecord);
    }


    private static ServiceRequest buildRecord(ResultSet rs) {

        ServiceRequest request = new ServiceRequest();
        try {
            request.setId(rs.getString("id"));
            request.setCreatedDate(rs.getString("created_date"));
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
            request.setVehicleBrandId(rs.getString("vehicle_brand_id"));
            request.setCategoryHash(rs.getString("category_hash"));
            request.setConfirmedDate(rs.getString("confirmed_date"));
            request.setPreferredDate(rs.getString("preferred_date"));

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
            serviceRequestItem.setServiceRequestId(resultSet.getString("service_request_id"));
            Category option = (Category) DB.selectRecordFrom("carfix.categories",new SqlUtils().add("id = ?", resultSet.getString("category_id")),ServiceRequestRepository::buildRecordCategories);
            serviceRequestItem.setItemCategoryId(option.getCategory());
            serviceRequestItem.setItemDescription(resultSet.getString("item_description"));
            return serviceRequestItem;
        } catch (SQLException throwables) {
            LOG.error("exception when building record for buildRecordServiceRequestItems");
            return null;
        }
    }


    private static Category buildRecordCategories(ResultSet rs) {
        Category option = new Category();
        try {
            option.setCategory(rs.getString("category"));
            option.setDescription(rs.getString("description"));
            option.setId(rs.getString("id"));
            return option;
        } catch (Exception e) {
            LOG.error("exception when buildingcategories");
            return null;
        }
    }

    private static Brand buildRecordBrands(ResultSet rs) {
        Brand brand = new Brand();
        try {
            brand.setId(rs.getString("id"));
            brand.setBrandName(rs.getString("brand_name"));
            return brand;
        } catch (Exception e) {
            LOG.error("exception when buildRecordBrands");
            return null;
        }
    }


    //method to update the status
    public static void updateStatus(String status, String serviceRequestId, Connection conn) throws Exception {
        String sql = "update carfix.service_request set status = ? where id = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,status);
            pstmt.setString(2,serviceRequestId);
            pstmt.execute();
        } catch(Exception e) {
            throw e;
        }
    }
}

