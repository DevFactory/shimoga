/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.operatornames;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

/**
 * Name of the reducer used to aggregate line search results.
 */
@NamedParameter()
public final class LineSearchEvaluationsReducer implements Name<String> {
}