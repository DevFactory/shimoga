/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.nggroup.impl.driver;

import org.apache.reef.driver.evaluator.FailedEvaluator;
import org.apache.reef.wake.EventHandler;

import java.util.logging.Logger;

public class TopologyFailedEvaluatorHandler implements EventHandler<FailedEvaluator> {

  private static final Logger LOG = Logger.getLogger(TopologyFailedEvaluatorHandler.class.getName());


  private final CommunicationGroupDriverImpl communicationGroupDriverImpl;

  public TopologyFailedEvaluatorHandler(final CommunicationGroupDriverImpl communicationGroupDriverImpl) {
    this.communicationGroupDriverImpl = communicationGroupDriverImpl;
  }

  @Override
  public void onNext(final FailedEvaluator failedEvaluator) {
    final String failedEvaluatorId = failedEvaluator.getId();
    LOG.entering("TopologyFailedEvaluatorHandler", "onNext", failedEvaluatorId);
    if (failedEvaluator.getFailedTask().isPresent()) {
      final String failedTaskId = failedEvaluator.getFailedTask().get().getId();
      LOG.finest("Failed Evaluator contains a failed task: " + failedTaskId);
      communicationGroupDriverImpl.failTask(failedTaskId);
      communicationGroupDriverImpl.removeTask(failedTaskId);
    }
    LOG.exiting("TopologyFailedEvaluatorHandler", "onNext", failedEvaluatorId);
  }

}
