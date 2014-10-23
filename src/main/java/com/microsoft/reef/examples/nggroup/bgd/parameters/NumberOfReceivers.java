/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.bgd.parameters;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

/**
 *
 */
@NamedParameter(doc = "The number of receivers for the operators")
public class NumberOfReceivers implements Name<Integer> {

}
