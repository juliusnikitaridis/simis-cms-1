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

import java.time.Duration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jobrunr.jobs.annotations.Job;

import com.simisinc.platform.infrastructure.distributedlock.LockManager;
import com.simisinc.platform.infrastructure.persistence.cms.WebPageHitRepository;
import com.simisinc.platform.infrastructure.scheduler.SchedulerManager;

/**
 * Deletes old web hits data
 *
 * @author matt rajkowski
 * @created 5/21/18 2:45 PM
 */
public class WebPageHitsCleanupJob {

  private static Log LOG = LogFactory.getLog(WebPageHitsCleanupJob.class);

  @Job(name = "Delete old web hits data")
  public static void execute() {
    // Distributed lock
    String lock = LockManager.lock(SchedulerManager.WEB_PAGE_HITS_CLEANUP_JOB, Duration.ofHours(4));
    if (lock == null) {
      return;
    }

    WebPageHitRepository.deleteOldWebHits();
  }
}
