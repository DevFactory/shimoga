/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.data.parser;

import com.microsoft.reef.examples.nggroup.tron.data.Example;


/**
 * Parses inputs into Examples.
 *
 * @param <T>
 */
public interface Parser<T> {

  public Example parse(final T input);

}
