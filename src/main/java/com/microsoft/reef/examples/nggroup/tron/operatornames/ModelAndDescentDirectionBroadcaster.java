/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.operatornames;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

/**
 * Name of the broadcast operator used to send a model and descent direction during line search.
 */
@NamedParameter()
public final class ModelAndDescentDirectionBroadcaster implements Name<String> {
}