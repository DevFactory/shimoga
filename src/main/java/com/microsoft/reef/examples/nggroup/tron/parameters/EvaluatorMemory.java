/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.parameters;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

/**
 * The memory used for each Evaluator. In MB.
 */
@NamedParameter(short_name = "memory", default_value = "1024", doc = "The memory used for each Evaluator. In MB.")
public final class EvaluatorMemory implements Name<Integer> {
}
