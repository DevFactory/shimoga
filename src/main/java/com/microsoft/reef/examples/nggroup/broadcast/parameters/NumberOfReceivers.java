/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.broadcast.parameters;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

/**
 *
 */
@NamedParameter(doc = "The number of receivers for the operators", short_name = "receivers")
public class NumberOfReceivers implements Name<Integer> {

}
