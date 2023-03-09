package com.simisinc.platform.rest.services.carfix;

import com.simisinc.platform.domain.model.carfix.Brand;
import com.simisinc.platform.domain.model.carfix.Category;
import com.simisinc.platform.domain.model.carfix.ServiceProvider;
import com.simisinc.platform.domain.model.carfix.ServiceRequest;
import com.simisinc.platform.infrastructure.database.DataResult;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceProviderRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceProviderSpecification;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceRequestRepository;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceRequestSpecification;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Description
 * Service to return all service records created. if a member id is set, return the service records for that member
 * @author Julius Nikitaridis
 * @created 18/11/22 11:28 AM
 */


public class RFSListService {

    private static Log LOG = LogFactory.getLog(RFSListService.class);

    public ServiceResponse get(ServiceContext context) {


        try {
            final String memberId = context.getParameter("memberId");
            final String vehicleId = context.getParameter("vehicleId");
            final String serviceRequestId = context.getParameter("serviceRequestId");
            final String serviceProviderId = context.getParameter("serviceProviderId");
            final String serviceProviderUniqueId = context.getParameter("serviceProviderUniqueId");

            //filter the service requests for a particular SP
            ServiceRequestSpecification specification = new ServiceRequestSpecification();
            if(serviceProviderId != null) {
                ServiceResponse response = new ServiceResponse(200);
                response.setData(getApplicableServiceRequests(serviceProviderId,null));
                return response;
            } else if(serviceProviderUniqueId != null) {
                ServiceResponse response = new ServiceResponse(200);
                response.setData(getApplicableServiceRequests(null,serviceProviderUniqueId));
                return response;
            } else if(memberId!= null) {
                specification.setMemberId(memberId);
            }
            else if(vehicleId != null) {
                specification.setVehicleId(vehicleId);
            }
            else if(serviceRequestId != null) {
                specification.setServiceRequestId(serviceRequestId);
            } else {
                //nothing to do here
            }
            DataResult result = ServiceRequestRepository.query(specification,null);
            List<ServiceRequest> serviceRequestList = (List<ServiceRequest>) result.getRecords();

            ServiceResponse response = new ServiceResponse(200);
            response.setData(serviceRequestList);
            return response;

        } catch (Exception e) {
            LOG.error("Error in RFSListService", e);
            ServiceResponse response = new ServiceResponse(400);
            response.getError().put("title", e.getMessage());
            return response;
        }
    }

    /**Julius Nikitaridis
     * Need to return all the service requests where the supperted brands and supported categories of the
     * give service provider match.
     * @param serviceProviderId
     * @return
     * @throws Exception
     */
    public List<ServiceRequest> getApplicableServiceRequests(String serviceProviderId,String serviceProviderUniqueId) throws Exception {
        ServiceProviderSpecification specification = new ServiceProviderSpecification();
        if(specification.getServiceProviderId() != null) {
            specification.setServiceProviderId(serviceProviderId);
        }
        if(serviceProviderUniqueId != null) {
            specification.setServiceProviderUniqueId(serviceProviderUniqueId);
        }
        ServiceProvider serviceProvider = (ServiceProvider)ServiceProviderRepository.query(specification,null).getRecords().get(0);
        List<Brand> serviceProviderBrandsIds = Arrays.asList(serviceProvider.getSupportedBrands()); //[{"id":"3838393939nd39","brandName":null},{"id":"fe14578f-1a2c-43f4-b293-2f7b039d3100","brandName":null},{"id":"208c4493-ed44-4508-80f7-0b7a42de7208","brandName":null},{"id":"299edb1b-f085-4eff-aec1-93fdddf190eb","brandName":null},{"id":"2619069d-0f4b-44ae-9a08-3d6ca0f586a2","brandName":null}]
        List<String>serviceProviderBrandsIdsOnly = new ArrayList<>();
        serviceProviderBrandsIds.stream().forEach(x->{
            serviceProviderBrandsIdsOnly.add(x.getId());
        });
        List<Category> serviceProviderCategories = Arrays.asList(serviceProvider.getSupportedCategories());
        List<String> serviceProviderCategoriesOnly = new ArrayList<>();
        serviceProviderCategories.stream().forEach(x->{
            serviceProviderCategoriesOnly.add(x.getId());
        });

        //check the service request id on the quote table

        //get all service requests by status
        ServiceRequestSpecification specification1 = new ServiceRequestSpecification();
        specification1.setStatus("CREATED"); //TODO this needs to be optimized - try and filter this further
        List<ServiceRequest> matchingServiceRequests = (List<ServiceRequest>) ServiceRequestRepository.query(specification1,null).getRecords();

        ArrayList<ServiceRequest> finalMatchingServiceRequests = new ArrayList<>();

        for (ServiceRequest serviceReq : matchingServiceRequests) {
            //check to see that there are no more than 8 quotes that already exist for this SR.
            if(ServiceRequestRepository.countNumberOfQuotes(serviceReq.getId()) > 8){
                continue;
            }
            String[] serviceReqCategories = serviceReq.getCategoryHash().split("\\|");
            List<String> serviceRegCategoriesList = Arrays.asList(serviceReqCategories);

            if (serviceProviderBrandsIdsOnly.contains(serviceReq.getVehicleBrandId()) && serviceProviderCategoriesOnly.containsAll(serviceRegCategoriesList)) {
                finalMatchingServiceRequests.add(serviceReq);
            }
        }

        return finalMatchingServiceRequests;
    }
}

