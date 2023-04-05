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

package com.simisinc.platform.infrastructure.scheduler.cms;

import com.simisinc.platform.domain.events.carfix.ServiceProviderReminderEvent;
import com.simisinc.platform.infrastructure.persistence.carfix.ServiceRequestRepository;
import com.simisinc.platform.infrastructure.workflow.WorkflowManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jobrunr.jobs.annotations.Job;

import java.util.List;

/**
 * sends email reminders to SP for all service requests that
 * have a confirmed date between now (when job is run sysdate) and same time tomorrow (24 hours later)
 *
 * This job should ALWAYS be run at 17:00
 *
 * @author julius niki
 * @created 4/04/23 2:00 PM
 */
public class SendReminderToServiceProviderJob {

  private static Log LOG = LogFactory.getLog(SendReminderToServiceProviderJob.class);

  @Job(name = "Send email reminders to SPs")
  public static void execute() throws Exception {
    List<ServiceRequestRepository.EmailReminderInfo> emailNotifications = ServiceRequestRepository.getServiceRequestsForTomorrow();

    for (ServiceRequestRepository.EmailReminderInfo emailNotification : emailNotifications) {
      ServiceProviderReminderEvent event = new ServiceProviderReminderEvent(emailNotification.getServiceProviderUser());
      event.setServiceProviderName(emailNotification.getServiceProviderName());
      event.setServiceProviderAddress(emailNotification.getServiceProviderAddress());
      event.setBookingConfirmedDate(emailNotification.getConfirmedDate());
      event.setCustomerReference(emailNotification.getBookingNumber());
      WorkflowManager.triggerWorkflowForEvent(event);
    }
  }
}
