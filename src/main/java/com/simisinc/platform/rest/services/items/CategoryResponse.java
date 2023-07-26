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

package com.simisinc.platform.rest.services.items;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.simisinc.platform.domain.model.items.Category;

/**
 * Description
 *
 * @author matt rajkowski
 * @created 1/22/19 12:12 PM
 */
public class CategoryResponse {

  String uniqueId;
  String name;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  String description;

  public CategoryResponse(Category record) {
    uniqueId = record.getUniqueId();
    name = record.getName();
    description = record.getDescription();
  }

  public String getUniqueId() {
    return uniqueId;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

}
