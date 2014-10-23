/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.parameters;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

/**
 * The input folder of the learner.
 */
@NamedParameter(short_name = "input")
public final class InputDir implements Name<String> {
}
