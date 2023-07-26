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

import com.simisinc.platform.application.cms.WebPageHitSnapshotCommand;
import com.simisinc.platform.infrastructure.distributedlock.LockManager;
import com.simisinc.platform.infrastructure.scheduler.SchedulerManager;

/**
 * Makes a summary snapshot of web page hits
 *
 * @author matt rajkowski
 * @created 5/21/18 2:00 PM
 */
public class WebPageHitSnapshotJob {

  private static Log LOG = LogFactory.getLog(WebPageHitSnapshotJob.class);

  @Job(name = "Make a snapshot of web page hits")
  public static void execute() {
    // Distributed lock
    String lock = LockManager.lock(SchedulerManager.WEB_PAGE_HIT_SNAPSHOT_JOB, Duration.ofMinutes(5));
    if (lock == null) {
      return;
    }

    WebPageHitSnapshotCommand.updateSnapshots();
  }
}
