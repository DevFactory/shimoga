/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.nggroup.impl.config.parameters;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

@NamedParameter(doc = "The fan out for the tree topology", default_value = "2", short_name = "fanout")
public final class TreeTopologyFanOut implements Name<Integer> {
  private TreeTopologyFanOut() {
  }
}
