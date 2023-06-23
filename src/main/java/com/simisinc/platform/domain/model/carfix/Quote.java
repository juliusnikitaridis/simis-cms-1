package com.simisinc.platform.domain.model.carfix;

import com.simisinc.platform.domain.model.Entity;
import com.simisinc.platform.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Julius Nikitaridis
 * class that will represent a quote from service providers
 * be 3 types of service requests
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Quote extends Entity {

    private String id;
    private String requestForServiceId;
    private String serviceProviderId;
    private String QuotationTotal;
    private ArrayList<QuoteItem> quotationItems;
    private String CreatedDate;
    private String bookingDate;
    private String serviceProviderName;
    private String vat;
    private String subtotal;
    private String status;
    private String dateType;

    //additional enrichment done from vehicle and customer tables
   private String vehicleModel;
   private String vehicleYear;
   private String vehicleMake;
   private String vehicleRegistration;
   private String customerFirstName;
   private String customerLastName;
   private String customerLocation;
   private String distanceFromSp;

}




