/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.group.operators;

import com.microsoft.reef.io.network.group.impl.operators.basic.BroadcastOp;
import org.apache.reef.exception.evaluator.NetworkException;
import org.apache.reef.tang.annotations.DefaultImplementation;

/**
 * MPI Broadcast operator.
 * <p/>
 * The sender or root send's an element that is received by all the receivers or other tasks.
 * <p/>
 * This is an asymmetric operation and hence the differentiation b/w Sender and Receiver.
 */
public interface Broadcast {

  /**
   * Sender or Root.
   */
  @DefaultImplementation(BroadcastOp.Sender.class)
  static interface Sender<T> extends GroupCommOperator {

    /**
     * Send element to all receivers.
     */
    void send(T element) throws NetworkException, InterruptedException;
  }

  /**
   * Receivers or Non-roots
   */
  @DefaultImplementation(BroadcastOp.Receiver.class)
  static interface Receiver<T> extends GroupCommOperator {

    /**
     * Receiver the element broadcasted by sender.
     *
     * @return the element broadcasted by sender
     */
    T receive() throws NetworkException, InterruptedException;
  }
}
