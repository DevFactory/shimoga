/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.data;

/**
 * A Dataset for a machine Learning Algorithm.
 */
public interface DataSet extends Iterable<Example> {

  /**
   * @return the number of Examples in this DataSet
   */
  public int getNumberOfExamples();

}
