/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.nggroup.impl.config.parameters;

import org.apache.reef.io.serialization.Codec;
import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

@NamedParameter(doc = "Codec used to serialize and deserialize data in operators")
public final class DataCodec implements Name<Codec> {
  private DataCodec() {
  }
}
