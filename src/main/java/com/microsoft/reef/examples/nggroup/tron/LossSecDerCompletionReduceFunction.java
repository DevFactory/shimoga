/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron;

import com.microsoft.reef.io.network.group.operators.Reduce.ReduceFunction;

import javax.inject.Inject;

public class LossSecDerCompletionReduceFunction
    implements ReduceFunction<Boolean> {

  @Inject
  public LossSecDerCompletionReduceFunction() {
  }

  @Override
  public Boolean apply(
      final Iterable<Boolean> completions) {
    for (final Boolean b : completions) {
      if (!b) {
        return false;
      }
    }
    return true;
  }
}
