/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.parameters;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;


// TODO: Document
@NamedParameter(short_name = "timeout", default_value = "2")
public final class Timeout implements Name<Integer> {
}
