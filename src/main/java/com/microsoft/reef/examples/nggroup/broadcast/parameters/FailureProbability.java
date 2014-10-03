/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.broadcast.parameters;

import com.microsoft.tang.annotations.Name;
import com.microsoft.tang.annotations.NamedParameter;

/**
 *
 */
@NamedParameter(doc = "Prob(failure)", default_value = "0.1")
public class FailureProbability implements Name<Double> {

}