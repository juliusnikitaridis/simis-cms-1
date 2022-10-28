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

import com.simisinc.platform.application.items.LoadCollectionCommand;
import com.simisinc.platform.domain.model.items.Category;
import com.simisinc.platform.domain.model.items.Collection;
import com.simisinc.platform.infrastructure.persistence.items.CategoryRepository;
import com.simisinc.platform.infrastructure.persistence.items.CollectionRepository;
import com.simisinc.platform.rest.controller.ServiceContext;
import com.simisinc.platform.rest.controller.ServiceResponse;
import com.simisinc.platform.rest.controller.ServiceResponseCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 *
 * @author matt rajkowski
 * @created 4/17/18 9:00 AM //test change
 */
public class CategoryListService {

  private static Log LOG = LogFactory.getLog(CategoryListService.class);

  // GET /categories/{collectionUniqueId}
  public ServiceResponse get(ServiceContext context) {

    // Determine the collection
    String collectionUniqueId = context.getPathParam();
    Collection collection = CollectionRepository.findByUniqueId(collectionUniqueId);
    if (collection == null) {
      ServiceResponse response = new ServiceResponse(400);
      response.getError().put("title", "Parent collection was not found");
      return response;
    }

    // Validate access to the collection
    if (LoadCollectionCommand.loadCollectionByIdForAuthorizedUser(collection.getId(), context.getUserId()) == null) {
      LOG.warn("User does not have access to this collection");
      ServiceResponse response = new ServiceResponse(400);
      response.getError().put("title", "Collection was not found");
      return response;
    }

    // Load the category list
    List<Category> categoryList = CategoryRepository.findAllByCollectionId(collection.getId());
    List<CategoryResponse> recordList = new ArrayList<>();
    for (Category category : categoryList) {
      recordList.add(new CategoryResponse(category));
    }

    // Prepare the response
    ServiceResponse response = new ServiceResponse(200);
    ServiceResponseCommand.addMeta(response, "category", recordList, null);
    response.setData(recordList);
    return response;
  }

}
