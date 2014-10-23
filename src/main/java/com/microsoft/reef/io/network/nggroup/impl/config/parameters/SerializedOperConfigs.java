/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.nggroup.impl.config.parameters;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

import java.util.Set;

@NamedParameter(doc = "Serialized operator configurations")
public final class SerializedOperConfigs implements Name<Set<String>> {
  private SerializedOperConfigs() {
  }
}
