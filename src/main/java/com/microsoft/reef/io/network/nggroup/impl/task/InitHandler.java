/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.nggroup.impl.task;

import com.microsoft.reef.io.network.group.operators.GroupCommOperator;
import org.apache.reef.io.network.exception.ParentDeadException;
import org.apache.reef.wake.EventHandler;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

class InitHandler implements EventHandler<GroupCommOperator> {

  private static final Logger LOG = Logger.getLogger(InitHandler.class.getName());

  private ParentDeadException exception = null;
  private final CountDownLatch initLatch;

  public InitHandler(final CountDownLatch initLatch) {
    this.initLatch = initLatch;
  }

  @Override
  public void onNext(final GroupCommOperator op) {
    LOG.entering("InitHandler", "onNext", op);
    try {
      op.initialize();
    } catch (final ParentDeadException e) {
      this.exception = e;
    }
    initLatch.countDown();
    LOG.exiting("InitHandler", "onNext", op);
  }

  public ParentDeadException getException() {
    return exception;
  }
}