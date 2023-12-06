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

package com.simisinc.platform.domain.events.cms;

import com.simisinc.platform.domain.events.Event;
import com.simisinc.platform.domain.model.User;
import lombok.NoArgsConstructor;

/**
 * Event details for when a user is invited
 *
 * @author matt rajkowski
 * @created 4/29/21 5:32 PM
 */
@NoArgsConstructor
public class UserInvitedEvent extends Event {

  public static final String ID = "user-invited";

  private User user = null;
  private User invitedBy = null;
  private String farmName = null;


  public UserInvitedEvent(User user, User invitedBy) {
    this.user = user;
    this.invitedBy = invitedBy;
  }

  public UserInvitedEvent(User user, User invitedBy, String farmName) {
    this.user = user;
    this.invitedBy = invitedBy;
    this.farmName = farmName;
  }

  @Override
  public String getDomainEventType() {
    return ID;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }

  public void setInvitedBy(User invitedBy) {
    this.invitedBy = invitedBy;
  }

  public User getInvitedBy() {
    return invitedBy;
  }

  public String getFarmName() {
    return farmName;
  }

  public void setFarmName(String farmName) {
    this.farmName = farmName;
  }
}
