/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.group.operators;

import com.microsoft.reef.io.network.group.impl.operators.basic.AllGatherOp;
import org.apache.reef.exception.evaluator.NetworkException;
import org.apache.reef.tang.annotations.DefaultImplementation;
import org.apache.reef.wake.Identifier;

import java.util.List;

/**
 * MPI AllGather Operator.
 * <p/>
 * Each task applies this operator on an element of type T. The result will be
 * a list of elements constructed using the elements all-gathered at each
 * task.
 */
@DefaultImplementation(AllGatherOp.class)
public interface AllGather<T> extends GroupCommOperator {

  /**
   * Apply the operation on element.
   *
   * @return List of all elements on which the operation was applied using default order
   */
  List<T> apply(T element) throws NetworkException,
      InterruptedException;

  /**
   * Apply the operation on element.
   *
   * @return List of all elements on which the operation was applied using order specified
   */
  List<T> apply(T element, List<? extends Identifier> order)
      throws NetworkException, InterruptedException;
}
