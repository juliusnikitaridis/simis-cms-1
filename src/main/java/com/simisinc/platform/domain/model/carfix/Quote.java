package com.simisinc.platform.domain.model.carfix;

import com.simisinc.platform.domain.model.Entity;
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
    private String date;
    private String serviceProviderName;

}




