/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.bgd.parameters;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

/**
 * Maximum Number of Iterations.
 */
@NamedParameter(doc = "Min Number of partitions", short_name = "minparts", default_value = "2")
public final class MinParts implements Name<Integer> {
}
