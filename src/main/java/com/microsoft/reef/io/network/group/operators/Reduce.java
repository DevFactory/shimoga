/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.group.operators;

import com.microsoft.reef.io.network.group.impl.operators.basic.ReduceOp;
import org.apache.reef.exception.evaluator.NetworkException;
import org.apache.reef.tang.annotations.DefaultImplementation;
import org.apache.reef.wake.Identifier;

import java.util.List;

/**
 * MPI Reduce operator.
 * <p/>
 * This is another operator with root being receiver All senders send an element
 * to the receiver. These elements are passed through a reduce function and its
 * result is made available at the root
 */
public interface Reduce {

  /**
   * Receiver or Root
   */
  @DefaultImplementation(ReduceOp.Receiver.class)
  static interface Receiver<T> extends GroupCommOperator {

    /**
     * Receive values sent by senders and pass them through the reduce
     * function in default order.
     *
     * @return Result of applying reduce function on the elements gathered in default order.
     */
    T reduce() throws InterruptedException, NetworkException;

    /**
     * Receive values sent by senders and pass them through the reduce
     * function in specified order.
     *
     * @return Result of applying reduce function on the elements gathered in specified order.
     */
    T reduce(List<? extends Identifier> order) throws InterruptedException, NetworkException;

    /**
     * The reduce function to be applied on the set of received values
     *
     * @return {@link ReduceFunction}
     */
    Reduce.ReduceFunction<T> getReduceFunction();
  }

  /**
   * Senders or non roots
   */
  @DefaultImplementation(ReduceOp.Sender.class)
  static interface Sender<T> extends GroupCommOperator {

    /**
     * Send the element to the root.
     */
    void send(T element) throws NetworkException, InterruptedException;

    /**
     * The {@link ReduceFunction} to be applied on the set of received values.
     *
     * @return {@link ReduceFunction}
     */
    Reduce.ReduceFunction<T> getReduceFunction();
  }

  /**
   * Interface for a Reduce Function takes in an {@link Iterable} returns an
   * aggregate value computed from the {@link Iterable}
   */
  static interface ReduceFunction<T> {
    /**
     * Apply the function on elements.
     *
     * @return aggregate value computed from elements.
     */
    T apply(Iterable<T> elements);
  }
}
