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
 */
@NoArgsConstructor

public class ServiceProviderReminderEvent extends Event {

  public static final String ID = "service-provider-reminder";
  private static String customerReference;
  public static String bookingConfirmedDate;
  private static String serviceProviderAddress;
  private static String serviceProviderName;
  private User user = null;

  public static String getCustomerReference() {
    return customerReference;
  }



  public static void setCustomerReference(String customerReference) {
    ServiceProviderReminderEvent.customerReference = customerReference;
  }

  public static String getBookingConfirmedDate() {
    return bookingConfirmedDate;
  }

  public static void setBookingConfirmedDate(String bookingConfirmedDate) {
    ServiceProviderReminderEvent.bookingConfirmedDate = bookingConfirmedDate;
  }

  public static String getServiceProviderAddress() {
    return serviceProviderAddress;
  }

  public static void setServiceProviderAddress(String serviceProviderAddress) {
    ServiceProviderReminderEvent.serviceProviderAddress = serviceProviderAddress;
  }

  public static String getServiceProviderName() {
    return serviceProviderName;
  }

  public static void setServiceProviderName(String serviceProviderName) {
    ServiceProviderReminderEvent.serviceProviderName = serviceProviderName;
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
