/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.parameters;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

/**
 * Maximum Number of Iterations.
 */
@NamedParameter(doc = "Number of iterations", short_name = "iterations", default_value = "100")
public final class Iterations implements Name<Integer> {
}
