/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.broadcast.parameters;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

/**
 *
 */
@NamedParameter(doc = "Model dimensions", short_name = "dim")
public class Dimensions implements Name<Integer> {

}
