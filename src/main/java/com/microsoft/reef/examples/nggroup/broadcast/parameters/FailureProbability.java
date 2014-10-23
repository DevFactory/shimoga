/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.broadcast.parameters;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

/**
 *
 */
@NamedParameter(doc = "Prob(failure)", default_value = "0.1")
public class FailureProbability implements Name<Double> {

}
