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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Event details for when a user signed up
 *
 * @author matt rajkowski
 * @created 4/29/21 5:32 PM
 * // Car brand and model : Reg Nr: XXXXXXX: Owners : Ms. XXXXXXX. Job description: XXXXXXXX Reference number XXXXXXX.
 */
@NoArgsConstructor

public class ServiceProviderReminderEvent extends Event {

    private String bookingListDisplayString;
    private String serviceProviderName;
    private User user = null;
    public static final String ID = "service-provider-reminder";


    public String getBookingListDisplayString() {
        return bookingListDisplayString;
    }

    public void setBookingListDisplayString(String bookingListDisplayString) {
        this.bookingListDisplayString = bookingListDisplayString;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ServiceProviderReminderEvent(User user) {
        this.user = user;
    }

    @Override
    public String getDomainEventType() {
        return ID;
    }

}
