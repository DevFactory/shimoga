/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.group.operators;

import com.microsoft.reef.io.network.group.impl.operators.basic.ScatterOp;
import org.apache.reef.exception.evaluator.NetworkException;
import org.apache.reef.tang.annotations.DefaultImplementation;
import org.apache.reef.wake.Identifier;

import java.util.List;

/**
 * MPI Scatter operator
 * <p/>
 * Scatter a list of elements to the receivers The receivers will receive a
 * sub-list of elements targeted for them. Supports non-uniform distribution
 * through the specification of counts
 */
public interface Scatter {

  /**
   * Sender or Root.
   */
  @DefaultImplementation(ScatterOp.Sender.class)
  static interface Sender<T> extends GroupCommOperator {

    /**
     * Distributes evenly across task ids sorted lexicographically.
     */
    void send(List<T> elements) throws NetworkException, InterruptedException;

    /**
     * Distributes as per counts across task ids sorted lexicographically.
     */
    void send(List<T> elements, Integer... counts) throws NetworkException, InterruptedException;

    /**
     * Distributes evenly across task ids sorted using order.
     */
    void send(List<T> elements, List<? extends Identifier> order)
        throws NetworkException, InterruptedException;

    /**
     * Distributes as per counts across task ids sorted using order.
     */
    void send(List<T> elements, List<Integer> counts,
              List<? extends Identifier> order) throws NetworkException, InterruptedException;
  }

  /**
   * Receiver or non-roots.
   */
  @DefaultImplementation(ScatterOp.Receiver.class)
  static interface Receiver<T> extends GroupCommOperator {
    /**
     * Receive the sub-list of elements targeted for the current receiver.
     *
     * @return list of elements targeted for the current receiver.
     */
    List<T> receive() throws InterruptedException, NetworkException;
  }
}
