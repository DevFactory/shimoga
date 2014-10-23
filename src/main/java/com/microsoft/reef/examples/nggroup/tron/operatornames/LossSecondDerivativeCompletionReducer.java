/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.operatornames;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

/**
 * Signals completion of loss second derivative computation
 */
@NamedParameter()
public final class LossSecondDerivativeCompletionReducer implements Name<String> {
}