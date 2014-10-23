/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron;

import com.microsoft.reef.examples.nggroup.tron.math.DenseVector;
import com.microsoft.reef.examples.nggroup.tron.math.Vector;
import com.microsoft.reef.io.network.group.operators.Reduce.ReduceFunction;
import org.apache.reef.io.network.util.Pair;

import javax.inject.Inject;

public class SumUpIntVecPairReduceFunction
    implements ReduceFunction<Pair<Integer, Vector>> {

  @Inject
  public SumUpIntVecPairReduceFunction() {
  }

  @Override
  public Pair<Integer, Vector> apply(
      final Iterable<Pair<Integer, Vector>> pgs) {

    int numEx = 0;
    Vector combinedProjectedDirection = null;

    for (final Pair<Integer, Vector> pg : pgs) {
      if (combinedProjectedDirection == null) {
        combinedProjectedDirection = new DenseVector(pg.second);
      } else {
        combinedProjectedDirection.add(pg.second);
      }
      numEx += pg.first;
    }

    return new Pair<>(numEx, combinedProjectedDirection);
  }
}
