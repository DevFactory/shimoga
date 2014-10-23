/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.group.operators;

import org.apache.reef.io.network.exception.ParentDeadException;
import org.apache.reef.tang.annotations.Name;

public interface GroupCommOperator {

  Class<? extends Name<String>> getOperName();

  Class<? extends Name<String>> getGroupName();

  void initialize() throws ParentDeadException;

  int getVersion();
}
