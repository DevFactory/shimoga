/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.nggroup.impl.config.parameters;

import com.microsoft.reef.io.network.group.operators.Reduce.ReduceFunction;
import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

@NamedParameter(doc = "The reduce function class that is associated with a reduce operator")
public final class ReduceFunctionParam implements Name<ReduceFunction> {
  private ReduceFunctionParam() {
  }
}
