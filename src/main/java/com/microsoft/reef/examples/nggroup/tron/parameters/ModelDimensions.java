/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.parameters;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

/**
 * The dimensionality of the model learned.
 */
@NamedParameter(doc = "Model dimensions", short_name = "dim")
public class ModelDimensions implements Name<Integer> {

}
