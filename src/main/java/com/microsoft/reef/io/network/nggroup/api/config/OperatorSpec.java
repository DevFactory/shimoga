/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.nggroup.api.config;

import org.apache.reef.annotations.audience.DriverSide;
import org.apache.reef.annotations.audience.Private;
import org.apache.reef.io.serialization.Codec;

/**
 * The specification of an operator submitted by
 * the user while configuring the communication
 * group
 */
@DriverSide
@Private
public interface OperatorSpec {

  /**
   * @return The codec class to be used to
   * serialize & desrialize data in
   * the operators
   */
  Class<? extends Codec> getDataCodecClass();

}
