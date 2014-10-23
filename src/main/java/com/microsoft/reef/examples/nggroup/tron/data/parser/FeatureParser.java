/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.data.parser;

/**
 * This parser interface should expose the
 * dimensionality of the data
 */
public interface FeatureParser<T> extends Parser<T> {
  int getDimensionality();
}
