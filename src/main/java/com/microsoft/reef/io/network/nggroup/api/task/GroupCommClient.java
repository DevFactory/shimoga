/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.nggroup.api.task;

import org.apache.reef.annotations.Provided;
import org.apache.reef.annotations.audience.TaskSide;
import org.apache.reef.tang.annotations.DefaultImplementation;
import org.apache.reef.tang.annotations.Name;


/**
 * The task side interface for the Group Communication Service
 */
@TaskSide
@Provided
@DefaultImplementation(value = com.microsoft.reef.io.network.nggroup.impl.task.GroupCommClientImpl.class)
public interface GroupCommClient {

  /**
   * @param string
   * @return The communication group client with the given name that gives access
   * to the operators configured on it that will be used to do group communication
   */
  CommunicationGroupClient getCommunicationGroup(Class<? extends Name<String>> groupName);
}
