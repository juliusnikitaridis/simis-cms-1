package com.simisinc.platform.infrastructure.persistence.carfix;

import com.simisinc.platform.domain.model.User;
import com.simisinc.platform.domain.model.carfix.*;
import com.simisinc.platform.infrastructure.database.*;
import com.simisinc.platform.infrastructure.persistence.UserRepository;
import com.simisinc.platform.rest.services.carfix.ServiceUtils;
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
    private static String TABLE_NAME_CATEGORYS = "carfix.categories";
    private static String TABLE_NAME_BRANDS = "carfix.brands";
    private static String[] PRIMARY_KEY = new String[]{"id"};
    private static String[] PRIMARY_KEY_ITEMS = new String[]{"id"};


    private static void addItems(ServiceRequest serviceRequest,Connection conn) throws Exception {
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
                .add("confirmed_date",record.getConfirmedDate())
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
                .add("preferred_date",record.getPreferredDate())
                .add("customer_reference",record.getCustomerReference())
                .addIfExists("service_advisor",record.getServiceAdvisor())
                .addIfExists("technician",record.getTechnician());

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

    public static ServiceRequest update(ServiceRequest record) throws Exception {

        SqlUtils updateValues = new SqlUtils()
                .addIfExists("created_date", String.valueOf(System.currentTimeMillis()))
                .addIfExists("type", record.getType())
                .addIfExists("confirmed_date",record.getConfirmedDate())
                .addIfExists("vehicle_id", record.getVehicleId())
                .addIfExists("member_id", record.getMemberId())
                .addIfExists("radius", record.getRadius())
                .addIfExists("status", record.getStatus())
                .addIfExists("current_odo_reading", record.getCurrentOdoReading())
                .addIfExists("picture_data", record.getPictureData())
                .addIfExists("additional_description", record.getAdditionalDescription())
                .addIfExists("last_service_date", record.getLastServiceDate())
                .addIfExists("vehicle_brand_id",record.getVehicleBrandId())
                .addIfExists("preferred_date",record.getPreferredDate())
                .addIfExists("customer_reference",record.getCustomerReference())
                .addIfExists("service_advisor",record.getServiceAdvisor())
                .addIfExists("technician",record.getTechnician());

        try (Connection connection = DB.getConnection();
             AutoStartTransaction a = new AutoStartTransaction(connection);
             AutoRollback transaction = new AutoRollback(connection)) {
            // In a transaction (use the existing connection)
            DB.update(connection, TABLE_NAME, updateValues, new SqlUtils().add("id = ?", record.getId()));
            transaction.commit();
            return record;

        } catch (Exception se) {
            LOG.error("SQLException: " + se.getMessage());
            throw new Exception(se.getMessage());
        }
    }




    public static ServiceRequest findById(String id) {
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
                    .addIfExists("confirmed_service_provider_id = ?", specification.getServiceProviderId())
                    .addIfExists("status = ?",specification.getStatus());


        }
        return DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, ServiceRequestRepository::buildRecord);
    }
    public static List<ServiceRequest> AcceptedSPQuotes(String ServiceProviderId, DataConstraints constraints){
        SqlUtils select = new SqlUtils();
        SqlUtils where = new SqlUtils();
        SqlUtils orderBy = new SqlUtils();

        where.add("confirmed_service_provider_id = ?",ServiceProviderId)
                .add("status != ?","CREATED");

        List<ServiceRequest> acceptedServiceRequests = (List<ServiceRequest>) DB.selectAllFrom(
                TABLE_NAME, select, where, orderBy, constraints, ServiceRequestRepository::buildRecord).getRecords();
        //first and last name of the customer
        acceptedServiceRequests.forEach(serviceRequest -> {
            User memberUserForServiceRequest = UserRepository.findByUniqueId(serviceRequest.getMemberId());
            serviceRequest.setUserFirstName(memberUserForServiceRequest.getFirstName());
            serviceRequest.setUserLastName(memberUserForServiceRequest.getLastName());
        });
        return acceptedServiceRequests;

    }

    //see how many quotes exist for serviceRequest
    public static int countNumberOfQuotes(String requestForServiceId) throws Exception {
        String sql = "select count(*) as ans from  carfix.quote where request_for_service_id = ?";
        try(Connection conn = DB.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1,requestForServiceId);
            ResultSet rs = pstmt.executeQuery();
            String count = null;
            while(rs.next()) {
                count = rs.getString("ans");
            }
            return Integer.valueOf(count);
        } catch(Exception e) {
            throw e;
        }
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
            request.setPictureData(ServiceUtils.readDataFromFile(rs.getString("picture_data")));
            request.setAdditionalDescription(rs.getString("additional_description"));
            request.setLastServiceDate(rs.getString("last_service_date"));
            request.setConfirmedServiceProvider(rs.getString("confirmed_service_provider_id"));
            request.setAcceptedQuoteId(rs.getString("accepted_quote_id"));
            request.setVehicleBrandId(rs.getString("vehicle_brand_id"));
            request.setCategoryHash(rs.getString("category_hash"));
            request.setConfirmedDate(rs.getString("confirmed_date"));
            request.setPreferredDate(rs.getString("preferred_date"));
            request.setCustomerReference(rs.getString("customer_reference"));
            request.setServiceAdvisor(rs.getString("service_advisor"));
            request.setTechnician(rs.getString("technician"));

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
    public static void updateStatus(String status, String serviceRequestId) throws Exception {
        String sql = "update carfix.service_request set status = ? where id = ?";
        try(Connection conn = DB.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1,status);
            pstmt.setString(2,serviceRequestId);
            pstmt.execute();
        } catch(Exception e) {
            throw e;
        }
    }

    public static void updateTechnician(String technician, String serviceRequestId) throws Exception {
        String sql = "update carfix.service_request set technician = ? where id = ?";
        try(Connection conn = DB.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1,technician);
            pstmt.setString(2,serviceRequestId);
            pstmt.execute();
        } catch(Exception e) {
            throw e;
        }
    }

    public static void updateServiceAdvisor(String serviceAdvisor, String serviceRequestId) throws Exception {
        String sql = "update carfix.service_request set service_advisor = ? where id = ?";
        try(Connection conn = DB.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1,serviceAdvisor);
            pstmt.setString(2,serviceRequestId);
            pstmt.execute();
        } catch(Exception e) {
            throw e;
        }
    }

    public static void addJobNumber(String job_num, String serviceRequestId) throws Exception {
        String sql = "update carfix.service_request set job_num = ? where id = ?";
        try(Connection conn = DB.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,job_num);
            pstmt.setString(2,serviceRequestId);
            pstmt.execute();
        } catch(Exception e) {
            throw e;
        }
    }

    //get all service requests between now and same timee tomorrow
    public static ArrayList<EmailReminderInfo> getServiceRequestsForTomorrow() throws  Exception {
        String sql = "select * from carfix.service_request where date(TO_CHAR(TO_TIMESTAMP(confirmed_date::BIGINT/ 1000), 'DD/MM/YYYY HH24:MI:SS')) > now() and date(TO_CHAR(TO_TIMESTAMP(confirmed_date::BIGINT/ 1000), 'DD/MM/YYYY HH24:MI:SS')) < now() + INTERVAL '1 DAY';\n";
        ArrayList<EmailReminderInfo>serviceRequestConfirmedServiceProviderIdList = new ArrayList<>();
        try(Connection conn = DB.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                EmailReminderInfo info = new EmailReminderInfo(rs.getString("confirmed_service_provider_id"));
                info.setCustomerReference(rs.getString("customer_reference"));
                info.setServiceRequestAdditionalDescription(rs.getString("additional_description"));
                info.setVehicleId(rs.getString("vehicle_id"));
                serviceRequestConfirmedServiceProviderIdList.add(info);
            }
        } catch(Exception e) {
            throw e;
        }


        //need to get user info for the service providers
        for (EmailReminderInfo s : serviceRequestConfirmedServiceProviderIdList) {
            User serviceProviderUser = UserRepository.findByUniqueId(s.getServiceProviderUniqueId());
            Vehicle serviceRequestVehicle = VehicleRepository.findById(s.getVehicleId());
            if(serviceRequestVehicle == null) {
                throw new Exception("vehicle for service request not found");
            }
            if(serviceProviderUser == null) {
                throw new Exception("could not find system user for service provider when sending reminders");
            }
            s.setServiceProviderUser(serviceProviderUser);
            s.setVehicleMake(serviceRequestVehicle.getMake());
            s.setVehicleRegistration(serviceRequestVehicle.getRegistration());
            s.setVehicleModel(serviceRequestVehicle.getModel());
            s.setServiceProviderName(serviceProviderUser.getFirstName());
        }

        return serviceRequestConfirmedServiceProviderIdList;
    }


    public static class EmailReminderInfo {
        private  String customerReference;
        private  String vehicleModel;
        private  String vehicleMake;
        private  String vehicleRegistration;
        private  String serviceProviderName;
        private  String serviceProviderUniqueId;
        private  String serviceRequestAdditionalDescription;
        private  User   serviceProviderUser;
        private String vehicleId;

        EmailReminderInfo(String serviceProviderUniqueId) {
            this.serviceProviderUniqueId = serviceProviderUniqueId;
        }

        public String getVehicleId() {
            return vehicleId;
        }

        public void setVehicleId(String vehicleId) {
            this.vehicleId = vehicleId;
        }

        public String getCustomerReference() {
            return customerReference;
        }

        public void setCustomerReference(String customerReference) {
            this.customerReference = customerReference;
        }

        public String getVehicleModel() {
            return vehicleModel;
        }

        public void setVehicleModel(String vehicleModel) {
            this.vehicleModel = vehicleModel;
        }

        public String getVehicleMake() {
            return vehicleMake;
        }

        public void setVehicleMake(String vehicleMake) {
            this.vehicleMake = vehicleMake;
        }

        public String getVehicleRegistration() {
            return vehicleRegistration;
        }

        public void setVehicleRegistration(String vehicleRegistration) {
            this.vehicleRegistration = vehicleRegistration;
        }

        public String getServiceProviderName() {
            return serviceProviderName;
        }

        public void setServiceProviderName(String serviceProviderName) {
            this.serviceProviderName = serviceProviderName;
        }

        public String getServiceProviderUniqueId() {
            return serviceProviderUniqueId;
        }

        public void setServiceProviderUniqueId(String serviceProviderUniqueId) {
            this.serviceProviderUniqueId = serviceProviderUniqueId;
        }

        public String getServiceRequestAdditionalDescription() {
            return serviceRequestAdditionalDescription;
        }

        public void setServiceRequestAdditionalDescription(String serviceRequestAdditionalDescription) {
            this.serviceRequestAdditionalDescription = serviceRequestAdditionalDescription;
        }

        public User getServiceProviderUser() {
            return serviceProviderUser;
        }

        public void setServiceProviderUser(User serviceProviderUser) {
            this.serviceProviderUser = serviceProviderUser;
        }
    }
}

