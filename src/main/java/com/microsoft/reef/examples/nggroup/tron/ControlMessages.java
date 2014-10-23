/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron;

import java.io.Serializable;

public enum ControlMessages implements Serializable {
  ComputeGradientWithModel,
  ComputeLossSecondDerivativeWithModel,
  ComputeLossSecondDerivative,
  ComputeProjectionDirection,
  Stop
}