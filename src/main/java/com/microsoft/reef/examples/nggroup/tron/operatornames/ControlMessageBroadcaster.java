/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.operatornames;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;

/**
 * Used to identify the broadcast operator for control flow messages.
 */
@NamedParameter()
public final class ControlMessageBroadcaster implements Name<String> {
}