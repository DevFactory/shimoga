/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.operatornames;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

/**
 * Name used for the Reduce operator for loss and gradient aggregation.
 */
@NamedParameter()
public final class ProjectedDirectionReducer implements Name<String> {
}