/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.nggroup.impl.config.parameters;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

import java.util.Set;

@NamedParameter(doc = "Serialized communication group configurations")
public final class SerializedGroupConfigs implements Name<Set<String>> {
  private SerializedGroupConfigs() {
  }
}
