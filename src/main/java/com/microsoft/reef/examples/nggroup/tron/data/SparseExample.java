/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.data;

import com.microsoft.reef.examples.nggroup.tron.math.Vector;

/**
 * Example implementation on a index and value array.
 */
public final class SparseExample implements Example {

  private static final long serialVersionUID = -2127500625316875426L;

  private final float[] values;
  private final int[] indices;
  private final double label;

  public SparseExample(final double label, final float[] values, final int[] indices) {
    this.label = label;
    this.values = values;
    this.indices = indices;
  }

  public int getFeatureLength() {
    return this.values.length;
  }

  @Override
  public double getLabel() {
    return this.label;
  }

  @Override
  public double predict(final Vector w) {
    double result = 0.0;
    for (int i = 0; i < this.indices.length; ++i) {
      result += w.get(this.indices[i]) * this.values[i];
    }
    return result;
  }

  @Override
  public void addToVector(final Vector gradientVector, final double gradient) {
    for (int i = 0; i < this.indices.length; ++i) {
      final int index = this.indices[i];
      final double contribution = gradient * this.values[i];
      final double oldValue = gradientVector.get(index);
      final double newValue = oldValue + contribution;
      gradientVector.set(index, newValue);
    }
  }
}
