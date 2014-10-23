/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.loss;

import javax.inject.Inject;

/**
 * The Squarred Error {@link LossFunction}.
 *
 * @author Markus Weimer <mweimer@microsoft.com>
 */
public class SquaredErrorLossFunction implements LossFunction {

  /**
   * Trivial constructor.
   */
  @Inject
  public SquaredErrorLossFunction() {
  }

  @Override
  public double computeLoss(final double y, final double f) {
    return 0.5 * Math.pow(y - f, 2.0);
  }

  @Override
  public double computeGradient(final double y, final double f) {
    return (f - y);
  }

  @Override
  public String toString() {
    return "SquaredErrorLossFunction{}";
  }

  @Override
  public double computeSecondGradient(final double y, final double f) {
    return 1;
  }
}
