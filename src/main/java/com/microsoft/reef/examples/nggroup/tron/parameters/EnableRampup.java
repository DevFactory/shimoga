/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.parameters;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

/**
 * Maximum Number of Iterations.
 */
@NamedParameter(doc = "Should we ram-up?", short_name = "rampup", default_value = "false")
public final class EnableRampup implements Name<Boolean> {
}
