/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.nggroup.impl.driver;

import org.apache.reef.driver.task.RunningTask;
import org.apache.reef.wake.EventHandler;

import java.util.logging.Logger;

public class TopologyRunningTaskHandler implements EventHandler<RunningTask> {

  private static final Logger LOG = Logger.getLogger(TopologyRunningTaskHandler.class.getName());

  private final CommunicationGroupDriverImpl communicationGroupDriverImpl;

  public TopologyRunningTaskHandler(final CommunicationGroupDriverImpl communicationGroupDriverImpl) {
    this.communicationGroupDriverImpl = communicationGroupDriverImpl;
  }

  @Override
  public void onNext(final RunningTask runningTask) {
    final String runningTaskId = runningTask.getId();
    LOG.entering("TopologyRunningTaskHandler", "onNext", runningTaskId);
    communicationGroupDriverImpl.runTask(runningTaskId);
    LOG.exiting("TopologyRunningTaskHandler", "onNext", runningTaskId);
  }

}
