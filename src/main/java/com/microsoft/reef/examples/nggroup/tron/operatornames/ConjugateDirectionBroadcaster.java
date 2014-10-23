/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.operatornames;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

/**
 * The name of the broadcast operator used for conjugate direction broadcasts.
 */
@NamedParameter()
public final class ConjugateDirectionBroadcaster implements Name<String> {
}