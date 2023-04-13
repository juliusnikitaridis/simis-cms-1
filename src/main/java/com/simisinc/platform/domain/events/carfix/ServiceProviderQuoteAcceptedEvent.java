/*
 * Copyright 2022 SimIS Inc. (https://www.simiscms.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.simisinc.platform.domain.events.carfix;

import com.simisinc.platform.domain.events.Event;
import com.simisinc.platform.domain.model.User;
import lombok.NoArgsConstructor;

/**
 * Event details for when a user signed up
 *
 * @author julius niki
 * @created 4/29/23 5:32 PM
 * //email that would be sent to service provider once a member has accepted a quote.
 * invoked in the AcceptQuote API
 */
@NoArgsConstructor

public class ServiceProviderQuoteAcceptedEvent extends Event {

    private String  customerFullName= null;
    private String vehicleMakeModel = null;
    private User serviceProviderUser = null;
    private String confirmedDate = null;
    private String quoteCreatedDate = null;
    private String serviceProviderName = null;
    public static final String ID = "service-provider-quote-accepted";


    public String getQuoteCreatedDate() {
        return quoteCreatedDate;
    }

    public void setQuoteCreatedDate(String quoteCreatedDate) {
        this.quoteCreatedDate = quoteCreatedDate;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public String getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(String confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public String getVehicleMakeModel() {
        return vehicleMakeModel;
    }

    public void setVehicleMakeModel(String vehicleMakeModel) {
        this.vehicleMakeModel = vehicleMakeModel;
    }

    public User getServiceProviderUser() {
        return serviceProviderUser;
    }

    public void setServiceProviderUser(User serviceProviderUser) {
        this.serviceProviderUser = serviceProviderUser;
    }

    @Override
    public String getDomainEventType() {
        return ID;
    }

}
