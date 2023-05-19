package com.simisinc.platform.domain.model.carfix;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.simisinc.platform.domain.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Julius Nikitaridis
 * class that will represent a service provider.
 * This class should be saved to the db along with the previously created users unique id.
 * 14-12-2022
 */

@Getter
@Setter
@NoArgsConstructor
public class ServiceProvider extends User {

    private String serviceProviderId;
    private String userId;
    private Brand[] supportedBrands; //this should be a jsonarray string ? //expand to table ?
    private Category[] supportedCategories;
    private String name;
    private String services;
    private String certifications;
    private String aboutUs;
    private String address;
    private String logoData;
    private String accreditations;
    private String rating;
    private String count;
    private String dropOff;
    private String RMI;
    private String operatingYear;
    private String operatingHours;
    private String accountFirstName;
    private String accountLastName;
    private String accountNo;
    private String accountBranch;
    private String accountBank;

    public String getSupportedBrandsAsJSONString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        if(this.supportedBrands == null) {
            return null;
        }
        return mapper.writeValueAsString(this.supportedBrands);
    }

    public String getSupportedCategoriesAsString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        if(this.supportedCategories == null) {
            return null;
        }
        return mapper.writeValueAsString(this.supportedCategories);
    }
}




