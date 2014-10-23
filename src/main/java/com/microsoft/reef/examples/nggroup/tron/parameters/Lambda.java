/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.parameters;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

/**
 * The regularization constant
 */
@NamedParameter(doc = "The regularization constant", short_name = "lambda", default_value = "1e-4")
public final class Lambda implements Name<Double> {
}
