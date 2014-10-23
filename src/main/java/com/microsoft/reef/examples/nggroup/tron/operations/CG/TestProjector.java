/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.operations.CG;

import com.microsoft.reef.examples.nggroup.tron.math.Vector;

public class TestProjector implements CGDirectionProjector {

  private final Vector[] matrix;

  public TestProjector(final Vector[] matrix) {
    this.matrix = matrix;
  }

  @Override
  public void project(final Vector CGDirection, final Vector projectedCGDirection) {
    for (int i = 0; i < matrix.length; i++) {
      final Vector row = matrix[i];
      projectedCGDirection.set(i, row.dot(CGDirection));
    }
  }

}
