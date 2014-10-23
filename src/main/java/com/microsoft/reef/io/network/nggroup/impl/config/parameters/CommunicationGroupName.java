/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.nggroup.impl.config.parameters;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

@NamedParameter(doc = "Name of the comm group")
public final class CommunicationGroupName implements Name<String> {
  private CommunicationGroupName() {
  }
}
