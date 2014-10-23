/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.parameters;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

/**
 *
 */
// TODO: Document
@NamedParameter(short_name = "splits", default_value = "5")
public final class NumSplits implements Name<Integer> {
}
