/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.data;

import com.microsoft.reef.examples.nggroup.tron.math.Vector;

import java.io.Serializable;

/**
 * Base interface for Examples for linear models.
 */
public interface Example extends Serializable {

  /**
   * Access to the label.
   *
   * @return the label
   */
  double getLabel();

  /**
   * Computes the prediction for this Example, given the model w.
   * <p/>
   * w.dot(this.getFeatures())
   *
   * @param w the model
   * @return the prediction for this Example, given the model w.
   */
  double predict(Vector w);

  /**
   * Useful for doing X'v kind of operations where
   * each example is multiplied by a scalar and added
   * to a resultant vector
   * <p/>
   * For example computing the gradientVector, can be done
   * by passing a vector and assuming that the
   * scalar is the gradient with respect to the dimension.
   */
  void addToVector(Vector vector, double scalar);
}
