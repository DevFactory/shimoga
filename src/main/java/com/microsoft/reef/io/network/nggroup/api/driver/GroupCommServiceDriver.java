/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.nggroup.api.driver;


import com.microsoft.reef.io.network.nggroup.impl.driver.GroupCommDriverImpl;
import org.apache.reef.annotations.Provided;
import org.apache.reef.annotations.audience.Private;
import org.apache.reef.driver.evaluator.FailedEvaluator;
import org.apache.reef.driver.task.FailedTask;
import org.apache.reef.driver.task.RunningTask;
import org.apache.reef.tang.annotations.DefaultImplementation;
import org.apache.reef.wake.EStage;

@Private
@Provided
@DefaultImplementation(value = GroupCommDriverImpl.class)
public interface GroupCommServiceDriver extends GroupCommDriver {

  /**
   * Not user facing but used the Group Communication Service class
   *
   * @return The running task stage that will handle the RunningTask
   * events
   */
  EStage<RunningTask> getGroupCommRunningTaskStage();

  /**
   * Not user facing but used the Group Communication Service class
   *
   * @return The running task stage that will handle the FailedTask
   * events
   */
  EStage<FailedTask> getGroupCommFailedTaskStage();

  /**
   * Not user facing but used the Group Communication Service class
   *
   * @return The running task stage that will handle the FailedEvaluator
   * events
   */
  EStage<FailedEvaluator> getGroupCommFailedEvaluatorStage();
}
