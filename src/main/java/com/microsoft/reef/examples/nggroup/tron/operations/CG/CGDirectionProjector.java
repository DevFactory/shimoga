/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.operations.CG;

import com.microsoft.reef.examples.nggroup.tron.math.Vector;
import org.apache.reef.exception.evaluator.NetworkException;

public interface CGDirectionProjector {
  void project(Vector CGDirection, Vector projectedCGDirection) throws NetworkException, InterruptedException;
}
